package cn.eoe.app.biz;

/*解析json数据*/
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;

/*业务层*/
public class BaseDao {
	// 解析 JSON 数据
	public ObjectMapper mObjectMapper = new ObjectMapper();

	protected Activity mActivity;

	public BaseDao() {
	};

	public BaseDao(Activity activity) {
		mActivity = activity;
	}
}
