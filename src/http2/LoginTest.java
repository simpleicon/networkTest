package http2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginTest {

	public static void main(String[] args) throws Exception {
		// ���� ���α׷� �ۼ�
		// LoginServlet(login)
		
		// id, pwd ���� ����
		
		String id = "qq";
		String pwd = "11";
		
		String address = "http://70.12.50.148/a/login?id="+id+"&pwd="+pwd;
		
		URL url = new URL(address);
		HttpURLConnection con = null;
		url = new URL(address);
		con = (HttpURLConnection) url.openConnection();
		con.setReadTimeout(5000);
		con.setRequestMethod("POST");
		con.connect();
		
		InputStreamReader isr = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String result = "";
		result = br.readLine();
		result = result.trim();
		
		if(result.equals("1")) {
			System.out.println("ok");
		} else {
			System.out.println("Fail");
		}
		
		
			
		
		// qq, 11 ���� �α��� or ����
		// �����̸� 1 ��� ���и� 0 ���
	}

}
