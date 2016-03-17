package cn.eoe.app.config;

/*需要访问的 Web 后台*/
public class Urls {

	public static final String BASIC_URL = "http://api.eoe.cn/client";
	// public static final String BASIC_URL1 = "http://api.eoe.name/client";

	/* 社区精选：精选资讯 精选博客 精选教程 */
	/* 未使用，由接下来的 TOP_***_URL 三个代替 */
	public static final String TOP_LIST = BASIC_URL + "/top";
	/* cn.eoe.app.biz.TopDao 类使用 */
	public static final String TOP_NEWS_URL = BASIC_URL + "/news?k=lists&t=top";
	public static final String TOP_BLOG_URL = BASIC_URL + "/blog?k=lists&t=top";
	public static final String TOP_WIKI_URL = BASIC_URL + "/wiki?k=lists&t=top";

	/* 新闻资讯 cn.eoe.app.biz.NewsDao 类使用 */
	public static final String NEWS_LIST = BASIC_URL + "/news?k=lists";

	/* 学习教程 cn.eoe.app.biz.WikiDao 类使用 */
	public static final String WIKI_LIST = BASIC_URL + "/wiki?k=lists";

	/* 社区博客 cn.eoe.app.biz.BlogDao 类使用 */
	public static final String BLOGS_LIST = BASIC_URL + "/blog?k=lists";

	/* 检索 cn.eoe.app.biz.SearchDao 类使用 */
	public static final String BASE_SEARCH_URL = BASIC_URL + "/search?";

	/* 用户登录 cn.eoe.app.ui.UserLoginUidActivity 类使用 */
	public static final String USER_LOGIN = BASIC_URL + "/key?uname=%s&pwd=%s";

	/* 获取用户信息接口 cn.eoe.app.biz.UserDao 类使用 */
	public static final String KEYBindURL = BASIC_URL + "/userinfo?key=%s";

	/**
	 * 1 k 2 act 3 model 4 itemid
	 */
	public static final String DETAILS_ActionBar = BASIC_URL
			+ "/bar?k=%s&act=%s&model=%s&itemid=%s";

	public static final String userlike = "userlike";
	public static final String favorite = "favorite";
	public static final String add = "add";
	public static final String del = "del";
	public static final String like = "like";
	public static final String useless = "useless";
	public static final String news = "news";
	public static final String wiki = "wiki";
	public static final String blog = "blog";
}
