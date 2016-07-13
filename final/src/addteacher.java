import java.io.IOException;
import java.net.Socket;


public class addteacher extends Thread{
	public void run() {
		try {
			while (true) {
				Socket socket = listen.soct.accept();
				System.out.println("get t");
				inteacher inteach = new inteacher(socket);
				inteach.start();
				System.out.println("µ²§ôaddteacher");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
