package Reflection;

import java.lang.reflect.InvocationTargetException;

public class demo2 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName("java.lang.String");
        Object obj = clazz.getDeclaredConstructor().newInstance();
    }
}
