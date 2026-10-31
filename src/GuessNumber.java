import java.util.Random;
import java.util.Scanner;

public class GuessNumber {
    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        int answer = random.nextInt(100) + 1;
        int attempts = 0;

        System.out.println("我已经想好了一个 1 到 100 之间的整数。来猜猜看！");

        while (true) {
            System.out.print("请输入你的猜测：");
            String input = scanner.nextLine();

            int guess;
            try {
                guess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的整数。");
                continue;
            }

            if (guess < 1 || guess > 100) {
                System.out.println("数字范围是 1 到 100，请重新输入。");
                continue;
            }

            attempts++;

            if (guess < answer) {
                System.out.println("小了，再试一次。");
            } else if (guess > answer) {
                System.out.println("大了，再试一次。");
            } else {
                System.out.println("猜对了！你一共猜了 " + attempts + " 次。");
                break;
            }
        }

        scanner.close();
    }
}
