package Reflection;

public class demo1 {
    public static void main(String[] args) {
//        Class<?> clazz = String.class;
        String str = "Hello";
        Class<?> clazz = str.getClass();
        System.out.println(clazz);
    }
}
