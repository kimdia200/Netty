package angel.chapter02.block;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 아래 블로킹 서버는 하나의 클라이언트가 접속하면 종료될때까지 다른 클라이언트의 접속을 받을 수 없다.
 * 따라서, 블로킹 모드의 소켓을 사용하는 서버는 다중 클라이언트의 접속처리를 하지 못하는 문제점을 갖고있다.
 * 아래의 문제점을 해결하기 위해 나온것은 클라이언트별로 스레드를 할당해서 처리하는 방식이다.
 */
public class BlockingServer {
    public static void main(String[] args){
        BlockingServer server = new BlockingServer();
        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException {
        ServerSocket server = new ServerSocket(8888);
        System.out.println("접속 대기중");

        while (true) {
            //서버를 실행하고 클라이언트를 연결하지 않으면 아래 부분에서 멈추게 된다.
            //즉 클라이언트의 연결이 없으면 프로그램은 아무 동작도 하지 않으며
            //해당 쓰레드 또한 해당 함수의 완료를 기다리며 해당 위치에서 대기하게 된다.
            Socket sock = server.accept();
            System.out.println("클라이언트 연결됨");

            OutputStream out = sock.getOutputStream();
            InputStream in = sock.getInputStream();

            //한번 클라이언트가 접속되면 종료될 대까지 아래의 반복문이 돌아가면서
            //입력받은 문자그대로를 클라이언트에 다시 보내준다.
            while (true) {
                try {

                    //클라이언트로부터 데이터가 들어오면 read메서드를 이용해 값을 읽어내는데
                    //read메서드가 진행되는 동안 스레드는 블로킹 된다.
                    int request = in.read();

                    //write 메서드의 경우 read와는 조금 다른데
                    //운영체제의 송신버터에 전송할 데이터를 기록한다.
                    // 만약 송신버퍼크기<<보낼 데이터의 크기 라면 송신 버퍼가 비워질때 까지 블로킹 된다.
                    out.write(request);
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
}
