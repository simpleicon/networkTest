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

	JLabel info = new JLabel("PORT��ȣ�� �Է��ϼ���.");
	JLabel text = new JLabel("�����Է�");
	JTextField portField = new JTextField(3);
	JTextField textField = new JTextField(20);
	JButton con = new JButton("��������");
	JButton disCon = new JButton("��������");
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
		setTitle("���� ���α׷�");
		clients = new ArrayList<>();
		this.setLayout(new BorderLayout());
		this.add("North", top);
		this.add("Center", center);
		this.add("South", bottom);
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			sendToAll("[��������] "+textField.getText());
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
			list.add("������ ���۵Ǿ����ϴ�.");
			Thread t1 = new StartThread();
			t1.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // start()

	// Ŭ���̾�Ʈ�� �����͸� �Է��ϸ� ��� Ŭ���̾�Ʈ���� ������ ����
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
	
	// ServerReceiver thread ȣ���ϴ� Ŭ����
	class StartThread extends Thread {
		public void run() {
			while (true) {
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
				list.add("[" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "]" + "���������Ͽ����ϴ�.");
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

		// ������� Ŭ���̾�Ʈ�� �߰��� �� ���� �����
		public void run() {
			System.out.println(Thread.currentThread());
			String name = "";
			try {
				name = in.readUTF();
				sendToAll("#" + name + "����");

				clients.add(out);
				list.add("���� ���������� ���� " + clients.size() + "�Դϴ�.");
				while (in != null) {
					sendToAll(in.readUTF());
				}
			} catch (IOException e) {
				// ignore
			} finally {
				sendToAll("#" + name + "���� �����̽��ϴ�.");
				clients.remove(out);
				list.add("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "���� ������ �����Ͽ����ϴ�.");
				list.add("���� ���������� ���� " + clients.size() + "�Դϴ�.");
			} // try
		} // run
	} // ReceiverThread
	
	
	// �̻��
	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
