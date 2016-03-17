package cn.eoe.app.view;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.eoe.app.R;
import cn.eoe.app.ui.DetailsActivity;
import cn.eoe.app.yf.ui.DetailsBookActivity;
import cn.eoe.app.utils.ImageUtil.ImageCallback;
import cn.eoe.app.utils.IntentUtil;
import cn.eoe.app.widget.XListView;
import cn.eoe.app.widget.XListView.IXListViewListener;

public abstract class BaseListFragment extends Fragment implements
		IXListViewListener {

	protected XListView listview;
	protected View view;
	public LayoutInflater mInflater;
	protected boolean mIsScroll = false;
	ObjectMapper mMapper = new ObjectMapper();
	protected BaseAdapter mAdapter;

	public ExecutorService executorService = Executors.newFixedThreadPool(5);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		view = inflater.inflate(R.layout.main, null);
		listview = (XListView) view.findViewById(R.id.list_view);
		initListView();
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void initListView() {
	}

	public void startDetailActivity(Activity mContext, String url,
			String title, String shareTitle) {
		IntentUtil.start_activity(mContext, DetailsActivity.class,
				new BasicNameValuePair("url", url), new BasicNameValuePair(
						"title", title), new BasicNameValuePair("sharetitle",
						shareTitle));
	}

	/**
	 * Description 显示书的章节信息
	 * 
	 * @param Activity
	 * @param 主标题
	 *            ，如男频
	 * @param 书名称
	 * @param 作者
	 * @param 图片URL地址
	 * @param 章节列表
	 */
	public void startDetailBookActivity(Activity mContext, String shareTitle,
			String bookTitle, String authorName, String imgThuUrl,
			String chapterUrls, String introduction, String bookId) {
		IntentUtil.start_activity(mContext, DetailsBookActivity.class,
				new BasicNameValuePair("sharetitle", shareTitle),
				new BasicNameValuePair("bookTitle", bookTitle),
				new BasicNameValuePair("authorName", authorName),
				new BasicNameValuePair("imgThuUrl", imgThuUrl),
				new BasicNameValuePair("chapterUrls", chapterUrls),
				new BasicNameValuePair("intro", introduction),
				new BasicNameValuePair("bookid", bookId));
	}

	public void startDetailBookActivity(Activity mContext, String bookId,
			String chapterId) {
		IntentUtil.start_activity(mContext, DetailsActivity.class,
				new BasicNameValuePair("bookid", bookId),
				new BasicNameValuePair("chapterid", chapterId));
	}

	protected void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}

	public cn.eoe.app.utils.ImageUtil.ImageCallback callback1 = new ImageCallback() {

		@Override
		public void loadImage(Bitmap bitmap, String imagePath) {
			// TODO Auto-generated method stub
			try {
				ImageView img = (ImageView) listview.findViewWithTag(imagePath);
				img.setImageBitmap(bitmap);
			} catch (NullPointerException ex) {
				Log.e("error", "ImageView = null");
			}
		}
	};

}
