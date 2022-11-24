// ChatMsg.java 채팅 메시지 ObjectStream 용.
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String room_id;
	private String code; // 100:로그인, 400:로그아웃, 200:채팅메시지, 300:Image
	private String data;
	private List<String> userlist = new ArrayList<String>();
	public ImageIcon img;

	public ChatMsg(String room_id, String code, String msg) {
		this.room_id = room_id;
		this.code = code;
		this.data = msg;
	}
	public ChatMsg(String room_id, String code, String msg, List<String> userlist) {
		this.room_id = room_id;
		this.code = code;
		this.data = msg;
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

	public String getRoomId() {
		return room_id;
	}

	public void setRoomId(String room_id) {
		this.room_id = room_id;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public List<String> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<String> userlist) {
		this.userlist = userlist;
	}
	
}