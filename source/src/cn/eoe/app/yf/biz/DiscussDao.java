package cn.eoe.app.yf.biz;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import cn.eoe.app.biz.BaseDao;
import cn.eoe.app.config.Constants;
import cn.eoe.app.entity.DetailsOwnDiscussJson;
import cn.eoe.app.entity.NewsResponseEntity;
import cn.eoe.app.utils.RequestCacheUtil;
import cn.eoe.app.yf.config.Urls;

public class DiscussDao extends BaseDao {

	public DiscussDao(Activity activity) {
		super(activity);
	}

	private NewsResponseEntity _newsResponse;

	public NewsResponseEntity get_newsResponse() {
		return _newsResponse;
	}

	public void set_newsResponse(NewsResponseEntity _newsResponse) {
		this._newsResponse = _newsResponse;
	}

	public String mapperJson(String url, boolean useCache) {
		// TODO Auto-generated method stub
		// DetailsOwnDiscussJson json = null;

		// String result = RequestCacheUtil.getRequestContent(mActivity,
		// url,
		// Constants.WebSourceType.Json,
		// Constants.DBContentType.Discuss, useCache);
		String result = RequestCacheUtil.getRequestContent(mActivity, url,
				Constants.WebSourceType.Json, Constants.DBContentType.Discuss,
				useCache);
		// json = mObjectMapper.readValue(result,
		// new TypeReference<DetailsOwnDiscussJson>() {
		// });
		return result;
	}
}
