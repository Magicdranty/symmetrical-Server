import java.io.IOException;
import java.net.Socket;



public class addstudent extends Thread {// ��ť��
		public void run() {
			try {
				while (true) {
					Socket socket = listen.socs.accept();
					System.out.println("get s");
					instudent instudents = new instudent(socket);
					instudents.start();
					System.out.println("����addstudent");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}