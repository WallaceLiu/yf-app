package cn.eoe.app.yf.entity.base;

public abstract class BaseContentItem {

	private Integer BookId;
	private Integer WebSourceId;
	private String BookUrl;
	private String BookName;
	private String BookTypeName;
	private String AuthorName;
	private String Introduction;
	private String BookImageUrl;
	private String ChaptersJsonUrl;
	private String ChaptersUrl;
	private String LastUpdateDate;
	private String LastChapterName;
	private String LastChapterUrl;
	private Integer Score;
	private Integer ClickCount;
	private Integer MonthClickCount;
	private Integer WeekClickCount;
	private Integer RecommentCount;
	private Integer MonthRecommended;
	private Integer Zan;

	public Integer getBookId() {
		return BookId;
	}

	public void setBookId(Integer bookId) {
		this.BookId = bookId;
	}

	public String getBookName() {
		return BookName;
	}

	public void setBookName(String bookName) {
		this.BookName = bookName;
	}

	public String getBookTypeName() {
		return BookTypeName;
	}

	public void setBookTypeName(String bookTypeName) {
		this.BookTypeName = bookTypeName;
	}

	public Integer getWebSourceId() {
		return WebSourceId;
	}

	public void setWebSourceId(Integer webSourceId) {
		this.WebSourceId = webSourceId;
	}

	public String getBookUrl() {
		return BookUrl;
	}

	public void setBookUrl(String bookUrl) {
		this.BookUrl = bookUrl;
	}

	public String getAuthorName() {
		return AuthorName;
	}

	public void setAuthorName(String authorName) {
		this.AuthorName = authorName;
	}

	public String getIntroduction() {
		return Introduction;
	}

	public void setIntroduction(String introduction) {
		this.Introduction = introduction;
	}

	public String getBookImageUrl() {
		return BookImageUrl;
	}

	public void setBookImageUrl(String bookImageUrl) {
		this.BookImageUrl = bookImageUrl;
	}

	public String getChaptersJsonUrl() {
		return ChaptersJsonUrl;
	}

	public void setChaptersJsonUrl(String chaptersJsonUrl) {
		this.ChaptersJsonUrl = chaptersJsonUrl;
	}

	public String getChaptersUrl() {
		return ChaptersUrl;
	}

	public void setChaptersUrl(String chaptersUrl) {
		this.ChaptersUrl = chaptersUrl;
	}

	public String getLastUpdateDate() {
		return LastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.LastUpdateDate = lastUpdateDate;
	}

	public String getLastChapterName() {
		return LastChapterName;
	}

	public void setLastChapterName(String lastChapterName) {
		this.LastChapterName = lastChapterName;
	}

	public String getLastChapterUrl() {
		return LastChapterUrl;
	}

	public void setLastChapterUrl(String lastChapterUrl) {
		this.LastChapterUrl = lastChapterUrl;
	}

	public Integer getScore() {
		return Score;
	}

	public void setScore(Integer score) {
		this.Score = score;
	}

	public Integer getClickCount() {
		return ClickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.ClickCount = clickCount;
	}

	public Integer getMonthClickCount() {
		return MonthClickCount;
	}

	public void setMonthClickCount(Integer monthClickCount) {
		this.MonthClickCount = monthClickCount;
	}

	public Integer getWeekClickCount() {
		return WeekClickCount;
	}

	public void setWeekClickCount(Integer weekClickCount) {
		this.WeekClickCount = weekClickCount;
	}

	public Integer getRecommentCount() {
		return RecommentCount;
	}

	public void setRecommentCount(Integer recommentCount) {
		this.RecommentCount = recommentCount;
	}

	public Integer getMonthRecommended() {
		return MonthRecommended;
	}

	public void setMonthRecommended(Integer monthRecommended) {
		this.MonthRecommended = monthRecommended;
	}

	public Integer getZan() {
		return Zan;
	}

	public void setZan(Integer zan) {
		this.Zan = zan;
	}

	public String toString() {
		return "name=" + this.getBookName() + ",author=" + this.getAuthorName()
				+ ",updateDate=" + this.getLastUpdateDate();
	}
}
