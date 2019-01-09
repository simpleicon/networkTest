package tcp6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	ServerSocket serversocket;
	int port = 8888;
	
	boolean flag = true;
	
	ArrayList<DataOutputStream> list; //Ŭ���̾�Ʈ ���ӽø��� ���ϻ��� > �ش� ������ ��Ʈ���� ��� ����α� ���ؼ�
	String client;
	
	public Server() throws IOException {
		list = new ArrayList<>();
		serversocket = new ServerSocket(port);
		
	}
	
	public void start() throws IOException {
		while(flag) {
			System.out.println("Ready Server");
			Socket socket = serversocket.accept(); //Ŭ���̾�Ʈ���� ������ �����Ǿ�� �ϱ� ������ ���������� �̿��ϸ� �ȵ�
			client = socket.getInetAddress().getHostAddress();
			System.out.println(client);
			
			new Receiver(socket, client).start();
			
		}
	}
	
	public void sendMsg(String msg) {
		// BroadCast msg
		Sender sender = new Sender();
		sender.setMsg(msg);
		sender.start();
		
	}
	
	class Receiver extends Thread{
		
		InputStream is;
		DataInputStream dis;
		String client;
		
		// For Sender
		OutputStream os;
		DataOutputStream dos;
		
		public Receiver(Socket socket, String client) throws IOException {
			this.client = client;
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			list.add(dos);
			System.out.println("connected cnt : " + list.size());
		}
		
		@Override
		public void run() {
			while(dis != null) {
				try {
					String msg = dis.readUTF();
					System.out.println(client + " : " + msg);
					sendMsg(msg);
				} catch (IOException e) {
//					e.printStackTrace();
					break;
				}
			}// end of while
			try {
				list.remove(dos);
				System.out.println("connected cnt : " + list.size());
				Thread.sleep(1000);
				if(dis != null) {
					dis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

	}
	
	class Sender extends Thread{
		
		String msg;
		
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
		@Override
		public void run() {
			if(list.size() == 0) {
				return;
			}
			for(DataOutputStream out : list) {
				if(out != null) {
					try {
						out.writeUTF(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public static void main(String[] args) {
		try {
			new Server().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
