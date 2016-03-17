package cn.eoe.app.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;

import cn.eoe.app.R;
import cn.eoe.app.yf.biz.DetailDao;
import cn.eoe.app.yf.config.Constants;
import cn.eoe.app.yf.config.Urls;
import cn.eoe.app.yf.entity.ChapterEntity;
import cn.eoe.app.yf.ui.DetailsBookActivity;
import cn.eoe.app.yf.ui.PageTurningActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/*翻书分页*/
public class BookPageFactory {
	private static final String TAG = "BookPageFactory";
	private static int DayCount = 15;// 天数
	private static final long CLEARTIME = DayCount * 24 * 60 * 60 * 1000;

	private Activity mActivity;
	private File book_file = null;
	private MappedByteBuffer m_mbBuff = null;
	private int m_mbBuffLen = 0;
	private int m_mbBufBegin = 0;
	private int m_mbBufEnd = 0;
	private byte[] m_mChapterbBuff = null;
	private int mChapterLineCount = 0;
	private int m_mChapterbBuffLen = 0;
	private int m_mChapterbBufBegin = 0;
	private int m_mChapterbBuffEnd = 0;
	private String m_strCharsetName = "GBK";

	private Bitmap m_book_bg = null;
	private int mWidth;
	private int mHeight;

	private float mMiddleWidth;
	private float mMiddleHeight;

	private Vector<String> m_chapter_lines = new Vector<String>();
	private Vector<String> m_lines = new Vector<String>();
	/* 字体大小，字体颜色，字体背景颜色，左右与边缘的距离，上下与边缘的距离 */
	private int font_size = 24;
	private int font_chapter_size = 25;
	private int font_textColor = Color.BLACK;
	private int font_bgColor = 0xffff9e85;
	private int font_marginWidth = 15;
	private int font_marginHeight = 20;

	private float mVisibleHeight; // 绘制内容的高
	private float mVisibleWidth; // 绘制内容的宽
	private int mLineCount; // 每页可以显示的行数

	private boolean m_isfirstPage, m_islastPage;

	// private int m_nLineSpaceing = 5;

	private Paint mPaint;

	private DetailDao detailDao;
	private String bookFileMD5;
	private String bookFilePath;
	private String bookFileName;
	private String bookFullName;
	private String bookContentUrl;

	// private Handler handler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// // TODO 接收消息并且去更新UI线程上的控件内容
	// if (msg.what == 0x123) {
	// bookFileMD5 = MD5.encode(bookContentUrl);
	// bookFilePath = getExternalCacheDir(mActivity);
	// bookFileName = bookFileMD5 + ".txt";
	// bookFullName = bookFilePath + File.separator + bookFileName;
	//
	// ObjectMapper mObjectMapper = new ObjectMapper();
	// // detailDao.setUrl(bookContentUrl);
	// // Map<String, String> bookContent = detailDao
	// // .contentMapperJson(false);
	// String result = RequestCacheUtil.getRequestContent(mActivity,
	// bookContentUrl, Constants.WebSourceType.Json,
	// Constants.DBContentType.Content_content, false);
	//
	// Map<String, String> bookContent = null;
	// try {
	// bookContent = mObjectMapper.readValue(result, Map.class);
	// } catch (JsonParseException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// } catch (JsonMappingException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// String contentStr = bookContent.get("Content");
	//
	// if (contentStr != "" && contentStr != null) {
	// saveBook(bookFilePath, bookFileName, bookContent);
	//
	// book_file = new File(bookFullName);
	// long lLen = book_file.length();
	// m_mbBuffLen = (int) lLen;
	// try {
	// m_mbBuff = new RandomAccessFile(book_file, "r")
	// .getChannel().map(
	// FileChannel.MapMode.READ_ONLY, 0, lLen);
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	// super.handleMessage(msg);
	// }
	// };

	public BookPageFactory(Activity activity, int width, int height) {
		mActivity = activity;
		mWidth = width;
		mHeight = height;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(font_size);
		mPaint.setColor(font_textColor);

		mVisibleWidth = mWidth - font_marginWidth * 2;
		mVisibleHeight = mHeight - font_marginHeight * 2;

		mMiddleWidth = mWidth / 2;
		mMiddleHeight = mHeight / 2;

		// 可显示的行数
		mLineCount = (int) (mVisibleHeight / font_size);

		detailDao = new DetailDao(mActivity, null);
	}

	/* 打开书 */
	@SuppressWarnings("resource")
	public void openbook(String strFilePath) throws IOException {
		book_file = new File(strFilePath);
		long lLen = book_file.length();
		m_mbBuffLen = (int) lLen;
		m_mbBuff = new RandomAccessFile(book_file, "r").getChannel().map(
				FileChannel.MapMode.READ_ONLY, 0, lLen);
	}

	public void openbookUrl(String contentUrl, boolean useCache)
			throws IOException {
		bookContentUrl = contentUrl;

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// Message msg = new Message();
		// msg.what = 0x123;
		// handler.sendMessage(msg);
		// }
		// }).start();
		MyTask mytask = new MyTask();
		mytask.execute();
	}

	public static String getExternalCacheDir(Context context) {
		// android 2.2 以后才支持的特性
		if (hasExternalCacheDir()) {
			return context.getExternalCacheDir().getPath() + File.separator
					+ "b";
		}

		// Before Froyo we need to construct the external cache dir ourselves
		// 2.2以前我们需要自己构造
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/b/";
		return Environment.getExternalStorageDirectory().getPath() + cacheDir;
	}

	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public void saveBook(String foldername, String fileName,
			Map<String, String> bookContent) {
		File folder = new File(foldername);
		if (folder != null && !folder.exists()) {
			if (!folder.mkdir() && !folder.isDirectory()) {
				Log.d(TAG, "Error: make dir failed!");
				return;
			}
		}

		String targetPath = foldername + File.separator + fileName;
		File targetFile = new File(targetPath);
		if (targetFile != null) {
			if (targetFile.exists()) {
				targetFile.delete();
			}

			OutputStreamWriter osw;
			try {
				osw = new OutputStreamWriter(new FileOutputStream(targetFile),
						"GBK");
				try {
					String content = bookContent.get("Content");
					osw.write(content);
					osw.flush();
					osw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	protected byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte b0, b1;
		if (m_strCharsetName.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuff.get(i);
				b1 = m_mbBuff.get(i + 1);
				if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}

		} else if (m_strCharsetName.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuff.get(i);
				b1 = m_mbBuff.get(i + 1);
				if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				b0 = m_mbBuff.get(i);
				if (b0 == 0x0a && i != nEnd - 1) {
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++) {
			buf[j] = m_mbBuff.get(i + j);
		}
		return buf;
	}

	/* 读取上一段落 */
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mbBuffLen - 1) {
				b0 = m_mbBuff.get(i++);
				b1 = m_mbBuff.get(i++);
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBuffLen - 1) {
				b0 = m_mbBuff.get(i++);
				b1 = m_mbBuff.get(i++);
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mbBuffLen) {
				b0 = m_mbBuff.get(i++);
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mbBuff.get(nFromPos + i);
		}
		return buf;
	}

	/* 读取上一段落 */
	protected byte[] readChapterNameForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mChapterbBuffLen - 1) {
				b0 = m_mChapterbBuff[i++];
				b1 = m_mChapterbBuff[i++];
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBuffLen - 1) {
				b0 = m_mChapterbBuff[i++];
				b1 = m_mChapterbBuff[i++];
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mChapterbBuffLen) {
				b0 = m_mChapterbBuff[i++];
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mChapterbBuff[nFromPos + i];
		}
		return buf;
	}

	protected Vector<String> pageDown() {
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mLineCount && m_mbBufEnd < m_mbBuffLen) {
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
			m_mbBufEnd += paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
				if (lines.size() >= mLineCount) {
					break;
				}
			}
			if (strParagraph.length() != 0) {
				try {
					m_mbBufEnd -= (strParagraph + strReturn)
							.getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	protected Vector<String> pageDownChapterName(String chapterName)
			throws UnsupportedEncodingException {
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		mChapterLineCount = (int) (mVisibleHeight / font_chapter_size);
		m_mChapterbBuff = chapterName.getBytes(m_strCharsetName);
		m_mChapterbBuffEnd = 0;
		m_mChapterbBuffLen = m_mChapterbBuff.length;

		while (lines.size() < mChapterLineCount
				&& m_mChapterbBuffEnd < m_mChapterbBuffLen) {
			// 读取一个段落
			byte[] paraBuf = readChapterNameForward(m_mChapterbBuffEnd);
			m_mChapterbBuffEnd += paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
				if (lines.size() >= mChapterLineCount) {
					break;
				}
			}
			if (strParagraph.length() != 0) {
				try {
					m_mChapterbBuffEnd -= (strParagraph + strReturn)
							.getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	protected void pageUp() {
		if (m_mbBufBegin < 0)
			m_mbBufBegin = 0;
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mLineCount && m_mbBufBegin > 0) {
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}
		while (lines.size() > mLineCount) {
			try {
				m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m_mbBufEnd = m_mbBufBegin;
		return;
	}

	public void prePage() throws IOException {
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage = true;
			return;
		} else
			m_isfirstPage = false;
		m_lines.clear();
		pageUp();
		m_lines = pageDown();
	}

	public void nextPage() throws IOException {
		if (m_mbBufEnd >= m_mbBuffLen) {
			m_islastPage = true;
			return;
		} else
			m_islastPage = false;
		m_lines.clear();
		m_mbBufBegin = m_mbBufEnd;
		m_lines = pageDown();
	}

	/* 绘制当前书页 */
	public void Draw(Canvas c) {
		if (m_lines.size() == 0)
			m_lines = pageDown();
		if (m_lines.size() > 0) {
			if (m_book_bg == null)
				c.drawColor(font_bgColor);
			else
				c.drawBitmap(m_book_bg, 0, 0, null);
			int y = font_marginHeight;
			for (String strLine : m_lines) {
				y += font_size;
				c.drawText(strLine, font_marginWidth, y, mPaint);
			}
		}
		float fPercent = (float) (m_mbBufBegin * 1.0 / m_mbBuffLen);
		DecimalFormat df = new DecimalFormat("#0.0");
		String strPercent = df.format((int) fPercent * 100) + "%";
		int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
		c.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint);
	}

	/* 绘制章节 */
	public void DrawChapter(Canvas c, String chapterName)
			throws UnsupportedEncodingException {
		m_chapter_lines.clear();

		if (m_chapter_lines.size() == 0)
			m_chapter_lines = pageDownChapterName(chapterName);
		if (m_chapter_lines.size() > 0) {
			if (m_book_bg == null)
				c.drawColor(font_bgColor);
			else
				c.drawBitmap(m_book_bg, 0, 0, null);
			int y = (int) mMiddleHeight - font_chapter_size
					* m_chapter_lines.size();
			for (String strLine : m_chapter_lines) {
				y += font_chapter_size;
				c.drawText(strLine, font_marginWidth - 2, y, mPaint);
			}
		}
	}

	/* 设置书页的背景 */
	public void setBgBitmap(Bitmap bgBitmap) {
		m_book_bg = bgBitmap;
	}

	/* 是否为第一页 */
	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	/* 是否为最后一页 */
	public boolean islastPage() {
		return m_islastPage;
	}

	class MyTask extends AsyncTask<String, Integer, String> {

		private boolean mUseCache;

		public MyTask() {
			mUseCache = false;
		}

		public MyTask(boolean useCache) {
			mUseCache = useCache;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			bookFileMD5 = MD5.encode(bookContentUrl);
			bookFilePath = getExternalCacheDir(mActivity);
			bookFileName = bookFileMD5 + ".txt";
			bookFullName = bookFilePath + File.separator + bookFileName;
		}

		// 请求链接互动额文章内容
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			detailDao.setUrl(bookContentUrl);
			Map<String, String> bookContent = detailDao
					.contentMapperJson(false);
			String contentStr = bookContent.get("Content");

			if (contentStr != "" && contentStr != null) {
				saveBook(bookFilePath, bookFileName, bookContent);

				book_file = new File(bookFullName);
				long lLen = book_file.length();
				m_mbBuffLen = (int) lLen;
				try {
					m_mbBuff = new RandomAccessFile(book_file, "r")
							.getChannel().map(FileChannel.MapMode.READ_ONLY, 0,
									lLen);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return contentStr;
		}

		@Override
		protected void onPostExecute(String bookContent) {
			super.onPostExecute(bookContent);

		}
	}
}
