import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class listen {
	static ServerSocket soct;
	static ServerSocket socs;
	static List<Map<String, Object>> studentlists = new ArrayList<Map<String, Object>>();
	static List<Map<String, Object>> teacherlists = new ArrayList<Map<String, Object>>();
	public listen() {
		try {
			soct = new ServerSocket(509);// for teacher
			socs = new ServerSocket(599);// for student
			System.out.println("Server is start1.");
			addteacher adt = new addteacher();
			adt.start();
			addstudent ads = new addstudent();
			ads.start();
		} catch (IOException e) {
			System.out.println("Socket ERROR");
		}
	}

	public static void main(String[] args) {
		new listen();
	}
}
