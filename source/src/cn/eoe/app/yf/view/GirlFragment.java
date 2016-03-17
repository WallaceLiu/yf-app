package cn.eoe.app.yf.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.eoe.app.R;
import cn.eoe.app.yf.biz.GirlDao;
import cn.eoe.app.ui.DetailsActivity;
import cn.eoe.app.utils.ImageUtil;
import cn.eoe.app.utils.IntentUtil;
import cn.eoe.app.utils.Utility;
import cn.eoe.app.utils.ImageUtil.ImageCallback;
import cn.eoe.app.view.BaseListFragment;
import cn.eoe.app.widget.XListView;
import cn.eoe.app.widget.XListView.IXListViewListener;
import cn.eoe.app.yf.entity.BookCategoryListEntity;
import cn.eoe.app.yf.entity.BookContentItemEntity;
import cn.eoe.app.yf.entity.BooksMoreResponse;

@SuppressLint("ValidFragment")
public class GirlFragment extends BaseListFragment {

	List<BookContentItemEntity> items_list = new ArrayList<BookContentItemEntity>();
	private Activity mActivity;
	private String more_url;
	private BookCategoryListEntity loadMoreEntity;
	private MyAdapter mAdapter;

	public GirlFragment() {
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0: // 获得更多文章列表的 url
				more_url = loadMoreEntity.getNextUrl();
				more_url = Utility.ReconstructMoreUrl(more_url);
				mAdapter.appendToList(loadMoreEntity.getItems());

				break;
			}
			onLoad();
		};
	};

	public GirlFragment(Activity c, BookCategoryListEntity categorys) {
		this.mActivity = c;

		if (categorys != null) {
			more_url = categorys.getNextUrl();
			more_url = Utility.ReconstructMoreUrl(more_url);
			this.items_list = categorys.getItems();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		listview.setXListViewListener(this);
		// construct the RelativeLayout
		listview.setXListViewListener(this);
		// construct the RelativeLayout
		mAdapter = new MyAdapter();
		mAdapter.appendToList(items_list);
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BookContentItemEntity item = (BookContentItemEntity) mAdapter
						.getItem(position - 1);
				startDetailBookActivity(mActivity, "女频", item.getBookName(),
						item.getAuthorName(), item.getBookImageUrl(), item
								.getChaptersJsonUrl(), item.getIntroduction(),
						item.getBookId().toString());
				// startDetailActivity(mActivity, item.getBookUrl(), "女频",
				// item.getBookName());
			}
		});
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	class MyAdapter extends BaseAdapter {

		List<BookContentItemEntity> mList = new ArrayList<BookContentItemEntity>();

		public MyAdapter() {

		}

		public void appendToList(List<BookContentItemEntity> lists) {

			if (lists == null) {
				return;
			}
			mList.addAll(lists);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			BookContentItemEntity item = mList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater
						.inflate(R.layout.book_item_layout, null);
				holder.header_ = (TextView) convertView
						.findViewById(R.id.tx_header_title);
				holder.title_ = (TextView) convertView
						.findViewById(R.id.txt_title);
				holder.short_ = (TextView) convertView
						.findViewById(R.id.txt_short_content);
				holder.img_thu = (ImageView) convertView
						.findViewById(R.id.img_thu);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.header_.setText(item.getAuthorName());
			holder.title_.setText(item.getBookName());
			holder.short_.setText(item.getIntroduction());
			String url = item.getBookImageUrl().replaceAll("=small", "=middle");
			if (url.equals(null) || url.equals("")) {
				holder.img_thu.setVisibility(View.GONE);
			} else {
				holder.img_thu.setVisibility(View.VISIBLE);
				ImageUtil.setThumbnailView(url, holder.img_thu, mActivity,
						callback1, false);
			}
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView header_;
		public TextView title_;
		public TextView short_;
		public ImageView img_thu;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		onLoad();
	}

	/* 加载更多 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (more_url == null || more_url.equals("")) {
			mHandler.sendEmptyMessage(1);
			return;
		} else {

			new Thread() {
				@Override
				public void run() {
					BooksMoreResponse response = new GirlDao(mActivity)
							.getMore(more_url);
					if (response != null) {
						loadMoreEntity = response.getResponse();
						mHandler.sendEmptyMessage(0);
					}
				}
			}.start();
		}

	}
}