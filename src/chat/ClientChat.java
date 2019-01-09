package chat;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientChat {
	public static void main(String[] args) {
		JFrame jf = new ClientFrame();
		jf.setSize(600, 500);
		jf.setVisible(true);
	}

}

class ClientFrame extends JFrame implements ActionListener, KeyListener {
	
	JLabel ip = new JLabel("서버 IP");
	JTextField ipField = new JTextField(13);
	JLabel port = new JLabel("포트번호");
	JTextField portField = new JTextField(5);
	JLabel id = new JLabel("ID");
	JTextField idField = new JTextField(5);
	JButton con = new JButton("접속요청");
	JButton disCon = new JButton("접속종료");
	List list = new List();
	JTextField uMsg = new JTextField(25);
	JPanel top = new JPanel();
	JPanel bottom = new JPanel();
	String uid = "";

	// 소켓의 생성
	Socket socket;
	DataOutputStream out;

	{
		top.setSize(600, 200);
		top.setLayout(new FlowLayout());
		top.add(ip);
		top.add(ipField);
		top.add(port);
		top.add(portField);
		top.add(id);
		top.add(idField);
		top.add(con);
		top.add(disCon);
		bottom.add(uMsg);
		bottom.setSize(600, 200);
		con.addActionListener(this);
		disCon.addActionListener(this);
		uMsg.addKeyListener(this);
	}

	public ClientFrame() {
		setTitle("채팅 클라이언트");
		this.setLayout(new BorderLayout());
		this.add("North", top);
		this.add("Center", list);
		this.add("South", bottom);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				out.writeUTF("[" + uid + "] " + uMsg.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			uMsg.setText("");
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == con) {
			String serverip = ipField.getText();
			int uport = Integer.parseInt(portField.getText());
			uid = idField.getText();
			start(uid, serverip, uport);
		} else if (obj == disCon) {
			System.exit(0);
		}

	}

	public void start(String uid, String serverip, int uport) {
		try {
			// 소켓을 생성하여 연결을 요청한다.
			socket = new Socket(serverip, uport);
			out = new DataOutputStream(socket.getOutputStream());
			if (socket.isConnected()) {
				System.out.println("서버에 연결되었습니다.");
				list.add("서버에 연결되었습니다.");
			} 
			// 접속자 이름전송
			out.writeUTF(uid);

			// Thread sender = new Thread(new ClientSender(socket, uid));
			Thread receiver = new Thread(new ClientReceiver(socket));

			// sender.start();
			receiver.start();
		} catch (ConnectException ce) {
			list.add("연결에 실패하였습니다. 잠시후 다시 시도해주십시오");
			ce.printStackTrace();
		} catch (Exception e) {
			list.add("심각한 오류!");
		}
	}

	class ClientReceiver extends Thread {
		DataInputStream dis;

		ClientReceiver(Socket socket) throws IOException {
			dis = new DataInputStream(socket.getInputStream());
		}

		public void run() {
			while (dis != null) {
				try {
					String re = dis.readUTF();
					System.out.println(re);
					list.add(re);
				} catch (IOException e) {
				}
			}
		} // run
	}// end class ClientReceiver

}
