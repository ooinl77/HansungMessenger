import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class StartingScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel rightPanel = new JPanel();
	private JPanel friendPanel; // 친구 목록
	private JPanel chatRoomPanel; // 채팅방 목록
	private ImageIcon baseProfile = new ImageIcon("img/user.jpg");
	private ImageIcon profilePicture = new ImageIcon("img/user.jpg");  
	private ImageIcon img2 = new ImageIcon("img/speech-bubble.jpg");
	private ImageIcon addUserIcon = new ImageIcon("img/add-user.jpg");
	private ImageIcon chatIcon = new ImageIcon("img/chat.jpg");
	private ImageIcon settingIcon = new ImageIcon("img/setting.jpg");
	private String statusMessage = "상태메세지";
	
	private JLabel profile; // 프로필 사진, 이름 
	private JLabel status; // 상태 메시지 
	private JButton addFriendButton; // 친구 추가 버튼 
	private JButton addRoom; // 채팅방 추가 버튼 
	private JButton setting; // 설정 버튼
	private JButton button1 = new JButton(baseProfile); // 친구 목록 이동 버튼
	private JButton button2 = new JButton(img2); // 채팅방 목록 이동 버튼
	private JScrollPane chatRoomList; // 채팅방 목록 창
	private JScrollPane friendListPane; // 친구 목록 창
	
	private JFrame frame;
	private FileDialog fd;
	private StatusDialog statusDialog;
	private ChatRoomDialog chatRoomDialog;
	private Vector<JLabel> roomVector;
	private Vector<JLabel> friendVector;
	
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public StartingScreen(String id, String ip_addr, String port_no) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		roomVector = new Vector<JLabel>();
		friendVector = new Vector<JLabel>();
		
		statusDialog = new StatusDialog(this, "상태 메세지 변경");
		chatRoomDialog = new ChatRoomDialog(this, "채팅방 생성");
		
		JPanel leftPanel = new JPanel();
		
		leftPanel.setBackground(new Color(230,230,230));
		leftPanel.setLayout(new GridLayout(10, 1, 0, 0));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightPanel.removeAll();
				setFriendPanel(id);
				rightPanel.updateUI();
			}
		});
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightPanel.removeAll();
				setChatRoomPanel();
				rightPanel.updateUI();
			}
		});
		leftPanel.add(button1);
		leftPanel.add(button2);
		
		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(rightPanel, BorderLayout.CENTER);
		setFriendPanel(id); // 초기화면
		setVisible(true);
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));


			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			//SendMessage("/login " + UserName);
			ChatMsg obcm = new ChatMsg(id, "100", "Hello");
			SendObject(obcm);
			
			ListenNetwork net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// Server Message를 수신해서 화면에 표시
		class ListenNetwork extends Thread {
			public void run() {
				while (true) {
					try {
						Object obcm = null;
						String msg = null;
						ChatMsg cm;
						try {
							obcm = ois.readObject();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						}
						if (obcm == null)
							break;
						if (obcm instanceof ChatMsg) {
							cm = (ChatMsg) obcm;
						} else
							continue;
					} catch (IOException e) {
						try {
//							dos.close();
//							dis.close();
							ois.close();
							oos.close();
							socket.close();
							break;
						} catch (Exception ee) {
							break;
						} // catch문 끝
					} // 바깥 catch문끝

				}
			}
		}
	private void setFriendPanel(String id) {
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setLayout(new BorderLayout(0, 0));
		rightPanel.add(myProfilePanel(id, profilePicture, statusMessage), BorderLayout.NORTH);
		rightPanel.add(friendListPanel(), BorderLayout.CENTER);
	}
	
	private void setChatRoomPanel() {
		rightPanel.setBackground(Color.WHITE);
		rightPanel.add(chatRoomPanel());
	}
	
	// user profile, 창을 바꿔도 프로필 사진과 상태메세지 유지
	private JPanel myProfilePanel(String id ,ImageIcon profilePicture, String statusMessage) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		panel.setBackground(Color.WHITE);
		myMouseAdapter listener = new myMouseAdapter();
		//profile = new JLabel(id, baseProfile, SwingConstants.LEFT);
		profile = new JLabel(id, profilePicture, SwingConstants.LEFT);
		status = new JLabel(statusMessage);
		// 프로필 사진, 상태메시지 변경 기능 추가
		profile.addMouseListener(listener);
		status.addMouseListener(listener);
		panel.add(profile, BorderLayout.WEST);
		panel.add(status, BorderLayout.EAST);
		
		return panel;
	}
	
	// 친구 목록
	private JPanel friendListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		friendListPane = new JScrollPane(friendList());
		panel.add(northPanel(), BorderLayout.NORTH);
		panel.add(friendListPane, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel northPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		JLabel label = new JLabel("친구");
		buttonAction btnAction = new buttonAction();
		addFriendButton = new JButton(addUserIcon);
		addFriendButton.addActionListener(btnAction);
		panel.add(label, BorderLayout.WEST);
		panel.add(addFriendButton, BorderLayout.EAST);
		
		return panel;
	}
	
	private JPanel friendList() {
		friendPanel = new JPanel();
		friendPanel.setBackground(Color.WHITE);
		friendPanel.setLayout(new GridLayout(100,1,0,0));
		for (int i=0; i<friendVector.size(); i++) {
			friendPanel.add(friendVector.get(i));
		}
		return friendPanel;
	}
	
	// 채팅방 목록 
	private JPanel chatRoomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		chatRoomList = new JScrollPane(chatRoomList());
		panel.add(northPanel2(), BorderLayout.NORTH);
		panel.add(chatRoomList, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel northPanel2() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		Color color = new Color(255,255,0);
		JLabel label = new JLabel("채팅");
		panel.add(label, BorderLayout.WEST);
		panel.add(chatPagePanel(), BorderLayout.EAST);
		return panel;
	}
	
	private JPanel chatRoomList() {
		chatRoomPanel = new JPanel();
		chatRoomPanel.setBackground(Color.WHITE);
		for (int i = 0; i< roomVector.size(); i++) {
			chatRoomPanel.add(roomVector.get(i));
		}
		chatRoomPanel.setLayout(new GridLayout(100,1,0,0));
		
		return chatRoomPanel;
	}
	
	// 채팅방 추가 기능, 설정 기능
	private JPanel chatPagePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2,0,0));
		buttonAction btnAction = new buttonAction();
		addRoom = new JButton(chatIcon);
		setting = new JButton(settingIcon);
		addRoom.addActionListener(btnAction);
		setting.addActionListener(btnAction);
		panel.add(addRoom);
		panel.add(setting);
		
		return panel;
	}
	
	// 친구 추가 버튼, 방 추가 버튼, 설정 버튼
	class buttonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 친구 추가 버튼 
			if (e.getSource() == addFriendButton) {
				
			}
			// 채팅방 추가 버튼
			else if (e.getSource() == addRoom) {
				chatRoomDialog.setVisible(true);

			}
			// 설정 버튼 
			else if (e.getSource() == setting) {
				//contentPane.setBackground(Color.YELLOW);
			}
		}
	}
	// 마우스 이벤트 처리
	class myMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel)e.getSource(); // 클릭된 채팅방
			// 프로필 사진 변경 
			if (e.getSource() == profile) {
				frame = new JFrame("프로필 사진 변경");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				fd.setVisible(true);
				
				ImageIcon icon = new ImageIcon(fd.getDirectory() + fd.getFile());
				Image img = icon.getImage();
				
				if (fd.getDirectory() == null && fd.getFile() == null) { // cancel 버튼을 누르는 경우 원래의 설정 유지
					profile.setIcon(profile.getIcon());
					
				}
				else { // 이미지를 선택한 경우 
					int width = icon.getIconWidth();
					int height = icon.getIconHeight();
					
					// 이미지를 고정 크기로 프로필 사진 설정  
					try {
						if ((width > 50 || height > 50) || (width < 50 || height < 50)) {
							width = 50;
							height = 50;
						}
						Image new_img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
						ImageIcon new_icon = new ImageIcon(new_img);
						profile.setIcon(new_icon);
						profilePicture.setImage(new_img);
					}
					catch(Exception ex) {
						
					}
				}
			}
			// 상태 메시지 변경 
			else if (e.getSource() == status) {
				statusDialog.setVisible(true);
			}
			// 채팅방 띄우기
			else if (l.getText().contains("채팅방") && e.getClickCount() == 2) { // 더블 클릭 시 
				System.out.println("hi");
			}
		}
	}
	
	// 상태 메세지 변경 
	class StatusDialog extends JDialog {
		private static final long serialVersionUID = 1L;
		private JTextField tf = new JTextField(10);
		private JButton changeButton = new JButton("확인");
		
		public StatusDialog(JFrame frame, String title) {
			super(frame, title);
			setLayout(new FlowLayout());
			tf.setText(statusMessage);
			this.add(tf);
			this.add(changeButton);
			setSize(300, 70);
			
			changeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(tf.getText().length() == 0) {
						statusMessage = "          ";
					}
					else {
						statusMessage = tf.getText();
					}
					status.setText(statusMessage);
					setVisible(false);
				}
			});
		}	
	}
	// 채팅방 생성 다이얼로그
	class ChatRoomDialog extends JDialog { 
		private static final long serialVersionUID = 1L;
		private JTextField roomNum = new JTextField();
		private JButton createBtn = new JButton("생성");
		public ChatRoomDialog(JFrame frame, String title) {
			super(frame, title);
			setLayout(new BorderLayout());
			this.add(roomNum, BorderLayout.NORTH);
			this.add(new JScrollPane(friendList()), BorderLayout.CENTER);
			this.add(createBtn, BorderLayout.SOUTH);
			setSize(300,500);
			
			createBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JLabel room = new JLabel("채팅방 " + roomNum.getText());
					room.setBorder(new LineBorder(Color.BLACK, 1, false));
					room.addMouseListener(new myMouseAdapter()); // 클릭 시 채팅방 띄우기 기능
					roomVector.add(room);
					chatRoomPanel.add(room);
					repaint();
					ChatRoom chatRoom = new ChatRoom(roomNum.getText());
					setVisible(false);
				}
				
			});
		}
	}
	
	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
		}
	}
}
