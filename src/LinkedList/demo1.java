package LinkedList;

import java.util.LinkedList;

public class demo1 {
    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("Google");
        linkedList.add("Java");
        linkedList.add("Python");
        linkedList.add("C++");
        // 使用 addFirst() 在头部添加元素
        linkedList.addFirst("html");
        System.out.println(linkedList);
        // 使用 addLast() 在尾部添加元素
        linkedList.addLast("css");
        System.out.println(linkedList);
        //使用removeFirst移除头部元素
        linkedList.removeFirst();
        System.out.println(linkedList);
        //使用removeLast移除尾部元素
        linkedList.removeLast();
        System.out.println(linkedList);
        //使用getFirst（）获取头部元素
        linkedList.getFirst();
        System.out.println(linkedList);
        //getLast（）获取尾部元素
        linkedList.getLast();
        System.out.println(linkedList);
        ///使用 for 配合 size() 方法来迭代列表中的元素
//        for (int i = 0;i< linkedList.size();i++){
//            System.out.println(linkedList.get(i));
//        }
        for (String s : linkedList) {
            System.out.println(s);
        }
    }
}
