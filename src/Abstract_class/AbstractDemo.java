package Abstract_class;

public class AbstractDemo {
    public static void main(String[] args) {
//        Employee e = new Employee("George W.", "Houston, TX", 43);
//        System.out.println("\n Call mailCheck using Employee reference--");
//        e.mailCheck();
        Employee s = new Salary("John Adams", "Boston, MA", 2, 2400.00);
        s.mailCheck();
    }
}
