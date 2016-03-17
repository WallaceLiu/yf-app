package cn.eoe.app.yf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.eoe.app.R;
import cn.eoe.app.yf.biz.SearchBookDao;
import cn.eoe.app.yf.entity.SearchEntity;
import cn.eoe.app.yf.view.SearchBookFragment;
import cn.eoe.app.ui.base.BaseFragmentActivity;

public class SearchBookActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ImageView btnGohome;
	private EditText edtSearch;
	private LinearLayout loadLayout;
	// 因为检索是通用的，在提示时，要根据各个页面做相应的变化
	private String mTag;
	private InputMethodManager imm;
	private SearchBookDao searchDao;
	private ImageView mWait;

	private SearchBookFragment searchFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.searchbook_layout);

		Intent i = getIntent();
		mTag = i.getStringExtra("tag");

		initData();

		initView();
	}

	private void initData() {
		searchDao = new SearchBookDao(this);
		imm = (InputMethodManager) getApplicationContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
	}

	private void initView() {
		btnGohome = (ImageView) findViewById(R.id.btn_gohome);
		btnGohome.setOnClickListener(this);
		edtSearch = (EditText) findViewById(R.id.edt_search);
		loadLayout = (LinearLayout) findViewById(R.id.view_loading);

		loadLayout.setVisibility(View.GONE);

		edtSearch.setHint("即将为您搜索 " + mTag);
		edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					imm.showSoftInput(v, 0);
				} else {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});

		edtSearch.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (v.getTag() == null) {
						v.setTag(1);
						edtSearch.clearFocus();
						String searchContent = edtSearch.getText().toString();
						// search by mTag, ex. boy channel, girl channel, liter
						// channel
						searchDao.setValue(mTag, searchContent);
						new MyTask().execute(searchDao);

					} else {
						v.setTag(null);
					}
					return true;
				}
				return false;
			}
		});
		mWait = (ImageView) findViewById(R.id.search_imageview_wait);
	}

	/**
	 * application icon<br/>
	 * back button
	 * 
	 * @param v
	 *            view
	 */
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_gohome:
			finish();
			break;
		}
	}

	/**
	 * search async task
	 * 
	 */
	public class MyTask extends AsyncTask<SearchBookDao, String, SearchEntity> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mWait.setVisibility(View.GONE);
			loadLayout.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected SearchEntity doInBackground(SearchBookDao... params) {
			SearchBookDao dao = params[0];
			SearchEntity list;
			if ((list = dao.mapperJson()) != null) {
				return list;
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(SearchEntity result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			loadLayout.setVisibility(View.GONE);

			searchFragment = new SearchBookFragment(SearchBookActivity.this,
					result);
			// must be import as blow
			// import android.support.v4.app.FragmentActivity;
			// import android.support.v4.app.FragmentTransaction;
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.book_container, searchFragment);
			fragmentTransaction.commit();
		}
	}
}
