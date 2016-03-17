package cn.eoe.app.yf.biz;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;

import android.app.Activity;
import android.util.Log;
import cn.eoe.app.biz.BaseDao;
import cn.eoe.app.config.Constants;
import cn.eoe.app.entity.DetailJson;
import cn.eoe.app.entity.DetailResponseEntity;
import cn.eoe.app.utils.RequestCacheUtil;
import cn.eoe.app.utils.Utility;
import cn.eoe.app.yf.entity.BookCategoryListEntity;
import cn.eoe.app.yf.entity.ChapterEntity;

public class DetailDao extends BaseDao {

	private static final String TAG = "DETAILDAO";
	private String mUrl;

	public DetailDao(Activity activity, String url) {
		super(activity);
		mUrl = url;// + Utility.getScreenParams(mActivity);
	}

	// private DetailResponseEntity mDetailResponseEntity;
	private List<ChapterEntity> mChapters;

	public List<ChapterEntity> getmDetailResponseEntity() {
		return mChapters;
	}

	public void setmDetailResponseEntity(List<ChapterEntity> mChapters) {
		this.mChapters = mChapters;
	}

	public String getUrl() {
		return this.mUrl;
	}

	public void setUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public List<ChapterEntity> chaptersMapperJson(boolean useCache) {
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity, mUrl,
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_content, useCache);
			// mChapters = new JSONArray(result);
			mObjectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
			mChapters = mObjectMapper.readValue(result,
					new TypeReference<List<ChapterEntity>>() {
					});
			Log.i(TAG, String.format("章节长：%s", mChapters.size()));
			return mChapters;
		} catch (Exception e) {
			// e.printStackTrace();
			Log.i(TAG, e.toString());
		}
		return null;
	}

	public Map<String, String> contentMapperJson(boolean useCache) {
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity, mUrl,
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_content, useCache);
			Log.i("info", mUrl);
			Map<String, String> content = mObjectMapper.readValue(result,
					Map.class);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public DetailResponseEntity mapperJson(boolean useCache) {
		try {
			// String result = RequestCacheUtil.getRequestContent(mActivity,
			// mUrl, Constants.WebSourceType.Json,
			// Constants.DBContentType.Content_content, useCache);
			// Log.i("info",mUrl);
			// DetailJson detailJson = mObjectMapper.readValue(result, new
			// TypeReference<DetailJson>() {});
			// this.mDetailResponseEntity = detailJson.getResponse();
			// return this.mDetailResponseEntity;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
