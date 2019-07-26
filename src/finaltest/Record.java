package finaltest;

import java.sql.Date;

public class Record {
	private String Uid;
	private long Bid;
	private boolean wORh;
	private Date regDate;
	private int score;
	private String memo;
	
	Record(String Uid, long Bid, boolean wORh, Date regDate, int score, String memo) {
		this.Uid = Uid;
		this.Bid = Bid;
		this.wORh = wORh;
		this.regDate = regDate;
		this.score = score;
		this.memo = memo;
	}
	
	public String getUid() {
		return this.Uid;
	}
	public long getBid() {
		return this.Bid;
	}
	public boolean getWorH() {
		return this.wORh;
	}
	public Date getRegDate() {
		return this.regDate;
	}
	public int getScore() {
		return this.score;
	}
	public String getMemo() {
		return this.memo;
	}
}
