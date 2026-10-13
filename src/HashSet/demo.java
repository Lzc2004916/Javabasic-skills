package HashSet;

import java.util.HashSet;

public class demo {
    public static void main(String[] args) {
        HashSet<String> sites = new HashSet<>();
        sites.add("google");
        sites.add("facebook");
        sites.add("youtube");
        sites.add("youtube");
        sites.add("taobao");
        System.out.println(sites);
        //查找元素是否存在
        sites.contains("google");
        System.out.println(sites);
        //删除元素
        sites.remove("taobao");
        System.out.println(sites);
        //计算大小
        System.out.println(sites.size());
        //迭代
        for (String site : sites) {
            System.out.println(site);
        }
        //删除所有元素
        sites.clear();
        System.out.println(sites);
    }
}
