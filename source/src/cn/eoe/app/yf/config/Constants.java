package cn.eoe.app.yf.config;

public class Constants {

	/*
	 * public static final class TAGS { public static final String NEWS_TAG =
	 * "news"; public static final String BLOG_TAG = "blog"; public static final
	 * String WIKI_TAG = "wiki"; }
	 */
	/* 标签：资讯 博客 教程 */
	public static final class TAGS {
		public static final String NEWS_TAG = "news";
		public static final String BLOG_TAG = "blog";
		public static final String WIKI_TAG = "wiki";
		// public static final String BOY_TAG = "boy";
		// public static final String GIRL_TAG = "girl";
		// public static final String LITERATURE_TAG = "liter";

		public static final String FAC_TAG = "收藏";
		public static final String RECOMMEND_TAG = "推荐";
		public static final String BOY_TAG = "男频";
		public static final String GIRL_TAG = "女频";
		public static final String LITERATURE_TAG = "文学";
	}

	public static final class DBContentType {
		public static final String Content_list = "list";
		public static final String Content_content = "content";
		public static final String Discuss = "discuss";
	}

	public static final class WebSourceType {
		public static final String Json = "json";
		public static final String Xml = "xml";
	}
}
