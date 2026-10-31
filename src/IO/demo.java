package IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class demo {
    public static void main(String[] args) throws IOException {
        char c;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("输入字符, 按下 'q' 键退出。");
        do {
            c = (char) reader.read();
            System.out.println(c);
        }while (c != 'q');
    }
}