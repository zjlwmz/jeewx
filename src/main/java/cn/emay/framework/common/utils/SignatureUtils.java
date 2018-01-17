package cn.emay.framework.common.utils;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;



/**
 * 
 * 数字签名工具类
 * @author zjlWm
 * @version 2014-11-14
 */
public class SignatureUtils {
	
	
	/**
	 * 获取一个签名
	 * @return
	 */
	public static String getSignature(String token,String content,String nonce){
		String[] arr = new String[] { token, content, nonce };
		Arrays.sort(arr);
		StringBuilder contentBuffere = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			contentBuffere.append(arr[i]);
		}
		String tmpStr = null;
		try{
			tmpStr =DigestUtils.sha1Hex(contentBuffere.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return tmpStr;
	}
	
	
	/**
	 * 微信公众号签名验证
	 * @param token	与接口配置信息中的Token要一致
	 * @param signature
	 * @param timestamp--//data
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String token,String signature, String data, String nonce) {
		String[] arr = new String[] { token, data, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		String tmpStr = null;
		try {
			tmpStr =DigestUtils.sha1Hex(content.toString());
			System.out.println("tmpStr:"+tmpStr);
		}catch (Exception e) {
			e.printStackTrace();
		}
		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equalsIgnoreCase(signature) : false;
	}
	
	
	/**
	 * 企业计算签名
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @param msg_signature
	 * @param msg_encrypt
	 * @return
	 */
	public static boolean qycheckSignature(String token,String timestamp,String nonce,String msg_signature,String msg_encrypt ){
		String[] arr = new String[] { token, timestamp, nonce,msg_encrypt };
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		String tmpStr = null;
		try {
			tmpStr =DigestUtils.sha1Hex(content.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(msg_signature.toUpperCase()) : false;
	}
	
	
	
	public static void main(String[] args) {
		boolean t=checkSignature("wh_weixinToken", "33991c2da2ab564507a3b561a8615b2fe2822e63", "1452584557", "673310580");
		System.out.println(t);
	}
	
}
