package Enum;

public class Test {
    //内部已经转化 public static final Color RED = new Color();
    enum Color {
        RED, GREEN, BLUE;

        private Color() {
            System.out.println("Constructor called for : " + this.toString());
        }
        public void colorInfo(){
            System.out.println("调用者：" + this.name());
        }
    }

    public static void main(String[] args) {
//        Color red = Color.RED;
//        System.out.println(red);
//        for (Enum.Test.Color myVar : Color.values()) {
//            System.out.println(myVar);
//        }
//        Color red = Color.BLUE;
//        switch (red) {
//            case RED:
//                System.out.println("红色");
//                break;
//            case GREEN:
//                System.out.println("绿色");
//                break;
//            case BLUE:
//                System.out.println("蓝色");
//                break;
//        }
//        Color[] arr = Color.values();
//        for (Color col : arr) {
//            // 查看索引
//            System.out.println(col + " at index " + col.ordinal());
//        }
//        // 使用 valueOf() 返回枚举常量，不存在的会报错 IllegalArgumentException
//        System.out.println(Color.valueOf("RED"));
//        System.out.println(Color.valueOf("red"));
        Color c1 = Color.RED;
        System.out.println(c1);
        c1.colorInfo();
    }
}