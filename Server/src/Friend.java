import java.awt.*;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Friend extends JLabel{
	private static final long serialVersionUID = 1L;
	private ImageIcon icon;
	private String id;
	private String statusMessage;
	
	public Friend(ImageIcon icon, String id, String statusMessage) {
		this.icon = icon;
		this.id = id;
		this.statusMessage = statusMessage;
		
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BorderLayout(0,0));
		this.add(new JLabel(icon), BorderLayout.WEST);
		this.add(new JLabel(id), BorderLayout.CENTER);
		this.add(new JLabel(statusMessage), BorderLayout.EAST);
	}

	public ImageIcon getIcon() {
		return icon;
	}
	
	public String getID() {
		return id;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}
