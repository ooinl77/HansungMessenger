// JavaObjClientView.java ObjecStram ��� Client
//�������� ä�� â
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
	private JPanel contentPane;
	private JTextField txtInput;
//	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����
	private Socket socket; // �������
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
	public ChatRoom(String room_id, String userlist) { // String username, String ip_addr, String port_no) {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.room_id = room_id;
		this.userlist = userlist;
		setBounds(100, 100, 374, 565);
		contentPane = new JPanel();
		contentPane.setBackground(Color.YELLOW);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		dialog = new EmoticonDialog(this, "�̸�Ƽ��");
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 352, 471);
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		doc = textArea.getStyledDocument();
		textArea.setEditable(true);
		textArea.setBackground(new Color(217,229,255));
		textArea.setFont(new Font("����ü", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField();
		txtInput.setBounds(80, 489, 218, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("����");
		btnSend.setFont(new Font("����", Font.PLAIN, 14));
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

	// Server Message�� �����ؼ� ȭ�鿡 ǥ��
	class ListenNetwork extends Thread {
		public void run() {
//			while (true) {
//				try {
//					Object obcm = null;
//					String msg = null;
//					ChatMsg cm;
//					try {
//						obcm = ois.readObject();
//					} catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						break;
//					}
//					if (obcm == null)
//						break;
//					if (obcm instanceof ChatMsg) {
//						cm = (ChatMsg) obcm;
//						msg = String.format("[%s] %s", cm.getId(), cm.getData());
//					} else
//						continue;
//					switch (cm.getCode()) {
//					case "200": // chat message
//						if(cm.getData().equals("(����)")) {
//							AppendText("[" + cm.getId() + "]");
//							AppendImage(new ImageIcon("img/haha.jpg"));
//						}
//						else if(cm.getData().equals("(��)")) {
//							AppendText("[" + cm.getId() + "]");
//							AppendImage(new ImageIcon("img/good.jpg"));
//						}
//						else 
//							AppendText(msg);
//						break;
//					case "300": // Image ÷��
//						AppendText("[" + cm.getId() + "]");
//						AppendImage(cm.img);
//						break;
//					case "500":
//						String[] user = cm.getData().split("\n");
//						v.removeAll(v);
//						v.add("User list\n");
//						v.add("Name\tStatus\n");
//						for(int i=0; i<user.length; i++) {
//							v.add(user[i]);
//						}
//						list.setListData(v);
//						break;
//					}
//				} catch (IOException e) {
//					AppendText("ois.readObject() error");
//					try {
//						ois.close();
//						oos.close();
//						socket.close();
//						break;
//					} catch (Exception ee) {
//						break;
//					} // catch�� ��
//				} // �ٱ� catch����
//
//			}
		}
	}

	// keyboard enter key ġ�� ������ ����
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button�� �����ų� �޽��� �Է��ϰ� Enter key ġ��
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
				txtInput.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				
				// ������ ���� �޽��� ������ ���
				StyleConstants.setAlignment(right,StyleConstants.ALIGN_RIGHT);
				
				try {
					if(msg.equals("(����)")) {
						printEmoticon(src[0]);
					}
					else if(msg.equals("(��)")) {
						printEmoticon(src[1]);
					}
					else if(msg.equals("(�ѵ�)")) {
						printEmoticon(src[2]);
					}
					else if(msg.equals("(���޾�)")) {
						printEmoticon(src[3]);
					}
					else if(msg.equals("(����)")) {
						printEmoticon(src[4]);
					}
					else if(msg.equals("(����)")) {
						printEmoticon(src[5]);
					}
					else if(msg.equals("(����)")) {
						printEmoticon(src[6]);
					}
					else if(msg.equals("(����)")) {
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

	// �̸�Ƽ�� ���
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
			// �׼� �̺�Ʈ�� sendBtn�϶� �Ǵ� textField ���� Enter key ġ��
			if (e.getSource() == imgBtn) {
				frame = new Frame("�̹���÷��");
				fd = new FileDialog(frame, "�̹��� ����", FileDialog.LOAD);
			
				fd.setVisible(true);
				
				//ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
				ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
				Image ori_img = img.getImage();
				
				double ratio;
				int width = img.getIconWidth();
				int height = img.getIconHeight();
				
				StyleConstants.setAlignment(right,StyleConstants.ALIGN_RIGHT);
				doc.setParagraphAttributes(doc.getLength(), 1, right, false);
				
				try {
					if (width > 200 || height > 200) {
						if (width > height) { // ���� ����
							ratio = (double) height / width;
							width = 200;
							height = (int) (width * ratio);
						} else { // ���� ����
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
				//obcm.setImg(img);
				//SendObject(obcm);
			}
		}
	}

	ImageIcon icon2 = new ImageIcon("img/icon2.jpg");

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// ������ �̵�
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// ȭ�鿡 ���
	public void AppendText(String msg) {
//		textArea.append(msg + "\n");
		AppendIcon(icon2);
		msg = msg.trim(); // �յ� blank�� \n�� �����Ѵ�
		StyleConstants.setAlignment(left,StyleConstants.ALIGN_LEFT);
		try {
			doc.setParagraphAttributes(doc.getLength(), 1, left, false);
			doc.insertString(doc.getLength(), msg + "\n", left);
		}
		catch(Exception ex) {}
		// ������ �̵�
//		int len = textArea.getDocument().getLength();
//		textArea.setCaretPosition(len);
//		textArea.replaceSelection(msg + "\n");
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
		
		// Image�� �ʹ� ũ�� �ִ� ���� �Ǵ� ���� 200 �������� ��ҽ�Ų��.
		try{
			if (width > 200 || height > 200) {
				if (width > height) { // ���� ����
					ratio = (double) height / width;
					width = 200;
					height = (int) (width * ratio);
				} else { // ���� ����
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

	// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
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

	// Server���� network���� ����
	public void SendMessage(String msg) {
//		try {
//			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
//			oos.writeObject(obcm);
//		} catch (IOException e) {
//			AppendText("oos.writeObject() error");
//			try {
//				ois.close();
//				oos.close();
//				socket.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//				System.exit(0);
//			}
//		}
	}

	public void SendObject(Object ob) { // ������ �޼����� ������ �޼ҵ�
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("�޼��� �۽� ����!!\n");
			AppendText("SendObject Error");
		}
	}
	
	// �̸�Ƽ�� â 
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
				// �̸�Ƽ���� Ŭ���ϸ� �̸�Ƽ�� ���� ���� 
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
