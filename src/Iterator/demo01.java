package Iterator;

import java.util.ArrayList;
import java.util.Iterator;

public class demo01 {
    public static void main(String[] args) {
        ArrayList<String> sites = new ArrayList<>();
        sites.add("Google");
        sites.add("Runoob");
        sites.add("Taobao");
        sites.add("Zhihu");
        //获取迭代器
        Iterator<String> it = sites.iterator();
        //使用迭代器遍历集合的所有元素
        while (it.hasNext()){
            System.out.println(it.next());
        }
    }
}
