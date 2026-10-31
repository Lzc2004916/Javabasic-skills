import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 学生成绩管理系统：控制台菜单 + 集合存储 + CSV 文件持久化。
 */
public class StudentScoreManager {
    private static final Path DATA_FILE = Paths.get("student_scores.csv");

    public static void main(String[] args) {
        StudentService service = new StudentService();
        service.loadFromFile(DATA_FILE);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        addStudent(scanner, service);
                        break;
                    case "2":
                        deleteStudent(scanner, service);
                        break;
                    case "3":
                        updateStudent(scanner, service);
                        break;
                    case "4":
                        findStudent(scanner, service);
                        break;
                    case "5":
                        service.printAll();
                        break;
                    case "6":
                        service.printStatistics();
                        break;
                    case "7":
                        service.saveToFile(DATA_FILE);
                        break;
                    case "0":
                        service.saveToFile(DATA_FILE);
                        System.out.println("数据已保存，程序结束。");
                        return;
                    default:
                        System.out.println("选项无效，请输入 0 到 7。");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== 学生成绩管理系统 =====");
        System.out.println("1. 新增学生");
        System.out.println("2. 删除学生");
        System.out.println("3. 修改学生成绩");
        System.out.println("4. 按学号查询");
        System.out.println("5. 查看全部学生（按成绩降序）");
        System.out.println("6. 成绩统计");
        System.out.println("7. 手动保存");
        System.out.println("0. 保存并退出");
        System.out.print("请选择：");
    }

    private static void addStudent(Scanner scanner, StudentService service) {
        int id = readPositiveInt(scanner, "请输入学号：");
        if (service.contains(id)) {
            System.out.println("该学号已经存在。");
            return;
        }
        String name = readNonEmptyText(scanner, "请输入姓名：");
        double score = readScore(scanner, "请输入成绩（0-100）：");
        service.add(new Student(id, name, score));
        System.out.println("新增成功。");
    }

    private static void deleteStudent(Scanner scanner, StudentService service) {
        int id = readPositiveInt(scanner, "请输入要删除的学号：");
        Student removed = service.delete(id);
        System.out.println(removed == null ? "没有找到该学生。" : "删除成功：" + removed);
    }

    private static void updateStudent(Scanner scanner, StudentService service) {
        int id = readPositiveInt(scanner, "请输入要修改的学号：");
        Student student = service.findById(id);
        if (student == null) {
            System.out.println("没有找到该学生。");
            return;
        }
        double score = readScore(scanner, "请输入新成绩（0-100）：");
        student.setScore(score);
        System.out.println("修改成功：" + student);
    }

    private static void findStudent(Scanner scanner, StudentService service) {
        int id = readPositiveInt(scanner, "请输入要查询的学号：");
        Student student = service.findById(id);
        System.out.println(student == null ? "没有找到该学生。" : student);
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // 统一在循环末尾提示输入错误。
            }
            System.out.println("请输入大于 0 的整数。");
        }
    }

    private static double readScore(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= 0 && value <= 100) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // 统一在循环末尾提示输入错误。
            }
            System.out.println("成绩必须是 0 到 100 之间的数字。");
        }
    }

    private static String readNonEmptyText(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty() && !value.contains(",")) {
                return value;
            }
            System.out.println("姓名不能为空，也不能包含英文逗号。");
        }
    }

    static class Student {
        private final int id;
        private final String name;
        private double score;

        Student(int id, String name, double score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }

        int getId() {
            return id;
        }

        String getName() {
            return name;
        }

        double getScore() {
            return score;
        }

        void setScore(double score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("学号：%d，姓名：%s，成绩：%.2f", id, name, score);
        }
    }

    static class StudentService {
        private final Map<Integer, Student> students = new LinkedHashMap<>();

        void add(Student student) {
            students.put(student.getId(), student);
        }

        boolean contains(int id) {
            return students.containsKey(id);
        }

        Student findById(int id) {
            return students.get(id);
        }

        Student delete(int id) {
            return students.remove(id);
        }

        void printAll() {
            if (students.isEmpty()) {
                System.out.println("暂无学生数据。");
                return;
            }
            List<Student> sorted = new ArrayList<>(students.values());
            sorted.sort(Comparator.comparingDouble(Student::getScore).reversed()
                    .thenComparingInt(Student::getId));
            System.out.println("\n--- 学生列表 ---");
            for (Student student : sorted) {
                System.out.println(student);
            }
        }

        void printStatistics() {
            if (students.isEmpty()) {
                System.out.println("暂无学生数据，无法统计。");
                return;
            }
            double total = 0;
            Student highest = null;
            Student lowest = null;
            for (Student student : students.values()) {
                total += student.getScore();
                if (highest == null || student.getScore() > highest.getScore()) {
                    highest = student;
                }
                if (lowest == null || student.getScore() < lowest.getScore()) {
                    lowest = student;
                }
            }
            System.out.printf("人数：%d%n平均分：%.2f%n最高分：%s%n最低分：%s%n",
                    students.size(), total / students.size(), highest, lowest);
        }

        void loadFromFile(Path path) {
            if (!Files.exists(path)) {
                return;
            }
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    if (lineNumber == 1 && line.equals("id,name,score")) {
                        continue;
                    }
                    String[] parts = line.split(",", -1);
                    if (parts.length != 3) {
                        System.out.println("跳过格式错误的数据行：" + lineNumber);
                        continue;
                    }
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        double score = Double.parseDouble(parts[2].trim());
                        if (id > 0 && !name.isEmpty() && score >= 0 && score <= 100) {
                            add(new Student(id, name, score));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("跳过格式错误的数据行：" + lineNumber);
                    }
                }
            } catch (IOException e) {
                System.out.println("读取数据文件失败：" + e.getMessage());
            }
        }

        void saveToFile(Path path) {
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write("id,name,score");
                writer.newLine();
                for (Student student : students.values()) {
                    writer.write(student.getId() + "," + student.getName() + "," + student.getScore());
                    writer.newLine();
                }
                System.out.println("数据已保存到：" + path.toAbsolutePath());
            } catch (IOException e) {
                System.out.println("保存数据文件失败：" + e.getMessage());
            }
        }
    }
}
