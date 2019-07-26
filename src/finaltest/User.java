package finaltest;

import java.sql.Date;

public class User {
	private String id;
	private Long pwd;
	private String Uname;
	private String birth;
	private String setNum;
	private Date regDate;
	
	User(String id, Long pwd, String Uname, String birth, String setNum, Date regDate) {
		this.id = id;
		this.pwd = pwd;
		this.Uname = Uname;
		this.birth = birth;
		this.setNum = setNum;
		this.regDate = regDate;
	}
	
	public String getId() {
		return this.id;
	}
	public Long getPwd() {
		return this.pwd;
	}
	public String getUname() {
		return this.Uname;
	}
	public String getBirth() {
		return this.birth;
	}
	public String getSetNum() {
		return this.setNum;
	}
	public Date getRegDate() {
		return this.regDate;
	}
	
}
