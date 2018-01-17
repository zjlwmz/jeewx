package org.jeecgframework.test.demo;

import java.io.File;
import java.util.List;
//生成视频文件的首帧为图片
//windows下的版本

public class Map4 {
	// public static final String FFMPEG_PATH = "E:/ffmpeg/ffmpeg.exe";
	public static boolean processImg(String veido_path, String ffmpeg_path) {
		File file = new File(veido_path);
		if (!file.exists()) {
			System.err.println("路径[" + veido_path + "]对应的视频文件不存在!");
			return false;
		}
		List<String> commands = new java.util.ArrayList<String>();
		commands.add(ffmpeg_path);
		commands.add("-i");
		commands.add(veido_path);
		commands.add("-y");
		commands.add("-f");
		commands.add("image2");
		commands.add("-ss");
		commands.add("0");// 这个参数是设置截取视频多少秒时的画面
		// commands.add("-t");
		// commands.add("0.001");
		commands.add("-s");
		commands.add("700x525");
		commands.add(veido_path.substring(0, veido_path.lastIndexOf(".")).replaceFirst("vedio", "file") + ".jpg");
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.start();
			System.out.println("截取成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		processImg("D:/apache-tomcat-7.0.68/webapps/jeewx/video/201609296335666813230660223.mp4", "F:/项目框架/jeecg/svn/jeecg-master/exe/ffmpeg.exe");
	}
}
