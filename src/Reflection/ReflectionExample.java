package Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionExample {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // 获取 Class 对象
        Class<?> clazz = Car.class;
        // 创建 Car 对象
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
        Object car = constructor.newInstance("Toyota", 2020);
        // 访问和修改字段
        Field model = clazz.getDeclaredField("model");
        Field year = clazz.getDeclaredField("year");
        model.setAccessible(true);
        year.setAccessible(true);
        // 打印原始字段值
        System.out.println("Original Model: " + model.get(car));
        System.out.println("Original Year:" + year.get(car));
        // 修改字段值
        model.set(car, "Honda");
        year.set(car, 2024);
        System.out.println("Updated Model: " + model.get(car));
        System.out.println("Updated Year: " + year.get(car));
        Method start = clazz.getMethod("start");
        start.invoke(car);
    }
}
class Car {
    private String model;
    private int year;

    public Car(String model, int year) {
        this.model = model;
        this.year = year;
    }
    public void start(){
        System.out.println("The " + model + " car of year " + year + " is starting.");
    }
}