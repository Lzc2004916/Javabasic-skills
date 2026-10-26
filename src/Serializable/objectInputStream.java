package Serializable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class objectInputStream {
    public static void main(String[] args) {
        MyClass obj = null;
       try {
           FileInputStream fileIn = new FileInputStream("object.ser");
           ObjectInputStream in = new ObjectInputStream(fileIn);
           obj = (MyClass) in.readObject();
           in.close();
           fileIn.close();
       }catch (IOException e){
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
    }
}
