package cn.eoe.app.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.umeng.fb.FeedbackAgent;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.eoe.app.R;
import cn.eoe.app.adapter.BasePageAdapter;
import cn.eoe.app.biz.BaseDao;
import cn.eoe.app.biz.BlogsDao;
import cn.eoe.app.biz.NewsDao;
import cn.eoe.app.biz.TopDao;
import cn.eoe.app.biz.WikiDao;
import cn.eoe.app.yf.biz.BoyDao;
import cn.eoe.app.yf.biz.LiteratureDao;
import cn.eoe.app.yf.biz.GirlDao;
import cn.eoe.app.yf.config.Constants;
import cn.eoe.app.yf.entity.BooksResponseEntity;
import cn.eoe.app.db.DBHelper;
import cn.eoe.app.entity.BlogsResponseEntity;
import cn.eoe.app.yf.entity.CategorysEntity;
import cn.eoe.app.entity.NavigationModel;
import cn.eoe.app.entity.NewsResponseEntity;
import cn.eoe.app.entity.WikiResponseEntity;
import cn.eoe.app.https.NetWorkHelper;
import cn.eoe.app.indicator.PageIndicator;
import cn.eoe.app.slidingmenu.SlidingMenu;
import cn.eoe.app.ui.base.BaseSlidingFragmentActivity;
import cn.eoe.app.yf.ui.SearchBookActivity;
import cn.eoe.app.utils.IntentUtil;
import cn.eoe.app.utils.PopupWindowUtil;
import cn.eoe.app.widget.CustomButton;
import cn.eoe.app.yf.ui.BookUserLoginUidActivity;
import cn.eoe.app.yf.ui.BookUserCenterActivity;

/*主页面，继承滑动菜单基类*/
public class MainActivity extends BaseSlidingFragmentActivity implements
		OnClickListener, AnimationListener {
	// 滑动菜单栏，文字和图标的键
	private final String LIST_TEXT = "text";
	private final String LIST_IMAGEVIEW = "img";

	// [start]变量
	/**
	 * 数字代表列表顺序，左侧滑动菜单列
	 */
	private int mTag = 0;
	private CustomButton cbFeedback; // 反馈按钮
	private CustomButton cbAbove;// 关于
	private View title;
	private LinearLayout mlinear_listview;

	// title标题
	private ImageView imgQuery;
	private ImageView imgMore;
	private ImageView imgLeft;
	private ImageView imgRight;
	private FrameLayout mFrameTv;
	private ImageView mImgTv;

	// views
	private ViewPager mViewPager;
	private BasePageAdapter mBasePageAdapter;
	private PageIndicator mIndicator;
	private LinearLayout loadLayout; // 加载等待提示
	private LinearLayout loadFaillayout; // 加载失败提示

	// init daos
	// 社区精选，博客，资讯，教程业务层
	private TopDao topDao;
	private BlogsDao blogsDao;
	private NewsDao newsDao;
	private WikiDao wikiDao;

	private GirlDao girlDao;
	private BoyDao boyDao;
	private LiteratureDao litDao;

	private List<Object> categoryList;

	private List<NavigationModel> navs;

	private ListView lvTitle;
	private SimpleAdapter lvAdapter;
	private LinearLayout llGoHome; // 返回或进入侧边栏菜单
	private ImageButton imgLogin; // 侧边栏登录图标
	private Button bn_refresh; // 刷新

	private TextView mAboveTitle;
	private SlidingMenu sm; // 侧边栏滑动菜单
	private boolean mIsTitleHide = false;
	private boolean mIsAnim = false;

	// load responseData Web 服务器发送过来的 JSON 数据，用于创建页面
	private BlogsResponseEntity responseData;
	private NewsResponseEntity newsResponseData;
	// private WikiResponseEntity wikiResponseData;

	private BooksResponseEntity litBooksResponseData;
	private BooksResponseEntity boyBooksResponseData;
	private BooksResponseEntity girlBooksResponseData;

	private String current_page;

	private InputMethodManager imm; // 输入键盘

	private boolean isShowPopupWindows = false;

	// [end]

	// [start]生命周期
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSlidingMenu(); // 初始化侧边栏，在 setContentView 前边执行
		setContentView(R.layout.above_slidingmenu);
		// 初始化业务层
		initClass();
		// 初始化控件
		initControl();
		// 初始化页面实际文章类别和文章列表，如，主程序初始化时为社区精选，分类及其内容
		initViewPager();
		// 初始化侧边菜单栏
		initListView();
		// 初始化菜单栏返回按钮
		initgoHome();
		// 初始化侧边菜单栏集合
		initNav();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			DBHelper db = DBHelper.getInstance(this);
			db.closeDb();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// [end]

	// [start]初始化函数
	private void initSlidingMenu() {
		setBehindContentView(R.layout.behind_slidingmenu);
		// customize the SlidingMenu
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
		// sm.setShadowWidth(20);
		sm.setBehindScrollScale(0);
	}

	/* 初始化界面控件 */
	/* 查找所有页面控件，以便在之后做相应的处理 */
	private void initControl() {
		/* 输入键盘 */
		imm = (InputMethodManager) getApplicationContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		/* 加载等待提示，加载失败提示 */
		loadLayout = (LinearLayout) findViewById(R.id.view_loading);
		loadFaillayout = (LinearLayout) findViewById(R.id.view_load_fail);
		/* 顶部菜单栏标题 */
		mAboveTitle = (TextView) findViewById(R.id.tv_above_title);
		mAboveTitle.setText("收藏");
		/* 顶部菜单检索按钮 */
		/* 检索按钮，在社区精选不显示，而在其他三个栏目显示 */
		imgQuery = (ImageView) findViewById(R.id.imageview_above_query);
		imgQuery.setOnClickListener(this);
		imgQuery.setVisibility(View.GONE);
		/* 顶部菜单栏更多按钮 */
		imgMore = (ImageView) findViewById(R.id.imageview_above_more);
		imgMore.setOnClickListener(this);

		imgLeft = (ImageView) findViewById(R.id.imageview_above_left);
		imgRight = (ImageView) findViewById(R.id.imageview_above_right);
		// editSearch.setOnKeyListener(onkey);

		mViewPager = (ViewPager) findViewById(R.id.above_pager);
		mIndicator = (PageIndicator) findViewById(R.id.above_indicator);
		lvTitle = (ListView) findViewById(R.id.behind_list_show);
		/* 顶部菜单栏返回/显示菜单栏选项 */
		llGoHome = (LinearLayout) findViewById(R.id.Linear_above_toHome);
		/* 顶部菜单栏登录按钮 */
		imgLogin = (ImageButton) findViewById(R.id.login_login);
		imgLogin.setOnClickListener(this);
		/* 反馈按钮 */
		cbFeedback = (CustomButton) findViewById(R.id.cbFeedback);
		cbFeedback.setOnClickListener(this);
		/* 关于按钮 */
		cbAbove = (CustomButton) findViewById(R.id.cbAbove);
		cbAbove.setOnClickListener(this);

		title = findViewById(R.id.main_title);
		mlinear_listview = (LinearLayout) findViewById(R.id.main_linear_listview);
		mFrameTv = (FrameLayout) findViewById(R.id.fl_off);
		mImgTv = (ImageView) findViewById(R.id.iv_off);
		/* 刷新按钮 */
		bn_refresh = (Button) findViewById(R.id.bn_refresh);
		bn_refresh.setOnClickListener(this);
	}

	/* 初始化业务层 */
	/* 社区精选，博客，资讯，教程 */
	private void initClass() {
		blogsDao = new BlogsDao(this);
		newsDao = new NewsDao(this);
		wikiDao = new WikiDao(this);
		topDao = new TopDao(this);

		// 男频 女频 文学
		boyDao = new BoyDao(this);
		girlDao = new GirlDao(this);
		litDao = new LiteratureDao(this);
	}

	/* 初始化社区精选 viewpager */
	private void initViewPager() {
		mBasePageAdapter = new BasePageAdapter(MainActivity.this);
		/* 设置 ViewPager */
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mBasePageAdapter);
		/* 设置 顶部菜单栏下，左右两边的效果 */
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnPageChangeListener(new MyPageChangeListener());
		/* 获得 Web 后台“社区精选”的数据，以便生成页面内容 */
		new MyTask().execute(topDao);
	}

	/* 初始化菜单栏内容 */
	private void initListView() {
		lvAdapter = new SimpleAdapter(this, getData(),
				R.layout.behind_list_show, new String[] { LIST_TEXT,
						LIST_IMAGEVIEW },
				new int[] { R.id.textview_behind_title,
						R.id.imageview_behind_icon }) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub.
				View view = super.getView(position, convertView, parent);
				if (position == mTag) {
					view.setBackgroundResource(R.drawable.back_behind_list);
					lvTitle.setTag(view);
				} else {
					view.setBackgroundColor(Color.TRANSPARENT);
				}
				return view;
			}
		};
		lvTitle.setAdapter(lvAdapter);
		// 设置侧边栏点击回调事件
		// 根据侧边栏单击的选择，加载相应的文章分类及其文章列表，设置顶部标题
		// 创建相应的 tab 页，及其文章列表 list
		lvTitle.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NavigationModel navModel = navs.get(position);
				// 初始化顶部导航标题
				mAboveTitle.setText(navModel.getName());
				current_page = navModel.getTags();
				if (lvTitle.getTag() != null) {
					if (lvTitle.getTag() == view) {
						MainActivity.this.showContent();
						return;
					}
					((View) lvTitle.getTag())
							.setBackgroundColor(Color.TRANSPARENT);
				}
				lvTitle.setTag(view);
				// 设置被点击的菜单背景
				view.setBackgroundResource(R.drawable.back_behind_list);

				// 根据点击不同的侧边栏菜单，获取不同的数据
				// 社区精选，不在顶部菜单栏显示检索按钮
				// 博客，资讯，教程都在顶部菜单栏显示检索按钮
				imgQuery.setVisibility(View.VISIBLE);

				switch (position) {
				case 0: // 收藏
					imgQuery.setVisibility(View.GONE);
					new MyTask().execute(topDao);
					break;
				case 1: // 推荐
					new MyTask().execute(newsDao);
					break;
				case 2: // 男频
					// new MyTask().execute(wikiDao);
					new MyTask().execute(boyDao);
					break;
				case 3: // 女频
					new MyTask().execute(girlDao);
					break;
				case 4: // 文学
					new MyTask().execute(litDao);
					break;
				case 5: // 我的
					new MyTask().execute(blogsDao);
					break;
				}
			}
		});

	}

	/* 初始化菜单栏导航数据 */
	private void initNav() {
		navs = new ArrayList<NavigationModel>();
		NavigationModel nav1 = new NavigationModel(getResources().getString(
				R.string.menuGood), ""); // 收藏
		NavigationModel nav2 = new NavigationModel(getResources().getString(
				R.string.menuNews), Constants.TAGS.NEWS_TAG); // 推荐
		NavigationModel nav3 = new NavigationModel(getResources().getString(
				R.string.menuBoy), Constants.TAGS.BOY_TAG); // 男频
		NavigationModel nav4 = new NavigationModel(getResources().getString(
				R.string.menuGirl), Constants.TAGS.GIRL_TAG); // 女频
		NavigationModel nav5 = new NavigationModel(getResources().getString(
				R.string.menuLiterature), Constants.TAGS.LITERATURE_TAG); // 文学
		// NavigationModel nav6 = new NavigationModel(getResources().getString(
		// R.string.menuBlog), Constants.TAGS.BLOG_TAG); // 我的
		// Collections.addAll(navs, nav1, nav2, nav3, nav4, nav5, nav6);
		Collections.addAll(navs, nav1, nav2, nav3, nav4, nav5);
	}

	private void initgoHome() {
		llGoHome.setOnClickListener(this);
	}

	/* 滑动侧边栏菜单数据 */
	/* 菜单名称和其相应的图标 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 社区精选 当作收藏
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuGood));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_handpick);
		list.add(map);
		// 资讯 当作推荐
		map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuNews));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_news);
		list.add(map);
		// 教程 男频
		map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuBoy));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_studio);
		list.add(map);
		// 女频
		map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuGirl));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_studio);
		list.add(map);
		// 文学
		map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuLiterature));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_studio);
		list.add(map);

		// 博客 我的
		map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuBlog));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_blog);
		list.add(map);
		return list;
	}

	// [end]

	// [start]继承方法
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) { // application icon back
		case R.id.Linear_above_toHome:
			showMenu();
			break;
		case R.id.login_login: // User login
			SharedPreferences share = this.getSharedPreferences(
					BookUserLoginUidActivity.SharedName, Context.MODE_PRIVATE);
			String Key = share.getString(BookUserLoginUidActivity.KEY, "");
			if (Key != "" && !Key.contains(":")) {
				Editor edit = share.edit();
				edit.putString(BookUserLoginUidActivity.KEY, "");
				edit.commit();
			}

			if (share.contains(UserLoginUidActivity.KEY)) {
				// before code.
				// share.contains(UserLoginUidActivity.KEY)
				// && !share.getString(UserLoginUidActivity.KEY, "")
				// .equals("")
				// enter user center
				IntentUtil.start_activity(this, BookUserCenterActivity.class);
			} else {
				// enter User login
				IntentUtil.start_activity(this, BookUserLoginUidActivity.class);
			}
			break;
		case R.id.imageview_above_more: // 操作栏更多，利用分类创建弹出窗体菜单
			if (isShowPopupWindows) {
				new PopupWindowUtil(mViewPager).showActionWindow(v, this,
						mBasePageAdapter.tabs);
			}
			break;
		case R.id.imageview_above_query: // search

			if (NetWorkHelper.isNetworkAvailable(MainActivity.this)) {
				IntentUtil.start_activity(this, SearchBookActivity.class,
						new BasicNameValuePair("tag", current_page));
			} else {
				Toast.makeText(getApplicationContext(), "网络连接失败,请检查网络",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cbFeedback:
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			break;
		case R.id.cbAbove:
			IntentUtil.start_activity(this, AboutActivity.class);
			break;
		// 若加载某个文章列表失败，则会显示“刷新”按钮，
		// 此时点击，按当前所处的分类位置，进行刷新
		case R.id.bn_refresh:
			switch (mTag) {
			case 0: // 收藏 fav
				imgQuery.setVisibility(View.GONE);
				new MyTask().execute(topDao);
				break;
			case 1: // 推荐 recommand
				new MyTask().execute(newsDao);
				break;
			case 2: // 男频 boy channel
				new MyTask().execute(boyDao);
				break;
			case 3: // 女频 girl channel
				new MyTask().execute(girlDao);
				break;
			case 4: // 文学 liter channel
				new MyTask().execute(litDao);
				break;
			case 5:
				new MyTask().execute(blogsDao);
				break;
			default:
				break;
			}
			break;
		}

	}

	/**
	 * 连续按两次返回键就退出
	 */
	private int keyBackClickCount = 0;

	// Activity 恢复状态时，keyBackClickCount 清零
	@Override
	protected void onResume() {
		super.onResume();
		keyBackClickCount = 0;
	}

	/* 监控手机物理按键 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 手机返回按键，及返回按键的次数
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				Toast.makeText(this,
						getResources().getString(R.string.press_again_exit),
						Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1:
				mFrameTv.setVisibility(View.VISIBLE);
				mImgTv.setVisibility(View.VISIBLE);
				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.tv_off);
				anim.setAnimationListener(new tvOffAnimListener());
				mImgTv.startAnimation(anim);
				break;
			default:
				break;
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) { // 手机菜单键

			if (sm.isMenuShowing()) {
				toggle();
			} else {
				showMenu();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/* 分发TouchEvent */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.dispatchTouchEvent(event);
		if (mIsAnim || mViewPager.getChildCount() <= 1) {
			return false;
		}
		final int action = event.getAction();

		float x = event.getX();
		float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			lastX = x;
			return false;
		case MotionEvent.ACTION_MOVE:
			float dY = Math.abs(y - lastY);
			float dX = Math.abs(x - lastX);
			boolean down = y > lastY ? true : false;
			lastY = y;
			lastX = x;
			if (dX < 8 && dY > 8 && !mIsTitleHide && !down) {
				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.push_top_in);
				// anim.setFillAfter(true);
				anim.setAnimationListener(MainActivity.this);
				title.startAnimation(anim);
			} else if (dX < 8 && dY > 8 && mIsTitleHide && down) {
				Animation anim = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.push_top_out);
				// anim.setFillAfter(true);
				anim.setAnimationListener(MainActivity.this);
				title.startAnimation(anim);
			} else {
				return false;
			}
			mIsTitleHide = !mIsTitleHide;
			mIsAnim = true;
			break;
		default:
			return false;
		}
		return false;
	}

	/* 在 kill 掉程序后执行 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	// [end]

	/* 根据 Web 后台返回的数据，显示页面。在此是显示社区精选 */
	public class MyTask extends AsyncTask<BaseDao, String, Map<String, Object>> {

		private boolean mUseCache;

		public MyTask() {
			mUseCache = true;
		}

		public MyTask(boolean useCache) {
			mUseCache = useCache;
		}

		/* 执行前，相关页面处理 */
		@Override
		protected void onPreExecute() {
			//
			imgLeft.setVisibility(View.GONE);
			imgRight.setVisibility(View.GONE);
			loadLayout.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.GONE);
			mViewPager.removeAllViews();
			mBasePageAdapter.Clear();
			MainActivity.this.showContent();
			super.onPreExecute();
			isShowPopupWindows = false;
		}

		/* 执行中 */
		@Override
		protected Map<String, Object> doInBackground(BaseDao... params) {
			BaseDao dao = params[0];
			// tab 页内容
			// List<CategorysEntity> categorys = new
			// ArrayList<CategorysEntity>();

			List<CategorysEntity> categorys = new ArrayList<CategorysEntity>();
			Map<String, Object> map = new HashMap<String, Object>();
			if (dao instanceof TopDao) { // 强制类型转换，如把 TopDao 强制转换为其基类
				mTag = 0; // 社区精选
				if ((categoryList = topDao.mapperJson(mUseCache)) != null) {
					// categoryList 集合，保存社区精选文章分类和其文章列表信息
					// categorys 集合，保存社区精选基本文章分类信息
					categorys = topDao.getCategorys();
					map.put("tabs", categorys); // 键值对
					map.put("list", categoryList);
				}
			} else if (dao instanceof BlogsDao) {
				mTag = 5; // 博客
				if ((responseData = blogsDao.mapperJson(mUseCache)) != null) {
					// 同 社区精选 的处理方式
					categoryList = (List) responseData.getList();
					categorys = responseData.getCategorys();
					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			} else if (dao instanceof NewsDao) {
				mTag = 1; // 资讯
				if ((newsResponseData = newsDao.mapperJson(mUseCache)) != null) {
					categoryList = (List) newsResponseData.getList();
					categorys = newsResponseData.getCategorys();
					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			} else if (dao instanceof BoyDao) {
				mTag = 2; // 男频
				if ((boyBooksResponseData = boyDao.mapperJson(mUseCache)) != null) {
					categoryList = (List) boyBooksResponseData.getList();
					categorys = boyBooksResponseData.getCategorys();
					map.put("tabs", categorys); // tabs 是‘更多’
					map.put("list", categoryList);
				}
			} else if (dao instanceof GirlDao) {
				mTag = 3; // 女频
				if ((girlBooksResponseData = girlDao.mapperJson(mUseCache)) != null) {
					categoryList = (List) girlBooksResponseData.getList();
					categorys = girlBooksResponseData.getCategorys();
					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			} else if (dao instanceof LiteratureDao) {
				mTag = 4; // 文学
				if ((litBooksResponseData = litDao.mapperJson(mUseCache)) != null) {
					categoryList = (List) litBooksResponseData.getList();
					categorys = litBooksResponseData.getCategorys();
					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			} else {
				return null;
			}
			return map;
		}

		/* 与 web 交互后，页面的处理 */
		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Map<String, Object> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			isShowPopupWindows = true;
			mBasePageAdapter.Clear();
			mViewPager.removeAllViews();
			if (!result.isEmpty()) {
				// result.get("tabs") 为分类信息
				// result.get("list") 为分类下的文章列表
				mBasePageAdapter.addFragment(
						(List<CategorysEntity>) result.get("tabs"),
						(List<Object>) result.get("list"), mTag);
				imgRight.setVisibility(View.VISIBLE);
				loadLayout.setVisibility(View.GONE); // 加载等待提示消失
				loadFaillayout.setVisibility(View.GONE); // 加载失败提示消失
			} else {
				mBasePageAdapter.addNullFragment();
				loadLayout.setVisibility(View.GONE); // 加载等待提示消失
				loadFaillayout.setVisibility(View.VISIBLE); // 加载失败提示显示
			}
			mViewPager.setVisibility(View.VISIBLE);
			mBasePageAdapter.notifyDataSetChanged(); // 动态更新list
			mViewPager.setCurrentItem(0);
			mIndicator.notifyDataSetChanged(); // 动态更新指示器

		}
	}

	/**
	 * viewPager切换页面
	 * 
	 * @author mingxv
	 */
	class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == 0) {
				getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_FULLSCREEN);
				imgLeft.setVisibility(View.GONE);
			} else if (arg0 == mBasePageAdapter.mFragments.size() - 1) {
				imgRight.setVisibility(View.GONE);
				getSlidingMenu()
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			} else {
				imgRight.setVisibility(View.VISIBLE);
				imgLeft.setVisibility(View.VISIBLE);
				getSlidingMenu()
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			}
		}
	}

	class tvOffAnimListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			defaultFinish();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (mIsTitleHide) {
			title.setVisibility(View.GONE);
		} else {

		}
		mIsAnim = false;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		title.setVisibility(View.VISIBLE);
		if (mIsTitleHide) {
			FrameLayout.LayoutParams lp = (LayoutParams) mlinear_listview
					.getLayoutParams();
			lp.setMargins(0, 0, 0, 0);
			mlinear_listview.setLayoutParams(lp);
		} else {
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) title
					.getLayoutParams();
			lp.setMargins(0, 0, 0, 0);
			title.setLayoutParams(lp);
			FrameLayout.LayoutParams lp1 = (LayoutParams) mlinear_listview
					.getLayoutParams();
			lp1.setMargins(0,
					getResources().getDimensionPixelSize(R.dimen.title_height),
					0, 0);
			mlinear_listview.setLayoutParams(lp1);
		}
	}

	private float lastX = 0;
	private float lastY = 0;

}
