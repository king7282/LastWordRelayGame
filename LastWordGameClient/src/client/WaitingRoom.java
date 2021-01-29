package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class WaitingRoom extends JFrame{
	private int roomCount;
	private Container c;
	private JButton button;
	private String ID;
	private JLabel text;
	
	public WaitingRoom(String name) {
		ID = name;
		roomCount = 0;
		
		setBounds(400,100,1200,900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(null);	    
		
		button = new JButton("방만들기");
		button.setSize(200,50);
		button.setLocation(950,780);		
		
		
		c.add(button);
		setVisible(true);
		System.out.println("make room! , my ID is "+ID);
	}
		
	public ReadyRoom makeRoom(String buttonText) {
		ReadyRoom newRoom = new ReadyRoom(buttonText);
		this.setVisible(false);
		return newRoom;
	}
	
	public JButton setRoom(String id) { // 여기엔 최초에 서버로 부터 방의 개수와 id를 받아와 기본 대기실 세팅을 한다.	
		JPanel center = new JPanel();
		center.setBackground(Color.yellow);		
		center.setSize(1200,300);	    
	    center.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
	    center.setLocation(0,300 * roomCount);
	    
	    System.out.println(roomCount);
	    
	    JButton button = new JButton("입장하기");
	    button.setToolTipText(id);
		button.setSize(200,50);
		button.setLocation(300,80);	    
	    center.add(button);
	    
	    this.roomCount++;	    
	    this.getContentPane().add(center);
	    this.update(getGraphics());
	    return button;
	}
	
	public void removeRoom() {
		Component[] list = c.getComponents();
		for(int i = 0 ; i < list.length ; i++) {
			if(!(list[i] instanceof JButton))
				c.remove(list[i]);		
				System.out.println("wait");
		}
		roomCount = 0;
		this.update(getGraphics());
	}
	
	public JButton getButton() {
		return this.button;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public int getRoomCnt() {
		return this.roomCount;
	}
	
	public void minusRoomCnt() {
		this.roomCount--;
	}
	
	public void showWait() {
		this.setVisible(true);
		this.update(getGraphics());
	}
}


