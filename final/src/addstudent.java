import java.io.IOException;
import java.net.Socket;



public class addstudent extends Thread {// ºÊÅ¥¥Î
		public void run() {
			try {
				while (true) {
					Socket socket = listen.socs.accept();
					System.out.println("get s");
					instudent instudents = new instudent(socket);
					instudents.start();
					System.out.println("µ²§ôaddstudent");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}