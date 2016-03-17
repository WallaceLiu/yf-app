package cn.eoe.app.yf.entity;

import java.util.List;

import cn.eoe.app.yf.entity.base.BaseContentList;

/*书列表和所属分类*/
public class BookCategoryListEntity extends BaseContentList {
	private List<BookContentItemEntity> Books;

	public List<BookContentItemEntity> getItems() {
		return Books;
	}

	public void setItems(List<BookContentItemEntity> items) {
		this.Books = items;
	}
}