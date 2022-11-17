import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartingScreen extends JFrame {

	private JPanel contentPane;
	private ImageIcon img = new ImageIcon("img/user.jpg");
	private ImageIcon img2 = new ImageIcon("img/speech-bubble.jpg");
	private ImageIcon addUserIcon = new ImageIcon("img/add-user.jpg");
	private ImageIcon chatIcon = new ImageIcon("img/chat.jpg");
	private ImageIcon settingIcon = new ImageIcon("img/setting.jpg");
	private JButton button1 = new JButton(img);
	private JButton button2 = new JButton(img2);
	private JPanel rightPanel = new JPanel();
	
	public StartingScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
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
		rightPanel.add(myProfilePanel(), BorderLayout.NORTH);
		rightPanel.add(friendListPanel(), BorderLayout.CENTER);
	}
	
	private void setChatRoomPanel() {
		rightPanel.setBackground(Color.WHITE);
		rightPanel.add(chatRoomPanel());
	}

	// user profile 
	private JPanel myProfilePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0,0));
		panel.setBackground(Color.WHITE);
		String id = "id";
		String statusMessage = "상태메세지";
		JLabel profile = new JLabel(id, img, SwingConstants.LEFT);
		JLabel status = new JLabel(statusMessage);
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
}
