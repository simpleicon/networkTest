package http2;

public class Down {

	public static void main(String[] args) {
		String address = "http://70.12.50.130/a/down.zip"; //�ش� �ּ��� ����
		
		DownThread dt = new DownThread(address);
		new Thread(dt).start();
	}

}
