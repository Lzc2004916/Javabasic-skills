package IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BRRead {
    public static void main(String[] args) throws IOException {
        char d;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("输入字符, 按下 'q' 键退出。");
        do {
            d = (char) reader.read();
            System.out.println(d);
        }while (d != 'q');
    }
}
