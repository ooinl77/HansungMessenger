// ChatMsg.java ä�� �޽��� ObjectStream ��.
import java.io.Serializable;
import javax.swing.ImageIcon;

class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String room_id;
	private String code; // 100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image
	private String data;
	//private Vector userlist = new Vector();
	private String userlist;
	public ImageIcon img;
	//�� ����
	public ChatMsg(String id, String code, String userlist) {
		this.id = id;
		this.code = code;
		this.userlist = userlist;
	}
	//�޼��� ����
	public ChatMsg(String id, String code, String room_id, String msg) {
		this.id = id;
		this.code = code;
		this.room_id = room_id;
		this.data = msg;
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