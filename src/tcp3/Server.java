package tcp3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket serverSocket;
	boolean flag = true;
	
	
	
	public Server() {
		try {
			serverSocket = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void start() {
		while(flag) {
			System.out.println("Server Ready");
			try {
				Socket socket = serverSocket.accept();
				System.out.println(socket.getInetAddress().getHostAddress());
				new Sender(socket).start(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class Sender extends Thread{ //��û�� Ŭ���̾�Ʈ���� �����͸� �Ѹ��� ����.  
		Socket socket;
		OutputStream os;
		OutputStreamWriter osw;
		
		public Sender(Socket socket) throws IOException{ //thread������ ����ó���� �ϸ� ȣ�����ʿ����� ������ ���� ��
			this.socket = socket;
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
		}
		
		@Override
		public void run() {
			try {
				osw.write("�ȳ��ϼ��� �����������Դϴ�.");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(osw!=null) {
					try {
						osw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String args[]) {
		new Server().start();
	}
}
