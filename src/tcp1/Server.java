package tcp1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		ServerSocket serversocket = null;
		Socket socket = null;
		
		
		//IO
		OutputStream os = null;
		OutputStreamWriter osw = null;
		
		try {
			serversocket = new ServerSocket(7777); //7777��Ʈ�� ���� �Ѵ�
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			System.out.println("Server Ready");
			socket = serversocket.accept(); // ��û�� ���� ������ ���μ����� ��������
			System.out.println("Server Accept");
			
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			
			osw.write("�ȳ��ϼ���?");
			System.out.println("Server End");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(osw!=null) {
				try {
					osw.close();
				} catch (IOException e) {
					//������ close�� �ٽ� �ɾ������.
					e.printStackTrace();
				}
			}
		}
		
	}

}
