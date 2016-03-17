package cn.eoe.app.yf.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cn.eoe.app.R;
import cn.eoe.app.utils.BookPageFactory;
import cn.eoe.app.widget.PageTurning;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 * Description 看书
 */
public class PageTurningActivity extends Activity {

	private PageTurning mPageWidget;
	private Bitmap mCurPageBitmap, mNextPageBitmap;
	private Canvas mCurPageCanvas, mNextPageCanvas;
	private BookPageFactory pagefactory;

	private int mHeight;
	private int mWeight;
	private String mUrl;
	private String mChapterName;

	@SuppressLint("SdCardPath")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		initData();
		initScreen();
		initControl();
		initPageTurning();
	}

	// 初始化变量
	private void initData() {
		Intent i = getIntent();
		mUrl = i.getStringExtra("url");
		mChapterName = i.getStringExtra("chaptername");
	}

	// 手机屏幕大小
	private void initScreen() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mHeight = dm.heightPixels;
		mWeight = dm.widthPixels;
	}

	// 初始化控件，当前页和下一页的Bitmap，Canvas，翻书控件
	private void initControl() {
		mCurPageBitmap = Bitmap.createBitmap(mWeight, mHeight,
				Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(mWeight, mHeight,
				Bitmap.Config.ARGB_8888);
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);

		mPageWidget = new PageTurning(this);
	}

	// 初始化翻书效果
	private void initPageTurning() {
		pagefactory = new BookPageFactory(this, mWeight, mHeight);
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.bg_book_content));
		try {
			pagefactory.openbookUrl(mUrl, false);
			// pagefactory.Draw(mCurPageCanvas);
			pagefactory.DrawChapter(mCurPageCanvas, mChapterName);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}


		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		mPageWidget.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				boolean ret = false;
				if (v == mPageWidget) {

					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(e.getX(), e.getY());
						pagefactory.Draw(mCurPageCanvas);

						if (mPageWidget.DragToRight()) {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (pagefactory.isfirstPage())
								return false;
							pagefactory.Draw(mNextPageCanvas);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (pagefactory.islastPage())
								return false;
							pagefactory.Draw(mNextPageCanvas);
						}

						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}

					ret = mPageWidget.onTouchEvent(e);
					return ret;
				}
				return false;
			}
		});
		setContentView(mPageWidget);
	}
}