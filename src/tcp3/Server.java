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
	
	class Sender extends Thread{ //요청한 클라이언트에게 데이터를 뿌리는 역할.  
		Socket socket;
		OutputStream os;
		OutputStreamWriter osw;
		
		public Sender(Socket socket) throws IOException{ //thread내에서 오류처리를 하면 호출한쪽에서는 오류가 난지 모름
			this.socket = socket;
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
		}
		
		@Override
		public void run() {
			try {
				osw.write("안녕하세요 ㅐㅐㅐ서버입니다.");
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
