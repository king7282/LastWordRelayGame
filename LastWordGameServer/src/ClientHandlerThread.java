import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import Domain.Communication;
import Domain.Room;
import Domain.RoomImpl;
import Domain.RoomListImpl;

public class ClientHandlerThread extends Thread{
	private SSLSocket client;
	private ObjectInputStream reader = null;
	private String id;
	
	public ClientHandlerThread(SSLSocket client, String id) throws IOException {
		this.client = client;
		this.id = id;
		PlayerList.getInstance().insertPlayer(id, new ObjectOutputStream(client.getOutputStream()));
	}
	
	@Override
	public void run() {
		try {
			Communication output = new Communication(1, id);
			Communication input = null;
			
			PlayerList playerList = PlayerList.getInstance();
			RoomListImpl roomListImpl = (RoomListImpl)RoomListImpl.getInstance();
			
			playerList.writeSocket(id, output);
			
			reader = new ObjectInputStream(client.getInputStream());

			while(true) {
				input = (Communication)reader.readObject();
				System.out.println("id : " + id);
				System.out.println("input Type : " + input.getType());
				System.out.println("input Content : " + (String)input.getContent());
				System.out.println("-----");
				
				if(input.getType() == 4) { // 끝내기
					RoomImpl room = (RoomImpl)roomListImpl.getRoom(playerList.getJoinRoom(id));
					if(room == null) {
						output.setType(4);
						playerList.writeSocket(id, output);
						
						break;
					}
					room.deleteParticipant(id);
					ArrayList<String> ids = room.getParticipant();
					
					if(room.getHostName() == id) {
						output.setType(7);
						output.setContent("OK");
						for(int i = 0; i < ids.size(); i++) {
							playerList.joinRoom(ids.get(i), "0");
							playerList.writeSocket(ids.get(i), output);
						}
						
						roomListImpl.deleteRoom(id);
						
						output.setType(3);
						playerList.broadCastSocket(output);
					}
					else {
						output.setType(3);
						output.setContent("OK");
						for(int i = 0; i < ids.size(); i++) {
							playerList.writeSocket(ids.get(i), output);
						}
					}
					
					output.setType(4);
					playerList.writeSocket(id, output);
					break;
				}
				else if(input.getType() == 5) { // 방 만들기
					roomListImpl.insertRoom(id, new RoomImpl(id, (String)input.getContent()));
					playerList.joinRoom(id, id);
					playerList.readyPress(id, 0);
					
					output.setType(5);
					output.setContent("OK");

					playerList.writeSocket(id, output);
					
					output.setType(3);
					playerList.broadCastSocket(output);
				}
				else if(input.getType() == 6) { // 방 접속
					RoomImpl room = (RoomImpl)roomListImpl.getRoom((String)input.getContent());
					playerList.joinRoom(id, room.getHostName());
					
					room.addParticipant(id);
					playerList.readyPress(id, 0);
					
					output.setType(6);
					output.setContent(room.getHostName());
					
					playerList.writeSocket(id, output);
					
					output.setType(3);
					for(int j = 0; j < room.getParticipant().size(); j++) {
						playerList.writeSocket(room.getParticipant().get(j), output);
					}
				}
				else if(input.getType() == 7) { // 방 나오기
					RoomImpl room = (RoomImpl)roomListImpl.getRoom(playerList.getJoinRoom(id));
					room.deleteParticipant(id);
					playerList.joinRoom(id, "0");
					
					output.setType(7);
					output.setContent("OK");
					playerList.writeSocket(id, output);
					ArrayList<String> ids = room.getParticipant();
					
					if(id == room.getHostName()) {
						for(int i = 0; i < ids.size(); i++) {
							playerList.writeSocket(ids.get(i), output);
							playerList.joinRoom(ids.get(i), "0");
						}
						
						roomListImpl.deleteRoom(id);
						
						output.setType(3);
						playerList.broadCastSocket(output);
					}
					else {
						output.setType(3);
						for(int i = 0; i < ids.size(); i++) {
							playerList.writeSocket(ids.get(i), output);
						}
					}
					
				}
				else if(input.getType() == 8) { // 레디 버튼 누름
					playerList.readyPress(id, (playerList.getPlayerReadyState(id) + 1) % 2);

					output.setType(8);
					output.setContent("OK");
				}
				else if(input.getType() == 9) { // 스타트 버튼 누름
					playerList.readyPress(id, (playerList.getPlayerReadyState(id) + 1) % 2);
					
					Room room = roomListImpl.getRoom(id);
					
					ArrayList<String> ids = room.getParticipant();
					boolean flag = true;
							
					for(int j = 0; j < ids.size() && flag; j++)
						if(playerList.getPlayerReadyState(ids.get(j)) == 0)
							flag = false;
							
					if(flag) {
						output.setType(9);
						output.setContent("OK");
								
						System.out.println(output.getType());
						System.out.println((String)output.getContent());
						for(int j = 0; j < ids.size(); j++) {
							playerList.writeSocket(ids.get(j), output);
						}

						GameThread game = new GameThread(ids);
								
						for(int j = 0; j < ids.size(); j++) {
							playerList.changePlayerGame(ids.get(j), game);
							playerList.joinRoom(ids.get(j), "0");
						}
								
						game.start(); // 게임 시작
								
						roomListImpl.deleteRoom(id);
								
						output.setType(3);
						output.setContent("OK");
								
						playerList.broadCastSocket(output);
					}
					else {
						output.setType(9);
						output.setContent("NO");
						playerList.writeSocket(id, output);
					}

				}
				else if(input.getType() == 12) { // 게임 중 단어를 보내옴
					if(playerList.getPlayerGame(id) != null && playerList.getPlayerGame(id).isAlive()) {
						if(!playerList.getPlayerGame(id).getCurrentTurnAnswer((String)input.getContent())) {
							output.setType(12);
							output.setContent("NO");
							
							playerList.writeSocket(id, output);
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				PlayerList.getInstance().closeSocket(id);
				reader.close();
				client.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
