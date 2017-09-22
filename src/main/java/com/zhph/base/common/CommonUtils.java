package com.zhph.base.common;

import java.io.File;

public class CommonUtils {

	/**
	 * 
	 * @功能描述：判断文件目录是否存在如果不存在则创建目录
	 * @param filePath 目录路径
	 * @throws Exception
	 */
	public static void mkDirs(String filePath) throws Exception{
		// 判断路径是否存在
		File directory = new File(filePath.toString());
		// 如果不存在则创建目录
		if (!(directory.exists() && directory.isDirectory())) {
			directory.mkdirs();
		} 
	}
}
