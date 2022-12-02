// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatRoom extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private String room_id;
	private String userlist;
	private StartingScreen mainview;
	private String ID;
	private JPanel contentPane;
	private JTextField txtInput;
//	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ImageIcon imgIcon = new ImageIcon("img/image.jpg");
	private ImageIcon emoIcon = new ImageIcon("img/emoticon.jpg");
	private ImageIcon[] src = {new ImageIcon("img/haha.jpg"), new ImageIcon("img/good.jpg"), new ImageIcon("img/bbudeut.jpg"),
			new ImageIcon("img/angry.jpg"), new ImageIcon("img/sleepy.jpg"), new ImageIcon("img/ssiik.jpg"),
			new ImageIcon("img/gamdong.jpg"), new ImageIcon("img/heukheuk.jpg")
	};
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JTextPane textArea;

	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;
	private JButton emoBtn;
	private EmoticonDialog dialog;
	
	private Vector<String> v = new Vector<String>();
	StyledDocument doc;
	SimpleAttributeSet right = new SimpleAttributeSet();
	SimpleAttributeSet left = new SimpleAttributeSet();
	
	/**
	 * Create the frame.
	 */
	public ChatRoom(String room_id, String userlist, StartingScreen mainview, String ID) { 
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.room_id = room_id;
		this.userlist = userlist;
		this.mainview = mainview;
		this.ID = ID;
		setBounds(100, 100, 374, 565);
		contentPane = new JPanel();
		contentPane.setBackground(Color.YELLOW);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		dialog = new EmoticonDialog(this, "이모티콘");
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 352, 471);
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		doc = textArea.getStyledDocument();
		textArea.setEditable(true);
		textArea.setBackground(new Color(217,229,255));
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField();
		txtInput.setBounds(80, 489, 218, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("전송");
		btnSend.setFont(new Font("굴림", Font.PLAIN, 14));
		btnSend.setBounds(303, 489, 61, 40);
		contentPane.add(btnSend);
		setVisible(true);

		imgBtn = new JButton(imgIcon);
		imgBtn.setBounds(10, 489, 36, 40);
		contentPane.add(imgBtn);
		
		emoBtn = new JButton(emoIcon);
		emoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(true);
			}
		});
		emoBtn.setBounds(45, 489, 36, 40);
		contentPane.add(emoBtn);

		try {
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			imgBtn.addActionListener(action2);

		} catch (NumberFormatException e) { //| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}
	
	public String getRoomId() {
		return room_id;
	}

	public String getUserList() {
		return userlist;
	}
	
	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				msg = txtInput.getText();
				
				ChatMsg obcm = new ChatMsg(ID, "200", room_id, userlist, msg);
				mainview.SendObject(obcm);
				
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				
				// 본인이 보낸 메시지 오른쪽 출력
				StyleConstants.setAlignment(right,StyleConstants.ALIGN_RIGHT);
				
				try {
					if(msg.equals("(하하)")) {
						printEmoticon(src[0]);
					}
					else if(msg.equals("(굿)")) {
						printEmoticon(src[1]);
					}
					else if(msg.equals("(뿌듯)")) {
						printEmoticon(src[2]);
					}
					else if(msg.equals("(열받아)")) {
						printEmoticon(src[3]);
					}
					else if(msg.equals("(졸려)")) {
						printEmoticon(src[4]);
					}
					else if(msg.equals("(씨익)")) {
						printEmoticon(src[5]);
					}
					else if(msg.equals("(감동)")) {
						printEmoticon(src[6]);
					}
					else if(msg.equals("(흑흑)")) {
						printEmoticon(src[7]);
					}
					else {
						doc.setParagraphAttributes(doc.getLength(), 1, right, false);
						doc.insertString(doc.getLength(), msg + "\n", right);
					}
				}
				catch(Exception ex) {}
			}
		}
	}

	// 이모티콘 출력
	public void printEmoticon(ImageIcon emoticon) {
		try {
			doc.setParagraphAttributes(doc.getLength(), 1, right, false);
			StyleConstants.setIcon(right, emoticon);
			doc.insertString(doc.getLength(), "\n" , right);
		}
		catch(Exception ex) {
			
		}
	}
	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == imgBtn) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
			
				fd.setVisible(true);
				ChatMsg obcmr = new ChatMsg(ID, "300", room_id, userlist, "IMG");
				
				ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
				Image ori_img = img.getImage();
				
				double ratio;
				int width = img.getIconWidth();
				int height = img.getIconHeight();
				
				StyleConstants.setAlignment(right,StyleConstants.ALIGN_RIGHT);
				doc.setParagraphAttributes(doc.getLength(), 1, right, false);
				
				try {
					if (width > 200 || height > 200) {
						if (width > height) { // 가로 사진
							ratio = (double) height / width;
							width = 200;
							height = (int) (width * ratio);
						} else { // 세로 사진
							ratio = (double) width / height;
							height = 200;
							width = (int) (height * ratio);
						}
						Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
						ImageIcon img_icon = new ImageIcon(new_img);
						StyleConstants.setIcon(right, img_icon);
						doc.insertString(doc.getLength(), "\n" , right);
					}
					else {
						StyleConstants.setIcon(right, img);
						doc.insertString(doc.getLength(), "\n" , right);
					}
				}
				catch(Exception ex) {}
				obcmr.setImg(img);
				mainview.SendObject(obcmr);
			}
		}
	}

	ImageIcon icon2 = new ImageIcon("img/icon2.jpg");

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg) {
//		textArea.append(msg + "\n");
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다
		StyleConstants.setAlignment(left,StyleConstants.ALIGN_LEFT);
		try {
			doc.setParagraphAttributes(doc.getLength(), 1, left, false);
			doc.insertString(doc.getLength(), msg + "\n", left);
		}
		catch(Exception ex) {}
	}

	public void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		
		StyleConstants.setAlignment(left,StyleConstants.ALIGN_LEFT);
		
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		try{
			if (width > 200 || height > 200) {
				if (width > height) { // 가로 사진
					ratio = (double) height / width;
					width = 200;
					height = (int) (width * ratio);
				} else { // 세로 사진
					ratio = (double) width / height;
					height = 200;
					width = (int) (height * ratio);
				}
				doc.setParagraphAttributes(doc.getLength(), 1, left, false);
				Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				ImageIcon new_icon = new ImageIcon(new_img);
				textArea.insertIcon(new_icon);
			} 
			else {
				doc.setParagraphAttributes(doc.getLength(), 1, left, false);
				textArea.insertIcon(ori_icon);
			}
		}
		catch(Exception e) {}
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	
	// 이모티콘 창 
	class EmoticonDialog extends JDialog {
		private static final long serialVersionUID = 1L;
		
		Vector<JButton> emoticon = new Vector<JButton>();
		
		public EmoticonDialog(JFrame frame, String title) {
			super(frame, title);
			setLayout(new GridLayout(2,4,0,0));
			setBackground(Color.WHITE);
			setSize(300, 200);
			
			for (int i=0; i<src.length; i++) {
				emoticon.add(new JButton(src[i]));
			}
			for (int i=0; i<emoticon.size(); i++) {
				this.add(emoticon.get(i));
				// 이모티콘을 클릭하면 이모티콘 정보 추출 
				emoticon.get(i).addActionListener(new emoticonSendListener(i));
			}
		}	
	}
	
	class emoticonSendListener implements ActionListener {
		private int num;
		
		public emoticonSendListener(int num) {
			this.num = num;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			printEmoticon(src[num]);
		}
		
	}
}
