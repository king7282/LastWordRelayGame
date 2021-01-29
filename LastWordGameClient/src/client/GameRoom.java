package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GameRoom extends JFrame{
	private Container c;
	private JButton button;
	private JTextField text;
	private String ID;
	private String hID;
	private JLabel timeLabel;
	private JLabel startText;
	private JLabel myID;
	private Timer timer;
	
	public GameRoom(String ID) {
		this.ID = ID;
		setBounds(400,100,1200,900);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		c = getContentPane();
		c.setBackground(Color.green);
		c.setLayout(null);
		
		button = new JButton("º¸³»±â");
		button.setSize(200,50);
		button.setLocation(950,780);		
		
		text = new JTextField(30);
		text.setSize(200, 50);
		text.setLocation(450, 780);
		
		startText = new JLabel("");
		startText.setFont (startText.getFont ().deriveFont (45.0f));
		startText.setSize(200, 50);
		startText.setLocation(580,100);
		
		myID = new JLabel(ID);
		myID.setFont (startText.getFont ().deriveFont (75.0f));
		myID.setSize(200, 50);
		myID.setLocation(480,380);
		
		c.add(myID);
		c.add(startText);
		c.add(text);
		c.add(button);
		setVisible(true);
	}	
	public void timeClean() {
		c.remove(timeLabel);
		timer.cancel();
		
	}
	public void time() {	
		if(timeLabel != null) {
			timeClean();
		}
		timeLabel = new JLabel("31");
		timeLabel.setFont (timeLabel.getFont ().deriveFont (30.0f));
		timeLabel.setSize(200, 50);
		timeLabel.setLocation(150, 780);
		c.add(timeLabel);
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {			
			@Override
			public void run() {
				int cnt = Integer.parseInt(timeLabel.getText());
				if(cnt == 0) {		
					c.remove(timeLabel);
					timer.cancel();					
				}
				cnt--;
				timeLabel.setText(Integer.toString(cnt));				
			}
		}, 0, 1000);
		this.update(getGraphics());
	}
	
	public void removeAll() {
		Component[] list = this.getComponents();
		for(int i = 0 ; i < list.length ; i++) {
			if(list[i] instanceof JLabel) {
				this.remove(list[i]);
			}
		}
	}
	
	public void setPlayer(String id , int cnt) {
		JLabel label = new JLabel(ID);
		label.setFont (label.getFont ().deriveFont (64.0f));
		label.setSize(200,100);
		label.setLocation(100 + (cnt*200), 200);				
		c.add(label);
		this.update(getGraphics());
	}
	
	public void showTextFeild(boolean s) {
		text.setVisible(s);
		button.setVisible(s);
		this.update(getGraphics());
	}
	
	public void setHost(String id) {
		this.hID = id;
	}
	
	public String getHost() {
		return this.hID;
	}
	
	public JButton getButton() {
		return this.button;
	}
	
	public JTextField getText() {
		return this.text;
	}
	
	public void setStartText(String text) {
		this.startText.setText(text);
	}
	
	public void hideGameRoom() {
		this.setVisible(false);
	}
}
 