package tcp1;

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
		Socket socket;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try {
			socket = new Socket("70.12.50.147", 7777);
			
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
