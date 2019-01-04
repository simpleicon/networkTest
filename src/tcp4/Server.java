package tcp4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket serverSocket;
	boolean flag = true;
	
	public Server() throws IOException{
		serverSocket = new ServerSocket(9999);
	}
	
	public void start() throws IOException{
		while(flag) {
			System.out.println("Server Ready");
			Socket socket = serverSocket.accept();
			new Sender(socket).start();
			
		}
	}
	
	class Sender extends Thread{ // Ŭ���̾�Ʈ ����, (��û�� ���Ӱ� �����ȴ�)
		// 1. Client�� ���ϸ��� ��û
		// 2. Client���� Ư�� ���ڸ� ����
		
		Socket socket;
		
		InputStream is;
		DataInputStream dis;
		
		OutputStream os;
		DataOutputStream dos;

		String client;
		
		public Sender(Socket socket) throws IOException {
			this.socket = socket;
			client = socket.getInetAddress().getHostAddress();
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			
		}

		@Override
		public void run() {
			try {
				String filename = dis.readUTF();
				System.out.println(client + ": "+filename);
				dos.writeUTF("HI. It's Server Msg");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(dos != null) {
						dos.close();
					}
					if(dis != null) {
						dis.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
