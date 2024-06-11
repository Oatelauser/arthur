package com.arthur.boot.file.core;

import com.arthur.boot.file.utils.FileChannelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link FileChannel}文件传输器，底层基于{@link FileChannel}的零拷贝
 * <p>
 * 1.大文件分片
 * 2.多个文件合并
 *
 * @author DearYang
 * @date 2022-10-11
 * @see <a href="https://springboot.io/t/topic/2147/2">零拷贝对文件高效的切片和合并</a>
 * @since 1.0
 */
@SuppressWarnings("unused")
public class FileChannelTransferStreaming {

	private static final Logger LOG = LoggerFactory.getLogger(FileChannelTransferStreaming.class);

	/**
	 * 在当前文件{@link Path}创建一个具有分片的文件名
	 * <p>
	 * 比如：/home/db.text -> /home/db.text.0
	 *
	 * @param chunkSize 分片的字节数
	 * @param srcFile   需要分片的文件路径
	 * @throws IOException io异常
	 */
	public static void shardFile(long chunkSize, Path srcFile) throws IOException {
		if (!Files.exists(srcFile) || Files.isDirectory(srcFile)) {
			return;
		}

		try (FileChannel fileChannel = FileChannel.open(srcFile, EnumSet.of(StandardOpenOption.READ))) {
			// 获取当前路径的绝对路径
			String chunkFileName = srcFile.getFileName() + ".%d";
			chunkFileName = srcFile.resolveSibling(chunkFileName).toString();
			shardFile(chunkSize, fileChannel, new DefaultChunkFileParser(chunkFileName), new RollbackTransferExceptionHandler());
		}
	}

	/**
	 * 基于文件{@link Path}进行分片
	 *
	 * @param chunkSize 分片的字节数
	 * @param srcFile   需要分片的文件路径
	 * @param parser    分片文件路径的解析器
	 * @param handler   文件传输异常处理器
	 * @throws IOException io异常
	 */
	public static void shardFile(long chunkSize, Path srcFile, ChunkFileParser parser,
								 @Nullable TransferExceptionHandler handler) throws IOException {
		try (FileChannel fileChannel = FileChannel.open(srcFile, EnumSet.of(StandardOpenOption.READ))) {
			shardFile(chunkSize, fileChannel, parser, handler);
		}
	}

	/**
	 * 基于文件{@link Path}进行分片
	 *
	 * @param chunkSize   分片的字节数
	 * @param fileChannel 需要分片的文件{@link FileChannel}
	 * @param parser      分片文件路径的解析器
	 * @param handler     文件传输异常处理器
	 * @throws IOException io异常
	 */
	public static void shardFile(long chunkSize, FileChannel fileChannel, ChunkFileParser parser,
								 @Nullable TransferExceptionHandler handler) throws IOException {
		Assert.notNull(fileChannel, "FileChannel cannot be null");
		Assert.notNull(parser, "ChunkFileParser cannot be null");
		Assert.state(chunkSize > 0, "Shard size cannot be less than 1 byte");

		Set<Path> rollbackPaths = new LinkedHashSet<>();
		try {
			shardFileInternal(chunkSize, fileChannel, parser, rollbackPaths);
		} catch (Exception ex) {
			if (handler != null) {
				handler.handlerException(rollbackPaths, ex);
			}
			throw ex;
		}
	}

	/**
	 * @see #mergeFile(Path, TransferExceptionHandler, Path...)
	 */
	public static void mergeFile(Path sourceFile, Path... srcPaths) throws IOException {
		mergeFile(sourceFile, new RollbackTransferExceptionHandler(), srcPaths);
	}

	/**
	 * 将多个文件合并为一个文件
	 *
	 * @param sourceFile 合并后的文件
	 * @param handler    异常处理器
	 * @param srcPaths   需要合并的多个文件
	 * @throws IOException io异常
	 */
	public static void mergeFile(Path sourceFile, @Nullable TransferExceptionHandler handler, Path... srcPaths) throws IOException {
		Assert.notEmpty(srcPaths, "Path array has not any element");
		Assert.noNullElements(srcPaths, "Path array has null element");
		Set<Path> eligiblePaths = Arrays.stream(srcPaths).filter(path -> Files.exists(path)
			&& !Files.isDirectory(path)).collect(Collectors.toSet());
		if (eligiblePaths.isEmpty()) {
			return;
		}

		EnumSet<StandardOpenOption> options = EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		try (FileChannel fileChannel = FileChannel.open(sourceFile, options)) {
			for (Path srcPath : eligiblePaths) {
				try (FileChannel chunkChannel = FileChannel.open(srcPath, EnumSet.of(StandardOpenOption.READ))) {
					FileChannelUtils.safeTransferTo(chunkChannel, fileChannel);
				}
			}
		} catch (Exception ex) {
			if (handler != null) {
				handler.handlerException(Collections.singleton(sourceFile), ex);
			}
			throw ex;
		}
	}

	/**
	 * 底层真正执行分片的逻辑
	 *
	 * @param chunkSize     分片的字节数
	 * @param fileChannel   文件通道
	 * @param parser        分片文件的解析器
	 * @param rollbackPaths 记录分片文件写入的路径
	 * @throws IOException io异常
	 */
	private static void shardFileInternal(long chunkSize, FileChannel fileChannel, ChunkFileParser parser, Set<Path> rollbackPaths) throws IOException {
		long fileSize = fileChannel.size();
		long chunkNumber = calculateChunkNumbers(chunkSize, fileSize);
		for (int i = 0; i < chunkNumber; i++) {
			long start = i * chunkSize;
			long end = start + chunkSize;
			if (end > fileSize) {
				end = fileSize;
			}
			// 分片文件名称
			Path chunkFile = parser.resolveChunkFile(i);
			rollbackPaths.add(chunkFile);
			EnumSet<StandardOpenOption> options = EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			try (FileChannel chunkFileChannel = FileChannel.open(chunkFile, options)) {
				FileChannelUtils.safeTransferTo(start, end - start, fileChannel, chunkFileChannel);
			}
		}
	}

	/**
	 * 计算分片数
	 *
	 * @param chunkSize 分片字节数
	 * @param fileSize  文件大小
	 * @return 分片数
	 */
	private static long calculateChunkNumbers(long chunkSize, long fileSize) {
		long numberOfChunk = fileSize / chunkSize;
		if (fileSize % chunkSize != 0) {
			++numberOfChunk;
		}
		return numberOfChunk;
	}

	/**
	 * 块文件解析器
	 */
	public interface ChunkFileParser {

		/**
		 * 解析块文件所在的路径
		 *
		 * @param index 分片索引个数
		 * @return {@link Path}
		 */
		Path resolveChunkFile(int index);

	}

	/**
	 * 异常处理器，通常用于文件回滚等操作
	 */
	public interface TransferExceptionHandler {

		/**
		 * 处理异常
		 *
		 * @param paths 该文件可能存在没有生成的文件
		 * @param ex    {@link Exception}
		 */
		void handlerException(Set<Path> paths, Exception ex);

	}

	public static class DefaultChunkFileParser implements ChunkFileParser {

		private final String filePathFormat;
		private String parent;

		public DefaultChunkFileParser(String filePathFormat) {
			this.filePathFormat = filePathFormat;
		}

		@Override
		public Path resolveChunkFile(int index) {
			String file = String.format(filePathFormat, index);
			if (parent != null) {
				return Path.of(parent, file);
			}
			return Path.of(file);
		}

		public void setParent(String parent) {
			this.parent = parent;
		}
	}

	/**
	 * 在传输过程中出现，回滚生成的文件，并且重新抛出异常
	 */
	public static class RollbackTransferExceptionHandler implements TransferExceptionHandler {

		@Override
		public void handlerException(Set<Path> paths, Exception ex) {
			if (CollectionUtils.isEmpty(paths)) {
				return;
			}

			paths.stream().filter(Files::exists).forEach(path -> {
				try {
					Files.delete(path);
				} catch (IOException e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Delete file [" + path + "] has error", e);
					}
				}
			});
		}
	}

}
