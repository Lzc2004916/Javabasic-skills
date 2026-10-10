package Override;

public class demo1 {
    static class Animal{
        public void move(){
            System.out.println("动物可以移动");
        }
    }
    static class Dog extends Animal{
        public void move() {
            System.out.println("狗可以跑和走");
        }
    }

    public static void main(String[] args) {
        Animal b = new Dog();
        b.move();
    }
}

