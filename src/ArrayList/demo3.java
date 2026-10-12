package ArrayList;

import java.util.ArrayList;
import java.util.Collections;

public class demo3 {
    public static void main(String[] args) {
        ArrayList<String> sites = new ArrayList<>();
        sites.add("Taobao");
        sites.add("Wiki");
        sites.add("Runoob");
        sites.add("Weibo");
        sites.add("Google");
        //字母排序
        Collections.sort(sites);
        sites.forEach(i -> System.out.println(i));
    }
}
