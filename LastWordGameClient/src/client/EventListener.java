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
		if(e.getActionCommand().equals("�游���")) {			
			Communication output = new Communication(5, ID);
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("�� ��û send! , type : 5");			
		}
		
		if(e.getActionCommand().equals("�����ϱ�")) {
			Communication output = new Communication(9,"256");//�ſ� �߿�
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}		
			
			System.out.println("���� ���� send! , type : 9");	
		}	
		
		if(e.getActionCommand().equals("�غ��ϱ�")) {
			Communication output = new Communication(8,"255");//�ſ� �߿�
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
			System.out.println("������ �غ� send! , type : 8");
		}
		
		if(e.getActionCommand().equals("�����ϱ�")) {			
			String rID = ((JButton) e.getSource()).getToolTipText();
			Communication output = new Communication(6, rID);
			try {
				outputStream.writeObject(output);
				outputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}		
			System.out.println("�� �����ϱ� send! , type : 6");
			
		}
		
		if(e.getActionCommand().equals("������")) {
			String text = textFeild.getText();
			Communication output = new Communication(12,text);//�ſ� �߿�
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
