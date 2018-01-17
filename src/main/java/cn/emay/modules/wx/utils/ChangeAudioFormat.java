package cn.emay.modules.wx.utils;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import java.io.File;


/**
 * 
 * @Title 音频文件 转换
 * @author zjlwm
 * @date 2017-2-23 下午1:22:15
 *
 */
public class ChangeAudioFormat {
	
	public static void main(String[] args) throws Exception {
		String path1 = "D:/apache-tomcat-7.0.68/webapps/jeewx/voice/20160904/6326459699994558464.amr";
		String path2 = "d:/apache-tomcat-7.0.68/webapps/jeewx/voice/20160904/6326459699994558464.mp3";
		changeToMp3(path1, path2);
	}

	public static void changeToMp3(String sourcePath, String targetPath) {
		File source = new File(sourcePath);
		File target = new File(targetPath);
		AudioAttributes audio = new AudioAttributes();
		Encoder encoder = new Encoder();

		audio.setCodec("libmp3lame");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);

		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}
	}
}
