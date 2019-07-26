package finaltest;

import java.sql.Date;

public class Book {
	private long Bid;
	private String Bname;
	private String author;
	private String publisher;
	private String link;
	private String imageLink;
	
	
	Book(long Bid, String Bname, String author, String publisher, String link, String imageLink) {
		this.Bid = Bid;
		this.Bname = Bname;
		this.author = author;
		this.publisher = publisher;
		this.link = link;
		this.imageLink = imageLink;
	}
	
	public long getBid() {
		return this.Bid;
	}
	public String getBname() {
		return this.Bname;
	}
	public String getAuthor() {
		return this.author;
	}
	public String getPublisher() {
		return this.publisher;
	}
	public String getLink() {
		return this.link;
	}
	public String getImageLink() {
		return this.imageLink;
	}
}
