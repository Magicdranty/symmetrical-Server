import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class inteacher extends Thread {// 加入list
	private Socket soct;
	private BufferedReader brt;
	private BufferedWriter bwt;
	private String function, pinforaccess;
	int flag = 1;

	public inteacher(Socket s) {
		try {
			soct = s;
			bwt = new BufferedWriter(new OutputStreamWriter(soct.getOutputStream(), "UTF-8"));
			brt = new BufferedReader(new InputStreamReader(soct.getInputStream(), "UTF-8"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", brt.readLine());
			map.put("socket", soct);
			map.put("function", function = brt.readLine());
			map.put("pinforaccess", pinforaccess = brt.readLine());
			Iterator<Map<String, Object>> Iterator = listen.teacherlists.iterator();
			while (Iterator.hasNext()) {// 檢查是否有重複名稱的老師(怕跳出再登入 記錄重複
				Map<String, Object> x = Iterator.next();
				if (x.get("name").equals(map.get("name"))) {
					System.out.println("!!!!!" + listen.teacherlists.size());
					Iterator.remove();
					System.out.println("!!!!!" + listen.teacherlists.size());
				}
			}
			listen.teacherlists.add(map);
			System.out.println("!!!!!" + listen.teacherlists.size());
			System.out.println("create 1 teacher" + map.get("name") + map.get("pinforaccess") + map.get("function"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("starts");
		try {
			String testorrace;
			updatepeople up = new updatepeople();// 老師端一進到等帶畫面 就開始每3秒計算人數
			up.start();
			if (brt.readLine().contains("start")) {// 收到老師按開始健
				flag = 0;// 停止人數計數
				System.out.println(listen.studentlists);
				testorrace = brt.readLine().toString();
				if (testorrace.contains("test")) {/////////////////////////// TEST
					System.out.println("test");
					Iterator<Map<String, Object>> sListIterator = listen.studentlists.iterator();
					Socket temps = null;
					while (sListIterator.hasNext()) {
						try {
							Map<String, Object> x = sListIterator.next();
							if (x.get("pinforaccess").equals(pinforaccess)) {
								System.out.println("AAA");
								temps = (Socket) x.get("socket");
								BufferedWriter bw = new BufferedWriter(
										new OutputStreamWriter(temps.getOutputStream(), "UTF-8"));
								bw.write("starttest\n");// fors
								bw.flush();
								temps.close();// 第一種考試 傳完權限之後就斷開socket
												// 並清除stlist資料
								sListIterator.remove();
								System.out.println("刪掉S:" + temps.isClosed());
							}
						} catch (Exception e) {// 傳完就斷掉 或者傳到已經關閉的SOCKET 就跳到這EX
												// 並且關掉
							temps.close();
							sListIterator.remove();
							System.out.println("抓到一個斷線者");
						}
					}
					System.out.println(listen.studentlists);
					Iterator<Map<String, Object>> tIterator = listen.teacherlists.iterator();
					while (tIterator.hasNext()) {
						Map<String, Object> xx = tIterator.next();
						if (xx.get("socket").equals(soct)) {
							soct.close();
							tIterator.remove();
							System.out.println("刪掉T:" + soct.isClosed());
						}
					}
					System.out.println(listen.teacherlists);
				} else {///////////////////////////////////////// RACE

					System.out.println("race");
					while (true) {/////
						Boolean keep = true;
						System.out.println("inrace");
						for (int i = 15; i >= -1; i--) {// -1 當作傳送 技序 還是節數的回圈
							if (i >= 0) {
								bwt.write(String.valueOf(i) + "\n");
								bwt.flush();
								System.out.println(String.valueOf(i));
							} else {// =-1 數完了 減查老師給的是con 還是end
								if (!brt.readLine().contains("end")) {
									System.out.println("con");
								} else {
									keep = false;// 若是end就準備結束測驗
									System.out.println("end");
								}
							}
							Iterator<Map<String, Object>> sListIterator = listen.studentlists.iterator();
							Socket temps = null;
							while (sListIterator.hasNext()) {
								try {// 內部的try catch
									System.out.println("inwhile" + String.valueOf(i));
									Map<String, Object> x = sListIterator.next();
									if (x.get("pinforaccess").equals(pinforaccess)) {
										System.out.println("checkstudenting");
										temps = (Socket) x.get("socket");
										BufferedWriter bw = new BufferedWriter(
												new OutputStreamWriter(temps.getOutputStream(), "UTF-8"));
										if (i >= 0) { // 0~14 計數 15 判斷用
											bw.write(String.valueOf(i) + "\n");
											System.out.println("ssss" + String.valueOf(i));
											bw.flush();
										} else if (i == -1) { // 告訴學生是否繼續
											if (keep) {
												bw.write("con\n");
												bw.flush();
											} else {
												bw.write("end\n");
												bw.flush();
												temps.close();
											}
										} 
									}
								} catch (Exception e) {
									temps.close();
									sListIterator.remove();
									System.out.println("抓到一個斷線者");
								}
							}
							try {// delay
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (!keep) {
								System.out.println("NKEEP B");
								System.out.println(listen.studentlists);
								break;
							}
						}
						if (!keep) {//
							soct.close();
							break;
						}
					} /////
				}
			}

			System.out.println("結束inteacher 並且close掉SOC");

		} catch (Exception e) {
			System.out.println("BYE TEACHER BREAK SOC CLOSE");
			flag = 0;

			Iterator<Map<String, Object>> sListIterator = listen.studentlists.iterator();
			Socket temps = null;
			while (sListIterator.hasNext()) {
				Map<String, Object> x = sListIterator.next();
				if (x.get("pinforaccess").equals(pinforaccess)) {
					temps = (Socket) x.get("socket");
					try {
						BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(temps.getOutputStream(), "UTF-8"));
						bw.write("@_@\n");
						bw.flush();
						temps.close();
						sListIterator.remove();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} // soct.close();
			System.out.println(listen.studentlists);

		}
	}

	class updatepeople extends Thread {
		public void run() {
			int count = 0;
			Iterator<Map<String, Object>> sListIterator;
			Map<String, Object> x;
			Socket temps;
			while (flag != 0) {// 老師按開始FLAG就會=0
				count = 0;
				sListIterator = listen.studentlists.iterator();
				while (sListIterator.hasNext()) {
					x = sListIterator.next();
					temps = (Socket) x.get("socket");
					if ((x.get("pinforaccess").equals(pinforaccess))) {
						try {
							temps.setOOBInline(false);
							temps.sendUrgentData(0xFF);
							count++;
						} catch (Exception ex) {

							sListIterator.remove();
							System.out.println("有人斷開" + listen.studentlists.size());
						}
					}
				}
				System.out.println("!" + count);
				try {
					bwt.write(count + "\n");
					bwt.flush();
					sleep(3000);
				} catch (Exception e1) {
					System.out.println(e1.toString());
				}

			}

		}
	}

}
