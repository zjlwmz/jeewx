package cn.emay.framework.common.utils;

import java.io.File;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

public class Mp3Utils {

	/**
	 * 获取mp3时长
	 * @param mp3File
	 * @return
	 */
	public static int getMp3TrackLength(String mp3FilePath) {
		try {
			File mp3File=new File(mp3FilePath);
			MP3File f = (MP3File) AudioFileIO.read(mp3File);
			MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
			return audioHeader.getTrackLength();
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	/**
	 * 获取mp3时长
	 * @param mp3File
	 * @return
	 */
	public static int getMp3TrackLength(File mp3File) {
		try {
			MP3File f = (MP3File) AudioFileIO.read(mp3File);
			MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
			return audioHeader.getTrackLength();
		} catch (Exception e) {
			return -1;
		}
	}

}
