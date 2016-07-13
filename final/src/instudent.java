import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class instudent extends Thread {
	private Socket soct;
	private BufferedReader brs;
	private BufferedWriter bws;

	public instudent(Socket s) {
		String pinforaccess;
		try {
			System.out.println("instu");
			soct = s;
			bws = new BufferedWriter(new OutputStreamWriter(soct.getOutputStream(), "UTF-8"));
			brs = new BufferedReader(new InputStreamReader(soct.getInputStream(), "UTF-8"));
			pinforaccess = brs.readLine();
			System.out.println(pinforaccess);
			System.out.println(pinforaccess);
			Iterator<Map<String, Object>> tIterator = listen.teacherlists.iterator();
			Boolean canin = false;
			while (tIterator.hasNext()) {// 檢查是否有相同pfa的老師在開房
				Map<String, Object> xx = tIterator.next();
				if (xx.get("pinforaccess").equals(pinforaccess)) {
					canin = true;
				}
			}
			if (canin != true) {
				bws.write("no\n");// 學生利用SOC 查詢是否有此房間
				bws.flush();
				System.out.println("!!!!!NONO");
			} else {
				System.out.println("!!!!!GOGO");
				bws.write("hr\n");// has room
				bws.flush();
				Map<String, Object> map = new HashMap<String, Object>();
				String temp = brs.readLine();
				if (temp.contains("ok")) {
					map.put("name", brs.readLine());
					map.put("socket", soct);
					map.put("function", brs.readLine());
					map.put("pinforaccess", pinforaccess);
					Iterator<Map<String, Object>> Iterator = listen.studentlists.iterator();
					while (Iterator.hasNext()) {
						Map<String, Object> x = Iterator.next();
						if (x.get("name").equals(map.get("name"))) {
							System.out.println("!!!!!" + listen.studentlists.size());
							Iterator.remove();
						}
					}
					listen.studentlists.add(map);
					System.out.println("!!!!!" + listen.studentlists.size());
					System.out.println("create 1 student" + map.get("name"));
				}else{
					System.out.println("BYE~ TEST ALREADY START");
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		System.out.println("結束instudent");
	}
}