/*
 * 
 * ������ Ŭ���̾�Ʈ�� 1:1�� ��ȭ�� ���� ������
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
	
	//�����κ��� ������ �޾ƿ��� �׷��� ������ ���λ����Ǵ� ���̾ƴ϶� �����κ��͹����ϱ� new��������ڿ� !!!! main�� vector�� ������ �ֱ����ؼ� main����
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
	
	//Ŭ���̾�Ʈ�� �޼��� �ޱ� 
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();//���
			main.area.append(msg+"\n");
			send(msg);//�ٽ� ������
			
		} catch (IOException e) {
			flag=false; //���羲���� ���̱� 
			//���Ϳ��� �� �����带 ���� 
			main.list.remove(this); //���� �� ������ �̱� ������ �� �ڽ��� �׿����� �׷��� ���Ϳ��� ����� 
			main.area.append("1�� ���� �� ����������"+main.list.size()+"\n");
			System.out.println("�б� �Ұ�"); //������ �� ������ �������� -! ������ �����µ� ���Ϳ� ���� ������ ��ܤ� �Ӵ�������� -->try-catch�� ������ ���� ��� �Ѱ�?
			//e.printStackTrace();
		}
	}
	
	public void send(String msg){
		try {
			//���� ������ �� ����
			for(int i=0;i<main.list.size();i++){ //(������������ )
				ServerThread st=main.list.elementAt(i);
				st.buffw.write(msg+"\n"); //���;��� Thread�� buffw 
				st.buffw.flush();
			}
			} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void run() {
		while(flag){ //������� flag�� ���� ��� �װ� ����
			listen();
		}
	
	}

}
