package cn.eoe.app.yf.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.eoe.app.R;
import cn.eoe.app.yf.biz.DiscussDao;
import cn.eoe.app.yf.config.Urls;
import cn.eoe.app.entity.DetailsDiscussItem;
import cn.eoe.app.entity.DetailsOwnDiscussJson;
import cn.eoe.app.https.HttpUtils;
import cn.eoe.app.ui.UserLoginUidActivity;
import cn.eoe.app.ui.base.BaseActivity;
import cn.eoe.app.utils.ImageUtil;
import cn.eoe.app.utils.ImageUtil.ImageCallback;
import cn.eoe.app.utils.Utility;

/**
 * book discuse
 */
public class DetailsDiscussActivity extends BaseActivity implements
		OnClickListener {
	private ListView mListview;
	private Button mEnter;
	private ImageView mGoBack;
	private EditText mEditDiscuss;
	private SimpleAdapter mAdapter;
	private List<Map<String, Object>> mlist;
	private TextView title;

	private String mDiscussList = "";
	private String mDiscuss = "";
	private String bookId = "";
	private String mUid = "";

	ObjectMapper mObjectMapper = new ObjectMapper();

	SharedPreferences share;
	private String mKey = "";
	private DiscussDao mDao;
	private String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.details_discuss_activity);

		share = getSharedPreferences(UserLoginUidActivity.SharedName,
				Context.MODE_PRIVATE);

		mDao = new DiscussDao(this);
		mKey = share.getString(UserLoginUidActivity.KEY, "");

		getIntentEntra();

		initControl();

		initList();

		new DataAsyncTask().execute(mDiscussList);
	}

	private void getIntentEntra() {
		Intent intent = getIntent();
		mDiscussList = intent.getStringExtra("discuss_list");
		// mDiscuss = intent.getStringExtra("discuss");

		mTitle = intent.getStringExtra("title");
		bookId = intent.getStringExtra("bookid");
		mUid = intent.getStringExtra("uid");
	}

	/*
	 * init control.<br/>
	 * 
	 * listview, title
	 */
	private void initControl() {
		mListview = (ListView) findViewById(R.id.details_listview_show);
		mEnter = (Button) findViewById(R.id.details_button_enter);
		mGoBack = (ImageView) findViewById(R.id.details_imageview_gohome);
		mEditDiscuss = (EditText) findViewById(R.id.details_edittext_discuss);
		mEnter.setOnClickListener(this);
		mGoBack.setOnClickListener(this);
		title = (TextView) findViewById(R.id.details_textview_title);
		title.setText(mTitle);
	}

	/*
	 * init discuse list.<br/>
	 * 
	 * image, name, content, time
	 */
	private void initList() {
		mlist = new ArrayList<Map<String, Object>>();
		mAdapter = new SimpleAdapter(this, mlist,
				R.layout.details_discuss_item, new String[] { "image", "name",
						"content", "time" }, new int[] {
						R.id.details_imageview_head,
						R.id.details_textview_name,
						R.id.details_textview_content,
						R.id.details_textview_time }) {
			@Override
			public void setViewImage(ImageView v, String value) {
				// TODO Auto-generated method stub
				super.setViewImage(v, value);
				ImageUtil.setThumbnailView(value, v,
						DetailsDiscussActivity.this, callback, false);
			}

			ImageCallback callback = new ImageCallback() {

				@Override
				public void loadImage(Bitmap bitmap, String imagePath) {
					// TODO Auto-generated method stub
					try {
						ImageView img = (ImageView) mListview
								.findViewWithTag(imagePath);
						img.setImageBitmap(bitmap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		};
		mListview.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.details_button_enter:
			//
			new PublishAsyncTask().execute();
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(DetailsDiscussActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.details_imageview_gohome:
			finish();
			break;
		}
	}

	class DataAsyncTask extends
			AsyncTask<String, Void, List<Map<String, Object>>> {
		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result;
			JSONArray jsonArray;
			try {
				result = HttpUtils.getByHttpClient(DetailsDiscussActivity.this,
						params[0]);
				if (result.isEmpty())
					return null;

				jsonArray = new JSONArray(result);

				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Map<String, Object> map;
				for (int i = 0; i < jsonArray.length(); i++) {
					map = new HashMap<String, Object>();
					JSONObject jo = (JSONObject) jsonArray.get(i);
					map.put("image", jo.getString("HEADIMAGEURL"));
					map.put("name", jo.getString("NICKNAME"));
					map.put("content", jo.getString("CONTENT"));
					map.put("time", jo.getString("TIME"));
					list.add(map);
				}
				return list;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == null)
				return;
			mlist.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}

	class PublishAsyncTask extends AsyncTask<Void, Void, Map<String, Object>> {

		private String getUrl(String url) {
			// return String.format(url + "&body=%s", mEditDiscuss.getText()
			// .toString())
			// + Utility.getParams(mKey);
			return String.format(Urls.YF_DISCUSE, bookId, mEditDiscuss
					.getText().toString())
					+ String.format("&loginname=%s", mUid);
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (mKey.equals(""))
				return null;
			String result = mDao.mapperJson(getUrl(mDiscuss), true);
			// JSONObject jo;
			if (result.contains("y")) {
				// DetailsDiscussItem item = json.getResponse().getItem();
				// JSONArray jsonArray;

				// jsonArray = new JSONArray(result);
				// jo = jsonArray.getJSONObject(0);

				Map<String, Object> map = new HashMap<String, Object>();
				// map.put("image", jo.get("headimageurl"));
				// map.put("name", jo.get("nickname"));
				// map.put("time", jo.get("time"));
				map.put("image", "");
				map.put("name", "");
				map.put("content", mEditDiscuss.getText().toString());
				map.put("time", "");

				return map;

			}
			return null;
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result == null) {
				showLongToast(getResources().getString(R.string.discuss_fail));
				return;
			}
			mlist.add(result);
			showLongToast(getResources().getString(R.string.discuss_succeed));
			mAdapter.notifyDataSetChanged();
		}
	}

	private String Date(String longtime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time = new Long(longtime + "000");
		String result = format.format(time);
		return result;
	}

}
