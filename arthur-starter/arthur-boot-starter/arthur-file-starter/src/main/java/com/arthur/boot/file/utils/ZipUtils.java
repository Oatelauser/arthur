package com.arthur.boot.file.utils;

import com.arthur.boot.file.constants.FileConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip工具类
 * <p>
 * 1.压缩：文件、文件夹的压缩
 * <br/>
 * 2.解压：文件、文件夹的解压
 *
 * @author DearYang
 * @date 2022-10-11
 * @see <a href="https://springboot.io/t/topic/2869">ZipUtils</a>
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class ZipUtils {

	private static final String SLASH = "/";

	/**
	 * 指定多个文件压缩为zip
	 *
	 * @param srcPath 需要被压缩的文件
	 * @param zipFile 压缩后的目标zip文件
	 * @throws IOException io异常
	 */
	public static void zip(Path[] srcPath, Path zipFile) throws IOException {
		if (!Files.exists(zipFile.getParent())) {
			Files.createDirectories(zipFile.getParent());
		}

		try (OutputStream os = Files.newOutputStream(zipFile, StandardOpenOption.CREATE_NEW);
			 ZipOutputStream zos = new ZipOutputStream(os)) {
			Arrays.stream(srcPath).forEach(file -> compressPath(file, zos));
			zos.closeEntry();
		}
	}

	/**
	 * @see #zip(Path[], Path)
	 */
	public static void zip(String srcDir, Path zipFile) throws IOException {
		zip(new Path[]{Path.of(srcDir)}, zipFile);
	}

	/**
	 * @see #zip(String, Path)
	 */
	public static void zip(String srcDir, String zipFile) throws IOException {
		zip(srcDir, Path.of(zipFile));
	}

	/**
	 * @see #zip(Path[], Path)
	 */
	public static void zip(String[] srcPath, Path zipFile) throws IOException {
		Path[] paths = new Path[srcPath.length];
		for (int i = 0; i < srcPath.length; i++) {
			paths[i] = Path.of(srcPath[i]);
		}
		zip(paths, zipFile);
	}

	/**
	 * @see #zip(String[], Path)
	 */
	public static void zip(String[] srcPath, String zipFile) throws IOException {
		zip(srcPath, Path.of(zipFile));
	}

	/**
	 * 将ZIP压缩包解压到指定目录
	 *
	 * @param srcZipFile zip文件
	 * @param sourceDir  解压目录
	 * @throws IOException io异常
	 */
	public static void unzip(Path srcZipFile, Path sourceDir) throws IOException {
		if (Files.notExists(srcZipFile) || Files.isDirectory(srcZipFile) || !Files.isDirectory(sourceDir)) {
			return;
		}
		if (Files.notExists(sourceDir)) {
			Files.createDirectories(sourceDir);
		}

		try (ZipFile zip = new ZipFile(srcZipFile.toFile());
			 ZipInputStream zis = new ZipInputStream(Files.newInputStream(srcZipFile))) {
			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {
				unzipFiles(sourceDir, zip, zipEntry);
			}
			zis.closeEntry();
		}
	}

	/**
	 * @see #unzip(Path, Path)
	 */
	public static void unzip(Path srcZipFile) throws IOException {
		unzip(srcZipFile, srcZipFile.getParent());
	}

	/**
	 * @see #unzip(Path, Path)
	 */
	public static void unzip(String srcZipFile, String sourceDir) throws IOException {
		unzip(Path.of(srcZipFile), Path.of(sourceDir));
	}

	/**
	 * @see #unzip(Path)
	 */
	public static void unzip(String srcZipFile) throws IOException {
		unzip(Path.of(srcZipFile));
	}

	/**
	 * 压缩路径
	 *
	 * @param srcPath 路径
	 * @param zos     zip输出流
	 */
	private static void compressPath(Path srcPath, ZipOutputStream zos) {
		if (!Files.exists(srcPath)) {
			return;
		}

		try {
			if (Files.isDirectory(srcPath)) {
				compressPacket(srcPath, zos);
				return;
			}
			compressFile(srcPath, zos);
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	/**
	 * 文件添加到压缩包中
	 *
	 * @param srcFile 文件
	 * @param zos     zip输出流
	 * @throws IOException io异常
	 */
	private static void compressFile(Path srcFile, ZipOutputStream zos) throws IOException {
		try (InputStream is = Files.newInputStream(srcFile)) {
			zos.putNextEntry(new ZipEntry(srcFile.getFileName().toString()));
			byte[] buffer = new byte[FileConstants.READ_BYTE_SIZE];
			int len;
			while ((len = is.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
		}
	}

	/**
	 * 目录添加到压缩包中
	 *
	 * @param srcDir 目录
	 * @param zos    zip输出流
	 * @throws IOException io异常
	 */
	private static void compressPacket(Path srcDir, ZipOutputStream zos) throws IOException {
		LinkedList<String> path = new LinkedList<>();
		Files.walkFileTree(srcDir, new FileVisitor<>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				// 开始遍历目录
				String folder = dir.getFileName().toString();
				path.addLast(folder);
				// 写入目录
				ZipEntry zipEntry = new ZipEntry(String.join(SLASH, path) + SLASH);
				try {
					zos.putNextEntry(zipEntry);
					zos.flush();
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				// 开始遍历文件
				try (InputStream inputStream = Files.newInputStream(file)) {
					// 创建一个压缩项，指定名称
					String fileName = path.size() > 0
						? (String.join(SLASH, path) + SLASH + file.getFileName().toString())
						: file.getFileName().toString();
					ZipEntry zipEntry = new ZipEntry(fileName);

					// 添加到压缩流
					zos.putNextEntry(zipEntry);
					// 写入数据
					byte[] buffer = new byte[FileConstants.READ_BYTE_SIZE];
					int len;
					while ((len = inputStream.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					zos.flush();
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
				// 结束遍历目录
				if (!path.isEmpty()) {
					path.removeLast();
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * zip解压
	 *
	 * @param srcDir   压缩包的文件或者目录
	 * @param zip      {@link ZipFile}
	 * @param zipEntry {@link ZipEntry}
	 * @throws IOException io异常
	 */
	private static void unzipFiles(Path srcDir, ZipFile zip, ZipEntry zipEntry) throws IOException {
		String name = zipEntry.getName();
		Path entryFile = srcDir.resolve(name);
		if (zipEntry.isDirectory()) {
			if (!Files.isDirectory(entryFile)) {
				Files.createDirectories(entryFile);
			}
			return;
		}

		try (InputStream is = zip.getInputStream(zipEntry);
			 OutputStream os = Files.newOutputStream(entryFile, StandardOpenOption.CREATE_NEW)) {
			is.transferTo(os);
		}
	}

}
