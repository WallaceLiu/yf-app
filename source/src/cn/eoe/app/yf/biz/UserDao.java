package cn.eoe.app.yf.biz;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import cn.eoe.app.biz.BaseDao;
import cn.eoe.app.yf.config.Urls;
import cn.eoe.app.yf.entity.UserJson;
import cn.eoe.app.yf.entity.UserResponse;
import cn.eoe.app.https.HttpUtils;
import cn.eoe.app.utils.Utility;

public class UserDao extends BaseDao {
	private Context mContext;

	public UserDao(Context context) {
		mContext = context;
	}

	public UserResponse mapperJson(String uid, String pwd) {
		// TODO Auto-generated method stub
		UserJson userJson;
		try {
			/*
			 * if (!key.contains(":")) { return null; }
			 */
			if (uid.isEmpty() || pwd.isEmpty()) {
				return null;
			}
			String url = String.format(Urls.YF_USER_CENTER, uid, pwd);
			// + Utility.getParams(key);
			String result = HttpUtils.getByHttpClient(mContext, url);
			userJson = mObjectMapper.readValue(result,
					new TypeReference<UserJson>() {
					});
			if (userJson == null) {
				return null;
			}
			return userJson.getResponse();
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
}
