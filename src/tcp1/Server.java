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
			serversocket = new ServerSocket(7777); //7777포트로 서비스 한다
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			System.out.println("Server Ready");
			socket = serversocket.accept(); // 요청이 오기 전까지 프로세스가 멈춰있음
			System.out.println("Server Accept");
			
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			
			osw.write("안녕하세요?");
			System.out.println("Server End");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(osw!=null) {
				try {
					osw.close();
				} catch (IOException e) {
					//본래는 close를 다시 걸어줘야함.
					e.printStackTrace();
				}
			}
		}
		
	}

}
