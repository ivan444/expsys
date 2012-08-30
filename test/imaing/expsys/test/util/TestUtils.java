package imaing.expsys.test.util;

import imaing.expsys.client.domain.Shop;

import java.util.Random;

public class TestUtils {

	public static String generateRandEmail() {
		StringBuffer sb = new StringBuffer();
		sb.append(generateRandStr(10));
		sb.append('@');
		sb.append(generateRandStr(3));
		sb.append('.');
		sb.append(generateRandStr(3));
		return sb.toString();
	}
	
	public static String generateRandStr(int length) {
		char[] uniqChars = new char[length];
		
		for (int i = 0; i < uniqChars.length; i++) {
			uniqChars[i] = (char) ('a' + (new Random().nextInt('z'-'a')) );
		}
		
		return new String(uniqChars);
	}
	
	public static String generateRandStr(String prefix, int length) {
		return prefix + generateRandStr(length);
	}
	
	public static Shop newShop() {
		Shop shp = new Shop();
		shp.setEmail(TestUtils.generateRandEmail());
		shp.setPassword(TestUtils.generateRandStr(10));
		shp.setShopName(TestUtils.generateRandStr("shop_", 4));
		return shp;
	}
}
