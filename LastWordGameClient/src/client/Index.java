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

//		String server = "10.27.24.152"; 숭파이
//		String server = "192.168.43.234"; 주영준
		String server = "localhost"; //server ip로 고쳐주세요
		int port = 9999; //server port로 고쳐주세요
		
		try {
			System.setProperty("javax.net.ssl.trustStore","C:\\Program Files\\Java\\jre1.8.0_161\\lib\\security\\cacerts"); //cacert 위치 적어주세요
			System.setProperty("javax.net.ssl.trustStorePassword","changeit"); //비밀번호 적어주세요
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
	            if(input.getType() == 1) { //맨 처음 서버와 컨택했을 때 넘어오는 데이터, 이를 통해 사용자 id 받음
	                name = (String)input.getContent(); //client id
	                board = new WaitingRoom(name);
	                event = new EventListener(board, writer);      
	    			board.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	                board.addWindowListener(new WindowAdapter() {
	        			@Override
	        			public void windowClosing(WindowEvent e) {
	        				Communication output = new Communication(4, "65535"); //매우 중요
	        				try {
	        					writer.writeObject(output);
	        					writer.flush();
	        				} catch (IOException e1) {
	        					e1.printStackTrace();
	        				}
	        				System.out.println("사용자 waitroom 종료 send! , type : 4");
	        			}
	        		});
	            }
	            else if(input.getType() == 2) { 
	            	
	            }else if(input.getType() == 3) { //broad cast 처리용  

	            }else if(input.getType() == 4) {
	            	System.exit(0);
	            }else if(input.getType() == 5) { //방 만들기
	    			readyRoom = board.makeRoom("시작하기");		
	    			readyRoom.getButton().addActionListener(event); //시작하기버튼 이벤트에 링크
	    			readyRoom.setHost(name);	   
	    			readyRoom.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    			readyRoom.addWindowListener(new WindowAdapter() {
	        			@Override
	        			public void windowClosing(WindowEvent e) {
	        				Communication output = new Communication(7, "65535"); //매우 중요
	        				try {
	        					writer.writeObject(output);
	        					writer.flush();
	        				} catch (IOException e1) {
	        					e1.printStackTrace();
	        				}
	        				System.out.println("사용자 readyroom 종료 send! , type : 7");

	        			}
	        		});
	            }else if(input.getType() == 6) { //입장하기
					readyRoom = board.makeRoom("준비하기");		
					readyRoom.getButton().addActionListener(event); //준비하기 버튼 링크
					readyRoom.setHost((String)input.getContent()); //input으로 부터 host명을 받아와서 세팅해준다
	    			readyRoom.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    			readyRoom.addWindowListener(new WindowAdapter() {
	        			@Override
	        			public void windowClosing(WindowEvent e) {
	        				Communication output = new Communication(7, "65535"); //매우 중요
	        				try {
	        					writer.writeObject(output);
	        					writer.flush();
	        				} catch (IOException e1) {
	        					e1.printStackTrace();
	        				}
	        				System.out.println("사용자 readyroom 종료 send! , type : 7");
	        			}
	        		});
	            }else if(input.getType() == 7) { //방 나오기
    				readyRoom.hideReadRoom();
    				board.showWait();	            	
	            }else if(input.getType() == 8) { //ready
	            	
	            }else if(input.getType() == 9) { // 게임시작
	            	String flag = (String)input.getContent();
	            	System.out.println("can we start? : "+flag);
	            	if(flag.equals("NO")) {
	            		JOptionPane.showMessageDialog(null, "참가자의 준비가 필요합니다.");
	            	}else {
	            		gameRoom = readyRoom.goGame(name);
	            		gameRoom.setHost(readyRoom.getHost());
	            		gameRoom.getButton().addActionListener(event);
	            		event.setTextFeild(gameRoom.getText());
	            		//board.minusRoomCnt();
	            	}
	            }else if(input.getType() == 10) { //누구차례 받자마자 count down
	            	String turn = ((Turn)input.getContent()).getCurId();//현재 누구차례인지 id
	            	String startText = ((Turn)input.getContent()).getPrevString();
	            	if(turn.equals(name)) {
	            		gameRoom.showTextFeild(true);
	            	}else {
	            		gameRoom.showTextFeild(false);
	            	}
	            	gameRoom.setStartText(startText);
            		gameRoom.time();
	            }else if(input.getType() == 11) { // timeout 누가 졌는가 이름만
	            	String loser = (String)input.getContent();
	            	JOptionPane.showMessageDialog(null, loser+"이(가) 졌습니다.");
	            	gameRoom.hideGameRoom();
	            	board.showWait();
	            	//waiting room으로 빠꾸되는 코드 준비 필요
	            }else if(input.getType() == 12) { // no 잘못된 말 보냄 - 입력텍스트
	            	String check = (String)input.getContent();	
	            	if(check.equals("NO")) {
	            		JOptionPane.showMessageDialog(null, "입력이 올바르지 않습니다.");
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
		int curRoomCnt = roomList.size(); //rmi에 갱신되어있는 room 수
		int setRoomCnt = board.getRoomCnt(); //현재 ui에 갱신되어있는 room 수

		if( setRoomCnt < curRoomCnt) { //추가 생성 방이 있을 경우

			for(int i = setRoomCnt ; i < curRoomCnt ; i++) {
				board.setRoom(roomList.get(i).getHostName()).addActionListener(event);//입장하기,시작하기 버튼 리스너 걸기 , waitRoom에 방 입장 ui 생성
			}
		}else if(setRoomCnt > curRoomCnt){

			board.removeRoom();
			for(int i = 0 ; i < curRoomCnt ; i++) {
				board.setRoom(roomList.get(i).getHostName()).addActionListener(event);//입장하기,시작하기 버튼 리스너 걸기 , waitRoom에 방 입장 ui 생성
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
