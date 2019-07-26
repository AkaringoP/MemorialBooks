package finaltest;

public class UnicodeMaker {
	public static String decode(String unicode) {		// 유니코드 --> 한글
		StringBuffer str = new StringBuffer();
		try {
	        char ch = 0;
	        for( int i= unicode.indexOf("\\u"); i > -1; i = unicode.indexOf("\\u") ){
	            ch = (char)Integer.parseInt( unicode.substring( i + 2, i + 6 ) ,16);
	            str.append(unicode.substring(0, i));
	            str.append(String.valueOf(ch));
	            unicode = unicode.substring(i + 6);
	        }
	        str.append(unicode);
	    } catch(Exception e) {
	    	
		}
		
		return str.toString();
	} 
	

    public static String encode(String korean) {		// 한글 --> 유니코드
        StringBuffer str = new StringBuffer();
        try {
        	for (int i = 0; i < korean.length(); i++) {
                if(((int) korean.charAt(i) == 32)) {
                    str.append(" ");
                    continue;
                }
                str.append("\\u");
                str.append(Integer.toHexString((int) korean.charAt(i)));

            }
        } catch (Exception e) {
        	
        }
        return str.toString();
    }
}
