package com.arthur.boot.file.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;

/**
 * FileChannel工具类
 *
 * @author DearYang
 * @date 2022-10-10
 * @see FileChannel
 * @since 1.0
 */
public abstract class FileChannelUtils {

	/**
	 * @see #safeTransferTo(FileChannel, WritableByteChannel)
	 */
	public static void safeTransferTo(Path srcFile, OutputStream os) throws IOException {
		try (FileChannel fileChannel = FileChannel.open(srcFile);
			 WritableByteChannel sourceChannel = Channels.newChannel(os)) {
			safeTransferTo(fileChannel, sourceChannel);
		}
	}

	/**
	 * 把目标文件通道的数据传输到源文件通道中
	 * <p>
	 * transferTo一次性最多只能处理2G(2147483647字节)数据，对于超过2G的数据会分片，因此需要循环读
	 *
	 * @param srcFileChannel    目标文件通道
	 * @param sourceFileChannel 源文件通道
	 * @throws IOException io异常
	 */
	public static void safeTransferTo(FileChannel srcFileChannel, WritableByteChannel sourceFileChannel) throws IOException {
		safeTransferTo(0, srcFileChannel.size(), srcFileChannel, sourceFileChannel);
	}

	/**
	 * 把目标文件通道的数据传输到源文件通道中
	 * <p>
	 * transferTo一次性最多只能处理2G(2147483647字节)数据，对于超过2G的数据会分片，因此需要循环读
	 *
	 * @param position          读取数据的开始位置
	 * @param size              一次读多少数据
	 * @param srcFileChannel    目标文件通道
	 * @param sourceFileChannel 源文件通道
	 * @throws IOException io异常
	 */
	public static void safeTransferTo(long position, long size, FileChannel srcFileChannel,
			WritableByteChannel sourceFileChannel) throws IOException {
		while (size > 0) {
			long count = srcFileChannel.transferTo(position, size, sourceFileChannel);
			if (count > 0) {
				position += count;
				size -= count;
			}
		}
	}

}
