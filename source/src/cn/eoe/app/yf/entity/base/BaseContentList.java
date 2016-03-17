package cn.eoe.app.yf.entity.base;

/*分类*/
public abstract class BaseContentList {
	private Integer Id;
	private String Name; // list_name
	private String NextUrl; // 相对应的加载更多
	private String PrevUrl; 

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		this.Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getNextUrl() {
		return NextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.NextUrl = nextUrl;
	}

	public String getPrevUrl() {
		return PrevUrl;
	}

	public void setPrevUrl(String prevUrl) {
		this.PrevUrl = prevUrl;
	}

}
