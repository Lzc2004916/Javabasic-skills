package Socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GreetingClient_A {
    public static void main(String[] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        try {
            Socket client = new Socket(serverName, port);
            System.out.println("已连接到服务器:"+ client.getRemoteSocketAddress());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            new Thread(()->{
                try {
                    while (true){
                        String msg = in.readUTF();
                        System.out.println("\n收到B线程的消息:"+msg);
                        System.out.println("请输入消息");
                    }
                }catch (IOException e){
                    System.out.println("\n连接已断开");
                    System.exit(0);
                }
            }).start();
            //主进程
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输出消息：");
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                out.writeUTF(msg);
                System.out.println("请输入消息:");
            }
            client.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
