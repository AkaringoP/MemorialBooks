package finaltest;

import javax.swing.*;

import finaltest.MainFrameForm.worksWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindIDnPW extends JDialog implements ActionListener {
	GridBagLayout gBag;
	JLabel Uname_L, birth_L, Uid_L;
	JTextField Uname_TF, birth_TF, Uid_TF;
	String pwNrepw;
	JButton findID_B, changePW_B;
	
	final String PATH1 = "./clavis_!.dat";
	final String PATH2 = "./clavis_@.dat";
	
	FileReader fr = null;
	BufferedReader br = null;
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private int uprs;
	
	public FindIDnPW(SignInform signInform, String title) {
		super(signInform, title, true);
		gBag = new GridBagLayout();
		setLayout(gBag);
		
		Uname_L = new JLabel("이름");
		Uname_L.setHorizontalAlignment(SwingConstants.CENTER);
		Uname_TF = new JTextField(12);
		birth_L = new JLabel("생년월일(8자리로)");
		birth_L.setHorizontalAlignment(SwingConstants.CENTER);
		birth_TF = new JTextField(12);
		findID_B = new JButton("아이디 찾기");
		findID_B.addActionListener(this);
		Uid_L = new JLabel("아이디");
		Uid_L.setHorizontalAlignment(SwingConstants.CENTER);
		Uid_TF = new JTextField(12);
		changePW_B = new JButton("비밀번호 변경");
		changePW_B.addActionListener(this);
		
		gbInsert(Uname_L, 0, 0, 1, 1);
		gbInsert(Uname_TF, 1, 0, 2, 1);
		gbInsert(birth_L, 0, 1, 1, 1);
		gbInsert(birth_TF, 1, 1, 2, 1);
		gbInsert(findID_B, 1, 2, 1, 1);
		gbInsert(Uid_L, 0, 3, 1, 1);
		gbInsert(Uid_TF, 1, 3, 2, 1);
		gbInsert(changePW_B, 1, 4, 1, 1);
		
		dbSet();
	}
	
	public void dbSet() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String dbURL = "jdbc:mysql://localhost:3307/finaltest?serverTimezone=UTC"; // localhost:3306 포트는 컴퓨터설치된 mysql주소
			String dbID = "root";
			String dbPassword = "1234";
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);		
		} catch (Exception e) {
			e.printStackTrace(); // 오류가 무엇인지 출력
		}
	}
	
	public void gbInsert(Component c, int x, int y, int w, int h){
		GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill= GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
	    gbc.gridx = x;
	    gbc.gridy = y;
	    gbc.gridwidth = w;
	    gbc.gridheight = h;
	    gBag.setConstraints(c,gbc);
	    add(c);       
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == findID_B) {
			Pattern pattern_BIRTH = Pattern.compile("^((19|20)\\d\\d)?([- /.])?(0[1-9]|1[012])([- /.])?(0[1-9]|[12][0-9]|3[01])$");
			Matcher matcher_BIRTH = pattern_BIRTH.matcher(birth_TF.getText());
			if(matcher_BIRTH.find() == false) {
				JOptionPane.showMessageDialog(null, "생년월일을 형식에 맞게 입력해주세요.");
				birth_TF.setText("");
			}
			else  {
				if(!Uname_TF.getText().equals("") && !birth_TF.getText().equals("")) {
					String findUid = "SELECT Uid FROM user WHERE Uname = ? AND birth = ?";
					try {
						pstmt = conn.prepareStatement(findUid);
						pstmt.setString(1, Uname_TF.getText());
						pstmt.setString(2, birth_TF.getText());
						rs = pstmt.executeQuery();
						
						if(rs.next()) {
							JOptionPane.showMessageDialog(null, "아이디 : " + rs.getString("Uid"));
							Uname_TF.setText("");
							birth_TF.setText("");
						}
						else {
							JOptionPane.showMessageDialog(null, "입력하신 정보의 계정이 존재하지 않습니다.");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
				}
				else
					JOptionPane.showMessageDialog(null, "비어있는 항목이 있습니다.");
			}
		}
		else if(e.getSource() == changePW_B) {
			if(!Uname_TF.getText().equals("") && !birth_TF.getText().equals("") && !Uid_TF.getText().equals("")) {
				String findEquals = "SELECT Uid FROM user WHERE Uname = ? AND birth = ? AND Uid = ?";
				try {
					pstmt = conn.prepareStatement(findEquals);
					pstmt.setString(1, Uname_TF.getText());
					pstmt.setString(2, birth_TF.getText());
					pstmt.setString(3, Uid_TF.getText());
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						JPanel panel = new JPanel();
						JLabel label = new JLabel("새로운 비밀번호를 입력해주세요 : ");
						JPasswordField pass = new JPasswordField(15);
						JLabel label2 = new JLabel("비밀번호 확인");
						JLabel bigPanel = new JLabel();
						JPasswordField pass2 = new JPasswordField(15);
						panel.add(label);
						panel.add(pass);
						panel.add(label2);
						panel.add(pass2);
						panel.setSize(400, 250);
						String[] options = new String[]{"OK", "Cancel"};
						int option = JOptionPane.showOptionDialog(null, panel, "비밀번호 변경",
						                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
						                         null, options, options[1]);
						if(option == 0 && String.valueOf(pass.getPassword()).equals(String.valueOf(pass2.getPassword()))) // pressing OK button
						{
						    String changePW = "UPDATE user SET encryptedPwd = ? WHERE Uid = ?";
							String key1 = "";
							long key2;
							
							fr = new FileReader(PATH1);
							br = new BufferedReader(fr);
							key1 = br.readLine();
							
							FileInputStream fis = new FileInputStream(PATH2);
							DataInputStream dis = new DataInputStream(fis);
							key2 = dis.readLong();
							Long encryptedPwd = (String.valueOf(pass.getPassword()) + key1).hashCode() * key2;
							
							pstmt = conn.prepareStatement(changePW);
							pstmt.setLong(1, encryptedPwd);
							pstmt.setString(2, Uid_TF.getText());
							uprs = pstmt.executeUpdate();
							
							if(uprs == 1) {
								JOptionPane.showMessageDialog(null, "변경되었습니다.");
								Uname_TF.setText("");
								birth_TF.setText("");
								Uid_TF.setText("");	
							}
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			else
				JOptionPane.showMessageDialog(null, "비어있는 항목이 있습니다.");
		}
	}
}
