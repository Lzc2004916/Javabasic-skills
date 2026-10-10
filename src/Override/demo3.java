package Override;

public class demo3 {
    public static void main(String[] args) {
        show(new Cat());
        show(new Dog());
    }
    public static void show(Animal a){
        a.eat();
        if (a instanceof Cat){
            ((Cat) a).work();
        }
        if (a instanceof Dog){
            ((Dog) a).work();
        }
    }
   static abstract class Animal {
        abstract void eat();
    }
   static class Cat extends Animal{
        public void eat() {
            System.out.println("吃鱼");
        }
        public void work() {
            System.out.println("抓老鼠");
        }
    }
   static class Dog extends Animal{
        public void eat() {
            System.out.println("吃骨头");
        }
        public void work() {
            System.out.println("看家");
        }
    }
}
