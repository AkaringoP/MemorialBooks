package finaltest;

import java.io.BufferedInputStream;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.xml.ws.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class BookApiJSON {
	static String key = "2c628b2c9c8d02cc973359e0e9965ec3";			// 국립중앙도서관 인증키
	static String categori = "", value = "";
	static int pageNo = -1, totalContent;
	JSONObject jsonObject;
	JSONArray contentsArray;
	
	public BookApiJSON(int pageNo, String categori, String value) throws Exception {
        JSONParser jsonParser = new JSONParser();
        this.pageNo = pageNo;
        this.value = value;
        if(categori == "제   목")
        	this.categori = "title";
        else if(categori == "작   가")
        	this.categori = "author";
        else if(categori == "출판사")
        	this.categori = "publisher";
        else if(categori == "ISBN")
        	this.categori = "isbn";
        jsonObject = (JSONObject)jsonParser.parse(readUrl());
        totalContent = Integer.parseInt((String)jsonObject.get("TOTAL_COUNT"));
        contentsArray = (JSONArray)jsonObject.get("docs");
	}
	    
    private static String readUrl() throws Exception{
        BufferedInputStream reader = null;
        
        try {
            URL url = new URL("http://seoji.nl.go.kr/landingPage/SearchApi.do?cert_key="
                    + key + "&result_style=json&page_no=" + pageNo + "&page_size=5&" + categori + "=" + value);
            
            reader = new BufferedInputStream(url.openStream());
            StringBuffer buffer = new StringBuffer();
            int i = 0;
            byte[] b = new byte[4096];
            while((i = reader.read(b)) != -1){
                buffer.append(new String(b, 0, i));
            }
            return buffer.toString();
            
        } finally{
            if(reader != null) reader.close();
            
        }
    }
}