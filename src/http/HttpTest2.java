package http;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTest2 implements Runnable{
	String cid;
	String speed;
	
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed.trim();
	}

	public void run() {
		URL url = null;
		String address = "http://70.12.50.148/a/test?cid="+cid+"&speed="+speed;
		HttpURLConnection con = null;
		try {
			url = new URL(address);
			con = (HttpURLConnection)url.openConnection();
			con.setReadTimeout(5000);
			con.setRequestMethod("GET");
			con.connect();
			con.getInputStream();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		
		
		

	}

}
