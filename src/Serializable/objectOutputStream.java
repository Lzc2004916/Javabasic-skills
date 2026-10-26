package Serializable;

import java.io.FileOutputStream;
import java.io.IOException;

public class objectOutputStream {
    public static void main(String[] args) {
        MyClass obj = new MyClass();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("object.ser");
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(fileOutputStream);
            out.writeObject(obj);
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
