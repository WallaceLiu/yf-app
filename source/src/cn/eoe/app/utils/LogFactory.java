package cn.eoe.app.utils;

/**
 * Description LogCat 调试日志工厂
 */
public class LogFactory {
	private static final String TAG = "YF_CN";// "EOE_CN";
	private static CommonLog log = null;

	public static CommonLog createLog() {
		if (log == null) {
			log = new CommonLog();
		}

		log.setTag(TAG);
		return log;
	}

	public static CommonLog createLog(String tag) {
		if (log == null) {
			log = new CommonLog();
		}

		if (tag == null || tag.length() < 1) {
			log.setTag(TAG);
		} else {
			log.setTag(tag);
		}
		return log;
	}
}