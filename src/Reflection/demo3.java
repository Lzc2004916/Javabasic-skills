package Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class demo3 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        //获取class对象
        Class<?> clazz = Person.class;
        //构造对象
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
        Object person = constructor.newInstance("join", 30);
        //访问字段
        Field nameFileld = clazz.getDeclaredField("name");
        nameFileld.setAccessible(true);
        System.out.println("Name："+nameFileld.get(person));
        //修改字段
        nameFileld.set(person,"Doe");
        System.out.println("update Name:" +nameFileld.get(person));
        //调用方法
        Method greet = clazz.getMethod("greet", String.class);
        greet.invoke(person,"World");
    }
}
class Person{
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public void greet(String message){
        System.out.println(name + " says: " + message);
    }
}
