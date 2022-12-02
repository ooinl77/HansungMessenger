import java.awt.*;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class ChatRoomList extends JLabel{
	private static final long serialVersionUID = 1L;
	private ImageIcon icon;
	private String userlist;
	private String room_id;
	
	public ChatRoomList(ImageIcon icon, String userlist, String room_id) {
		this.icon = icon;
		this.userlist = userlist;
		this.room_id = room_id;
		
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BorderLayout(0,0));
		this.add(new JLabel(icon), BorderLayout.WEST);
		this.add(new JLabel(userlist), BorderLayout.CENTER);
	}

	
	public String getUserList() {
		return userlist;
	}
	
	public String getRoomID() {
		return room_id;
	}
}
