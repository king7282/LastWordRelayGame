package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ReadyRoom extends JFrame{
	private Container c;
	private JButton button;
	private String host = null;
	
	public ReadyRoom(String buttonText) {
		setBounds(400,100,1200,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c = getContentPane();
		c.setBackground(Color.red);
		c.setLayout(null);	    
		
		button = new JButton(buttonText);
		button.setSize(200,50);
		button.setLocation(950,780);		
		
		c.add(button);		
		System.out.println("ready room");
		
		
		setVisible(true);
	}
	public GameRoom goGame(String ID) {
		GameRoom game = new GameRoom(ID);
		this.setVisible(false);
		return game;
	}	
	
	public JButton getButton() {
		return this.button;
	}
	
	public void removeAll() {
		Component[] list = c.getComponents();
		for(int i = 0 ; i < list.length ; i++) {
			if(list[i] instanceof JLabel) {
				c.remove(list[i]);
			}
		}
	}
	
	public void setPlayer(String ID, int cnt) {
		JLabel label = new JLabel(ID);
		label.setFont (label.getFont ().deriveFont (64.0f));
		label.setSize(200,100);
		label.setLocation(100 + (cnt*200), 200);
		c.add(label);
		this.update(getGraphics());
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getHost() {
		return this.host;
	}
	
	public void hideReadRoom() {
		this.setVisible(false);
	}
}
