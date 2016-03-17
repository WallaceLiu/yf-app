package cn.eoe.app.yf.config;

/*需要访问的 Web 后台*/
public class Urls {
	/* 请求基地址 */
	public static final String YF_BASE_1OOF_URL = "http://www.1oof.com";
	public static final String YF_BASE_YFZWW_URL = "http://www.yfzww.com";
	// public static final String YF_BASE_YFZWW_URL = "http://192.168.1.100";

	/* 通用请求地址 */
	public static final String YF_COMMON_URL = YF_BASE_YFZWW_URL
			+ "/Handle/GetJsonCategorys.ashx?kind=%s&orderby=0";

	/* 男频请求地址 */
	public static final String YF_BOY_URL = YF_BASE_YFZWW_URL
			+ "/Handle/GetJsonCategorys.ashx?kind=2&orderby=0";

	/* 女频请求地址 */
	public static final String YF_GIRL_URL = YF_BASE_YFZWW_URL
			+ "/Handle/GetJsonCategorys.ashx?kind=3&orderby=0";

	/* 文学请求地址 */
	public static final String YF_LITERATURE_URL = YF_BASE_YFZWW_URL
			+ "/Handle/GetJsonCategorys.ashx?kind=1&orderby=0";

	/* 类别下书请求地址 */
	public static final String YF_CLASS_URL = YF_BASE_YFZWW_URL
			+ "/Handle/GetJsonClassBook.ashx?kind=%s&c=%s&p=%s";

	/* 书章节信息请求地址 */
	public static final String YF_BOOKCHAPTERS_URL = YF_BASE_YFZWW_URL
			+ "/handle/GetJsonChaptersList.ashx?bookid=%s";

	/* 书章节内容请求地址 */
	public static final String YF_BOOKCONTENT_URL = YF_BASE_YFZWW_URL
			+ "/Handle/GetJsonChapterContent.ashx?bookid=%s&chapterid=%s";

	/* 推荐请求地址 */
	public static final String YF_RECOMMEND_URL = YF_BASE_YFZWW_URL
			+ "/HttpHandle/LoadCategoryBooks.ashx?kind=1&orderby=lev";

	/* 用户登录 */
	public static final String YF_USER_LOGIN = YF_BASE_YFZWW_URL
			+ "/Handle/Login.ashx?loginname=%s&password=%s";
	/* 用户中心 */
	public static final String YF_USER_CENTER = YF_BASE_YFZWW_URL
			+ "/Handle/UserCenter.ashx?loginname=%s&password=%s";
	/* 用户注册请求地址 */
	public static final String YF_USER_REGISTER = YF_BASE_YFZWW_URL
			+ "/Handle/RegisterUser.ashx?loginname=%s&password=%s";

	/* 检索请求地址 */
	public static final String YF_SEARCH = YF_BASE_YFZWW_URL
			+ "/Handle/SearchBook.ashx?kind=%s&key=%s&p=%s";

	/* 获得评论请求地址 */
	public static final String YF_GETDISCUSE = YF_BASE_YFZWW_URL
			+ "/Handle/getdiscuse.ashx?bookid=%s";

	/* 评论请求地址 */
	public static final String YF_DISCUSE = YF_BASE_YFZWW_URL
			+ "/Handle/Comment.ashx?bookid=%s&content=%s";

	/* 用户偏好请求地址 */
	public static final String YF_PREFERENCE = YF_BASE_YFZWW_URL
			+ "/Handle/UserPreferences.ashx?action=%s&key=%s&bookid=%s&good=%s&bad=%s&collect=%s";
	/* 用户偏好请求地址 - 好 */
	public static final String YF_PREFERENCE_GOOD = YF_BASE_YFZWW_URL
			+ "/Handle/UserPreferences.ashx?action=add&key=%s&bookid=%s&good=%s";
	/* 用户偏好请求地址 - 坏 */
	public static final String YF_PREFERENCE_BAD = YF_BASE_YFZWW_URL
			+ "/Handle/UserPreferences.ashx?action=add&key=%s&bookid=%s&bad=%s";
	/* 用户偏好请求地址 - 收藏 */
	public static final String YF_PREFERENCE_COLLECT = YF_BASE_YFZWW_URL
			+ "/Handle/UserPreferences.ashx?action=add&key=%s&bookid=%s&collect=%s";

	/* 书请求地址 */
	public static final String YF_BOOK = YF_BASE_YFZWW_URL
			+ "/Handle/book.ashx?id=%s";

}
