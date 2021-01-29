package Domain;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RoomListImpl extends UnicastRemoteObject implements RoomList, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static RoomList instance;
	private HashMap<String, Room> roomList;
	
	private RoomListImpl() throws RemoteException{
		roomList = new HashMap<String, Room>();
	}
	
	public static RoomList getInstance() throws RemoteException {
		if(instance == null)
			instance = new RoomListImpl();
		return instance;
	}
	
	public void insertRoom(String id, RoomImpl room) throws RemoteException{
		roomList.put(id, room);
	}
	
	public Room getRoom(String id) throws RemoteException {
		return roomList.get(id);
	}
	
	public ArrayList<Room> getRoomList() throws RemoteException{
		ArrayList<Room> result = new ArrayList<>();
		
		Iterator<String> keys = roomList.keySet().iterator();
		
		while(keys.hasNext()) {
			result.add(roomList.get(keys.next()));
		}
		
		return result;
	}
	
	public void deleteRoom(String id) {
		if(roomList.get(id) != null) {
			synchronized(this) {
				roomList.remove(id);
			}
		}
	}
}
