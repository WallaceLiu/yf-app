package cn.eoe.app.yf.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import cn.eoe.app.R;
import cn.eoe.app.yf.biz.DetailDao;
import cn.eoe.app.yf.config.Urls;
import cn.eoe.app.yf.entity.ChapterEntity;
import cn.eoe.app.db.DetailColumn;
import cn.eoe.app.db.biz.DetailDB;
import cn.eoe.app.entity.DetailResponseEntity;
import cn.eoe.app.https.HttpUtils;
import cn.eoe.app.https.NetWorkHelper;
import cn.eoe.app.yf.ui.DetailsDiscussActivity;
import cn.eoe.app.ui.UserLoginUidActivity;
import cn.eoe.app.ui.base.BaseActivity;
import cn.eoe.app.utils.ImageUtil;
import cn.eoe.app.utils.IntentUtil;
import cn.eoe.app.utils.Utility;
import cn.eoe.app.utils.ImageUtil.ImageCallback;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * look chapters list of book.
 */
public class DetailsBookActivity extends BaseActivity implements
		OnClickListener {

	private static final String TAG = "DetailsBookActivity";
	// 业务层，minetype，编码
	private Activity mActivity;
	private DetailDao detailDao;
	private DetailResponseEntity responseEntity;
	private List<ChapterEntity> chapters;
	static final String mimeType = "text/html";
	static final String encoding = "utf-8";

	private TextView bookName;
	private TextView author;
	private TextView introduction;
	private ImageView imgThu;
	// 文章的详细内容页面底部的操作栏
	// 太棒了，一般般，收藏，分享，评论等操作，及其图标
	private RelativeLayout good;
	private RelativeLayout bed;
	private RelativeLayout collect;
	private RelativeLayout share;
	private RelativeLayout discuss;

	private ImageView imgGood;
	private ImageView imgBed;
	private ImageView imgCollect;
	private ImageView imgShare;
	private ImageView imgDiscuss;

	private boolean IsGood = false;
	private boolean IsBed = false;
	private boolean IsCollect = false;
	private TextView detailTitle;
	// 加载文章的等待界面
	private LinearLayout loadLayout;
	// 加载文章失败的界面
	private LinearLayout failLayout;
	// 加载文章失败时的刷新按钮
	private Button bn_refresh;
	// 操作栏应用图标 - 返回
	private ImageView imgGoHome;
	// 显示网页的文章
	// private WebView mWebView;
	private ListView listChapters;
	private String mUrl;
	private String mTitle;
	private String shareUrl;
	private String shareTitle;
	private String bookTitle;
	private String authorName;
	private String intro;
	private String imgThuUrl;
	private String chaptersUrl;
	private String bookId;

	private int screen_width;

	SharedPreferences sharePre;
	// 文章信息存入 SQLite 数据库
	private DetailDB mDetailDB;
	private String mKey;
	private int mDBID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// detailsbook_activity is Summary page, including
		// thumb, author, name, chapter list of book
		setContentView(R.layout.detailsbook_activity);

		Intent i = getIntent();
		// mUrl = i.getStringExtra("url");
		// mTitle = i.getStringExtra("title");
		shareTitle = i.getStringExtra("sharetitle");
		bookId = i.getStringExtra("bookid");
		bookTitle = i.getStringExtra("bookTitle");
		authorName = i.getStringExtra("authorName");
		intro = i.getStringExtra("intro");
		imgThuUrl = i.getStringExtra("imgThuUrl");
		chaptersUrl = i.getStringExtra("chapterUrls");
		mUrl = String.format(Urls.YF_BOOK, bookId);

		sharePre = getSharedPreferences(UserLoginUidActivity.SharedName,
				Context.MODE_PRIVATE);
		mKey = sharePre.getString(UserLoginUidActivity.KEY, "");

		initData();
		initControl();
		initAppraise();
	}

	/*
	 * init.<br/>
	 * 
	 * screen width
	 */
	private void initData() {
		mActivity = this;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screen_width = dm.widthPixels;
	}

	private void initControl() {
		detailDao = new DetailDao(this, chaptersUrl);
		detailTitle = (TextView) findViewById(R.id.details_textview_title);
		detailTitle.setText(shareTitle);

		imgThu = (ImageView) findViewById(R.id.item_imgthu);
		bookName = (TextView) findViewById(R.id.item_bookName);
		author = (TextView) findViewById(R.id.item_author);
		introduction = (TextView) findViewById(R.id.item_introduction);
		listChapters = (ListView) findViewById(R.id.item_bookchapters);

		bookName.setText(bookTitle);
		author.setText(authorName);
		introduction.setText(intro);
		/*
		 * init button and set listener, including good, bad, collect
		 */
		loadLayout = (LinearLayout) findViewById(R.id.view_loading);
		failLayout = (LinearLayout) findViewById(R.id.view_load_fail);
		bn_refresh = (Button) findViewById(R.id.bn_refresh);
		good = (RelativeLayout) findViewById(R.id.brlBGood);
		bed = (RelativeLayout) findViewById(R.id.brlBBed);
		collect = (RelativeLayout) findViewById(R.id.brlBCollect);
		share = (RelativeLayout) findViewById(R.id.brlBShare);
		discuss = (RelativeLayout) findViewById(R.id.brlBDiscuss);

		ImageUtil.setThumbnailView(imgThuUrl, imgThu, this, callback1, false);

		loadLayout.setVisibility(View.GONE);
		failLayout.setVisibility(View.GONE);

		good.setOnClickListener(this);
		bed.setOnClickListener(this);
		collect.setOnClickListener(this);
		share.setOnClickListener(this);
		discuss.setOnClickListener(this);
		bn_refresh.setOnClickListener(this);

		imgGood = (ImageView) findViewById(R.id.imageview_details_good);
		imgBed = (ImageView) findViewById(R.id.imageview_details_bed);
		imgCollect = (ImageView) findViewById(R.id.imageview_details_collect);
		imgShare = (ImageView) findViewById(R.id.imageview_details_share);
		imgDiscuss = (ImageView) findViewById(R.id.imageview_details_discuss);
		imgGoHome = (ImageView) findViewById(R.id.details_imageview_gohome);
		imgGoHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		MyTask mTask = new MyTask();
		mTask.execute();
	}

	// 初始化评价
	// 设置评价图标
	// 若曾对文章进行了评价，则再次查看时，需要设置相应的图标
	private void initAppraise() {
		mDetailDB = new DetailDB(this);
		Cursor cursor = mDetailDB.querySQL(mUrl);
		if (cursor.moveToFirst()) {
			mDBID = cursor.getInt(cursor.getColumnIndex(DetailColumn._ID));
			setAppraise(
					cursor.getInt(cursor.getColumnIndex(DetailColumn.GOOD)),
					cursor.getInt(cursor.getColumnIndex(DetailColumn.BAD)),
					cursor.getInt(cursor.getColumnIndex(DetailColumn.COLLECT)));
		}
	}

	// init 初始化评价（太棒了，一般般，收藏） icon
	public void setAppraise(int good, int bad, int collect) {
		initGood(IsGood = (good == 1));
		initBad(IsBed = (bad == 1));
		initCollect(IsCollect = (collect == 1));
	}

	private void initGood(boolean b) {
		if (b) {
			imgGood.setImageResource(R.drawable.button_details_good_selected);
		} else {
			imgGood.setImageResource(R.drawable.button_details_good_default);
		}
	}

	private void initBad(boolean b) {
		if (b) {
			imgBed.setImageResource(R.drawable.button_details_bed_selected);
		} else {
			imgBed.setImageResource(R.drawable.button_details_bed_default);
		}
	}

	private void initCollect(boolean b) {
		if (b) {
			imgCollect
					.setImageResource(R.drawable.button_details_collect_selected);
		} else {
			imgCollect
					.setImageResource(R.drawable.button_details_collect_default);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mKey.equals(null) && mKey.equals("")) {
			return;
		}
		if (mDBID == -1 && (IsGood || IsBed || IsCollect)) {
			// add
			mDetailDB.insertSQL(mUrl, mKey, IsGood ? 1 : 0, IsBed ? 1 : 0,
					IsCollect ? 1 : 0);
		} else if (mDBID != -1) {
			// update
			mDetailDB.updateSQL(mDBID, IsGood ? 1 : 0, IsBed ? 1 : 0,
					IsCollect ? 1 : 0);
		}
		mDetailDB.dbClose();
	}

	/**
	 * load chapters list
	 */
	class MyTask extends AsyncTask<String, Integer, List<ChapterEntity>> {

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
			loadLayout.setVisibility(View.VISIBLE);
			failLayout.setVisibility(View.GONE);
		}

		// 请求链接获得文章内容
		@Override
		protected List<ChapterEntity> doInBackground(String... params) {
			// TODO Auto-generated method stub
			if ((chapters = detailDao.chaptersMapperJson(mUseCache)) != null) {
				Log.i(TAG, String.format("chapters : %s", chapters.size()));
				return chapters;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<ChapterEntity> chapterList) {
			super.onPostExecute(chapterList);
			if (chapterList != null) {
				// 创建一个List集合，List集合的元素是Map
				final List<Map<String, Object>> chaptersList = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < chapterList.size(); i++) {
					ChapterEntity chapter = chapterList.get(i);
					Map<String, Object> chapterItem = new HashMap<String, Object>();
					chapterItem.put("chapterId", chapter.getChapterId());
					chapterItem.put("chapterName", chapter.getChapterName());
					chaptersList.add(chapterItem);
				}

				// create SimpleAdapter and set list,
				// create read book listener
				SimpleAdapter simpleAdapter = new SimpleAdapter(mActivity,
						chaptersList, R.layout.detailsbook_item,
						new String[] { "chapterName" },
						new int[] { R.id.item_chapter });
				listChapters.setAdapter(simpleAdapter);
				listChapters.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Map<String, Object> item = chaptersList.get(position);
						Intent intent = new Intent();
						intent.setClass(DetailsBookActivity.this,
								PageTurningActivity.class);
						Bundle paras = new Bundle();
						paras.putString("url", String.format(
								Urls.YF_BOOKCONTENT_URL, bookId,
								item.get("chapterId").toString()));
						paras.putString("chaptername", item.get("chapterName")
								.toString());
						intent.putExtras(paras);
						startActivity(intent);
					}
				});
				listChapters
						.setOnItemSelectedListener(new OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								Map<String, Object> item = chaptersList
										.get(position);
								Intent intent = new Intent();
								intent.setClass(DetailsBookActivity.this,
										PageTurningActivity.class);
								Bundle paras = new Bundle();
								paras.putString("url", String.format(
										Urls.YF_BOOKCONTENT_URL, bookId, item
												.get("chapterId").toString()));
								paras.putString("chaptername",
										item.get("chapterName").toString());
								intent.putExtras(paras);
								startActivity(intent);
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
							}
						});

				// if success, then load page and fail page will be hide.
				loadLayout.setVisibility(View.GONE);
				failLayout.setVisibility(View.GONE);
			} else {
				loadLayout.setVisibility(View.GONE);
				failLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	/*
	 * bottom operation.<br/>
	 * 
	 * must be login before this operation
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bn_refresh) {
			new MyTask().execute();
			return;
		}
		if (chapters == null)
			return;
		if (mKey.equals(null) || mKey.equals("")) {
			showLongToast(getResources().getString(R.string.user_login_prompt));
			return;
		}
		String url = null;
		switch (v.getId()) {
		case R.id.brlBGood: // 好
			// url = updateUrl(IsGood, responseEntity.getBar().getUserlike()
			// .getGood());
			url = updateUrl(IsGood, Urls.YF_PREFERENCE_GOOD);
			break;
		case R.id.brlBBed: // 坏
			// url = updateUrl(IsBed, responseEntity.getBar().getUserlike()
			// .getBad());
			url = updateUrl(IsBed, Urls.YF_PREFERENCE_BAD);
			break;
		case R.id.brlBCollect: // 收藏
			// url = updateUrl(IsCollect,
			// responseEntity.getBar().getFavorite());
			url = updateUrl(IsCollect, Urls.YF_PREFERENCE_COLLECT);
			break;
		case R.id.brlBShare: // 分享
			// recommandToYourFriend(shareUrl, shareTitle);
			break;
		case R.id.brlBDiscuss: // 评论
			// IntentUtil.start_activity(DetailsBookActivity.this,
			// DetailsDiscussActivity.class, new BasicNameValuePair(
			// "discuss_list", responseEntity.getBar()
			// .getComment().getGet()),
			// new BasicNameValuePair("discuss", responseEntity.getBar()
			// .getComment().getSubmit()), new BasicNameValuePair(
			// "title", mTitle));
			IntentUtil.start_activity(
					DetailsBookActivity.this,
					DetailsDiscussActivity.class,
					new BasicNameValuePair("title", mTitle),
					new BasicNameValuePair("bookid", this.bookId),
					new BasicNameValuePair("discuss_list", String.format(
							Urls.YF_GETDISCUSE, this.bookId)),
					new BasicNameValuePair("uid", sharePre.getString(
							UserLoginUidActivity.UID, "")));

			break;
		}
		// good, bad, collect webserver operation
		if (url != null) {
			if (!NetWorkHelper.checkNetState(this)) {
				showLongToast(getResources().getString(R.string.httpisNull));
				return;
			}
			new LoginAsyncTask().execute(url, v.getId());
		}
	}

	/**
	 * create url of good, bad, collect
	 * 
	 * @param isAdd
	 *            if isAdd is true, then ok, and otherwise cancel
	 * @param url
	 *            base url for create new request url
	 */
	private String updateUrl(boolean isAdd, String url) {
		String result = url;
		if (isAdd) {
			result = result.replaceAll("add", "del");
		}
		String key = sharePre.getString(UserLoginUidActivity.KEY, "");
		result = String.format(result, key, bookId, isAdd);
		// result += Utility.getParams(key);
		return result;
	}

	/*
	 * async task.</br>
	 * 
	 * must be login before good, bad, collect operation.
	 */
	class LoginAsyncTask extends AsyncTask<Object, Void, Boolean> {

		private int id;

		@Override
		protected Boolean doInBackground(Object... params) {
			// TODO Auto-generated method stub
			id = Integer.parseInt(params[1].toString()); // control ID
			// if (!HttpUtils.isNetworkAvailable(DetailsActivity.this)) {
			// showLongToast(getResources().getString(R.string.httpisNull));
			// return false;
			// }
			String result;
			try {
				result = HttpUtils.getByHttpClient(DetailsBookActivity.this,
						params[0].toString());
				JSONObject jsonObj = new JSONObject(result);
				boolean bl = jsonObj.getBoolean("status");
				return bl;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			switch (id) {
			case R.id.brlBGood:
				if (result) {
					IsGood = !IsGood;
					initGood(IsGood);
					showShortToast("评价成功");
				} else {
					showShortToast("评价失败");
				}
				break;
			case R.id.brlBBed:
				if (result) {
					IsBed = !IsBed;
					initBad(IsBed);
					showShortToast("评价成功");
				} else {
					showShortToast("评价失败");
				}
				break;
			case R.id.brlBCollect:
				if (result) {
					IsCollect = !IsCollect;
					initCollect(IsCollect);
					if (IsCollect) {
						showShortToast("收藏成功");
					} else {
						showShortToast("取消收藏成功");
					}
				} else {
					showShortToast("收藏操作失败");
				}
				break;
			}
		}
	}

	public cn.eoe.app.utils.ImageUtil.ImageCallback callback1 = new ImageCallback() {

		@Override
		public void loadImage(Bitmap bitmap, String imagePath) {
			// TODO Auto-generated method stub
			try {
				imgThu.setImageBitmap(bitmap);
			} catch (NullPointerException ex) {
				Log.e("error", "ImageView = null");
			}
		}
	};
}