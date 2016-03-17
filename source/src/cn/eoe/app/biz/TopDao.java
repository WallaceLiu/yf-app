package cn.eoe.app.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import cn.eoe.app.config.Constants;
import cn.eoe.app.config.Urls;
import cn.eoe.app.entity.BlogsCategoryListEntity;
import cn.eoe.app.entity.BlogsMoreResponse;
import cn.eoe.app.entity.NewsCategoryListEntity;
import cn.eoe.app.entity.NewsMoreResponse;
import cn.eoe.app.entity.WikiCategoryListEntity;
import cn.eoe.app.entity.WikiMoreResponse;
import cn.eoe.app.utils.RequestCacheUtil;
import cn.eoe.app.utils.Utility;
import cn.eoe.app.yf.entity.CategorysEntity;

/*社区精选*/
public class TopDao extends BaseDao {

	private NewsCategoryListEntity newsCategorys;
	private BlogsCategoryListEntity blogsCategorys;
	private WikiCategoryListEntity wikiCategorys;

	// 社区精选基本分类信息，用于生成 tab 页标签信息
	List<CategorysEntity> tabs = new ArrayList<CategorysEntity>();

	public TopDao(Activity activity) {
		super(activity);
	}

	public List<Object> mapperJson(boolean useCache) {
		// 社区精选文章分类及其文章列表集合
		List<Object> topCategorys = new ArrayList<Object>();
		tabs.clear();
		try {
			// 文章所属分类及其文章列表
			// 资讯精选
			String resultNews = RequestCacheUtil.getRequestContent(mActivity,
					Urls.TOP_NEWS_URL + Utility.getScreenParams(mActivity),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, useCache);
			// 将 json 转换成 NewsMoreResponse 对象
			NewsMoreResponse newsMoreResponse = mObjectMapper.readValue(
					resultNews, new TypeReference<NewsMoreResponse>() {
					});
			if (newsMoreResponse != null) {
				this.newsCategorys = newsMoreResponse.getResponse();
			}
			// 博客精选
			String resultBlogs = RequestCacheUtil.getRequestContent(mActivity,
					Urls.TOP_BLOG_URL + Utility.getScreenParams(mActivity),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, useCache);
			BlogsMoreResponse blogsMoreResponse = mObjectMapper.readValue(
					resultBlogs, new TypeReference<BlogsMoreResponse>() {
					});
			if (blogsMoreResponse != null) {
				this.blogsCategorys = blogsMoreResponse.getResponse();
			}
			// 教程精选
			String resultWiki = RequestCacheUtil.getRequestContent(mActivity,
					Urls.TOP_WIKI_URL + Utility.getScreenParams(mActivity),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, useCache);
			WikiMoreResponse wikiMoreResponse = mObjectMapper.readValue(
					resultWiki, new TypeReference<WikiMoreResponse>() {
					});
			if (wikiMoreResponse != null) {
				this.wikiCategorys = wikiMoreResponse.getResponse();
			}
			// 将三个精选，添加到集合
			Collections.addAll(topCategorys, newsCategorys, blogsCategorys,
					wikiCategorys);

			return topCategorys;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/* 社区精选基本分类信息，用于生成 tab 页标签 */
	public List<CategorysEntity> getCategorys() {
		CategorysEntity cate1 = new CategorysEntity();
		CategorysEntity cate2 = new CategorysEntity();
		CategorysEntity cate3 = new CategorysEntity();
		cate1.setName("精选资讯");
		cate2.setName("精选博客");
		cate3.setName("精选教程");
		tabs.add(cate1);
		tabs.add(cate2);
		tabs.add(cate3);
		return tabs;
	}

}
