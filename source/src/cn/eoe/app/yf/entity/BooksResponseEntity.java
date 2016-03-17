package cn.eoe.app.yf.entity;

import java.util.List;

import cn.eoe.app.yf.entity.base.BaseResponseData;

/*response的实体类的封装*/
public class BooksResponseEntity extends BaseResponseData {

	private List<BookCategoryListEntity> list; // response 中的List的封装

	public List<BookCategoryListEntity> getList() {
		return list;
	}

	public void setList(List<BookCategoryListEntity> list) {
		this.list = list;
	}
}