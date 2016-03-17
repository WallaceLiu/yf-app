package cn.eoe.app.yf.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.util.Log;
import cn.eoe.app.biz.BaseDao;
import cn.eoe.app.utils.RequestCacheUtil;
import cn.eoe.app.yf.config.Constants;
import cn.eoe.app.yf.config.Urls;
import cn.eoe.app.yf.entity.BookCategoryListEntity;
import cn.eoe.app.yf.entity.BooksMoreResponse;
import cn.eoe.app.yf.entity.BooksResponseEntity;
import cn.eoe.app.yf.entity.CategorysEntity;

/**
 * Description： 男频
 * 
 */
public class BoyDao extends BaseDao {

	private static final String TAG = "BOYDAO";

	private BooksResponseEntity _booksResponse = new BooksResponseEntity();
	private List<BookCategoryListEntity> _list;
	private List<CategorysEntity> _cList = new ArrayList<CategorysEntity>();

	public BoyDao(Activity activity) {
		super(activity);
	}

	public BooksResponseEntity getBlogsResponse() {
		return _booksResponse;
	}

	public void setBlogsResponse(BooksResponseEntity booksResponse) {
		this._booksResponse = booksResponse;
	}

	public BooksResponseEntity mapperJson(boolean useCache) {

		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					Urls.YF_BOY_URL, Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, useCache);
			mObjectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
			_list = mObjectMapper.readValue(result,
					new TypeReference<List<BookCategoryListEntity>>() {
					});

			// File file = new File("sdcard/boy.json");
			// FileInputStream inputStream = new FileInputStream(file);
			// ObjectMapper mapper = new ObjectMapper();
			// mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
			// _list = mapper.readValue(inputStream,
			// new TypeReference<List<BookCategoryListEntity>>() {
			// });

			if (_list == null) {
				return null;
			}
			for (int i = 0; i < _list.size(); i++) {
				BookCategoryListEntity e = _list.get(i);
				CategorysEntity ce = new CategorysEntity();
				ce.setName(e.getName());
				_cList.add(ce);
			}
			_booksResponse.setList(_list);
			_booksResponse.setCategorys(_cList);
			return _booksResponse;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, e.toString());
			// e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.i(TAG, e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Log.i(TAG, e.toString());
		} catch (Exception e) {
			// e.printStackTrace();
			Log.i(TAG, e.toString());
		}
		return null;
	}

	public BooksMoreResponse getMore(String more_url) {
		BooksMoreResponse response;
		try {
			String result = RequestCacheUtil.getRequestContent(mActivity,
					more_url, Constants.WebSourceType.Json,
					Constants.DBContentType.Content_list, true);
			mObjectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
			BookCategoryListEntity classBooks = mObjectMapper.readValue(result,
					new TypeReference<BookCategoryListEntity>() {
					});
			response = new BooksMoreResponse();
			response.setResponse(classBooks);
			/*
			 * response = mObjectMapper.readValue(result, new
			 * TypeReference<BooksMoreResponse>() { });
			 */
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