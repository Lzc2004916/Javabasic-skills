package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetingServer extends Thread {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动，等待客户端连接");
            System.out.println("等待客户端A...");
            Socket client1 = serverSocket.accept();
            System.out.println("客户端A已连接"+ client1.getRemoteSocketAddress());
            Socket client2 = serverSocket.accept();
            System.out.println("客户端B已连接"+ client2.getRemoteSocketAddress());
            //线程A
            new Thread(()->{
                try {
                    DataInputStream in = new DataInputStream(client1.getInputStream());
                    DataOutputStream out = new DataOutputStream(client2.getOutputStream());
                    while (true){
                        String msg = in.readUTF();
                        System.out.println("客户端1->客户端2:" + msg);
                        out.writeUTF(msg);
                    }
                }catch (IOException e){
                    System.out.println("客户端1断开");
                }
            }).start();
            //线程B
            new Thread(()->{
                try {
                    DataInputStream in = new DataInputStream(client2.getInputStream());
                    DataOutputStream out = new DataOutputStream(client1.getOutputStream());
                    while (true){
                        String msg = in.readUTF();
                        System.out.println("客户端2 → 客户端1: " + msg);
                        out.writeUTF(msg);
                    }
                }catch (IOException e){
                    System.out.println("客户端2断开");
                }
            }).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}