package tcp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		String host = "70.12.50.130";
		Socket socket = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		boolean flag = true;
		
		while(flag) {
			try {
				socket = new Socket(host, 7777);
				if(socket.isConnected()) {
					break;
				}
			} catch (Exception e) {
				System.out.println("Retry...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} 
		}
		
		try {
			
			is = socket.getInputStream(); // 소켓을 통해 내려온 outputstream의 결과를 inputstream으로 받는다.
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			
			String result = br.readLine();
			System.out.println(result);
			
		} catch (UnknownHostException e) {
			System.out.println("unknown Host");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException...");
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
