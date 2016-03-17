package cn.eoe.app.yf.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import cn.eoe.app.biz.BaseDao;
import cn.eoe.app.yf.config.Urls;
import cn.eoe.app.entity.BlogSearchJson;
import cn.eoe.app.yf.entity.BookCategoryListEntity;
import cn.eoe.app.yf.entity.BookContentItemEntity;
import cn.eoe.app.yf.entity.CategorysEntity;
import cn.eoe.app.yf.entity.SearchEntity;
import cn.eoe.app.entity.NewsSearchJson;
import cn.eoe.app.entity.WikiSearchJson;
import cn.eoe.app.https.HttpUtils;

//import cn.eoe.app.utils.Utility;

/*检索*/
public class SearchBookDao extends BaseDao {

	private String mTag;
	private String keyWord;
	private boolean hasChild = true;

	public SearchBookDao(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	public SearchBookDao(String tag, String key) {
		this.mTag = tag;
		this.keyWord = key;
	}

	public void setValue(String mTag, String mKey) {
		this.mTag = mTag; // 男频 女频 文学
		this.keyWord = mKey; // 检索关键字
	}

	/**
	 * 这里的逻辑判断得优化 目前仅为实现功能编写的代码 不妥
	 * 
	 * @return
	 */
	public SearchEntity mapperJson() {
		SearchEntity searchBook;

		try {
			String result = HttpUtils.getByHttpClient(mActivity,
					String.format(Urls.YF_SEARCH, mTag, keyWord, "1"));
			if (result.isEmpty())
				return null;
			mObjectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
			searchBook = mObjectMapper.readValue(result,
					new TypeReference<SearchEntity>() {
					});

			return searchBook;
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

	public SearchEntity getMore(String more_url) {
		SearchEntity searchBook;

		try {
			String result = HttpUtils.getByHttpClient(mActivity, more_url);
			mObjectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
			searchBook = mObjectMapper.readValue(result,
					new TypeReference<SearchEntity>() {
					});

			return searchBook;
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

	public boolean getHasChild() {
		return hasChild;
	}
}
