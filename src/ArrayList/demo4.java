package ArrayList;

import java.util.ArrayList;
import java.util.Collections;

public class demo4 {
    public static void main(String[] args) {
        ArrayList<Integer> myNumbers = new ArrayList<Integer>();
        myNumbers.add(33);
        myNumbers.add(15);
        myNumbers.add(20);
        myNumbers.add(34);
        myNumbers.add(8);
        myNumbers.add(12);
        //从小到大排序
        Collections.sort(myNumbers);
        myNumbers.forEach(i -> System.out.println(i));
    }
}
