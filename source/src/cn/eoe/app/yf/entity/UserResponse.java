package cn.eoe.app.yf.entity;

import java.util.List;

public class UserResponse {
	private UserInfoItem info; // 用户信息
	private List<UserFavoriteList> favorite; // 用户收藏

	public UserInfoItem getInfo() {
		return info;
	}

	public void setInfo(UserInfoItem info) {
		this.info = info;
	}

	public List<UserFavoriteList> getFavorite() {
		return favorite;
	}

	public void setFavorite(List<UserFavoriteList> favorite) {
		this.favorite = favorite;
	}

}
