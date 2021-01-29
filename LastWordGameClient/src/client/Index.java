package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Domain.Communication;
import Domain.Room;
import Domain.RoomList;
import Domain.Turn;
 
public class Index {
	private String name;
	
	private WaitingRoom board;
    private ReadyRoom readyRoom;
    private GameRoom gameRoom;
    private EventListener event;
    private ArrayList<Room> roomList;
    
	public static void main(String[] args) {	
		Index index = new Index();
		index.client();
	}
	public Index() {
		this.name = null;
		this.board = null;
		this.readyRoom = null;
		this.gameRoom = null;
		this.event = null;
	}
	
	private void client() {		
		SSLSocketFactory f = null;
		SSLSocket c = null;

//		String server = "10.27.24.152"; ������
//		String server = "192.168.43.234"; �ֿ���
		String server = "localhost"; //server ip�� �����ּ���
		int port = 9999; //server port�� �����ּ���
		
		try {
			System.setProperty("javax.net.ssl.trustStore","C:\\Program Files\\Java\\jre1.8.0_161\\lib\\security\\cacerts"); //cacert ��ġ �����ּ���
			System.setProperty("javax.net.ssl.trustStorePassword","changeit"); //��й�ȣ �����ּ���
			f = (SSLSocketFactory) SSLSocketFactory.getDefault();
			c = (SSLSocket) f.createSocket(server, port);
			String[] supported = c.getSupportedCipherSuites();
			c.setEnabledCipherSuites(supported);

			c.startHandshake();			

			
			ObjectOutputStream writer = new ObjectOutputStream(c.getOutputStream());
	        ObjectInputStream reader = new ObjectInputStream(c.getInputStream());

	        
	        Communication input = null;   
	        
	  	        
	         while(true) {
	            writer.reset();
	            input = (Communication) reader.readObject();
	            System.out.println("input type : " + input.getType());
	            if(input.getType() == 1) { //�� ó�� ������ �������� �� �Ѿ���� ������, �̸� ���� ����� id ����
	                name = (String)input.getContent(); //client id
	                board = new WaitingRoom(name);
	                event = new EventListener(board, writer);      
	    			board.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	                board.addWindowListener(new WindowAdapter() {
	        			@Override
	        			public void windowClosing(WindowEvent e) {
	        				Communication output = new Communication(4, "65535"); //�ſ� �߿�
	        				try {
	        					writer.writeObject(output);
	        					writer.flush();
	        				} catch (IOException e1) {
	        					e1.printStackTrace();
	        				}
	        				System.out.println("����� waitroom ���� send! , type : 4");
	        			}
	        		});
	            }
	            else if(input.getType() == 2) { 
	            	
	            }else if(input.getType() == 3) { //broad cast ó����  

	            }else if(input.getType() == 4) {
	            	System.exit(0);
	            }else if(input.getType() == 5) { //�� �����
	    			readyRoom = board.makeRoom("�����ϱ�");		
	    			readyRoom.getButton().addActionListener(event); //�����ϱ��ư �̺�Ʈ�� ��ũ
	    			readyRoom.setHost(name);	   
	    			readyRoom.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    			readyRoom.addWindowListener(new WindowAdapter() {
	        			@Override
	        			public void windowClosing(WindowEvent e) {
	        				Communication output = new Communication(7, "65535"); //�ſ� �߿�
	        				try {
	        					writer.writeObject(output);
	        					writer.flush();
	        				} catch (IOException e1) {
	        					e1.printStackTrace();
	        				}
	        				System.out.println("����� readyroom ���� send! , type : 7");

	        			}
	        		});
	            }else if(input.getType() == 6) { //�����ϱ�
					readyRoom = board.makeRoom("�غ��ϱ�");		
					readyRoom.getButton().addActionListener(event); //�غ��ϱ� ��ư ��ũ
					readyRoom.setHost((String)input.getContent()); //input���� ���� host���� �޾ƿͼ� �������ش�
	    			readyRoom.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    			readyRoom.addWindowListener(new WindowAdapter() {
	        			@Override
	        			public void windowClosing(WindowEvent e) {
	        				Communication output = new Communication(7, "65535"); //�ſ� �߿�
	        				try {
	        					writer.writeObject(output);
	        					writer.flush();
	        				} catch (IOException e1) {
	        					e1.printStackTrace();
	        				}
	        				System.out.println("����� readyroom ���� send! , type : 7");
	        			}
	        		});
	            }else if(input.getType() == 7) { //�� ������
    				readyRoom.hideReadRoom();
    				board.showWait();	            	
	            }else if(input.getType() == 8) { //ready
	            	
	            }else if(input.getType() == 9) { // ���ӽ���
	            	String flag = (String)input.getContent();
	            	System.out.println("can we start? : "+flag);
	            	if(flag.equals("NO")) {
	            		JOptionPane.showMessageDialog(null, "�������� �غ� �ʿ��մϴ�.");
	            	}else {
	            		gameRoom = readyRoom.goGame(name);
	            		gameRoom.setHost(readyRoom.getHost());
	            		gameRoom.getButton().addActionListener(event);
	            		event.setTextFeild(gameRoom.getText());
	            		//board.minusRoomCnt();
	            	}
	            }else if(input.getType() == 10) { //�������� ���ڸ��� count down
	            	String turn = ((Turn)input.getContent()).getCurId();//���� ������������ id
	            	String startText = ((Turn)input.getContent()).getPrevString();
	            	if(turn.equals(name)) {
	            		gameRoom.showTextFeild(true);
	            	}else {
	            		gameRoom.showTextFeild(false);
	            	}
	            	gameRoom.setStartText(startText);
            		gameRoom.time();
	            }else if(input.getType() == 11) { // timeout ���� ���°� �̸���
	            	String loser = (String)input.getContent();
	            	JOptionPane.showMessageDialog(null, loser+"��(��) �����ϴ�.");
	            	gameRoom.hideGameRoom();
	            	board.showWait();
	            	//waiting room���� ���ٵǴ� �ڵ� �غ� �ʿ�
	            }else if(input.getType() == 12) { // no �߸��� �� ���� - �Է��ؽ�Ʈ
	            	String check = (String)input.getContent();	
	            	if(check.equals("NO")) {
	            		JOptionPane.showMessageDialog(null, "�Է��� �ùٸ��� �ʽ��ϴ�.");
	            	}else {

	            	}
	            }
	            
	            
	            roomList = ((RoomList)Naming.lookup("rmi://"+server+"/roomList")).getRoomList();
	            updateGUI();
	            if(port == 1) break;
	         }
			
			writer.close();
			reader.close();
			c.close();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();			
		} catch ( RemoteException e) {
			e.printStackTrace();
		} catch ( NotBoundException e) {
			e.printStackTrace();
		} catch (IOException err) {			
			err.printStackTrace();
		} 
	}
	
	private void updateGUI() throws RemoteException {
		int curRoomCnt = roomList.size(); //rmi�� ���ŵǾ��ִ� room ��
		int setRoomCnt = board.getRoomCnt(); //���� ui�� ���ŵǾ��ִ� room ��

		if( setRoomCnt < curRoomCnt) { //�߰� ���� ���� ���� ���

			for(int i = setRoomCnt ; i < curRoomCnt ; i++) {
				board.setRoom(roomList.get(i).getHostName()).addActionListener(event);//�����ϱ�,�����ϱ� ��ư ������ �ɱ� , waitRoom�� �� ���� ui ����
			}
		}else if(setRoomCnt > curRoomCnt){

			board.removeRoom();
			for(int i = 0 ; i < curRoomCnt ; i++) {
				board.setRoom(roomList.get(i).getHostName()).addActionListener(event);//�����ϱ�,�����ϱ� ��ư ������ �ɱ� , waitRoom�� �� ���� ui ����
			}
			System.out.println("setting waiting room complete");
		}
		
		if(readyRoom == null) return;
		for(int i = 0 ; i < roomList.size() ; i++) {
			ArrayList<String> participants = roomList.get(i).getParticipant();
			String rID = readyRoom.getHost();		
			if(roomList.get(i).getHostName().equals(rID)) {
				readyRoom.removeAll();
				for(int j = 0 ; j < participants.size() ; j++) {					
					readyRoom.setPlayer(participants.get(j), j);					
				}
			}
		}
		
	}
	
}
