package cn.emay.framework.common.media;

import java.io.File;
import java.util.List;


/**
 * 视频索缩略图截取
 * @date 2016-10-01
 * @author lenovo
 *
 */
public class MediaPreview {

	// public static final String FFMPEG_PATH = "E:/ffmpeg/ffmpeg.exe";
	public static String processImg(String veido_path, String ffmpeg_path) {
		File file = new File(veido_path);
		if (!file.exists()) {
			System.err.println("路径[" + veido_path + "]对应的视频文件不存在!");
			return null;
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
		String imageUrl=veido_path.substring(0, veido_path.lastIndexOf(".")).replaceFirst("vedio", "file") + ".jpg";
		commands.add(imageUrl);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.start();
			System.out.println("截取成功");
			return imageUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static String processImg(String veido_path){
		return processImg(veido_path, "F:/项目框架/jeecg/svn/jeecg-master/exe/ffmpeg.exe");
	}
	
	public static void main(String[] args) {
		processImg("D:/apache-tomcat-7.0.68/webapps/jeewx/video/201609296335666813230660223.mp4", "F:/项目框架/jeecg/svn/jeecg-master/exe/ffmpeg.exe");
	}
}
