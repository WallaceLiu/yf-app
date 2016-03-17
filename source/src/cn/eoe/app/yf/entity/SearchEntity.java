package cn.eoe.app.yf.entity;

import java.util.List;

import cn.eoe.app.yf.entity.base.BaseContentList;

public class SearchEntity extends BaseContentList {
	private List<BookContentItemEntity> Books;

	public List<BookContentItemEntity> getBooks() {
		return Books;
	}

	public void setBooks(List<BookContentItemEntity> books) {
		this.Books = books;
	}
}
