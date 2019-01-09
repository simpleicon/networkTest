package chat;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class ServerChat {
	public static void main(String[] args) {
		JFrame jf = new ServerFrame();
		jf.setSize(450, 600);
		jf.setVisible(true);
	}

}// end Server class

class ServerFrame extends JFrame implements ActionListener, KeyListener {
	ServerSocket serverSocket = null;
	Socket socket = null;

	ArrayList<DataOutputStream> clients;

	JLabel info = new JLabel("PORT번호를 입력하세요.");
	JLabel text = new JLabel("공지입력");
	JTextField portField = new JTextField(3);
	JTextField textField = new JTextField(20);
	JButton con = new JButton("서버시작");
	JButton disCon = new JButton("서버종료");
	List list = new List();
	List chatlist = new List();
	JPanel top = new JPanel();
	JPanel center = new JPanel();
	JPanel bottom = new JPanel();

	{
		top.setSize(450, 200);
		top.setLayout(new FlowLayout());
		top.add(info);
		top.add(portField);
		top.add(con);
		top.add(disCon);
		center.setSize(450, 200);
		center.setLayout(new GridLayout());
		center.add(list);
		center.add(chatlist);
		bottom.add(text);
		bottom.add(textField);
		bottom.setSize(450, 200);
		con.addActionListener(this);
		textField.addKeyListener(this);
		disCon.addActionListener(this);
	}

	public ServerFrame() {
		setTitle("서버 프로그램");
		clients = new ArrayList<>();
		this.setLayout(new BorderLayout());
		this.add("North", top);
		this.add("Center", center);
		this.add("South", bottom);
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendToAll("[공지사항] "+textField.getText());
			textField.setText("");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == con) {
			int portNum = Integer.parseInt(portField.getText());
			start(portNum);
		} else if (obj == disCon) {
			System.exit(0);
		}

	}

	public void start(int port) {

		try {
			serverSocket = new ServerSocket(port);
			list.add("서버가 시작되었습니다.");
			Thread t1 = new StartThread();
			t1.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // start()

	// 클라이언트가 데이터를 입력하면 모든 클라이언트에게 데이터 전달
	void sendToAll(String msg) {
//		Iterator it = clients.iterator();
		msg = new Date().toString().substring(10,19)+" "+msg;
		chatlist.add(msg);

		
		for(DataOutputStream out : clients) {
			if(out != null) {
				try {
					out.writeUTF(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	} // sendToAll
	
	// ServerReceiver thread 호출하는 클래스
	class StartThread extends Thread {
		public void run() {
			while (true) {
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
				list.add("[" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "]" + "에서접속하였습니다.");
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		}
	}

	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;

		ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
			}
		}

		// 쓰레드는 클라이언트가 추가될 때 마다 생긴다
		public void run() {
			System.out.println(Thread.currentThread());
			String name = "";
			try {
				name = in.readUTF();
				sendToAll("#" + name + "접속");

				clients.add(out);
				list.add("현재 서버접속자 수는 " + clients.size() + "입니다.");
				while (in != null) {
					sendToAll(in.readUTF());
				}
			} catch (IOException e) {
				// ignore
			} finally {
				sendToAll("#" + name + "님이 나가셨습니다.");
				clients.remove(out);
				list.add("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속을 종료하였습니다.");
				list.add("현재 서버접속자 수는 " + clients.size() + "입니다.");
			} // try
		} // run
	} // ReceiverThread
	
	
	// 미사용
	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
