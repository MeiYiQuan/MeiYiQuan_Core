package com.salon.backstage.common.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("rawtypes")
public class Utf8BomRemover extends DirectoryWalker {
	public static void main(String[] args) throws IOException {
		new Utf8BomRemover("java").start(new File("D:\\git-space\\new-red-carpet-manager"));
	}

	private String extension = null;

	public Utf8BomRemover(String extension) {
		this.extension = extension;
	}

	@SuppressWarnings("unchecked")
	public void start(File rootDir) throws IOException {
		walk(rootDir, null);
	}

	protected void handleFile(File file, int depth, Collection results) throws IOException {
		if (extension == null || extension.equalsIgnoreCase(FilenameUtils.getExtension(file.toString()))) {
			remove(file);
		}
	}

	private void remove(File file) throws IOException {
		byte[] bs = FileUtils.readFileToByteArray(file);
		if (bs[0] == -17 && bs[1] == -69 && bs[2] == -65) {
			byte[] nbs = new byte[bs.length - 3];
			System.arraycopy(bs, 3, nbs, 0, nbs.length);
			FileUtils.writeByteArrayToFile(file, nbs);
			System.out.println("Remove BOM: " + file);
		}
	}
}
