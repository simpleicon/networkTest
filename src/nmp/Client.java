package nmp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	String host = "70.12.243.62";
	int port = 8888;
	
	Scanner sc;
	Socket socket;
	
	boolean flag = true; //������ ���� ���� ��û 
	Sender sender;
	
	int battery = 33;
	int status;
	int cargoload;
	
	public Client() throws IOException {
		while(flag) {
			try {
				socket = new Socket(host, port);
				if(socket.isConnected()) {
					break; // ������ ���� ����Ǹ� ���� ���� 
				}
			} catch (Exception e) {
				System.out.println("retry..");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} 
		} // end of while
		
		//Ready to receive
		new Receiver(socket).start();
	
				
	}
	
	public void chargeBattery() throws InterruptedException, IOException {
		
		while(battery >= 100) {
			battery++;
			Thread.sleep(1000);
			sender = new Sender(socket);
			sender.setMsg(battery+"");
			sender.start();
		}
	}
	
	public void start() throws IOException {
		System.out.println("Start Client");
		sc = new Scanner(System.in);
		while(flag) {
			System.out.println("Input Msg: 000100, 011500, 020088");
			String msg = sc.nextLine();
			
			
			
			
			if(msg.equals("q")) {
				break;
			}
			//Ready to Send
			sender = new Sender(socket);
			sender.setMsg(msg);
			sender.start();
		}
		try {
			Thread.sleep(1000);
			sc.close();
			if(socket != null) {
				socket.close();
			}
			System.out.println("Client End");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	class Sender extends Thread{
		OutputStream os;
		DataOutputStream dos;
		
		String msg;
		
		public Sender() {
			
		}
		public Sender(Socket socket) throws IOException {
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		}
		
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
		@Override
		public void run() {
			if(dos != null) {
				try {
					dos.writeUTF(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	class Receiver extends Thread{
		InputStream is;
		DataInputStream dis;
		
		public Receiver() {
		}
		
		public Receiver(Socket socket) throws IOException {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			
		}
		@Override
		public void run() {
			while(dis != null) {
				String msg;
				try {
					msg = dis.readUTF();
					System.out.println("Server : " + msg);
					if(msg == "1") {
						chargeBattery();
					}
				} catch (IOException | InterruptedException e) {
					System.out.println("DisConnected");
					break;
				}
			}// end of while
			try {
				Thread.sleep(1000);// ������ ���¿��� �ٷ� ���� ������ �������ɼ�
				if(dis != null) {
					dis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
	}
	
	public static void main(String[] args) {
		try {
			new Client().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
