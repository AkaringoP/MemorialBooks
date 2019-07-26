package finaltest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.*;
import java.util.*;
import java.io.*;

public class SignUpForm extends JDialog implements ActionListener, FocusListener {

	GridBagLayout gBag;
	JLabel id_L, dupleID_L, pw_L, rePw_L, name_L, birth_L;
	JTextField id_TF, name_TF, birth_TF;
	JPasswordField pw_TF, rePw_TF;
	JButton confirm_B, cancel_B, clear_B;
	
	final String PATH1 = "./clavis_!.dat";
	final String PATH2 = "./clavis_@.dat";
	
	FileReader fr = null;
	BufferedReader br = null;
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	SignUpForm(SignInform signInform, String title) {
		super(signInform, title, true);
		gBag = new GridBagLayout();
		
		setLayout(gBag);
		
		id_L = new JLabel("아이디");
		id_L.setHorizontalAlignment(SwingConstants.LEFT);
		dupleID_L = new JLabel("중복되는 아이디가 있습니다.");
		dupleID_L.setForeground(Color.RED);
		dupleID_L.setVisible(false);
		pw_L = new JLabel("비밀번호");
		pw_L.setHorizontalAlignment(SwingConstants.LEFT);
		rePw_L = new JLabel("비밀번호 확인");
		rePw_L.setHorizontalAlignment(SwingConstants.LEFT);
		name_L = new JLabel("이름");
		name_L.setHorizontalAlignment(SwingConstants.LEFT);
		birth_L = new JLabel("생년월일(8자리 숫자로)");
		birth_L.setHorizontalAlignment(SwingConstants.LEFT);
		
		id_TF = new JTextField(15);
		id_TF.addFocusListener(this);
		pw_TF = new JPasswordField(15);
		rePw_TF = new JPasswordField(15);
		name_TF = new JTextField(10);
		birth_TF = new JTextField(8);
		birth_TF.addActionListener(this);
		
		clear_B = new JButton("새로 작성");
		clear_B.addActionListener(this);
		cancel_B = new JButton("취소");
		cancel_B.addActionListener(this);
		confirm_B = new JButton("확인");
		confirm_B.addActionListener(this);
		
		gbInsert(id_L, 0, 0, 1, 1);
		gbInsert(id_TF, 1, 0, 2, 1);
		gbInsert(dupleID_L, 1, 1, 1, 1);
		gbInsert(pw_L, 0, 2, 1, 1);
		gbInsert(pw_TF, 1, 2, 2, 1);
		gbInsert(rePw_L, 0, 3, 1, 1);
		gbInsert(rePw_TF, 1, 3, 2, 1);
		gbInsert(name_L, 0, 4, 1, 1);
		gbInsert(name_TF, 1, 4, 2, 1);
		gbInsert(birth_L, 0, 5, 1, 1);
		gbInsert(birth_TF, 1, 5, 2, 1);
		gbInsert(confirm_B, 0, 6, 1, 1);
		gbInsert(clear_B, 2, 6, 1, 1);
		gbInsert(cancel_B, 1, 6, 1, 1);
		
		controlSUDB(true);
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
	
	public void controlSUDB(boolean isThisWorks) {
		try {
			if(isThisWorks == true) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String dbURL = "jdbc:mysql://localhost:3307/finaltest?serverTimezone=UTC"; // localhost:3306 포트는 컴퓨터설치된 mysql주소
				String dbID = "root";
				String dbPassword = "1234";
				conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
				System.out.println("SignUpform DB준비됨");
			}
			else {
				conn.close();
				System.out.println("SignUpform DB종료");
			}
		} catch (Exception e) {
			e.printStackTrace(); // 오류가 무엇인지 출력
		}
	}
	
	public int join(User user) {
		String SQL = "INSERT INTO user VALUES (?,?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getId());
			pstmt.setLong(2, user.getPwd());
			pstmt.setString(3, user.getUname());
			pstmt.setString(4, user.getBirth());
			pstmt.setString(5, user.getSetNum());
			pstmt.setDate(6, user.getRegDate());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // DB 오류
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == clear_B) {
			id_TF.setText("");
			pw_TF.setText("");
			rePw_TF.setText("");
			name_TF.setText("");
			birth_TF.setText("");
		}
		else if(e.getSource() == cancel_B) {
			id_TF.setText("");
			pw_TF.setText("");
			rePw_TF.setText("");
			name_TF.setText("");
			birth_TF.setText("");
			controlSUDB(false);
			SignInform.controlSIDB(true);
			setVisible(false);
		}
		else if(e.getSource() == confirm_B || e.getSource() == birth_TF) {
			Pattern pattern_BIRTH = Pattern.compile("^((19|20)\\d\\d)?([- /.])?(0[1-9]|1[012])([- /.])?(0[1-9]|[12][0-9]|3[01])$");
			Matcher matcher_BIRTH = pattern_BIRTH.matcher(birth_TF.getText());
			String key1 = null; 
			long key2 = 0;
			
			try {
				System.out.println("SignUp처리중..");
				if(matcher_BIRTH.find() == false) {
					JOptionPane.showMessageDialog(null, "생년월일을 형식에 맞게 입력해주세요.");
					birth_TF.setText("");
				}
				else {
					if(id_TF.getText().equals("") || pw_TF.getPassword().equals("") ||
							name_TF.getText().equals("") || birth_L.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "비어있는 항목이 있습니다.");
					}
					else {
						Date sqlRegDate = new Date(new java.util.Date().getTime());
						
						/*						비밀번호 암호화 과정				*/
						fr = new FileReader(PATH1);
						br = new BufferedReader(fr);
						key1 = br.readLine();
						
						FileInputStream fis = new FileInputStream(PATH2);
						DataInputStream dis = new DataInputStream(fis);
						key2 = dis.readLong();
						Long encryptedPwd = (String.valueOf(pw_TF.getPassword()) + key1).hashCode() * key2;
						
						User newUser = new User(id_TF.getText(), encryptedPwd, name_TF.getText(), birth_TF.getText(), "000000", sqlRegDate);
						int result = join(newUser);
						if(result == 1) { 
							JOptionPane.showMessageDialog(null, "회원가입 성공");
							id_TF.setText("");
							pw_TF.setText("");
							rePw_TF.setText("");
							name_TF.setText("");
							birth_TF.setText("");
							conn.close();
							pstmt.close();
							rs.close();
							
							controlSUDB(false);
							SignInform.controlSIDB(true);
							setVisible(false);
						}
						else if(result == -1)
							JOptionPane.showMessageDialog(null, "레코드 추가 실패했습니다.");
						else
							JOptionPane.showMessageDialog(null, "실패");
					}
				}
			} catch (SQLException | NumberFormatException | IOException e2) {
				e2.printStackTrace();
			}
			
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		/*					        아이디 체크								*/
		if(e.getSource() == id_TF) {						
			String idCheck = "SELECT * FROM user WHERE Uid = ?";
			Pattern pattern_ID = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9]{4,11}$");
			Matcher matcher_ID = pattern_ID.matcher(id_TF.getText());
			try {
				pstmt = conn.prepareStatement(idCheck);
				pstmt.setString(1, id_TF.getText());
				rs = pstmt.executeQuery();
				
				if(matcher_ID.find() == false) {
					JOptionPane.showMessageDialog(null, "아이디를 형식에 맞게 입력해주세요."
							+ "(시작은 영문으로만, 특수문자 안되며 영문, 숫자로만 이루어진 5 ~ 12자 이하)");
					id_TF.setText("");
					id_TF.requestFocus();
				}
				else {
					if(rs.next()) {
						JOptionPane.showMessageDialog(null, "중복되는 아이디가 있습니다.");
						id_TF.setText("");
						id_TF.requestFocus();
					}
				}
			} catch (SQLException e2) {
				System.out.println("연결에 실패하였습니다.");
				e2.printStackTrace();
			}
		}
		/*					        비밀번호 확인 체크							*/
		else if (e.getSource() == rePw_TF) {
			if(pw_TF.getPassword() != rePw_TF.getPassword()) {
				JOptionPane.showMessageDialog(null, "비밀번호를 확인해주세요.");
				pw_TF.setText("");
				rePw_TF.setText("");
				pw_TF.requestFocus();
			}
		}
	}
}