package io.renren.util;

import java.io.File;

public class FileUtil {
	// 判断文件是否存在
	public static boolean fileExists(File file) {
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	// 判断文件夹是否存在
	public static boolean dirExists(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
}
