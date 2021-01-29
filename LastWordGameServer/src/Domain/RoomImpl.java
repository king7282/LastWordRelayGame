package Domain;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RoomImpl extends UnicastRemoteObject implements Serializable, Room{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hostName, roomName;
	private ArrayList<String> participant;
	
	public RoomImpl(String hostName, String roomName) throws RemoteException{
		this.hostName = hostName;
		this.roomName = roomName;
		participant = new ArrayList<>();
		participant.add(hostName);
		
	}
	
	public String getHostName() throws RemoteException{
		return hostName;
	}
	
	public String getRoomName() throws RemoteException{
		return roomName;
	}
	
	public int getTotalNumber() throws RemoteException{
		return participant.size();
	}
	
	public void addParticipant(String name) {
		synchronized(this) {
			participant.add(name);
		}
	}
	
	public void deleteParticipant(String name) {
		for(int i = 0; i < participant.size(); i++) {
			if(participant.get(i).equals(name)) {
				synchronized(this) { 
					participant.remove(i);
				}
				break;
			}
		}
	}
	
	public ArrayList<String> getParticipant() throws RemoteException{
		return (ArrayList<String>)participant.clone();
	}
}
