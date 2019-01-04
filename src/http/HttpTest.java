package http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpTest {

	public static void main(String[] args) throws Exception {
		URL url = null;
		String address = "http://www.naver.com";
		url = new URL(address); // malformedURLException 발생가능
		
		InputStreamReader isr = new InputStreamReader(url.openStream()); // 해당 url에 요청하여 내려오는 inputstream
		BufferedReader br = new BufferedReader(isr);
		String str = "";
		while((str = br.readLine()) != null) {
			System.out.println(str);
		}
		
		br.close();
		
		
		

	}

}
