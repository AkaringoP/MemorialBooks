package finaltest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

public class MainFrameForm extends JFrame implements ActionListener, MouseListener {

	private JPanel contentPane;
	JPanel menuBox, pageCard, hRBookPanel, wRBookPanel, cfgPanel;
	JButton willReadMenu_B, hadReadMenu_B, emptyBT1_B, emptyBT2_B, setConfig_B;
	CardLayout cardL;
	String wRHeader[] = {"ISBN", "제목", "작가", "출판사", "링크"};
	String wRContents[][] = {
			{"", "", "", "", ""}
	};
	DefaultTableModel wR_TM = new DefaultTableModel(wRContents, wRHeader) {
		public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
	};
	
	String hRHeader[] = {"ISBN", "제목", "작가", "출판사", "메모", "평점"};
	String hRContents[][] = {
			{"", "", "", "", "", ""}
	};
	DefaultTableModel hR_TM = new DefaultTableModel(hRContents, hRHeader) {
		public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
	};
	
	Color default_C = new Color(40, 104, 176);
	Color hover_C = new Color(20, 83, 145);
	Color disabled_C = new Color(166, 168, 171);
	
	Color default_BC = new Color(238, 238, 238);
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private int uprs;
	
	public enum worksWindow {
		WILL, HAD, CONFIG, NONE
	}
	public static worksWindow whichWorksWindow = worksWindow.NONE;
	public static String connectedID = "";
	String time;

	private JPanel indexPanel;
	
	public static JTable wRBooksTable;
	private JPanel wR_UpPanel;
	private JPanel wR_DownPanel;
	private JPanel panel_1;
	private JButton enRollBook_wRB;
	private JButton dropBook_wRB;
	private JLabel Hello_wRL;
	private JButton logOut_wRB;
	
	private JPanel hR_UpPanel;
	private JPanel hR_DownPanel;
	private JPanel panel_3;
	private JButton enRollBook_hRB;
	private JButton dropBook_hRB;
	private JLabel Hello_hRL;
	private JButton logOut_hRB;
	public static JTable hRBooksTable;
	
	/**
	 * Create the frame.
	 */
	public MainFrameForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		menuBox = new JPanel();
		menuBox.setBorder(new EmptyBorder(0, 0, 0, 0));
		menuBox.setPreferredSize(new Dimension(100, 600));
		menuBox.setMinimumSize(new Dimension(100, 600));
		contentPane.add(menuBox, BorderLayout.WEST);
		
		willReadMenu_B = new JButton("읽을 책");
		willReadMenu_B.setPreferredSize(new Dimension(100, 100));
		willReadMenu_B.setFont(new Font(getName(), Font.BOLD, 15));
		willReadMenu_B.setForeground(Color.WHITE);
		willReadMenu_B.setBackground(default_C);
		willReadMenu_B.addActionListener(this);
		willReadMenu_B.addMouseListener(this);
		menuBox.add(willReadMenu_B);
		
		hadReadMenu_B = new JButton("읽은 책");
		hadReadMenu_B.setPreferredSize(new Dimension(100, 100));
		hadReadMenu_B.setFont(new Font(getName(), Font.BOLD, 15));
		hadReadMenu_B.setForeground(Color.WHITE);
		hadReadMenu_B.setBackground(default_C);
		hadReadMenu_B.addActionListener(this);
		hadReadMenu_B.addMouseListener(this);
		menuBox.add(hadReadMenu_B);
		
		emptyBT1_B = new JButton();
		emptyBT1_B.setPreferredSize(new Dimension(100, 100));
		emptyBT1_B.setEnabled(false);
		menuBox.add(emptyBT1_B);
		
		emptyBT2_B = new JButton();
		emptyBT2_B.setPreferredSize(new Dimension(100, 100));
		emptyBT2_B.setEnabled(false);
		menuBox.add(emptyBT2_B);
		
		setConfig_B = new JButton("환경설정");
		setConfig_B.setPreferredSize(new Dimension(100, 100));
		setConfig_B.setFont(new Font(getName(), Font.BOLD, 15));
		setConfig_B.setForeground(Color.WHITE);
		setConfig_B.setBackground(default_C);
		setConfig_B.addActionListener(this);
		setConfig_B.addMouseListener(this);
		menuBox.add(setConfig_B);
		
		cardL = new CardLayout(0, 0);
		
		pageCard = new JPanel();
		pageCard.setBackground(default_BC);
		contentPane.add(pageCard, BorderLayout.CENTER);
		pageCard.setLayout(cardL);
		
		indexPanel = new JPanel();
		pageCard.add(indexPanel, "indexPanel");
		
		wRBookPanel = new JPanel();
		pageCard.add(wRBookPanel, "wRBookPanel");
		wRBookPanel.setLayout(new BorderLayout(0, 0));
		
		wRBooksTable = new JTable(wR_TM);
		JScrollPane wR_SCR = new JScrollPane(wRBooksTable);
		wRBooksTable.addMouseListener(this);
		wRBookPanel.add(wR_SCR, BorderLayout.CENTER);
		
		wR_UpPanel = new JPanel();
		wR_UpPanel.setPreferredSize(new Dimension(774, 30));
		wRBookPanel.add(wR_UpPanel, BorderLayout.NORTH);
		wR_UpPanel.setLayout(new BorderLayout(0, 0));
		
		Hello_wRL = new JLabel("");
		wR_UpPanel.add(Hello_wRL, BorderLayout.WEST);
		
		logOut_wRB = new JButton("로그아웃");
		wR_UpPanel.add(logOut_wRB, BorderLayout.EAST);
		logOut_wRB.addActionListener(this);
		
		wR_DownPanel = new JPanel();
		wR_DownPanel.setPreferredSize(new Dimension(774, 30));
		wRBookPanel.add(wR_DownPanel, BorderLayout.SOUTH);
		wR_DownPanel.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		wR_DownPanel.add(panel_1, BorderLayout.WEST);
		
		enRollBook_wRB = new JButton("등록");
		panel_1.add(enRollBook_wRB);
		enRollBook_wRB.addActionListener(this);
		
		dropBook_wRB = new JButton("삭제");
		dropBook_wRB.addActionListener(this);
		panel_1.add(dropBook_wRB);
		
		hRBookPanel = new JPanel();
		pageCard.add(hRBookPanel, "hRBookPanel");
		hRBookPanel.setLayout(new BorderLayout(0, 0));
		
		hR_UpPanel = new JPanel();
		hR_UpPanel.setPreferredSize(new Dimension(774, 30));
		hRBookPanel.add(hR_UpPanel, BorderLayout.NORTH);
		hR_UpPanel.setLayout(new BorderLayout(0, 0));
		
		Hello_hRL = new JLabel("");
		hR_UpPanel.add(Hello_hRL, BorderLayout.WEST);
		
		logOut_hRB = new JButton("로그아웃");
		logOut_hRB.addActionListener(this);
		hR_UpPanel.add(logOut_hRB, BorderLayout.EAST);
		
		hR_DownPanel = new JPanel();
		hR_DownPanel.setPreferredSize(new Dimension(774, 30));
		hRBookPanel.add(hR_DownPanel, BorderLayout.SOUTH);
		hR_DownPanel.setLayout(new BorderLayout(0, 0));
		
		panel_3 = new JPanel();
		hR_DownPanel.add(panel_3, BorderLayout.WEST);
		
		enRollBook_hRB = new JButton("등록");
		panel_3.add(enRollBook_hRB);
		enRollBook_hRB.addActionListener(this);
		
		dropBook_hRB = new JButton("삭제");
		dropBook_hRB.addActionListener(this);
		panel_3.add(dropBook_hRB);
		
		hRBooksTable = new JTable(hR_TM);
		JScrollPane hR_SCR = new JScrollPane(hRBooksTable);
		hRBooksTable.addMouseListener(this);
		hRBookPanel.add(hR_SCR, BorderLayout.CENTER);
		
		cfgPanel = new JPanel();
		pageCard.add(cfgPanel, "cfgPanel");
	}
	
	public void display() {
		setVisible(true);
		setTitle("도서기록장");
		setLocationRelativeTo(null);

		Thread t1 = new Thread(new TimerCount());
		t1.start();
		
		SignInform sif = new SignInform(this, "로그인");
		sif.setSize(300, 200);
		sif.setLocationRelativeTo(null);
		sif.setVisible(true);
		sif.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	class TimerCount implements Runnable {
		public void run() {
			for(;;) {
				try {
					GregorianCalendar gc = new GregorianCalendar();
					time = gc.get(gc.HOUR) + "시" + 
							gc.get(gc.MINUTE) + "분" + 
							gc.get(gc.SECOND)+ "초";
					Thread.sleep(1000);
					setTitle("//** 도서기록장 **// 현재시간 : " + time);
				} catch(InterruptedException ie) {
					
				}
			}
		}
	}
	
	public void dbSet() {
		if(connectedID != "") {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String dbURL = "jdbc:mysql://localhost:3307/finaltest?serverTimezone=UTC"; // localhost:3306 포트는 컴퓨터설치된 mysql주소
				String dbID = "root";
				String dbPassword = "1234";
				conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
				System.out.println("MainFrame DB준비됨");
				String findNameQuery = "SELECT Uname from USER WHERE Uid = ?";
				pstmt = conn.prepareStatement(findNameQuery);
				pstmt.setString(1, connectedID);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					if(whichWorksWindow == worksWindow.WILL)
						Hello_wRL.setText("  " + rs.getString("Uname") + "님 안녕하세요!");
					else if(whichWorksWindow == worksWindow.HAD)
						Hello_hRL.setText("  " + rs.getString("Uname") + "님 안녕하세요!");
				}
					
			} catch (Exception e) {
				e.printStackTrace(); // 오류가 무엇인지 출력
			}
		}
	}
	
	public void tableSet() {
		if(whichWorksWindow == worksWindow.WILL) {	
			String joinRECnBK = "SELECT * FROM recordedbooks rb INNER JOIN book b ON rb.Bid = b.Bid WHERE rb.Uid = ? AND rb.wORh = ?";
			String countNum = "SELECT count(*) as count FROM recordedbooks rb INNER JOIN book b ON rb.Bid = b.Bid WHERE rb.Uid = ?";
			DefaultTableModel m = (DefaultTableModel)MainFrameForm.wRBooksTable.getModel();
			m.setNumRows(0);
			try {
				pstmt = conn.prepareStatement(countNum);
				pstmt.setString(1, connectedID);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					int culCount = rs.getInt("count");
					pstmt = conn.prepareStatement(joinRECnBK);
					pstmt.setString(1, connectedID);
					pstmt.setInt(2, 0);
					rs = pstmt.executeQuery();
					
					for(int i=0; i<culCount; i++) {
						if(rs.next()) {
							m.insertRow(MainFrameForm.wRBooksTable.getRowCount()
									, new Object[]{String.valueOf(rs.getLong("Bid")),rs.getString("Bname"),rs.getString("author"),rs.getString("publisher"),rs.getString("link")});
						}	
					}
					wRBooksTable.updateUI();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		else if(whichWorksWindow == worksWindow.HAD) {		// HAD
			String joinRECnBK = "SELECT * FROM recordedbooks rb INNER JOIN book b ON rb.Bid = b.Bid WHERE rb.Uid = ? AND rb.wORh = ?";
			String countNum = "SELECT count(*) as count FROM recordedbooks rb INNER JOIN book b ON rb.Bid = b.Bid WHERE rb.Uid = ?";
			DefaultTableModel m = (DefaultTableModel)MainFrameForm.hRBooksTable.getModel();
			m.setNumRows(0);
			try {
				pstmt = conn.prepareStatement(countNum);
				pstmt.setString(1, connectedID);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					int culCount = rs.getInt("count");
					pstmt = conn.prepareStatement(joinRECnBK);
					pstmt.setString(1, connectedID);
					pstmt.setInt(2, 1);
					rs = pstmt.executeQuery();
					
					for(int i=0; i<culCount; i++) {
						if(rs.next()) {
							m.insertRow(MainFrameForm.hRBooksTable.getRowCount()
									, new Object[]{String.valueOf(rs.getLong("Bid")),rs.getString("Bname"),rs.getString("author"),rs.getString("publisher"),rs.getString("memo"), rs.getDouble("score")});
						}	
					}
					hRBooksTable.updateUI();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2 && (e.getSource() == wRBooksTable || e.getSource() == hRBooksTable)) {
			JTable t = (JTable)e.getSource();
			TableModel m = t.getModel();
			int row = t.getSelectedRow();
			BookInfoForm.selectedBid = Long.parseLong((String)t.getValueAt(row, 0));
			
			BookInfoForm bif = new BookInfoForm(this, "책 상세정보");
			bif.setSize(650, 400);
			bif.setLocationRelativeTo(null);
			bif.setVisible(true);
			bif.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			bif.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					BookInfoForm.selectedBid = -1;
					if(whichWorksWindow == worksWindow.WILL) {
						cardL.show(pageCard, "hRBookPanel");
						cardL.show(pageCard, "wRBookPanel");
					}
					else if(whichWorksWindow == worksWindow.HAD) {
						cardL.show(pageCard, "wRBookPanel");
						cardL.show(pageCard, "hRBookPanel");
					}
				}
			});
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
		if(e.getSource() == willReadMenu_B) {
			willReadMenu_B.setBackground(hover_C);
			if(willReadMenu_B.isEnabled() == false)
				willReadMenu_B.setBackground(disabled_C);
		}	
		else if(e.getSource() == hadReadMenu_B) {
			hadReadMenu_B.setBackground(hover_C);
			if(hadReadMenu_B.isEnabled() == false)
				hadReadMenu_B.setBackground(disabled_C);
		}
		else if(e.getSource() == setConfig_B) {
			setConfig_B.setBackground(hover_C);
			if(setConfig_B.isEnabled() == false)
				setConfig_B.setBackground(disabled_C);
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == willReadMenu_B) {
			willReadMenu_B.setBackground(default_C);
			if(willReadMenu_B.isEnabled() == false)
				willReadMenu_B.setBackground(disabled_C);
		}
			
		if(e.getSource() == hadReadMenu_B) {
			hadReadMenu_B.setBackground(default_C);
			if(hadReadMenu_B.isEnabled() == false)
				hadReadMenu_B.setBackground(disabled_C);
		}
		if(e.getSource() == setConfig_B) {
			setConfig_B.setBackground(default_C);
			if(setConfig_B.isEnabled() == false)
				setConfig_B.setBackground(disabled_C);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == willReadMenu_B) {
			willReadMenu_B.setBackground(disabled_C);
			willReadMenu_B.setEnabled(false);
			hadReadMenu_B.setBackground(default_C);
			hadReadMenu_B.setEnabled(true);
			setConfig_B.setBackground(default_C);
			setConfig_B.setEnabled(true);
			hRBookPanel.setBackground(default_BC);
			whichWorksWindow = worksWindow.WILL;
			
			dbSet();
			tableSet();
			cardL.show(pageCard, "wRBookPanel");
		}
		else if(e.getSource() == hadReadMenu_B) {
			willReadMenu_B.setBackground(default_C);
			willReadMenu_B.setEnabled(true);
			hadReadMenu_B.setBackground(disabled_C);
			hadReadMenu_B.setEnabled(false);
			setConfig_B.setBackground(default_C);
			setConfig_B.setEnabled(true);
			wRBookPanel.setBackground(default_BC);
			whichWorksWindow = worksWindow.HAD;
			
			dbSet();
			tableSet();
			cardL.show(pageCard, "hRBookPanel");
		}
		else if(e.getSource() == setConfig_B) {
			willReadMenu_B.setBackground(default_C);
			willReadMenu_B.setEnabled(true);
			hadReadMenu_B.setBackground(default_C);
			hadReadMenu_B.setEnabled(true);
			setConfig_B.setBackground(disabled_C);
			setConfig_B.setEnabled(false);
			cfgPanel.setBackground(default_BC);
			whichWorksWindow = worksWindow.CONFIG;
			
			dbSet();
			cardL.show(pageCard, "cfgPanel");
		}
		else if(e.getSource() == enRollBook_wRB || e.getSource() == enRollBook_hRB) {
			SearchBookForm sbf = new SearchBookForm();
			sbf.display();
		}
		else if(e.getSource() == dropBook_wRB || e.getSource() == dropBook_hRB) {
			// 레코드에서만 삭제됨 --> 북에는 남아있음
			// 삭제 순서 : 테이블에서 먼저 삭제하고 DB 삭제
			// 마우스로 선택한 항목만 삭제
			// 추후 선택상자 3개짜리해서 북 레코드에서도 지울 수 있게
			if(whichWorksWindow == worksWindow.WILL) {
				int ans = JOptionPane.showConfirmDialog(this, "이 레코드를 지우시겠습니까?", "확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(ans == 0) {
					JTable t = wRBooksTable;
					TableModel m = t.getModel();
					int row = t.getSelectedRow();
					BookInfoForm.selectedBid = Long.parseLong((String)t.getValueAt(row, 0));
					((DefaultTableModel)t.getModel()).removeRow(row);
					
					try {
						String dropRecord = "DELETE FROM recordedbooks WHERE Bid = ? AND Uid = ?";
						pstmt = conn.prepareStatement(dropRecord);
						pstmt.setLong(1, BookInfoForm.selectedBid);
						pstmt.setString(2, connectedID);
						uprs = pstmt.executeUpdate();
						
						if(uprs == 1) {
							wRBooksTable.updateUI();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			else if(whichWorksWindow == worksWindow.HAD) {
				int ans = JOptionPane.showConfirmDialog(this, "이 레코드를 지우시겠습니까?", "확인", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if(ans == 0) {
					JTable t = hRBooksTable;
					TableModel m = t.getModel();
					int row = t.getSelectedRow();
					BookInfoForm.selectedBid = Long.parseLong((String)t.getValueAt(row, 0));
					((DefaultTableModel)t.getModel()).removeRow(row);
					
					try {
						String dropRecord = "DELETE FROM recordedbooks WHERE Bid = ? AND Uid = ?";
						pstmt = conn.prepareStatement(dropRecord);
						pstmt.setLong(1, BookInfoForm.selectedBid);
						pstmt.setString(2, connectedID);
						uprs = pstmt.executeUpdate();
						
						if(uprs == 1) {
							hRBooksTable.updateUI();
						}
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		else if(e.getSource() == logOut_wRB || e.getSource() == logOut_hRB) {
			try {
				conn.close();
				System.out.println("MainFrame DB종료");
				indexPanel.setBackground(default_BC);
				cardL.show(pageCard, "indexPanel");
				connectedID = "";
				
				willReadMenu_B.setBackground(default_C);
				willReadMenu_B.setEnabled(true);
				hadReadMenu_B.setBackground(default_C);
				hadReadMenu_B.setEnabled(true);
				setConfig_B.setBackground(default_C);
				setConfig_B.setEnabled(true);
				whichWorksWindow = worksWindow.NONE;
				
				
				SignInform sif = new SignInform(this, "로그인");
				sif.setSize(300, 200);
				sif.isThisWorks = true;
				sif.controlSIDB(sif.isThisWorks);
				sif.setLocationRelativeTo(null);
				sif.setVisible(true);
				sif.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

}
