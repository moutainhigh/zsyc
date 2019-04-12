package com.zsyc.common;

import java.util.Arrays;

/**
 * Created by lcs on 2019-01-13.
 */
public class StringUtil {

	/**
	 * 驼峰转正式线
	 * @param str
	 * @return
	 */
	public static String camelToUnderline(String str) {
		if(str == null || str.length() == 0) return str;
		StringBuilder result = new StringBuilder();
		result.append(str.charAt(0));
		for (int i = 1, len = str.length(); i < len; i++) {
			if(Character.isUpperCase(str.charAt(i))){
				result.append("_");
			}
			result.append(str.charAt(i));
		}
		return result.toString().toLowerCase();
	}

}
