package finaltest;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import finaltest.MainFrameForm.worksWindow;

import org.w3c.dom.Element;

public class BookInfoForm extends JDialog implements ActionListener, MouseListener, KeyListener {

	private JPanel contentPane;
	private JTextField wR_author_TF;
	private JTextField wR_publisher_TF;
	private JTextField wR_isbn_TF;
	private JTextField wR_pubYear_TF;
	private JTextField wR_kdcName_TF;
	private JTextField wR_classNo_TF;


	private JTextField hR_author_TF;
	private JTextField hR_publisher_TF;
	private JTextField hR_isbn_TF;
	private JTextField hR_pubYear_TF;
	private JTextField hR_kdcName_TF;
	private JTextField hR_classNo_TF;
	private JTextArea hR_memo_TA;
	
	JPanel bookImagePanel, InfoPanel;
	JPanel wRPanel, wrInner1_P, panel, panel_1, panel_2, panel_3, panel_4, panel_5, panel_6, panel_7, panel_8, panel_9, panel_10, panel_11, panel_12;
	JLabel wR_Bname_L, wR_author_L, wR_publisher_L, wR_isbn_L, wR_pubYear_L, wR_kdcName_L, wR_classNo_L;
	JPanel hRPanel, panel_13, panel_14, panel_15, panel_16, panel_17, panel_18, panel_19, panel_20, panel_21, panel_22, panel_23, panel_24, panel_25, panel_26;
	JLabel hR_Bname_L, hR_author_L, hR_publisher_L, hR_isbn_L, hR_pubYear_L, hR_kdcName_L, hR_classNo_L, hR_score_L, hR_printScore_L;
	JButton wR_link_B, changeFont_B;
	JSlider scoreSlider;
	JLabel image_Label;
	
	String imageUrl_String, converted_url;
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private int uprs;
	CardLayout cardL;
	
	public static long selectedBid = -1;
	
	XMLParser xmlP;
	NodeList nList;
	Document doc = null;
	private JButton saveMemoNScore_B;
	private JButton wR_to_hR_B;
	
	/**
	 * Create the frame.
	 */
	public BookInfoForm(JFrame frame, String title) {
		super(frame, title, true);
		setBounds(100, 100, 650, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		cardL = new CardLayout(0, 0);
		
		bookImagePanel = new JPanel();
		bookImagePanel.setSize(new Dimension(245, 351));
		bookImagePanel.setPreferredSize(new Dimension(245, 351));
		contentPane.add(bookImagePanel, BorderLayout.WEST);
		
		image_Label = new JLabel();
		bookImagePanel.add(image_Label, BorderLayout.CENTER);
		
		InfoPanel = new JPanel();
		contentPane.add(InfoPanel, BorderLayout.CENTER);
		InfoPanel.setLayout(cardL);
		
		wRPanel = new JPanel();
		wRPanel.setPreferredSize(new Dimension(395, 10));
		InfoPanel.add(wRPanel, "wRPanel");
		
		wrInner1_P = new JPanel();
		wrInner1_P.setAlignmentX(Component.LEFT_ALIGNMENT);
		wrInner1_P.setPreferredSize(new Dimension(375, 50));
		wRPanel.add(wrInner1_P);
		wrInner1_P.setLayout(new BorderLayout(0, 0));
		
		wR_Bname_L = new JLabel("Title");
		wR_Bname_L.setFont(new Font("나눔명조", Font.BOLD, 22));
		wrInner1_P.add(wR_Bname_L, BorderLayout.WEST);
		
		panel = new JPanel();
		panel.setRequestFocusEnabled(false);
		panel.setPreferredSize(new Dimension(375, 30));
		wRPanel.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(150, 10));
		panel.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		wR_author_L = new JLabel(" 작가");
		panel_1.add(wR_author_L, BorderLayout.WEST);
		
		wR_author_TF = new JTextField();
		wR_author_TF.setFocusable(false);
		wR_author_L.setEnabled(false);
		wR_author_TF.setAlignmentX(Component.LEFT_ALIGNMENT);
		wR_author_TF.setText("작가");
		panel_1.add(wR_author_TF, BorderLayout.EAST);
		wR_author_TF.setColumns(8);
		
		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		wR_publisher_L = new JLabel(" 출판사");
		panel_2.add(wR_publisher_L, BorderLayout.WEST);
		
		wR_publisher_TF = new JTextField();
		wR_publisher_TF.setFocusable(false);
		wR_publisher_TF.setText("출판사");
		wR_publisher_TF.setEnabled(true);
		wR_publisher_TF.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_2.add(wR_publisher_TF, BorderLayout.EAST);
		wR_publisher_TF.setColumns(13);
		
		panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(375, 30));
		wRPanel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		panel_4 = new JPanel();
		panel_4.setPreferredSize(new Dimension(225, 10));
		panel_3.add(panel_4, BorderLayout.WEST);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		wR_isbn_L = new JLabel(" ISBN");
		wR_isbn_L.setPreferredSize(new Dimension(40, 15));
		panel_4.add(wR_isbn_L, BorderLayout.WEST);
		
		wR_isbn_TF = new JTextField();
		wR_isbn_TF.setFocusable(false);
		wR_isbn_TF.setText("ISBN");
		wR_isbn_TF.setEnabled(true);
		panel_4.add(wR_isbn_TF, BorderLayout.EAST);
		wR_isbn_TF.setColumns(13);
		
		panel_5 = new JPanel();
		panel_3.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		wR_pubYear_L = new JLabel(" 출판년도");
		panel_5.add(wR_pubYear_L, BorderLayout.WEST);
		
		wR_pubYear_TF = new JTextField();
		wR_pubYear_TF.setFocusable(false);
		wR_pubYear_TF.setEnabled(true);
		panel_5.add(wR_pubYear_TF, BorderLayout.EAST);
		wR_pubYear_TF.setColumns(8);
		
		panel_6 = new JPanel();
		panel_6.setPreferredSize(new Dimension(375, 30));
		wRPanel.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		panel_7 = new JPanel();
		panel_7.setPreferredSize(new Dimension(225, 10));
		panel_6.add(panel_7, BorderLayout.WEST);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		wR_kdcName_L = new JLabel(" 분류");
		panel_7.add(wR_kdcName_L, BorderLayout.WEST);
		
		wR_kdcName_TF = new JTextField();
		wR_kdcName_TF.setFocusable(false);
		wR_kdcName_TF.setEnabled(true);
		panel_7.add(wR_kdcName_TF, BorderLayout.EAST);
		wR_kdcName_TF.setColumns(13);
		
		panel_8 = new JPanel();
		panel_8.setPreferredSize(new Dimension(375, 30));
		wRPanel.add(panel_8);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		panel_9 = new JPanel();
		panel_9.setPreferredSize(new Dimension(225, 10));
		panel_8.add(panel_9, BorderLayout.WEST);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		wR_classNo_L = new JLabel();
		wR_classNo_L.setText(" KDC");
		
		wR_classNo_L.addMouseListener(this);
		panel_9.add(wR_classNo_L, BorderLayout.WEST);
		
		wR_classNo_TF = new JTextField();
		wR_classNo_TF.setFocusable(false);
		wR_classNo_TF.setEnabled(true);
		panel_9.add(wR_classNo_TF, BorderLayout.EAST);
		wR_classNo_TF.setColumns(13);
		
		panel_10 = new JPanel();
		panel_10.setPreferredSize(new Dimension(375, 150));
		wRPanel.add(panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));
		
		panel_11 = new JPanel();
		panel_11.setPreferredSize(new Dimension(375, 30));
		panel_10.add(panel_11, BorderLayout.SOUTH);
		panel_11.setLayout(new BorderLayout(0, 0));
		
		panel_12 = new JPanel();
		panel_11.add(panel_12, BorderLayout.EAST);
		
		wR_link_B = new JButton("구매하러 가기");
		wR_link_B.addActionListener(this);
		panel_12.add(wR_link_B);
		
		wR_to_hR_B = new JButton("읽은 책에 등록");
		wR_to_hR_B.addActionListener(this);
		panel_12.add(wR_to_hR_B);
		
		hRPanel = new JPanel();
		InfoPanel.add(hRPanel, "hRPanel");
		
		panel_13 = new JPanel();
		panel_13.setPreferredSize(new Dimension(375, 50));
		hRPanel.add(panel_13);
		panel_13.setLayout(new BorderLayout(0, 0));
		
		hR_Bname_L = new JLabel("Title");
		hR_Bname_L.setFont(new Font("나눔명조", Font.BOLD, 22));
		panel_13.add(hR_Bname_L, BorderLayout.WEST);
		
		panel_14 = new JPanel();
		panel_14.setPreferredSize(new Dimension(375, 30));
		hRPanel.add(panel_14);
		panel_14.setLayout(new BorderLayout(0, 0));
		
		panel_15 = new JPanel();
		panel_15.setPreferredSize(new Dimension(150, 10));
		panel_14.add(panel_15, BorderLayout.WEST);
		panel_15.setLayout(new BorderLayout(0, 0));
		
		hR_author_L = new JLabel(" 작가");
		panel_15.add(hR_author_L, BorderLayout.WEST);
		
		hR_author_TF = new JTextField();
		hR_author_TF.setFocusable(false);
		hR_author_TF.setText("작가");
		panel_15.add(hR_author_TF, BorderLayout.EAST);
		hR_author_TF.setColumns(8);
		
		panel_16 = new JPanel();
		panel_14.add(panel_16, BorderLayout.CENTER);
		panel_16.setLayout(new BorderLayout(0, 0));
		
		hR_publisher_L = new JLabel(" 출판사");
		panel_16.add(hR_publisher_L, BorderLayout.WEST);
		
		hR_publisher_TF = new JTextField();
		hR_publisher_TF.setFocusable(false);
		hR_publisher_TF.setText("출판사");
		panel_16.add(hR_publisher_TF, BorderLayout.EAST);
		hR_publisher_TF.setColumns(13);
		
		panel_17 = new JPanel();
		panel_17.setPreferredSize(new Dimension(375, 30));
		hRPanel.add(panel_17);
		panel_17.setLayout(new BorderLayout(0, 0));
		
		panel_18 = new JPanel();
		panel_18.setPreferredSize(new Dimension(225, 10));
		panel_17.add(panel_18, BorderLayout.WEST);
		panel_18.setLayout(new BorderLayout(0, 0));
		
		hR_isbn_L = new JLabel(" ISBN");
		panel_18.add(hR_isbn_L, BorderLayout.WEST);
		
		hR_isbn_TF = new JTextField();
		hR_isbn_TF.setFocusable(false);
		hR_isbn_TF.setText("ISBN");
		panel_18.add(hR_isbn_TF, BorderLayout.EAST);
		hR_isbn_TF.setColumns(13);
		
		panel_19 = new JPanel();
		panel_17.add(panel_19, BorderLayout.CENTER);
		panel_19.setLayout(new BorderLayout(0, 0));
		
		hR_pubYear_L = new JLabel(" 출판년도");
		panel_19.add(hR_pubYear_L, BorderLayout.WEST);
		
		hR_pubYear_TF = new JTextField();
		hR_pubYear_TF.setEditable(false);
		panel_19.add(hR_pubYear_TF, BorderLayout.EAST);
		hR_pubYear_TF.setColumns(8);
		
		panel_20 = new JPanel();
		panel_20.setPreferredSize(new Dimension(375, 30));
		hRPanel.add(panel_20);
		panel_20.setLayout(new BorderLayout(0, 0));
		
		panel_21 = new JPanel();
		panel_21.setPreferredSize(new Dimension(225, 10));
		panel_20.add(panel_21, BorderLayout.WEST);
		panel_21.setLayout(new BorderLayout(0, 0));
		
		hR_kdcName_L = new JLabel(" 분류");
		panel_21.add(hR_kdcName_L, BorderLayout.WEST);
		
		hR_kdcName_TF = new JTextField();
		hR_kdcName_TF.setFocusable(false);
		panel_21.add(hR_kdcName_TF, BorderLayout.EAST);
		hR_kdcName_TF.setColumns(13);
		
		panel_22 = new JPanel();
		panel_22.setPreferredSize(new Dimension(375, 30));
		hRPanel.add(panel_22);
		panel_22.setLayout(new BorderLayout(0, 0));
		
		panel_23 = new JPanel();
		panel_23.setPreferredSize(new Dimension(225, 10));
		panel_22.add(panel_23, BorderLayout.WEST);
		panel_23.setLayout(new BorderLayout(0, 0));
		
		hR_classNo_L = new JLabel(" KDC");
		hR_classNo_L.addMouseListener(this);
		panel_23.add(hR_classNo_L, BorderLayout.WEST);
		
		hR_classNo_TF = new JTextField();
		hR_classNo_TF.setFocusable(false);
		panel_23.add(hR_classNo_TF, BorderLayout.EAST);
		hR_classNo_TF.setColumns(13);
		
		changeFont_B = new JButton("글꼴 변경");
		changeFont_B.addActionListener(this);
		panel_22.add(changeFont_B, BorderLayout.CENTER);
		
		saveMemoNScore_B = new JButton("저장");
		saveMemoNScore_B.addActionListener(this);
		panel_22.add(saveMemoNScore_B, BorderLayout.EAST);
		
		panel_24 = new JPanel();
		panel_24.setPreferredSize(new Dimension(375, 150));
		hRPanel.add(panel_24);
		panel_24.setLayout(new BorderLayout(0, 0));
		
		panel_25 = new JPanel();
		panel_25.setPreferredSize(new Dimension(375, 30));
		panel_24.add(panel_25, BorderLayout.SOUTH);
		panel_25.setLayout(new BorderLayout(0, 0));
		
		panel_26 = new JPanel();
		panel_26.setPreferredSize(new Dimension(225, 10));
		panel_25.add(panel_26, BorderLayout.WEST);
		panel_26.setLayout(new BorderLayout(0, 0));
		
		hR_score_L = new JLabel(" 평점");
		panel_26.add(hR_score_L, BorderLayout.WEST);
		
		scoreSlider = new JSlider();
		scoreSlider.setValue(5);
		scoreSlider.setMaximum(10);
		scoreSlider.setPreferredSize(new Dimension(170, 26));
		scoreSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				printScore(scoreSlider.getValue());
			}
		});
		panel_26.add(scoreSlider, BorderLayout.EAST);
		
		hR_printScore_L = new JLabel("★ ★ ☆   ");
		hR_printScore_L.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel_25.add(hR_printScore_L, BorderLayout.EAST);
		
		hR_memo_TA = new JTextArea(6, 22);
		hR_memo_TA.setSize(new Dimension(375, 0));
		hR_memo_TA.setFont(new Font("나눔스퀘어라운드 Regular", Font.PLAIN, 15));
		hR_memo_TA.setText("Memo");
		hR_memo_TA.setAlignmentY(Component.TOP_ALIGNMENT);
		hR_memo_TA.setAlignmentX(Component.LEFT_ALIGNMENT);
		hR_memo_TA.setLineWrap(true);
		panel_24.add(hR_memo_TA, BorderLayout.CENTER);
		hR_memo_TA.setColumns(30);
			
		dbSet();
		try {
			String joinRECnBK = "SELECT * FROM recordedbooks rb INNER JOIN book b ON rb.Bid = b.Bid WHERE b.Bid = ?";
			pstmt = conn.prepareStatement(joinRECnBK);
			pstmt.setLong(1, selectedBid);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.WILL) {
					wR_Bname_L.setText(rs.getString("Bname"));
					wR_author_TF.setText(rs.getString("author"));
					wR_publisher_TF.setText(rs.getString("publisher"));
					wR_isbn_TF.setText("" + rs.getLong("Bid"));
					cardL.show(InfoPanel, "wRPanel");
				}
				else if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.HAD) {
					hR_Bname_L.setText(rs.getString("Bname"));
					hR_author_TF.setText(rs.getString("author"));
					hR_publisher_TF.setText(rs.getString("publisher"));
					hR_isbn_TF.setText("" + rs.getLong("Bid"));
					hR_memo_TA.setText(rs.getString("memo"));
					scoreSlider.setValue(rs.getInt("score"));
					printScore(scoreSlider.getValue());
					cardL.show(InfoPanel, "hRPanel");
				}
				
				XMLParser xmlP = new XMLParser(String.valueOf(rs.getLong("Bid")), URLEncoder.encode(rs.getString("Bname"), "UTF-8"), URLEncoder.encode(rs.getString("author"), "UTF-8"));
				String pub_year_info = "", kdc_name_1s = "", class_no = "";
				
				
				DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
				doc = dBuilder.parse(new InputSource(new StringReader(xmlP.readUrl(false))));
				// root tag 
				doc.getDocumentElement().normalize();
				
				nList = doc.getElementsByTagName("item");
				
				if(nList.getLength() == 0) 
					doc = dBuilder.parse(new InputSource(new StringReader(xmlP.readUrl(true))));
					
				// root tag 
				doc.getDocumentElement().normalize();
				
				nList = doc.getElementsByTagName("item");
					
				
		        for(int i=0; i<nList.getLength();i++){
		 
		            for(Node node = nList.item(i).getFirstChild(); node!=null; node=node.getNextSibling()){ //첫번째 자식을 시작으로 마지막까지 다음 형제를 실행
		            	if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.WILL) {
		            		if(node.getNodeName().equals("pub_year_info")){
			                    pub_year_info = node.getTextContent();
			                }else if(node.getNodeName().equals("kdc_name_1s")){
			                    kdc_name_1s = node.getTextContent();
			                }else if(node.getNodeName().equals("class_no")){
			                    class_no = node.getTextContent();
			                }
		            		
							wR_pubYear_TF.setText(pub_year_info);
							wR_kdcName_TF.setText(kdc_name_1s);
							wR_classNo_TF.setText(class_no);
		            	}
		            	else if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.HAD) {
		            		if(node.getNodeName().equals("pub_year_info")){
		            			pub_year_info = node.getTextContent();
			                }else if(node.getNodeName().equals("kdc_name_1s")){
			                	kdc_name_1s = node.getTextContent();
			                }else if(node.getNodeName().equals("class_no")){
			                	class_no = node.getTextContent();
			                }
		            		
		            		hR_pubYear_TF.setText(pub_year_info);
							hR_kdcName_TF.setText(kdc_name_1s);
							hR_classNo_TF.setText(class_no);
							
		            	}
		            }
		        }	
		        if(!rs.getString("imagelink").equals("")) {
	    			resizeImage(rs.getString("imagelink"));
	    		}
	    		else {
	    			bookImagePanel.remove(image_Label);
	    			bookImagePanel.repaint();
	    			bookImagePanel.revalidate();
	    		}	
			}
		} catch(Exception e) {
			
		}
	}
	
	public void dbSet() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String dbURL = "jdbc:mysql://localhost:3307/finaltest?serverTimezone=UTC"; // localhost:3306 포트는 컴퓨터설치된 mysql주소
			String dbID = "root";
			String dbPassword = "1234";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			System.out.println("BookInfoForm DB준비됨");
				
		} catch (Exception e) {
			e.printStackTrace(); // 오류가 무엇인지 출력
		}
	}
	
	public void resizeImage(String title_url) {
		Image downloadedImage = null;
		int w, h;
		double ratio;						// 사진 줄이는 비율
		try {
			URL url = new URL(title_url);
	        downloadedImage = ImageIO.read(url);
	        ratio = (double)bookImagePanel.getHeight() / (double)downloadedImage.getHeight(null);
	        w = (int)(downloadedImage.getWidth(null) * ratio);
	        h = (int)(downloadedImage.getHeight(null) * ratio);
	        
	        Image resizedImage = downloadedImage.getScaledInstance(w, h, downloadedImage.SCALE_SMOOTH);
	        image_Label.setIcon(new ImageIcon(resizedImage));
	        bookImagePanel.add(image_Label);
	        bookImagePanel.repaint();
	        bookImagePanel.revalidate();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	class Imagepanel extends JPanel {
		public void paint(Graphics g, Image img) {
			g.drawImage(img, 0, 0, null);
		}
	}
	
	public void printScore(int scoreValue) {
		switch(scoreValue) {
		case 10:
			hR_printScore_L.setText("★ ★ ★ ★ ★   ");
			break;
		case 9:
			hR_printScore_L.setText("★ ★ ★ ★ ☆   ");
			break;
		case 8:
			hR_printScore_L.setText("★ ★ ★ ★   ");
			break;
		case 7:
			hR_printScore_L.setText("★ ★ ★ ☆   ");
			break;
		case 6:
			hR_printScore_L.setText("★ ★ ★   ");
			break;
		case 5:
			hR_printScore_L.setText("★ ★ ☆   ");
			break;
		case 4:
			hR_printScore_L.setText("★ ★   ");
			break;
		case 3:
			hR_printScore_L.setText("★ ☆   ");
			break;
		case 2:
			hR_printScore_L.setText("★   ");
			break;
		case 1:
			hR_printScore_L.setText("☆   ");
			break;
		default:
			hR_printScore_L.setText("");
			break;
		}
	}
	
	public void display() {
		setTitle("");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					selectedBid = -1;
					if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.WILL) {
						wR_Bname_L.setText("제목");
						wR_author_TF.setText("");
						wR_publisher_TF.setText("");
						wR_isbn_TF.setText("");
						wR_pubYear_TF.setText("");
						wR_kdcName_TF.setText("");
						wR_classNo_TF.setText("");
					}
					else if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.HAD) {
						hR_Bname_L.setText("제목");
						hR_author_TF.setText("");
						hR_publisher_TF.setText("");
						hR_isbn_TF.setText("");
						hR_pubYear_TF.setText("");
						hR_kdcName_TF.setText("");
						hR_classNo_TF.setText("");
						hR_memo_TA.setText("");
						scoreSlider.setValue(5);
						hR_printScore_L.setText("");
					}
				}
			});
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == wR_classNo_L || e.getSource() == hR_classNo_L)
			try {
				final URI uri = new URI("https://ko.wikipedia.org/wiki/%ED%95%9C%EA%B5%AD%EC%8B%AD%EC%A7%84%EB%B6%84%EB%A5%98%EB%B2%95");
				open(uri);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == wR_classNo_L)
			wR_classNo_L.setToolTipText("한국십진분류법");
		else if(e.getSource() == hR_classNo_L)
			hR_classNo_L.setToolTipText("한국십진분류법");
		else if(e.getSource() == wR_Bname_L) 
			wR_Bname_L.setToolTipText(wR_Bname_L.getText());
		else if(e.getSource() == wR_author_TF)
			wR_author_TF.setToolTipText(wR_author_TF.getText());
		else if(e.getSource() == hR_Bname_L)
			hR_Bname_L.setToolTipText(hR_Bname_L.getText());
		else if(e.getSource() == hR_author_TF)
			hR_author_TF.setToolTipText(hR_author_TF.getText());
			
	}
	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == wR_classNo_L)
			wR_classNo_L.setText(" KDC");
		else if(e.getSource() == hR_classNo_L)
			hR_classNo_L.setText(" KDC");
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	  }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == wR_link_B) {
			try {
				final URI uri = new URI("https://book.naver.com/search/search.nhn?sm=sta_hty.book&sug=&where=nexearch&query=" + wR_isbn_TF.getText());
				open(uri);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
		else if(e.getSource() == wR_to_hR_B) {
			String WRtoHR = "UPDATE recordedbooks SET wORh = 1 WHERE Uid = ? AND Bid = ?";
			try {
				pstmt = conn.prepareStatement(WRtoHR);
				pstmt.setString(1, MainFrameForm.connectedID);
				pstmt.setLong(2, selectedBid);
				uprs = pstmt.executeUpdate();
				
				if(uprs == 1) {
					JOptionPane.showMessageDialog(null, "변경되었습니다.");
					selectedBid = -1;
					MainFrameForm.wRBooksTable.updateUI();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == changeFont_B) {
			// 환경설정에 저장
			JFontChooser fontChooser = new JFontChooser();
			 int result = fontChooser.showDialog(hR_memo_TA);
			 if (result == JFontChooser.OK_OPTION)
			 {
			      Font font = fontChooser.getSelectedFont(); 
			      hR_memo_TA.setFont(font); 
			 }
		}
		else if(e.getSource() == saveMemoNScore_B) {
			String updateMemoNScore = "UPDATE recordedbooks SET memo = ?, score = ? WHERE Bid = ? AND Uid = ?";
			String memo = hR_memo_TA.getText();
			int score = scoreSlider.getValue();
			
			try {
				pstmt = conn.prepareStatement(updateMemoNScore);
				pstmt.setString(1, memo);
				pstmt.setInt(2, score);
				pstmt.setLong(3, selectedBid);
				pstmt.setString(4, MainFrameForm.connectedID);
				uprs = pstmt.executeUpdate();
				
				if(uprs == 1) {
					JOptionPane.showMessageDialog(null, "변경되었습니다.");
					MainFrameForm.hRBooksTable.updateUI();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}

}
