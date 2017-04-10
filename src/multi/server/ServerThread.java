/*
 * 
 * 접속한 클라이언트와 1:1로 대화를 나눌 쓰레드
 * 
 * */
package multi.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;


public class ServerThread extends Thread{
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	JTextArea area;
	ServerMain main;
	boolean flag=true;
	
	//서버로부터 소켓을 받아오자 그런데 소켓은 새로생성되는 것이아니라 서버로부터받으니까 new말고생성자에 !!!! main의 vector에 접근헤 주기위해서 main으로
	public ServerThread(Socket socket, ServerMain main) {
		this.socket=socket;
		this.main=main;
		try {
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	//클라이언트의 메세지 받기 
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();//듣기
			main.area.append(msg+"\n");
			send(msg);//다시 보내기
			
		} catch (IOException e) {
			flag=false; //현재쓰레드 죽이기 
			//벡터에서 이 쓰레드를 제거 
			main.list.remove(this); //내가 그 쓰레드 이기 때문에 나 자신을 죽여야함 그래야 벡터에서 사라짐 
			main.area.append("1명 퇴장 후 현재접속자"+main.list.size()+"\n");
			System.out.println("읽기 불가"); //유저가 팍 나갈때 에러문뜸 -! 유저는 나갔는데 벡터엔 아직 유저의 명단ㅇ ㅣ담겨져있음 -->try-catch가 끊어짐 따라서 어떻게 한것?
			//e.printStackTrace();
		}
	}
	
	public void send(String msg){
		try {
			//현재 접속한 자 전부
			for(int i=0;i<main.list.size();i++){ //(접속한자전부 )
				ServerThread st=main.list.elementAt(i);
				st.buffw.write(msg+"\n"); //벡터안의 Thread의 buffw 
				st.buffw.flush();
			}
			} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void run() {
		while(flag){ //쓰레드는 flag에 의해 살고 죽고를 결정
			listen();
		}
	
	}

}
