package cn.emay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiParser {

	private HashMap<List<Integer>, String> convertMap = new HashMap<List<Integer>, String>();
	
	private static EmojiParser mParser;
	
	public static EmojiParser getInstance() {
		if (mParser == null) {
			mParser = new EmojiParser();
		}
		return mParser;
	}
	
	
	public String parseEmoji(String input) {
		if (input == null || input.length() <= 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		int[] codePoints = toCodePointArray(input);
		List<Integer> key = null;
		for (int i = 0; i < codePoints.length; i++) {
			key = new ArrayList<Integer>();
			if (i + 1 < codePoints.length) {
				key.add(codePoints[i]);
				key.add(codePoints[i + 1]);
				if (convertMap.containsKey(key)) {
					String value = convertMap.get(key);
					if (value != null) {
						result.append("[e]" + value + "[/e]");
					}
					i++;
					continue;
				}
			}
			key.clear();
			key.add(codePoints[i]);
			if (convertMap.containsKey(key)) {
				String value = convertMap.get(key);
				if (value != null) {
					result.append("[e]" + value + "[/e]");
				}
				continue;
			}
			result.append(Character.toChars(codePoints[i]));
		}
		return result.toString();
	}

	private int[] toCodePointArray(String str) {
		char[] ach = str.toCharArray();
		int len = ach.length;
		int[] acp = new int[Character.codePointCount(ach, 0, len)];
		int j = 0;
		for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
			cp = Character.codePointAt(ach, i);
			acp[j++] = cp;
		}
		return acp;
	}

	public String convertToUnicode(String emo) {
		emo = emo.substring(emo.indexOf("_") + 1);
		if (emo.length() < 6) {
			return new String(Character.toChars(Integer.parseInt(emo, 16)));
		}
		String[] emos = emo.split("_");
		char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
		char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
		char[] emoji = new char[char0.length + char1.length];
		for (int i = 0; i < char0.length; i++) {
			emoji[i] = char0[i];
		}
		for (int i = char0.length; i < emoji.length; i++) {
			emoji[i] = char1[i - char0.length];
		}
		return new String(emoji);
	}

	public void convertToEmoji(String content) {
		String regex = "\\[e\\](.*?)\\[/e\\]";
		Pattern pattern = Pattern.compile(regex);
		String emo = "";
		String unicode = parseEmoji(content);
		Matcher matcher = pattern.matcher(unicode);
		while (matcher.find()) {
			emo = matcher.group();
			try {
				System.out.println("emoji_" + emo.substring(emo.indexOf("]") + 1, emo.lastIndexOf("[")));
				
			} catch (Exception e) {
				break;
			}
		}
	}
	
	
}
