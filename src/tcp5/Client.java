package tcp5;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	Socket socket;
	String server = "70.12.50.147";
	int port = 9999;
	boolean flag = true;
	
	OutputStream os;
	DataOutputStream dos;
	
	InputStream is;
	DataInputStream dis;
	BufferedInputStream bi;
	
	FileOutputStream fos;
	
	
	public Client() {
		
	}
	
	public void start() {
		Scanner scanner = null;
		
		while(flag) {
			System.out.println("Input Msg ...");
			scanner = new Scanner(System.in);
			String msg = scanner.nextLine();
			if(msg.equals("q")) {
				System.out.println("Client End");
				break;
			}
			
			try {
				socket = new Socket(server, port);
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);
				dos.writeUTF(msg);
				
				is = socket.getInputStream();
				dis = new DataInputStream(is);
				bi = new BufferedInputStream(dis);
				
				fos = new FileOutputStream("C:\\down\\"+msg);
				
				while(true) {
					int data = bi.read();
					if(data == -1) {
						break;
					}
					fos.write(data);
				}
				
				System.out.println("Download Completed");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(dis != null) {
						dis.close();
					}
					if(dos != null) {
						dos.close();
					}
					if(fos != null) {
						fos.close();
					}
				} catch(Exception e) {
					
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		new Client().start();
	}

}
