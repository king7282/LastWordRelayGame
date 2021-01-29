package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JTextField;

import Domain.Communication;

public class EventListener implements ActionListener {
	private ObjectOutputStream outputStream;
	private WaitingRoom waitRoom;
	private String ID;
	private JTextField textFeild;
	
	public EventListener(WaitingRoom room, ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
		this.waitRoom = room;		
		waitRoom.getButton().addActionListener(this);		
		this.ID = waitRoom.getID(); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("방만들기")) {			
			Communication output = new Communication(5, ID);
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("방 요청 send! , type : 5");			
		}
		
		if(e.getActionCommand().equals("시작하기")) {
			Communication output = new Communication(9,"256");//매우 중요
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}		
			
			System.out.println("방장 시작 send! , type : 9");	
		}	
		
		if(e.getActionCommand().equals("준비하기")) {
			Communication output = new Communication(8,"255");//매우 중요
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
			System.out.println("참가자 준비 send! , type : 8");
		}
		
		if(e.getActionCommand().equals("입장하기")) {			
			String rID = ((JButton) e.getSource()).getToolTipText();
			Communication output = new Communication(6, rID);
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}		
			System.out.println("방 입장하기 send! , type : 6");
			
		}
		
		if(e.getActionCommand().equals("보내기")) {
			String text = textFeild.getText();
			Communication output = new Communication(12,text);//매우 중요
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
			System.out.println("text send! , type : 12 , content : "+text);
		}
	}
	
	public void setTextFeild(JTextField text) {
		this.textFeild = text;
	}
}
