import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class StartingScreen extends JFrame {

	private JPanel contentPane;
	private ImageIcon baseProfile = new ImageIcon("img/user.jpg");
	private ImageIcon profilePicture = new ImageIcon("img/user.jpg"); 
	private String statusMessage = "상태메세지"; 
	private ImageIcon img2 = new ImageIcon("img/speech-bubble.jpg");
	private ImageIcon addUserIcon = new ImageIcon("img/add-user.jpg");
	private ImageIcon chatIcon = new ImageIcon("img/chat.jpg");
	private ImageIcon settingIcon = new ImageIcon("img/setting.jpg");
	private JButton button1 = new JButton(baseProfile);
	private JButton button2 = new JButton(img2);
	private JPanel rightPanel = new JPanel();
	
	private JLabel profile; // 프로필 사진, 이름 
	private JLabel status; // 상태 메시지 
	
	private JFrame frame;
	private FileDialog fd;
	private StatusDialog dialog;
	
	public StartingScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		dialog = new StatusDialog(this, "상태 메세지 변경");
		JPanel leftPanel = new JPanel();
		
		leftPanel.setBackground(new Color(230,230,230));
		leftPanel.setLayout(new GridLayout(10, 1, 0, 0));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightPanel.removeAll();
				setFriendPanel();
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
		setFriendPanel(); // 초기화면
		setVisible(true);
	}
	
	private void setFriendPanel() {
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setLayout(new BorderLayout(0, 0));
		rightPanel.add(myProfilePanel(profilePicture, statusMessage), BorderLayout.NORTH);
		rightPanel.add(friendListPanel(), BorderLayout.CENTER);
	}
	
	private void setChatRoomPanel() {
		rightPanel.setBackground(Color.WHITE);
		rightPanel.add(chatRoomPanel());
	}
	
	// user profile, 창을 바꿔도 프로필 사진과 상태메세지 유지
	private JPanel myProfilePanel(ImageIcon profilePicture, String statusMessage) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		panel.setBackground(Color.WHITE);
		changeProfile changeProfile = new changeProfile();
		String id = "id";
		//profile = new JLabel(id, baseProfile, SwingConstants.LEFT);
		profile = new JLabel(id, profilePicture, SwingConstants.LEFT);
		status = new JLabel(statusMessage);
		// 프로필 사진, 상태메시지 변경 기능 추가
		profile.addMouseListener(changeProfile);
		status.addMouseListener(changeProfile);
		panel.add(profile, BorderLayout.WEST);
		panel.add(status, BorderLayout.EAST);
		
		return panel;
	}
	
	// 친구 목록
	private JPanel friendListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		JScrollPane friendList = new JScrollPane();
		panel.add(northPanel(), BorderLayout.NORTH);
		panel.add(friendList, BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel northPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		JLabel label = new JLabel("친구");
		JButton addFriendButton = new JButton(addUserIcon);
		panel.add(label, BorderLayout.WEST);
		panel.add(addFriendButton, BorderLayout.EAST);
		
		return panel;
	}
	
	// 채팅방 목록 
	private JPanel chatRoomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		JScrollPane chatRoomList = new JScrollPane();
		panel.add(northPanel2(), BorderLayout.NORTH);
		panel.add(chatRoomList, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel northPanel2() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		JLabel label = new JLabel("채팅");
		panel.add(label, BorderLayout.WEST);
		panel.add(chatPagePanel(), BorderLayout.EAST);
		return panel;
	}
	
	// 채팅방 추가 기능, 설정 기능
	private JPanel chatPagePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2,0,0));
		JButton addRoom = new JButton(chatIcon);
		JButton setting = new JButton(settingIcon);
		panel.add(addRoom);
		panel.add(setting);
		
		return panel;
	}
	
	class changeProfile extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
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
					double ratio;
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
				dialog.setVisible(true);
			}
		}
	}
	
	// 상태 메세지 변경 
	class StatusDialog extends JDialog {
		private JTextField tf = new JTextField(10);
		private JButton changeButton = new JButton("확인");
		
		public StatusDialog(JFrame frame, String title) {
			super(frame, title);
			setLayout(new FlowLayout());
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
}
