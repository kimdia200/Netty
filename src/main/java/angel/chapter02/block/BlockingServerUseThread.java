package angel.chapter02.block;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BlockingServer클래스의 단일 클라이언트 연결방식을
 * Thread를 사용해 다중 클라이언트 연결 가능 방식으로 변경하였다.
 * 현업에서는 매번 Thread를 생성하는 대신 ThreadPool을 만들어놓고 사용하는것이 바람직하다.
 *
 * 단, 아래와 같이 Thread를 추가로 적요한다고해도
 * 여전히 ServerSocket은 한번에 하나씩 순차적으로 처리 할 수 밖에없다.
 */
public class BlockingServerUseThread {
    public static void main(String[] args){
        BlockingServerUseThread server = new BlockingServerUseThread();
        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException {
        ServerSocket server = new ServerSocket(8888);

        while (true) {
            System.out.println("접속 대기중 ...");
            Socket sock = server.accept();
            new Worker(sock).start();
        }
    }
}

class Worker extends Thread{
    Socket sock;
    public Worker(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try (
            InputStream in = sock.getInputStream();
            OutputStream out = sock.getOutputStream();
        ){
            System.out.println("연결 성공!!!");
            while (true) {
                int request = in.read();
                out.write(request);
            }
        } catch (IOException e) {
            System.out.println("연결 종료...");
        }
    }
}