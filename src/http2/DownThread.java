package http2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownThread implements Runnable{
	URL url;
	String address;
	
	public DownThread(String address) {
		this.address = address;
	}
	
	@Override
	public void run() {
		FileOutputStream fos = null;
		try {
			url = new URL(address);
			InputStream in = url.openStream();
			fos = new FileOutputStream("src//down.zip"); //저장할 장소와 이름
			int ch = 0;
			while((ch = in.read()) != -1) {
				fos.write(ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
