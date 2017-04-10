/*키 입력을 해야 서버의 메세지를 받는 현재의 기능을 보완한다.
 * (채팅은 엔터를 치지않았을 때도 남이 보내지는건 받아지니까!)
 * 무한루프를 돌면서 서버의 메세지를 받을 존재가 필요하며 
 * 무한루프를 실행해야하므로 스레드로 정의한다!
 * 
 * */

package multi.client;

import java.awt.geom.Area;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientThread extends Thread{
	ClientMain main; //!!!!!!!!!원래는 area를 받아왔겠찌만 클라이언트프레임 자체를보유해보자 자료형이 ClientMain
	Socket socket;
	BufferedWriter buffw;
	BufferedReader buffr;
	
	public ClientThread(Socket socket, ClientMain main) {
		this.socket=socket;
		this.main=main;
		try {
			buffr=new BufferedReader(new InputStreamReader(socket. getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();
			main.area.append(main.nickName+"의 말 :"+msg+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void send(String msg){
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	public void run() {
		//끝없이 청취해야하니까 무한루프
		while(true){
			listen();
		}
	}
}
