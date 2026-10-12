package ArrayList;

import java.util.ArrayList;

public class demo1 {
    public static void main(String[] args) {
        ArrayList<String> sites = new ArrayList<>();
        sites.add("Google");
        sites.add("Runoob");
        sites.add("Taobao");
        sites.add("Weibo");
        System.out.println(sites);
        //访问元素
        System.out.println(sites.get(1));
        //修改元素
        sites.set(1,"Wiki");
        System.out.println(sites);
        //删除元素
        System.out.println("删除索引为3的元素");
        sites.remove(3);
        System.out.println(sites);
        //元素数量
        System.out.println("数量" + sites.size());
        //迭代数组列表
        for (String i : sites) {
            System.out.println(i);
        }
    }
}
