package com.ruishengtech.framework.core;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 当前使用解密类,如不使用请去掉描述
 *
 */
public class AESTools {
	
	/**
	 * 加密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] encrypt(String content, String password) {
		try {
			/*KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));*/
			
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			kgen.init(128, random);
			
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
//			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			kgen.init(128, new SecureRandom(password.getBytes()));
//			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = getKey(password).getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
	
	public static SecretKey getKey(String strKey) {
        try {         
           KeyGenerator _generator = KeyGenerator.getInstance( "AES" );
           SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
           secureRandom.setSeed(strKey.getBytes());
           _generator.init(128,secureRandom);
               return _generator.generateKey();
       }catch (Exception e) {
            throw new RuntimeException( " 初始化密钥出现异常 " );
       }
   } 
	
	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {

		String content = "test";
		String password = "12345678";
		// 加密
		System.out.println("加密前：" + content.substring(0,4));
		byte[] encryptResult = encrypt(content, password);
		String encryptResultStr = parseByte2HexStr(encryptResult);
		System.out.println("加密后：" + encryptResultStr);
		// 解密
		byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);
		byte[] decryptResult = decrypt(decryptFrom, password);
		System.out.println("解密后：" + new String(decryptResult,"UTF-8"));

	}

}
