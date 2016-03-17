package cn.eoe.app.utils;

import java.security.SecureRandom;
import java.util.Date;

import cn.eoe.app.yf.config.Urls;
import android.app.Activity;
import android.util.DisplayMetrics;

public class Utility {
	private static final int MAX_NONCE = 0 + 10;

	private static final String LABEL_App_sign = "api_sign";
	private static final String LABEL_TIME = "timestamp";
	private static final String LABEL_NONCE = "nonce";
	private static final String LABEL_UID = "uid";

	private static final SecureRandom sRandom = new SecureRandom();

	private static char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static String getNonce() {
		byte[] bytes = new byte[MAX_NONCE / 2];
		sRandom.nextBytes(bytes);
		return hexString(bytes);
	}

	public static String hexString(byte[] source) {
		if (source == null || source.length <= 0) {
			return "";
		}

		final int size = source.length;
		final char str[] = new char[size * 2];
		int index = 0;
		byte b;
		for (int i = 0; i < size; i++) {
			b = source[i];
			str[index++] = sHexDigits[b >>> 4 & 0xf];
			str[index++] = sHexDigits[b & 0xf];
		}
		return new String(str);
	}

	// 获得时间戳
	private static long getTimestamp() {
		Date date = new Date();
		long i = date.getTime();
		return i;
	}

	private static String getAPIsig(String key, long timestamp, String nonce,
			String uid) {
		// api_sig =
		// MD5("api_key"+@api_key+"nonce"+@nonce+"timestamp"+@timestamp)
		String result = null;
		StringBuilder builder = new StringBuilder();
		synchronized (builder) {
			builder.append(key);
			builder.append(timestamp);
			builder.append(nonce);
			builder.append(uid);
			result = MD5.encode(builder.toString());
			builder.delete(0, builder.length());
		}
		return result;
	}

	/**
	 * &…………………………
	 * 
	 * @param key
	 * @return
	 */
	public static String getParams(String key) {
		String result = "";
		try {
			String[] temp = key.split(":");
			long timestamp = getTimestamp();
			String nonce = getNonce();
			String api_sign = getAPIsig(key, timestamp, nonce, temp[1]);

			StringBuilder builder = new StringBuilder();

			synchronized (result) {
				builder.append(String.format("&" + LABEL_UID + "=%s", temp[1]));
				builder.append(String.format("&" + LABEL_NONCE + "=%s", nonce));
				builder.append(String.format("&" + LABEL_TIME + "=%s",
						timestamp));
				builder.append(String.format("&" + LABEL_App_sign + "=%s",
						api_sign));
				result = builder.toString();
				builder.delete(0, builder.length());
			}
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * Description 获得屏幕参数
	 * 
	 * @param activity
	 * @return 屏幕链接参数字符串
	 */
	public static String getScreenParams(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return "&screen="
				+ (dm.heightPixels > dm.widthPixels ? dm.widthPixels + "*"
						+ dm.heightPixels : dm.heightPixels + "*"
						+ dm.widthPixels);
	}

	/*
	 * 重新构造链接 http://www.yfzww.com/rankjson2/50000/0_3.htm
	 */
	public static String ReconstructMoreUrl(String more_url) {
		if (!more_url.isEmpty()) {
			String[] urls = more_url.split("/");
			if (urls.length >= 2) {
				// 2 表示男频, 类别，页码
				String new_more_url = String.format(Urls.YF_CLASS_URL, "2",
						urls[urls.length - 2], urls[urls.length - 1]);
				return new_more_url;
			}
			return null;
		}
		return null;
	}
}
