package cn.eoe.app.entity;

/**
 * 返回的 大的json的 中的categorys的封装
 * 
 * @author wangxin
 * 
 */
public class CategorysEntity {

	private String name; // 分类的名称，如资讯下的分类，包括最新、推荐、热门等等
	private String url;  // 分类的地址

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
