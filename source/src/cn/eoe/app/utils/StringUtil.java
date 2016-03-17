package cn.eoe.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * Description 字符串工具
 */
public class StringUtil {
	// Url 正则表达式
	public static final String URL_REG_EXPRESSION = "^(https?://)?([a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+)+(/*[A-Za-z0-9/\\-_&:?\\+=//.%]*)*";
	// EMAIL 正则表达式
	public static final String EMAIL_REG_EXPRESSION = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";

	/**
	 * Description 是否为 Url
	 * 
	 * @param s
	 * @return true 为是
	 */
	public static boolean isUrl(String s) {
		if (s == null) {
			return false;
		}
		return Pattern.matches(URL_REG_EXPRESSION, s);
	}

	/**
	 * Description 是否为 email
	 * 
	 * @param s
	 * @return true 为是
	 */
	public static boolean isEmail(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches(EMAIL_REG_EXPRESSION, s);
	}

	/**
	 * Description 是否为空
	 * 
	 * @param s
	 * @return true 为是
	 */
	public static boolean isBlank(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches("\\s*", s);
	}

	/**
	 * Description 字符串连接
	 * 
	 * @param spliter
	 *            分隔符
	 * @param arr
	 *            字符
	 * @return 通过分隔符连接的字符串
	 */
	public static String join(String spliter, Object[] arr) {
		if (arr == null || arr.length == 0) {
			return "";
		}
		if (spliter == null) {
			spliter = "";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1) {
				break;
			}
			if (arr[i] == null) {
				continue;
			}
			builder.append(arr[i].toString());
			builder.append(spliter);
		}
		return builder.toString();
	}

	/**
	 * Description 读文件
	 * 
	 * @param f
	 *            文件
	 * @return 文件内容
	 */
	public static String fromFile(File f) throws IOException {
		InputStream is = new FileInputStream(f);
		byte[] bs = new byte[is.available()];
		is.read(bs);
		is.close();
		return new String(bs);
	}

	/**
	 * Description 写文件
	 * 
	 * @param f
	 *            文件
	 * @param s
	 *            字符串
	 */
	public static void toFile(File f, String s) throws IOException {
		// 只有手机rom有足够的空间才写入本地缓存
		if (CommonUtil.enoughSpaceOnPhone(s.getBytes().length)) {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(s.getBytes());
			fos.close();
		}
	}
}
