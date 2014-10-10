package hr.fer.zari.rasip.tiger.util;

import java.util.Random;

import org.apache.commons.codec.binary.Base64;

public class StringUtils {

	private static Random random = new Random();
	
	public static String randomToken(int length){
		byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		Base64 base64Gen = new Base64();
		String token = base64Gen.encodeAsString(bytes).replaceAll("=", "");
		return token;
	}

}
