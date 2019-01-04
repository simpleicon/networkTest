package http;

import java.util.Random;

public class Test {

	public static void main(String[] args) {
		HttpTest2 sender = new HttpTest2();

		while(true) {
		
			String cid = "id001";
			Random r = new Random();
			int s = r.nextInt(200)+1;
			String speed = s+"";
			
			sender.setCid(cid);
			sender.setSpeed(speed);
	
			Thread t = new Thread(sender);;
			t.start();
			try {
				t.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
