import java.io.FileInputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import Domain.Dictionary;
import Domain.RoomListImpl;

public class Server {
	public static void main(String[] args) {
		
		if(args.length != 1) {
			System.out.println("Usage: Classname Port");
			System.exit(1);
		}
		
		int sPort = Integer.parseInt(args[0]);
		
		String ksName = "D:\\jyj\\LastWordGameServer\\bin\\.keystore\\SSLSocketServerKey"; // serverKey ��ġ ������ SSLSocketServer!!
		char KeyStorePass[] = "wndudwns".toCharArray();
		char keyPass[] = "wndudwns".toCharArray();
		
		final KeyStore ks;
		final KeyManagerFactory kmf;
		final SSLContext sc;
		
		SSLServerSocketFactory ssf = null;
		SSLServerSocket s = null;
		SSLSocket c = null;
		
		try {
			String server = InetAddress.getLocalHost().getHostAddress(); // address �� �������� ������ üũ�غ���
			System.out.println("server IP : " + server);
			
			ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), KeyStorePass);
			
			kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, keyPass);
			
			
			sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			
			
			ssf = sc.getServerSocketFactory();
			s = (SSLServerSocket) ssf.createServerSocket(sPort);
			int count = 0;
			
			Dictionary.openDictionary("dictionary.txt"); // dictionary.txt�� �����ձ� �ܾ� �߰�
			System.setProperty("java.rmi.server.hostname", server); 
			Naming.rebind("rmi://" + server + ":1099/roomList", RoomListImpl.getInstance()); // class�ִ� ���� rmi �� �����!!
			
			while(true) {
				c = (SSLSocket) s.accept();
				System.out.println("client ���� : " + c.getInetAddress().getHostAddress() + ":" + c.getPort());
				count++;
				
				ClientHandlerThread thread = new ClientHandlerThread(c, Integer.toString(count));
				
				thread.start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
