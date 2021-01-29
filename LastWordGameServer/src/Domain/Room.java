package Domain;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Room extends Remote{
	public String getHostName() throws RemoteException;
	public String getRoomName() throws RemoteException;
	public int getTotalNumber() throws RemoteException;
	public ArrayList<String> getParticipant() throws RemoteException;
}
