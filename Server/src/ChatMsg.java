// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private int room_id;
	private String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image
	private String data;
	//private Vector userlist = new Vector();
	private String userlist;
	public ImageIcon img;

	public ChatMsg(String id, String code, String msg) {
		this.id = id;
		this.code = code;
		this.data = msg;
	}
	public ChatMsg(int room_id, String code, String userlist) {
		this.room_id = room_id;
		this.code = code;
		this.userlist = userlist;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public int getRoomId() {
		return room_id;
	}

	public void setRoomId(int room_id) {
		this.room_id = room_id;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public String getUserlist() {
		return userlist;
	}

	public void setUserlist(String userlist) {
		this.userlist = userlist;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}