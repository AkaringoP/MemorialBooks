package finaltest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.*;
import java.net.URL;

public class XMLParser {
	static String key = "2c628b2c9c8d02cc973359e0e9965ec3";
	static String isbn = "";
	static String title = "";
	static String author = "";
	
	public XMLParser(String isbn, String title, String author) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
	}
	
	public String getTagValue(String tag, Element eElement) {
	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if(nValue == null) 
	        return null;
	    return nValue.getNodeValue();
	}
	
	public Document parseXML(InputStream stream) throws Exception{
		 
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
 
        try{
 
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
 
            doc = objDocumentBuilder.parse(stream);
 
        }catch(Exception ex){
            throw ex;
        }   
        return doc;
    }
	
	public String readUrl(boolean whichText) throws Exception{ // whichText가 0이면 isbn
        BufferedInputStream reader = null;
        URL url = null;
        try {
        	if(!whichText) {
        		url = new URL("http://www.nl.go.kr/app/nl/search/openApi/search.jsp?key="
                        + key + "&category=dan&detailSearch=true&isbnOp=isbn&isbnCode=" + isbn);
        	}
        	else {
        		url = new URL("http://www.nl.go.kr/app/nl/search/openApi/search.jsp?key="
                        + key + "&category=dan&detailSearch=true&f1=title&v1=" + title
                        + "&f2=author&v2=" + author);
        	}
            
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
