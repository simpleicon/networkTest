package tcp5;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
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
	
	class Sender extends Thread{ // 클라이언트 마다, (요청시 새롭게 생성된다)
		// 1. Client가 파일명을 요청
		// 2. 파일을 찾는다
		// 3. 
		
		Socket socket;
		
		InputStream is;
		DataInputStream dis;
		
		OutputStream os;
		DataOutputStream dos;
		BufferedOutputStream bos;
		
		FileInputStream fis;

		String client;
		
		public Sender(Socket socket) throws IOException {
			this.socket = socket;
			client = socket.getInetAddress().getHostAddress();
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			bos = new BufferedOutputStream(dos);
			
		}

		@Override
		public void run() {
			try {
				String filename = dis.readUTF();
				filename = filename.trim().toLowerCase();
				fis = new FileInputStream("src\\"+filename);
				while(true) {
					int data = fis.read();
					if(data == -1) {
						break;
					}
					bos.write(data);
				}
				System.out.println("COMPLETED");
				
				
				
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
					if(fis != null) {
						fis.close();
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
