import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import Domain.Communication;

public class PlayerList {
	private HashMap<String, Integer> playerReady;
	private HashMap<String, ObjectOutputStream> playerWriter;
	private HashMap<String, GameThread> playerGame;
	private HashMap<String, String> playerJoinRoom;
	public static PlayerList instance;
	
	private PlayerList() {
		playerReady = new HashMap<>();
		playerWriter = new HashMap<>();
		playerGame = new HashMap<>();
		playerJoinRoom = new HashMap<>();
	}
	
	public static PlayerList getInstance() {
		if(instance == null)
			instance = new PlayerList();
		return instance;
	}
	
	public void insertPlayer(String name, ObjectOutputStream stream) {
		synchronized(this) {
			playerReady.put(name, 0);
			playerWriter.put(name, stream);
			playerGame.put(name, null);
			playerJoinRoom.put(name, "0");
		}
	}
	
	public void readyPress(String name, Integer value) {
		synchronized(this) {
			playerReady.put(name, value);
		}
	}
	
	public Integer getPlayerReadyState(String name) {
		return playerReady.get(name);
	}

	public void writeSocket(String id, Communication output) throws IOException {
		synchronized(playerWriter.get(id)) {
			playerWriter.get(id).reset();
			playerWriter.get(id).writeObject(output);
			playerWriter.get(id).flush();
		}
	}
	
	public void broadCastSocket(Communication output) throws IOException {
		Iterator<String> keys = playerWriter.keySet().iterator();
		
		while(keys.hasNext()) {
			String key = keys.next();
			
			synchronized(playerWriter.get(key)) {
				playerWriter.get(key).reset();
				playerWriter.get(key).writeObject(output);
				playerWriter.get(key).flush();
			}
		}
	}
	
	public void closeSocket(String id) {
		if(playerWriter.get(id) != null) {
			synchronized(this) {
				playerWriter.remove(id);
			}
		}
		
		if(playerReady.get(id) != null) {
			synchronized(this) {
				playerReady.remove(id);
			}
		}
		
		if(playerGame.get(id) != null) {
			synchronized(this) {
				playerGame.remove(id);
			}
		}
		
		if(playerJoinRoom.get(id) != null) {
			synchronized(this) {
				playerJoinRoom.remove(id);
			}
		}
	}
	
	public void changePlayerGame(String name, GameThread game) {
		synchronized(this) {
			playerGame.replace(name, game);
		}
	}
	
	public GameThread getPlayerGame(String name) {
		return playerGame.get(name);
	}
	
	public void joinRoom(String name, String roomHost) {
		synchronized(this) {
			playerJoinRoom.put(name, roomHost);
		}
	}
	
	public String getJoinRoom(String name) {
		return playerJoinRoom.get(name);
	}
}
