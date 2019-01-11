package tcp7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client2 {
	
	//�е���� ���
	String host = "70.12.244.144";
	int port = 8888;
	
	Scanner sc;
	Socket socket;
	
	boolean flag = true; //������ ���� ���� ��û 
	Sender sender;
	
	public Client2() throws IOException {
		while(flag) {
			try {
				socket = new Socket(host, port);
				if(socket.isConnected()) {
					System.out.println("connected!");
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
	
	public void start() throws IOException {
		System.out.println("Start Client");
		Random ran = new Random();
		while(flag) {
			
			//������ ���(00,01,02)��  ������(ex 000100, 011500, 020088)�� �Բ� �����ϰ� ���� ������ ����
			// 0.5�ʿ� �ѹ���
			String rannum =  "0"+ ran.nextInt(3);
			String msg = null;
//			System.out.println("rannum : " +rannum);
			
			if(rannum.equals("00")) {
				msg = rannum + String.format("%04d", ran.nextInt(200));
//				System.out.println(msg);
			}else if(rannum.equals("01")) {
				msg = rannum + String.format("%04d", ran.nextInt(9999));
//				System.out.println(msg);
			}else if(rannum.equals("02")) {
				msg = rannum + String.format("%04d", ran.nextInt(100));
//				System.out.println(msg);
			}
			
//			if(msg.equals("q")) {
//				break;
//			}
			//Ready to Send
			sender = new Sender(socket);
			sender.setMsg(msg);
			sender.start();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
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
				} catch (IOException e) {
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
			new Client2().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
