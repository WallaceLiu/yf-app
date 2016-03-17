package cn.eoe.app.yf.biz;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import cn.eoe.app.biz.BaseDao;
import cn.eoe.app.yf.config.Constants;
import cn.eoe.app.yf.config.Urls;
import cn.eoe.app.utils.RequestCacheUtil;
import cn.eoe.app.utils.Utility;
import cn.eoe.app.yf.entity.BooksJson;
import cn.eoe.app.yf.entity.BooksMoreResponse;
import cn.eoe.app.yf.entity.BooksResponseEntity;

public class RecommendDao extends BaseDao {

	public RecommendDao(Activity activity) {
		super(activity);
	}

	private BooksResponseEntity _booksResponse;

	public BooksResponseEntity get_newsResponse() {
		return _booksResponse;
	}

	public void set_newsResponse(BooksResponseEntity _booksResponse) {
		this._booksResponse = _booksResponse;
	}

	public BooksResponseEntity mapperJson(boolean useCache) {
		// TODO Auto-generated method stub
		BooksJson booksJson;
		try {
			String result = RequestCacheUtil
					.getRequestContent(mActivity, Urls.YF_RECOMMEND_URL
							+ Utility.getScreenParams(mActivity),
							Constants.WebSourceType.Json,
							Constants.DBContentType.Content_list, useCache);
			booksJson = mObjectMapper.readValue(result,
					new TypeReference<BooksJson>() {
					});
			if (booksJson == null) {
				return null;
			}
			this._booksResponse = booksJson.getResponse();
			return _booksResponse;

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

	public BooksMoreResponse getMore(String more_url) {
		BooksMoreResponse response;
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					more_url + Utility.getScreenParams(mActivity),
					Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, true);
			response = mObjectMapper.readValue(result,
					new TypeReference<BooksMoreResponse>() {
					});
			return response;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
