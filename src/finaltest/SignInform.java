package finaltest;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class SignInform extends JDialog implements ActionListener {
	
	GridBagLayout gBag;
	JLabel id, pwd;
	JTextField idField;
	JPasswordField pwdField;
	JButton FindIDPW_B, SignUp_B, SignIn_B;
	
	public static boolean isThisWorks = false;				// 다른 프레임에 알리기 위함
	
	final String PATH1 = "./clavis_!.dat";
	final String PATH2 = "./clavis_@.dat";
	
	FileReader fr = null;
	BufferedReader br = null;
	
	private static Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	FindIDnPW fip;
	SignUpForm suf;
	
	SignInform(JFrame frame, String title) {
		super(frame, title, true);
		gBag = new GridBagLayout();
		setLayout(gBag);
		
		id = new JLabel("아이디");
		id.setHorizontalAlignment(SwingConstants.CENTER);
		pwd = new JLabel("비밀번호");
		pwd.setHorizontalAlignment(SwingConstants.CENTER);
		idField = new JTextField();
		pwdField = new JPasswordField();
		pwdField.addActionListener(this);
		FindIDPW_B = new JButton("ID/PW 찾기");
		FindIDPW_B.addActionListener(this);
		SignUp_B = new JButton("회원가입");
		SignUp_B.addActionListener(this);
		SignIn_B = new JButton("로그인");
		SignIn_B.addActionListener(this);
		
		gbInsert(id, 0, 0, 1, 1);
		gbInsert(idField, 1, 0, 2, 1);
		gbInsert(pwd, 0, 1, 1, 1);
		gbInsert(pwdField, 1, 1, 2, 1);
		gbInsert(FindIDPW_B, 0, 2, 1, 1);
		gbInsert(SignUp_B, 1, 2, 1, 1);
		gbInsert(SignIn_B, 2, 2, 1, 1);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				dispose();
				if(MainFrameForm.connectedID.equals(""))	// 로그인 되지 않고 창이 종료되면 --> 그냥 끄자
					System.exit(0);
			}
		});
		
		isThisWorks = true;
		controlSIDB(isThisWorks);
	}
	
	public void display() {
		setVisible(true);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	
	public static void controlSIDB(boolean isThisWorks) {
		try {
			if(isThisWorks == true) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String dbURL = "jdbc:mysql://localhost:3307/finaltest?serverTimezone=UTC"; // localhost:3306 포트는 컴퓨터설치된 mysql주소
				String dbID = "root";
				String dbPassword = "1234";
				conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
				System.out.println("SignInform DB준비됨");
			}
			else {
				conn.close();
				System.out.println("SignInform DB종료");
			}
		} catch (Exception e) {
			e.printStackTrace(); // 오류가 무엇인지 출력
		}
	}
	
	public int login(String id, Long pwd) {
		String SQL = "SELECT encryptedPwd FROM user WHERE Uid = ?";
		try {
			// pstmt : prepared statement 정해진 sql문장을 db에 삽입하는 형식으로 인스턴스가져옴
			pstmt = conn.prepareStatement(SQL);
			
			// sql인젝션 같은 해킹기법을 방어하는것... pstmt을 이용해 하나의 문장을 미리 준비해서(물음표사용)
			// 물음표해당하는 내용을 유저아이디로, 매개변수로 이용.. 1)존재하는지 2)비밀번호무엇인지
			pstmt.setString(1, id);
			
			// rs:result set 에 결과보관
			rs = pstmt.executeQuery();
			
			// 결과가 존재한다면 실행
			if (rs.next()) {
				// 패스워드 일치한다면 실행
				if (rs.getLong(1) == pwd) {
					MainFrameForm.connectedID = id;
					idField.setText("");
					pwdField.setText("");
					pstmt.close();
					rs.close();
					return 1; // 로그인 성공
				} else {
					idField.setText("");
					pwdField.setText("");
					idField.requestFocus();
					return 0; // 비밀번호 불일치
				}
			}
			else {
				idField.setText("");
				pwdField.setText("");
				idField.requestFocus();
				return -1; // 아이디가 없음 오류	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; // 데이터베이스 오류를 의미
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == FindIDPW_B) {
			if(fip == null)
				fip = new FindIDnPW(this, "아이디 / 비밀번호 찾기");
			fip.setSize(300, 380);
			fip.setLocationRelativeTo(null);
			fip.setVisible(true);
			fip.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		else if(e.getSource() == SignUp_B) {
			controlSIDB(false);
			if(suf == null)
				suf = new SignUpForm(this, "회원가입");
			suf.setSize(300, 500);
			suf.setLocationRelativeTo(null);
			suf.setVisible(true);
			suf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		else if(e.getSource() == SignIn_B || e.getSource() == pwdField) {
			String key1 = null;
			long key2 = 0;
			try {
				fr = new FileReader(PATH1);
				br = new BufferedReader(fr);
				key1 = br.readLine();
				
				FileInputStream fis = new FileInputStream(PATH2);
				DataInputStream dis = new DataInputStream(fis);
				key2 = dis.readLong();
				
			} catch (NumberFormatException | IOException e1) {
				e1.printStackTrace();
			}
			
			Long encryptedPwd = (String.valueOf(pwdField.getPassword()) + key1).hashCode() * key2;
			
			int result = this.login(idField.getText(), encryptedPwd);
			if(result == 1) {
				JOptionPane.showMessageDialog(null, "로그인 성공!");
				isThisWorks = false;
				controlSIDB(isThisWorks);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				setVisible(false);
			}
			else if(result == 0)
				JOptionPane.showMessageDialog(null, "아이디나 비밀번호가 일치하지 않습니다.");
			else if(result == -1)
				JOptionPane.showMessageDialog(null, "등록된 아이디가 없습니다.");
			else if(result == -2)
				JOptionPane.showMessageDialog(null, "데이터베이스 오류.");
		}
	}
}
