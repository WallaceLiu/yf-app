package cn.eoe.app.utils;

import android.util.Log;

/**
 * Description：LogCat 调试日志
 */
public class CommonLog {
	private String tag = "CommonLog";
	public static int logLevel = Log.VERBOSE;
	public static boolean isDebug = true;

	public CommonLog() {
	}

	public CommonLog(String tag) {
		this.tag = tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();

		if (sts == null) {
			return null;
		}

		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}

			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}

			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}

			return "[" + Thread.currentThread().getId() + ": "
					+ st.getFileName() + ":" + st.getLineNumber() + "]";
		}

		return null;
	}

	// 信息
	public void info(Object str) {
		if (logLevel <= Log.INFO) {
			String name = getFunctionName();
			String ls = (name == null ? str.toString() : (name + " - " + str));
			Log.i(tag, ls);
		}
	}

	// 信息
	public void i(Object str) {
		if (isDebug) {
			info(str);
		}
	}

	// 详细
	public void verbose(Object str) {
		if (logLevel <= Log.VERBOSE) {
			String name = getFunctionName();
			String ls = (name == null ? str.toString() : (name + " - " + str));
			Log.v(tag, ls);
		}
	}

	// 详细
	public void v(Object str) {
		if (isDebug) {
			verbose(str);
		}
	}

	// 警告
	public void warn(Object str) {
		if (logLevel <= Log.WARN) {
			String name = getFunctionName();
			String ls = (name == null ? str.toString() : (name + " - " + str));
			Log.w(tag, ls);
		}
	}

	// 警告
	public void w(Object str) {
		if (isDebug) {
			warn(str);
		}
	}

	// 错误
	public void error(Object str) {
		if (logLevel <= Log.ERROR) {
			String name = getFunctionName();
			String ls = (name == null ? str.toString() : (name + " - " + str));
			Log.e(tag, ls);
		}
	}

	// 错误
	public void error(Exception ex) {
		if (logLevel <= Log.ERROR) {
			StringBuffer sb = new StringBuffer();
			String name = getFunctionName();
			StackTraceElement[] sts = ex.getStackTrace();

			if (name != null) {
				sb.append(name + " - " + ex + "\r\n");
			} else {
				sb.append(ex + "\r\n");
			}

			if (sts != null && sts.length > 0) {
				for (StackTraceElement st : sts) {
					if (st != null) {
						sb.append("[ " + st.getFileName() + ":"
								+ st.getLineNumber() + " ]\r\n");
					}
				}
			}

			Log.e(tag, sb.toString());
		}
	}

	// 错误
	public void e(Object str) {
		if (isDebug) {
			error(str);
		}
	}

	// 错误
	public void e(Exception ex) {
		if (isDebug) {
			error(ex);
		}
	}

	// 调试
	public void debug(Object str) {
		if (logLevel <= Log.DEBUG) {
			String name = getFunctionName();
			String ls = (name == null ? str.toString() : (name + " - " + str));
			Log.d(tag, ls);
		}
	}

	// 调试
	public void d(Object str) {
		if (isDebug) {
			debug(str);
		}
	}
}