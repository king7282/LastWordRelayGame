package Domain;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RoomList extends Remote{
   public Room getRoom(String id) throws RemoteException;
   public ArrayList<Room> getRoomList() throws RemoteException;
}