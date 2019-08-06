package finaltest;

import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SearchBookForm extends JFrame implements ActionListener, MouseListener {

	/**			책 검색을 위한 폼			**/
	
	private JPanel contentPane;
	JPanel lowButtonBox, highButtonBox;
	JComboBox categori_CB;
	private JTextField search_TF;
	JButton search_B;
	JButton total_B, firstPage_B, pageDown_B, previous_B, current_B, next_B, pageUp_B, lastPage_B, confirm_B, cancel_B;
	
	static int selectedOrder = -1;
	
	static String key = "2c628b2c9c8d02cc973359e0e9965ec3";
	
	Color disabled_C = new Color(203, 203, 203);
	Color normalNhover_C = new Color(112, 120, 132);
	Color Pressed_C = new Color(48, 58, 72);
	Color disabledNnormal_BC = new Color(255, 255, 255);
	Color hoverNpressed_BC = new Color(233, 233, 233);
	
	JPanel contentBox_Outer;
	private JPanel[] contentBox_Inner = new JPanel[5];
	private JPanel[] inner_Image = new JPanel[5];
	private JLabel[] image_Label = new JLabel[5];
	private JPanel[] inner_Information = new JPanel[5];
	private JPanel[] inner_RadioB = new JPanel[5];
	private JRadioButton[] radioB = new JRadioButton[5];
	private JLabel[] information_Title = new JLabel[5];
	private JLabel[] information_Author = new JLabel[5];
	private JLabel[] information_Isbn = new JLabel[5];
	private JLabel[] information_Publisher = new JLabel[5];
	ButtonGroup radio_BTG;
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	/**
	 * Create the frame.
	 */
	public SearchBookForm() {
		setSize(new Dimension(590, 600));
		setPreferredSize(new Dimension(590, 600));
		setMinimumSize(new Dimension(590, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 550);
		contentPane = new JPanel();
		contentPane.setSize(new Dimension(554, 560));
		contentPane.setMinimumSize(new Dimension(554, 560));
		contentPane.setPreferredSize(new Dimension(554, 560));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		lowButtonBox = new JPanel();
		lowButtonBox.setSize(new Dimension(473, 33));
		lowButtonBox.setMinimumSize(new Dimension(473, 33));
		contentPane.add(lowButtonBox, BorderLayout.SOUTH);
		
		total_B = new JButton("총 \" \"권");
		total_B.setEnabled(false);
		total_B.addMouseListener(this);
		total_B.setBackground(disabledNnormal_BC);
		total_B.setForeground(normalNhover_C);
		lowButtonBox.add(total_B);
		
		firstPage_B = new JButton("1");
		firstPage_B.addActionListener(this);
		firstPage_B.addMouseListener(this);
		firstPage_B.setBackground(disabledNnormal_BC);
		firstPage_B.setForeground(normalNhover_C);
		lowButtonBox.add(firstPage_B);
		
		pageDown_B = new JButton("<");
		pageDown_B.addActionListener(this);
		pageDown_B.addMouseListener(this);
		pageDown_B.setBackground(disabledNnormal_BC);
		pageDown_B.setForeground(normalNhover_C);
		lowButtonBox.add(pageDown_B);
		
		previous_B = new JButton("\" \"");
		previous_B.addActionListener(this);
		previous_B.addMouseListener(this);
		previous_B.setBackground(disabledNnormal_BC);
		previous_B.setForeground(normalNhover_C);
		lowButtonBox.add(previous_B);
		
		current_B = new JButton("\" \"");
		current_B.addMouseListener(this);
		current_B.setBackground(disabledNnormal_BC);
		current_B.setForeground(normalNhover_C);
		lowButtonBox.add(current_B);
		
		next_B = new JButton("\" \"");
		next_B.addActionListener(this);
		next_B.addMouseListener(this);
		next_B.setBackground(disabledNnormal_BC);
		next_B.setForeground(normalNhover_C);
		lowButtonBox.add(next_B);
		
		pageUp_B = new JButton(">");
		pageUp_B.addActionListener(this);
		pageUp_B.addMouseListener(this);
		pageUp_B.setBackground(disabledNnormal_BC);
		pageUp_B.setForeground(normalNhover_C);
		lowButtonBox.add(pageUp_B);
		
		lastPage_B = new JButton("\"\"");
		lastPage_B.addActionListener(this);
		lastPage_B.addMouseListener(this);
		lastPage_B.setBackground(disabledNnormal_BC);
		lastPage_B.setForeground(normalNhover_C);
		lowButtonBox.add(lastPage_B);
		
		confirm_B = new JButton("확인");
		confirm_B.addActionListener(this);
		confirm_B.addMouseListener(this);
		confirm_B.setBackground(disabledNnormal_BC);
		confirm_B.setForeground(normalNhover_C);
		lowButtonBox.add(confirm_B);
		
		cancel_B = new JButton("취소");
		cancel_B.addActionListener(this);
		cancel_B.addMouseListener(this);
		cancel_B.setBackground(disabledNnormal_BC);
		cancel_B.setForeground(normalNhover_C);
		lowButtonBox.add(cancel_B);
		
		highButtonBox = new JPanel();
		contentPane.add(highButtonBox, BorderLayout.NORTH);
		
		categori_CB = new JComboBox();
		categori_CB.setModel(new DefaultComboBoxModel(new String[] {"//구분//", "제   목", "작   가", "출판사", "ISBN"}));
		categori_CB.addMouseListener(this);
		categori_CB.setBackground(disabledNnormal_BC);
		categori_CB.setForeground(normalNhover_C);
		highButtonBox.add(categori_CB);
		
		search_TF = new JTextField();
		search_TF.setFont(new Font("나눔고딕코딩", Font.PLAIN, 24));
		highButtonBox.add(search_TF);
		search_TF.addActionListener(this);
		search_TF.setColumns(15);
		
		search_B = new JButton("검색");
		search_B.setFont(new Font("굴림체", Font.PLAIN, 18));
		search_B.addActionListener(this);
		search_B.addMouseListener(this);
		search_B.setBackground(disabledNnormal_BC);
		search_B.setForeground(normalNhover_C);
		highButtonBox.add(search_B);
		
		contentBox_Outer = new JPanel();
		contentBox_Outer.setPreferredSize(new Dimension(545, 478));
		contentBox_Outer.setAlignmentY(Component.TOP_ALIGNMENT);
		contentBox_Outer.setMinimumSize(new Dimension(545, 478));
		contentBox_Outer.setSize(new Dimension(544, 478));
		contentPane.add(contentBox_Outer, BorderLayout.CENTER);
		contentBox_Outer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		radio_BTG = new ButtonGroup();
		
		for(int i=0; i<5; i++) {
			contentBox_Inner[i] = new JPanel();
			contentBox_Inner[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			contentBox_Inner[i].setPreferredSize(new Dimension(474, 90));
			contentBox_Inner[i].setSize(new Dimension(544, 90));
			contentBox_Outer.add(contentBox_Inner[i]);
			contentBox_Inner[i].setLayout(new BorderLayout(0, 0));
			
			inner_Image[i] = new JPanel();
			inner_Image[i].setPreferredSize(new Dimension(90, 90));
			contentBox_Inner[i].add(inner_Image[i], BorderLayout.WEST);
			
			image_Label[i] = new JLabel();
			inner_Image[i].add(image_Label[i], BorderLayout.CENTER);
			
			inner_Information[i] = new JPanel();
			inner_Information[i].setSize(new Dimension(363, 90));
			inner_Information[i].setMinimumSize(new Dimension(435, 90));
			inner_Information[i].setPreferredSize(new Dimension(435, 90));
			contentBox_Inner[i].add(inner_Information[i], BorderLayout.CENTER);
			inner_Information[i].setLayout(new BorderLayout(0, 0));
			
			information_Title[i] = new JLabel();
			information_Title[i].setFont(new Font("HY견고딕", Font.PLAIN, 35));
			information_Title[i].addMouseListener(this);
			inner_Information[i].add(information_Title[i], BorderLayout.NORTH);
			
			information_Author[i] = new JLabel();
			information_Author[i].setPreferredSize(new Dimension(100, 45));
			information_Author[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			information_Author[i].addMouseListener(this);
			inner_Information[i].add(information_Author[i], BorderLayout.WEST);
			
			information_Isbn[i] = new JLabel();
			information_Isbn[i].setPreferredSize(new Dimension(145, 45));
			information_Isbn[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			information_Isbn[i].addMouseListener(this);
			inner_Information[i].add(information_Isbn[i], BorderLayout.EAST);
			
			information_Publisher[i] = new JLabel();
			information_Publisher[i].setPreferredSize(new Dimension(190, 45));
			information_Publisher[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			information_Publisher[i].addMouseListener(this);
			inner_Information[i].add(information_Publisher[i], BorderLayout.CENTER);
			
			inner_RadioB[i] = new JPanel();
			inner_RadioB[i].setPreferredSize(new Dimension(20, 90));
			contentBox_Inner[i].add(inner_RadioB[i], BorderLayout.EAST);
			inner_RadioB[i].setLayout(new BorderLayout(0, 0));
			
			radioB[i] = new JRadioButton("");
			inner_RadioB[i].add(radioB[i], BorderLayout.CENTER);
			radio_BTG.add(radioB[i]);
		}
	}
	
	public void display() {
		setTitle("책 검색");
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void lowButtonSet(int pageNo, int endPage) {
		if(pageNo == 1) {
			firstPage_B.setEnabled(false);
			firstPage_B.setText(" ");
			pageDown_B.setEnabled(false);
			previous_B.setEnabled(false);
			previous_B.setText(" ");
			current_B.setEnabled(false);
			current_B.setText("" + pageNo);
			next_B.setEnabled(true);
			next_B.setText("" + (pageNo + 1));
			pageUp_B.setEnabled(true);
			lastPage_B.setEnabled(true);
			lastPage_B.setText("" + endPage);
		}
		else if(pageNo == 2 || pageNo == 3) { 
			firstPage_B.setEnabled(false);
			firstPage_B.setText(" ");
			pageDown_B.setEnabled(false);
			previous_B.setEnabled(true);
			previous_B.setText("" + (pageNo - 1));
			current_B.setEnabled(false);
			current_B.setText("" + pageNo);
			next_B.setEnabled(true);
			next_B.setText("" + (pageNo + 1));
			pageUp_B.setEnabled(true);
			lastPage_B.setEnabled(true);
			lastPage_B.setText("" + endPage);
		}
		else if(pageNo == endPage) {
			firstPage_B.setEnabled(true);
			firstPage_B.setText("1");
			pageDown_B.setEnabled(true);
			previous_B.setEnabled(true);
			previous_B.setText("" + (pageNo - 1));
			current_B.setEnabled(false);
			current_B.setText("" + pageNo);
			next_B.setEnabled(false);
			next_B.setText(" ");
			pageUp_B.setEnabled(false);
			lastPage_B.setEnabled(false);
		}
		else if(pageNo == endPage - 1 || pageNo == endPage - 2) {
			firstPage_B.setEnabled(true);
			firstPage_B.setText("1");
			pageDown_B.setEnabled(true);
			previous_B.setEnabled(true);
			previous_B.setText("" + (pageNo - 1));
			current_B.setEnabled(false);
			current_B.setText("" + pageNo);
			next_B.setEnabled(true);
			next_B.setText("" + (pageNo + 1));
			pageUp_B.setEnabled(false);
			lastPage_B.setEnabled(false);
		}
		else {
			firstPage_B.setEnabled(true);
			firstPage_B.setText("1");
			pageDown_B.setEnabled(true);
			previous_B.setEnabled(true);
			previous_B.setText("" + (pageNo - 1));
			current_B.setEnabled(false);
			current_B.setText("" + pageNo);
			next_B.setEnabled(true);
			next_B.setText("" + (pageNo + 1));
			pageUp_B.setEnabled(true);
			lastPage_B.setEnabled(true);
			lastPage_B.setText("" + endPage);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == firstPage_B) {
			firstPage_B.setBackground(hoverNpressed_BC);
			firstPage_B.setForeground(Pressed_C);
			if(firstPage_B.isEnabled() == false) {
				firstPage_B.setBackground(disabledNnormal_BC);
				firstPage_B.setForeground(disabled_C);
			}
		}
		else if(e.getSource() == pageDown_B) {
			pageDown_B.setBackground(hoverNpressed_BC);
			pageDown_B.setForeground(Pressed_C);
			if(pageDown_B.isEnabled() == false) {
				pageDown_B.setBackground(disabledNnormal_BC);
				pageDown_B.setForeground(disabled_C);
			}
		}
		else if(e.getSource() == previous_B) {
			previous_B.setBackground(hoverNpressed_BC);
			previous_B.setForeground(Pressed_C);
			if(previous_B.isEnabled() == false) {
				previous_B.setBackground(disabledNnormal_BC);
				previous_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == current_B) {
			current_B.setBackground(disabledNnormal_BC);
			current_B.setForeground(disabled_C);
		}
		else if(e.getSource() == next_B) {
			next_B.setBackground(hoverNpressed_BC);
			next_B.setForeground(Pressed_C);
			if(next_B.isEnabled() == false) {
				next_B.setBackground(disabledNnormal_BC);
				next_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == pageUp_B) {
			pageUp_B.setBackground(hoverNpressed_BC);
			pageUp_B.setForeground(Pressed_C);
			if(pageUp_B.isEnabled() == false) {
				pageUp_B.setBackground(disabledNnormal_BC);
				pageUp_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == lastPage_B) {
			lastPage_B.setBackground(hoverNpressed_BC);
			lastPage_B.setForeground(Pressed_C);
			if(lastPage_B.isEnabled() == false) {
				lastPage_B.setBackground(disabledNnormal_BC);
				lastPage_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == confirm_B) {
			confirm_B.setBackground(hoverNpressed_BC);
			confirm_B.setForeground(Pressed_C);
		}
		else if(e.getSource() == cancel_B) {
			cancel_B.setBackground(hoverNpressed_BC);
			cancel_B.setForeground(Pressed_C);
		}
		else if(e.getSource() == search_B) {
			cancel_B.setBackground(hoverNpressed_BC);
			cancel_B.setForeground(Pressed_C);
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == firstPage_B) {
			firstPage_B.setBackground(hoverNpressed_BC);
			firstPage_B.setForeground(normalNhover_C);
			if(firstPage_B.isEnabled() == false) {
				firstPage_B.setBackground(disabledNnormal_BC);
				firstPage_B.setForeground(normalNhover_C);
			}
		}
		if(e.getSource() == pageDown_B) {
			pageDown_B.setBackground(hoverNpressed_BC);
			pageDown_B.setForeground(normalNhover_C);
			if(pageDown_B.isEnabled() == false) {
				pageDown_B.setBackground(disabledNnormal_BC);
				pageDown_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == previous_B) {
			previous_B.setBackground(hoverNpressed_BC);
			previous_B.setForeground(normalNhover_C);
			if(previous_B.isEnabled() == false) {
				previous_B.setBackground(disabledNnormal_BC);
				previous_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == current_B) {
			current_B.setBackground(disabledNnormal_BC);
			current_B.setForeground(disabled_C);
		}
		else if(e.getSource() == next_B) {
			next_B.setBackground(hoverNpressed_BC);
			next_B.setForeground(normalNhover_C);
			if(next_B.isEnabled() == false) {
				next_B.setBackground(disabledNnormal_BC);
				next_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == pageUp_B) {
			pageUp_B.setBackground(hoverNpressed_BC);
			pageUp_B.setForeground(normalNhover_C);
			if(pageUp_B.isEnabled() == false) {
				pageUp_B.setBackground(disabledNnormal_BC);
				pageUp_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == lastPage_B) {
			lastPage_B.setBackground(hoverNpressed_BC);
			lastPage_B.setForeground(normalNhover_C);
			if(lastPage_B.isEnabled() == false) {
				lastPage_B.setBackground(disabledNnormal_BC);
				lastPage_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == confirm_B) {
			confirm_B.setBackground(hoverNpressed_BC);
			confirm_B.setForeground(normalNhover_C);
		}
		else if(e.getSource() == cancel_B) {
			cancel_B.setBackground(hoverNpressed_BC);
			cancel_B.setForeground(normalNhover_C);
		}
		else if(e.getSource() == search_B) {
			search_B.setBackground(hoverNpressed_BC);
			search_B.setForeground(normalNhover_C);
		}
		for(int i = 0; i < 5; i++) {
			if(e.getSource() == information_Title[i]) 
				information_Title[i].setToolTipText(information_Title[i].getText());
			else if(e.getSource() == information_Author[i]) 
				information_Author[i].setToolTipText(information_Author[i].getText());
			else if(e.getSource() == information_Publisher[i])
				information_Publisher[i].setToolTipText(information_Publisher[i].getText());
			else if(e.getSource() == information_Isbn[i])
				information_Isbn[i].setToolTipText(information_Isbn[i].getText());
		}
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == firstPage_B) {
			firstPage_B.setBackground(disabledNnormal_BC);
			firstPage_B.setForeground(normalNhover_C);
			if(firstPage_B.isEnabled() == false) {
				firstPage_B.setBackground(disabledNnormal_BC);
				firstPage_B.setForeground(disabled_C);
			}
		}
		if(e.getSource() == pageDown_B) {
			pageDown_B.setBackground(disabledNnormal_BC);
			pageDown_B.setForeground(normalNhover_C);
			if(pageDown_B.isEnabled() == false) {
				pageDown_B.setBackground(disabledNnormal_BC);
				pageDown_B.setForeground(disabled_C);
			}
		}
		else if(e.getSource() == previous_B) {
			previous_B.setBackground(disabledNnormal_BC);
			previous_B.setForeground(normalNhover_C);
			if(previous_B.isEnabled() == false) {
				previous_B.setBackground(disabledNnormal_BC);
				previous_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == current_B) {
			current_B.setBackground(disabledNnormal_BC);
			current_B.setForeground(disabled_C);
		}
		else if(e.getSource() == next_B) {
			next_B.setBackground(disabledNnormal_BC);
			next_B.setForeground(normalNhover_C);
			if(next_B.isEnabled() == false) {
				next_B.setBackground(disabledNnormal_BC);
				next_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == pageUp_B) {
			pageUp_B.setBackground(disabledNnormal_BC);
			pageUp_B.setForeground(normalNhover_C);
			if(pageUp_B.isEnabled() == false) {
				pageUp_B.setBackground(disabledNnormal_BC);
				pageUp_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == lastPage_B) {
			lastPage_B.setBackground(disabledNnormal_BC);
			lastPage_B.setForeground(normalNhover_C);
			if(lastPage_B.isEnabled() == false) {
				lastPage_B.setBackground(disabledNnormal_BC);
				lastPage_B.setForeground(normalNhover_C);
			}
		}
		else if(e.getSource() == confirm_B) {
			confirm_B.setBackground(disabledNnormal_BC);
			confirm_B.setForeground(normalNhover_C);
		}
		else if(e.getSource() == cancel_B) {
			cancel_B.setBackground(disabledNnormal_BC);
			cancel_B.setForeground(normalNhover_C);
		}
		else if(e.getSource() == search_B) {
			search_B.setBackground(disabledNnormal_BC);
			search_B.setForeground(normalNhover_C);
		}
	}
	
	public void resizeImage(String title_url) {
		Image downloadedImage = null;
		int w, h;
		double ratio;						// 사진 줄이는 비율
		try {
			URL url = new URL(title_url);
	        downloadedImage = ImageIO.read(url);
	        ratio = (double)inner_Image[0].getHeight() / (double)downloadedImage.getHeight(null);
	        w = (int)(downloadedImage.getWidth(null) * ratio);
	        h = (int)(downloadedImage.getHeight(null) * ratio);
	        
	        Image resizedImage = downloadedImage.getScaledInstance(w, h, downloadedImage.SCALE_SMOOTH);
	        image_Label[selectedOrder].setIcon(new ImageIcon(resizedImage));
	        inner_Image[selectedOrder].add(image_Label[selectedOrder]);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	class Imagepanel extends JPanel {
		public void paint(Graphics g, Image img) {
			g.drawImage(img, 0, 0, null);
		}
	}
	
	public int enrollBookDB(Book book) {
		String enrollToBookDB = "INSERT INTO book VALUES (?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(enrollToBookDB);
			pstmt.setLong(1, book.getBid());
			pstmt.setString(2, book.getBname());
			pstmt.setString(3, book.getAuthor());
			pstmt.setString(4, book.getPublisher());
			pstmt.setString(5, book.getLink());
			pstmt.setString(6, book.getImageLink());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	
	public int enrollRecordDB(Record record) {
		String enrollToRecordDB = "INSERT INTO recordedbooks VALUES (?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(enrollToRecordDB);
			pstmt.setString(1, record.getUid());
			pstmt.setLong(2, record.getBid());
			pstmt.setBoolean(3, record.getWorH());
			pstmt.setDate(4, record.getRegDate());
			pstmt.setInt(5, record.getScore());
			pstmt.setString(6, record.getMemo());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == search_B || e.getSource() == search_TF) {
			String imageUrl_String, converted_url;
			int endPage;
			Image image;
			try {
				if(categori_CB.getSelectedItem().toString() == "//구분//")
					return;
				BookApiJSON bApi = new BookApiJSON(1, categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				total_B.setText("총 " + bApi.totalContent + "권");
				if(bApi.totalContent % 5 == 0)
					endPage = bApi.totalContent / 5;
				else
					endPage = (bApi.totalContent / 5) + 1;
				lowButtonSet(1, endPage);
				for(int i = 0; i < bApi.contentsArray.size(); i++) {
					selectedOrder = i;
					information_Author[i].setText("");
					information_Title[i].setText("");
					information_Publisher[i].setText("");
					information_Isbn[i].setText("");
					JSONObject eachBook = (JSONObject)bApi.contentsArray.get(i);
					imageUrl_String = (String)eachBook.get("TITLE_URL");
					if(imageUrl_String.equals("")) {
						inner_Image[i].remove(image_Label[i]);
						inner_Image[i].repaint();
						inner_Image[i].revalidate();
					}
					else {
						converted_url = imageUrl_String.substring(52, imageUrl_String.length());
						resizeImage(converted_url);
						imageUrl_String = "";
					}
					information_Author[i].setText((String)eachBook.get("AUTHOR"));
					information_Isbn[i].setText("ISBN : " + (String)eachBook.get("EA_ISBN"));
					information_Publisher[i].setText("출판사 : " + (String)eachBook.get("PUBLISHER"));
					information_Title[i].setText((String)eachBook.get("TITLE"));
				}
				selectedOrder = -1;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == firstPage_B) {
			String imageUrl_String, converted_url;
			int endPage;
			try {
				if(categori_CB.getSelectedItem().toString() == "//구분//")
					return;
				BookApiJSON bApi = new BookApiJSON(1, categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				total_B.setText("총 " + bApi.totalContent + "권");
				if(bApi.totalContent % 5 == 0)
					endPage = bApi.totalContent / 5;
				else
					endPage = (bApi.totalContent / 5) + 1;
				lowButtonSet(1, endPage);
				
				for(int i = 0; i < 5; i++) {
					information_Author[i].setText("");
					information_Title[i].setText("");
					information_Publisher[i].setText("");
					information_Isbn[i].setText("");
				}
				
				for(int i = 0; i < bApi.contentsArray.size(); i++) {
					selectedOrder = i;
					JSONObject eachBook = (JSONObject)bApi.contentsArray.get(i);
					imageUrl_String = (String)eachBook.get("TITLE_URL");
					if(imageUrl_String.equals("")) {
						inner_Image[i].remove(image_Label[i]);
						inner_Image[i].repaint();
						inner_Image[i].revalidate();
					}
					else {
						converted_url = imageUrl_String.substring(52, imageUrl_String.length());
						resizeImage(converted_url);
						imageUrl_String = "";
					}
					information_Author[i].setText((String)eachBook.get("AUTHOR"));
					information_Isbn[i].setText("ISBN : " + (String)eachBook.get("EA_ISBN"));
					information_Publisher[i].setText("출판사 : " + (String)eachBook.get("PUBLISHER"));
					information_Title[i].setText((String)eachBook.get("TITLE"));
				}
				selectedOrder = -1;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == pageDown_B) {
			String imageUrl_String, converted_url;
			int endPage;
			try {
				if(categori_CB.getSelectedItem().toString() == "//구분//")
					return;
				BookApiJSON bApi = new BookApiJSON(Integer.parseInt(current_B.getText()) - 3, categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				total_B.setText("총 " + bApi.totalContent + "권");
				if(bApi.totalContent % 5 == 0)
					endPage = bApi.totalContent / 5;
				else
					endPage = (bApi.totalContent / 5) + 1;
				lowButtonSet(Integer.parseInt(current_B.getText()) - 3, endPage);
				
				for(int i = 0; i < 5; i++) {
					information_Author[i].setText("");
					information_Title[i].setText("");
					information_Publisher[i].setText("");
					information_Isbn[i].setText("");
				}
				
				for(int i = 0; i < bApi.contentsArray.size(); i++) {
					selectedOrder = i;
					JSONObject eachBook = (JSONObject)bApi.contentsArray.get(i);
					imageUrl_String = (String)eachBook.get("TITLE_URL");
					if(imageUrl_String.equals("")) {
						inner_Image[i].remove(image_Label[i]);
						inner_Image[i].repaint();
						inner_Image[i].revalidate();
					}
					else {
						converted_url = imageUrl_String.substring(52, imageUrl_String.length());
						resizeImage(converted_url);
						imageUrl_String = "";
					}
					information_Author[i].setText((String)eachBook.get("AUTHOR"));
					information_Isbn[i].setText("ISBN : " + (String)eachBook.get("EA_ISBN"));
					information_Publisher[i].setText("출판사 : " + (String)eachBook.get("PUBLISHER"));
					information_Title[i].setText((String)eachBook.get("TITLE"));
				}
				selectedOrder = -1;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == previous_B) {
			String imageUrl_String, converted_url;
			int endPage;
			try {
				if(categori_CB.getSelectedItem().toString() == "//구분//")
					return;
				BookApiJSON bApi = new BookApiJSON(Integer.parseInt(current_B.getText()) - 1, categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				total_B.setText("총 " + bApi.totalContent + "권");
				if(bApi.totalContent % 5 == 0)
					endPage = bApi.totalContent / 5;
				else
					endPage = (bApi.totalContent / 5) + 1;
				lowButtonSet(Integer.parseInt(current_B.getText()) - 1, endPage);
				
				for(int i = 0; i < 5; i++) {
					information_Author[i].setText("");
					information_Title[i].setText("");
					information_Publisher[i].setText("");
					information_Isbn[i].setText("");
				}
				
				for(int i = 0; i < bApi.contentsArray.size(); i++) {
					selectedOrder = i;
					JSONObject eachBook = (JSONObject)bApi.contentsArray.get(i);
					imageUrl_String = (String)eachBook.get("TITLE_URL");
					if(imageUrl_String.equals("")) {
						inner_Image[i].remove(image_Label[i]);
						inner_Image[i].repaint();
						inner_Image[i].revalidate();
					}
					else {
						converted_url = imageUrl_String.substring(52, imageUrl_String.length());
						resizeImage(converted_url);
						imageUrl_String = "";
					}
					information_Author[i].setText((String)eachBook.get("AUTHOR"));
					information_Isbn[i].setText("ISBN : " + (String)eachBook.get("EA_ISBN"));
					information_Publisher[i].setText("출판사 : " + (String)eachBook.get("PUBLISHER"));
					information_Title[i].setText((String)eachBook.get("TITLE"));
				}
				selectedOrder = -1;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == next_B) {
			String imageUrl_String, converted_url;
			int endPage;
			try {
				if(categori_CB.getSelectedItem().toString() == "//구분//")
					return;
				BookApiJSON bApi = new BookApiJSON(Integer.parseInt(current_B.getText()) + 1, categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				total_B.setText("총 " + bApi.totalContent + "권");
				if(bApi.totalContent % 5 == 0)
					endPage = bApi.totalContent / 5;
				else
					endPage = (bApi.totalContent / 5) + 1;
				lowButtonSet(Integer.parseInt(current_B.getText()) + 1, endPage);
				
				for(int i = 0; i < 5; i++) {
					information_Author[i].setText("");
					information_Title[i].setText("");
					information_Publisher[i].setText("");
					information_Isbn[i].setText("");
				}
				
				for(int i = 0; i < bApi.contentsArray.size(); i++) {
					selectedOrder = i;
					JSONObject eachBook = (JSONObject)bApi.contentsArray.get(i);
					imageUrl_String = (String)eachBook.get("TITLE_URL");
					if(imageUrl_String.equals("")) {
						inner_Image[i].remove(image_Label[i]);
						inner_Image[i].repaint();
						inner_Image[i].revalidate();
					}
					else {
						converted_url = imageUrl_String.substring(52, imageUrl_String.length());
						resizeImage(converted_url);
						imageUrl_String = "";
					}
					information_Author[i].setText((String)eachBook.get("AUTHOR"));
					information_Isbn[i].setText("ISBN : " + (String)eachBook.get("EA_ISBN"));
					information_Publisher[i].setText("출판사 : " + (String)eachBook.get("PUBLISHER"));
					information_Title[i].setText((String)eachBook.get("TITLE"));
				}
				selectedOrder = -1;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == pageUp_B) {
			String imageUrl_String, converted_url;
			int endPage;
			try {
				if(categori_CB.getSelectedItem().toString() == "//구분//")
					return;
				BookApiJSON bApi = new BookApiJSON(Integer.parseInt(current_B.getText()) + 3, categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				total_B.setText("총 " + bApi.totalContent + "권");
				if(bApi.totalContent % 5 == 0)
					endPage = bApi.totalContent / 5;
				else
					endPage = (bApi.totalContent / 5) + 1;
				lowButtonSet(Integer.parseInt(current_B.getText()) + 3, endPage);
				
				for(int i = 0; i < 5; i++) {
					information_Author[i].setText("");
					information_Title[i].setText("");
					information_Publisher[i].setText("");
					information_Isbn[i].setText("");
				}
				
				for(int i = 0; i < bApi.contentsArray.size(); i++) {
					selectedOrder = i;
					JSONObject eachBook = (JSONObject)bApi.contentsArray.get(i);
					imageUrl_String = (String)eachBook.get("TITLE_URL");
					if(imageUrl_String.equals("")) {
						inner_Image[i].remove(image_Label[i]);
						inner_Image[i].repaint();
						inner_Image[i].revalidate();
					}
					else {
						converted_url = imageUrl_String.substring(52, imageUrl_String.length());
						resizeImage(converted_url);
						imageUrl_String = "";
					}
					information_Author[i].setText((String)eachBook.get("AUTHOR"));
					information_Isbn[i].setText("ISBN : " + (String)eachBook.get("EA_ISBN"));
					information_Publisher[i].setText("출판사 : " + (String)eachBook.get("PUBLISHER"));
					information_Title[i].setText((String)eachBook.get("TITLE"));
				}
				selectedOrder = -1;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == lastPage_B) {
			String imageUrl_String, converted_url;
			int endPage;
			try {
				if(categori_CB.getSelectedItem().toString() == "//구분//")
					return;
				BookApiJSON bApi1 = new BookApiJSON(1, categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				if(bApi1.totalContent % 5 == 0)
					endPage = bApi1.totalContent / 5;
				else
					endPage = (bApi1.totalContent / 5) + 1;
				BookApiJSON bApi = new BookApiJSON(endPage, categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				total_B.setText("총 " + bApi.totalContent + "권");
				lowButtonSet(endPage, endPage);
				
				for(int i = 0; i < 5; i++) {
					information_Author[i].setText("");
					information_Title[i].setText("");
					information_Publisher[i].setText("");
					information_Isbn[i].setText("");
				}
				
				for(int i = 0; i < bApi.contentsArray.size(); i++) {
					selectedOrder = i;
					JSONObject eachBook = (JSONObject)bApi.contentsArray.get(i);
					imageUrl_String = (String)eachBook.get("TITLE_URL");
					if(imageUrl_String.equals("")) {
						inner_Image[i].remove(image_Label[i]);
						inner_Image[i].repaint();
						inner_Image[i].revalidate();
					}
					else {
						converted_url = imageUrl_String.substring(52, imageUrl_String.length());
						resizeImage(converted_url);
						imageUrl_String = "";
					}
					information_Author[i].setText((String)eachBook.get("AUTHOR"));
					information_Isbn[i].setText("ISBN : " + (String)eachBook.get("EA_ISBN"));
					information_Publisher[i].setText("출판사 : " + (String)eachBook.get("PUBLISHER"));
					information_Title[i].setText((String)eachBook.get("TITLE"));
				}
				selectedOrder = -1;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == confirm_B) {
			try {
				BookApiJSON bApi = new BookApiJSON(Integer.parseInt(current_B.getText()), categori_CB.getSelectedItem().toString(), URLEncoder.encode(search_TF.getText(), "UTF-8"));
				String imageLink;
				for(int i=0; i<bApi.contentsArray.size(); i++) {
					if(radioB[i].isSelected()) {
						JSONObject eachBook = (JSONObject)bApi.contentsArray.get(i);
						
						Class.forName("com.mysql.cj.jdbc.Driver");
						String dbURL = "jdbc:mysql://localhost:3307/finaltest?serverTimezone=UTC"; // localhost:3306 포트는 컴퓨터설치된 mysql주소
						String dbID = "root";
						String dbPassword = "1234";
						
						conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
						String rbsCheck = "SELECT * FROM recordedbooks WHERE Uid = ? AND Bid = ?";
						pstmt = conn.prepareStatement(rbsCheck);
						pstmt.setString(1, MainFrameForm.connectedID);
						pstmt.setLong(2, Long.parseLong((String)eachBook.get("EA_ISBN")));
						rs = pstmt.executeQuery();
						
						if(!rs.next()) {		// 1. 레코드에 책이 등록 안되어 있다면
							String bkCheck = "SELECT * FROM book WHERE Bid = ?";
							pstmt = conn.prepareStatement(bkCheck);
							pstmt.setLong(1, Long.parseLong((String)eachBook.get("EA_ISBN")));
							rs = pstmt.executeQuery();
							
							if(!rs.next()) {	// 2. 그리고 북에 책이 등록 안되어 있다면 --> 둘 다 새로 등록
								if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.WILL) {	 
									String link = "https://book.naver.com/search/search.nhn?sm=sta_hty.book&sug=&where=nexearch&query=" + (String)eachBook.get("EA_ISBN");
									String imageUrl_String = (String)eachBook.get("TITLE_URL");
									if(!imageUrl_String.equals(""))
										imageLink = imageUrl_String.substring(52, imageUrl_String.length());
									else
										imageLink = "";
									Book newBook = new Book(Long.parseLong((String)eachBook.get("EA_ISBN")), (String)eachBook.get("TITLE"), 
											(String)eachBook.get("AUTHOR"), (String)eachBook.get("PUBLISHER"), link, imageLink);
									int result = enrollBookDB(newBook);
									
									if(result == 1) {	// 2-1. 북DB에 등록이 되고
										Date sqlRegDate = new Date(new java.util.Date().getTime());
										boolean wORh = false;
										Record newRecord = new Record(MainFrameForm.connectedID, Long.parseLong((String)eachBook.get("EA_ISBN")), 
												wORh, sqlRegDate, 0, "");
										result = enrollRecordDB(newRecord);
										
										if(result == 1) {	// 2-2. 레코드DB에 등록을 했다면 --> 테이블에 추가해준다.
											String joinRECnBK = "SELECT * FROM recordedbooks rb INNER JOIN book b ON rb.Bid = b.Bid WHERE b.Bid = ?";
											pstmt = conn.prepareStatement(joinRECnBK);
											pstmt.setLong(1, Long.parseLong((String)eachBook.get("EA_ISBN")));
											rs = pstmt.executeQuery();
											
											if(rs.next()) {
												DefaultTableModel m = (DefaultTableModel)MainFrameForm.wRBooksTable.getModel();
												m.insertRow(MainFrameForm.wRBooksTable.getRowCount()
														, new Object[]{String.valueOf(rs.getLong("Bid")),rs.getString("Bname"),rs.getString("author"),rs.getString("publisher"),rs.getString("link")});
												MainFrameForm.wRBooksTable.updateUI();
												
												JOptionPane.showMessageDialog(null, "등록 성공.");
												rs.close();
												pstmt.close();
												setVisible(false);
											}
										}
									}
								}
								else if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.HAD) {	
									String link = "https://book.naver.com/search/search.nhn?sm=sta_hty.book&sug=&where=nexearch&query=" + (String)eachBook.get("EA_ISBN");
									String imageUrl_String = (String)eachBook.get("TITLE_URL");
									if(!imageUrl_String.equals(""))
										imageLink = imageUrl_String.substring(52, imageUrl_String.length());
									else
										imageLink = "";
									Book newBook = new Book(Long.parseLong((String)eachBook.get("EA_ISBN")), (String)eachBook.get("TITLE"), 
											(String)eachBook.get("AUTHOR"), (String)eachBook.get("PUBLISHER"), link, imageLink);
									int result = enrollBookDB(newBook);
									
									if(result == 1) {	// 2-1. 북DB에 등록이 되고
										Date sqlRegDate = new Date(new java.util.Date().getTime());
										boolean wORh = true;
										Record newRecord = new Record(MainFrameForm.connectedID, Long.parseLong((String)eachBook.get("EA_ISBN")), 
												wORh, sqlRegDate, 0, "");
										result = enrollRecordDB(newRecord);
										
										if(result == 1) {	// 2-2. 레코드DB에 등록을 했다면 --> 테이블에 추가해준다.
											String joinRECnBK = "SELECT * FROM recordedbooks rb INNER JOIN book b ON rb.Bid = b.Bid WHERE b.Bid = ?";
											pstmt = conn.prepareStatement(joinRECnBK);
											pstmt.setLong(1, Long.parseLong((String)eachBook.get("EA_ISBN")));
											rs = pstmt.executeQuery();
											
											if(rs.next()) {
												DefaultTableModel m = (DefaultTableModel)MainFrameForm.hRBooksTable.getModel();
												m.insertRow(MainFrameForm.hRBooksTable.getRowCount() 
														, new Object[]{String.valueOf(rs.getLong("Bid")),rs.getString("Bname"),rs.getString("author"),rs.getString("publisher"),rs.getString("memo"), rs.getDouble("score")});
												MainFrameForm.hRBooksTable.updateUI();
												
												JOptionPane.showMessageDialog(null, "등록 성공.");
												rs.close();
												pstmt.close();
												setVisible(false);
											}
										}
									}
								}
							} 
							else {	// 3. 레코드 X, 북 O --> "SELECT * FROM book WHERE Bid = ?" 에 대한 결과값 있을 때 -> 북에서 데이터를 받아오고, 레코드에만 등록 
								Date sqlRegDate = new Date(new java.util.Date().getTime());
								boolean wORh = false;
								if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.WILL)
									wORh = false;
								else if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.HAD)
									wORh = true;
								Record newRecord = new Record(MainFrameForm.connectedID, Long.parseLong((String)eachBook.get("EA_ISBN")), 
										wORh, sqlRegDate, 0, "");
								int result = enrollRecordDB(newRecord);
						
								if(result == 1) {
									String joinRECnBK = "SELECT * FROM recordedbooks rb INNER JOIN book b ON rb.Bid = b.Bid WHERE b.Bid = ?";
									pstmt = conn.prepareStatement(joinRECnBK);
									pstmt.setLong(1, Long.parseLong((String)eachBook.get("EA_ISBN")));
									rs = pstmt.executeQuery();
									
									if(rs.next()) {
										if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.WILL) {
											DefaultTableModel m = (DefaultTableModel)MainFrameForm.wRBooksTable.getModel();
											m.insertRow(MainFrameForm.wRBooksTable.getRowCount()
													, new Object[]{String.valueOf(rs.getLong("Bid")),rs.getString("Bname"),rs.getString("author"),rs.getString("publisher"),rs.getString("link")});
											MainFrameForm.wRBooksTable.updateUI();
										}
										else if(MainFrameForm.whichWorksWindow == MainFrameForm.worksWindow.HAD) {
											DefaultTableModel m = (DefaultTableModel)MainFrameForm.hRBooksTable.getModel();
											m.insertRow(MainFrameForm.hRBooksTable.getRowCount()
													, new Object[]{String.valueOf(rs.getLong("Bid")),rs.getString("Bname"),rs.getString("author"),rs.getString("publisher"),rs.getString("memo"), rs.getDouble("score")});
											MainFrameForm.hRBooksTable.updateUI();
										}
									
										JOptionPane.showMessageDialog(null, "등록 성공.");
										rs.close();
										pstmt.close();
										setVisible(false);
									}
								}		// if(result == 1) 의 종료문
							} 			// <3. 레코드 X, 북 O --> "SELECT * FROM book WHERE Bid = ?" 에 대한 결과값 있을 때 -> 북에서 데이터를 받아오고, 레코드에만 등록> 의 종료문
						}				// <// 1. 레코드에 책이 등록 안되어 있다면> 의 종료문
						else {	// 레코드에 책이 등록되어 있었다면 --> 북에는 이미 등록되어 있겠지
							JOptionPane.showMessageDialog(null, "책이 이미 등록되어 있습니다.");
						}
					}
				}
				if(radio_BTG.getSelection() == null) {	// 라디오버튼에 아무것도 체크 안되어 있다면
					JOptionPane.showMessageDialog(null, "항목을 선택해주세요.");
				}
			} catch (Exception e1) {
				e1.printStackTrace(); // 오류가 무엇인지 출력
			}
		}
		else if(e.getSource() == cancel_B) {
			setVisible(false);
		}
	}
}
