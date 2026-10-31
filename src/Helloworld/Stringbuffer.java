package Helloworld;

public class Stringbuffer {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder(10);
        sb.append("helloworld");
        System.out.println(sb);
        sb.append("!");
        System.out.println(sb);
        sb.insert(11,"java");
        System.out.println(sb);
        sb.delete(11,15);
        System.out.println(sb);
    }
}