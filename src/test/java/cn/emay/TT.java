package cn.emay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TT {

	public static void main(String[] args) {
		String txt = "img[http://jeewx.ngrok.geofound.cn/jeewx/upload/chat/image/20160904171729/44089a8b936049e39785e54fa8e8a74d.png]ddiimg[http://jeewx.ngrok.geofound.cn/jeewx/upload/chat/image/20160904171729/44089a8b936049e39785e54fa8e8a75d.png]";

		String re1 = "img\\[([^\\\\s]+?)\\]"; // Non-greedy match on filler

		boolean dd=Pattern.matches("img\\[([^\\\\s]+?)\\]", txt);
		System.out.println(dd);
		
		
		Pattern p = Pattern.compile(re1);
		Matcher m = p.matcher(txt);
		while(m.find()){
			String value1=m.group(0);
			String value2=m.group(1);
			System.out.println(value1);
			System.out.println(value2);
			
			txt=txt.replace(value1, "<a href='"+value2+"'>图片</a>");
		}
		
		System.out.println(txt);
	}

}
