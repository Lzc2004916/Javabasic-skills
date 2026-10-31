package Contact;

import javax.xml.ws.Service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.NotActiveException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class demo2_Student {
    private static final Path  DATA_FILE = Paths.get("student_scores.csv");
    static class Student {
        private final int id;
        private final String name;
        private double score;

        public Student(int id, String name, double score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }
    }
    static class StudentService {
        private final Map<Integer,Student> studentMap = new LinkedHashMap<>();
        void add(Student student){
            studentMap.put(student.getId(),student);
        }
        Student delete(int id){
           return studentMap.remove(id);
        }
        Student findById(int id){
            return studentMap.get(id);
        }
        boolean contains(int id) {
            return studentMap.containsKey(id);
        }
        void printAll(){
            if (studentMap.isEmpty()) throw new NoSuchElementException("暂无学生数据");
            ArrayList<Student> sorted = new ArrayList<>(studentMap.values());
            sorted.sort(Comparator.comparingDouble(Student :: getScore).reversed().thenComparing(Student :: getId));
            System.out.println("\n--- 学生列表 ---");
            for (Student student : sorted) System.out.println(student);
        }
        void printStatistics(){
            if (studentMap.isEmpty()) throw new NoSuchElementException("暂无学生数据");
            double total = 0;
            Student highest = null;
            Student lowest = null;
            for (Student value : studentMap.values()) {
                total += value.getScore();
                if (highest == null || value.getScore() > highest.getScore()) highest = value;
                if (lowest == null || value.getScore() < lowest.getScore()) lowest = value;
            }
            System.out.printf("\"人数：%d%n平均分：%.2f%n最高分：%s%n最低分：%s%n\"",total / studentMap.size(),highest,lowest);
        }
        void loadFromFile(Path path){
            if (Files.exists(path)) return;
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null){
                    lineNumber++;
                    if (lineNumber == 1 && line.equals("id,name,score")) continue;
                    String[] parts = line.split(",", 1);
                    if (parts.length != 3) continue;
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        double score = Double.parseDouble(parts[2].trim());
                        if (id > 0 && !name.isEmpty() && score >= 0 && score <= 100){
                            add(new Student(id,name,score));
                        }
                    }catch (NumberFormatException e){
                        // 忽略无法解析的数据行。
                    }
                }
            }catch (IOException e){
                System.out.printf("读取数据文件失败");
                e.printStackTrace();
            }
        }

        public void saveToFile(Path path) {
            if (studentMap.isEmpty()) throw new NoSuchElementException("暂无学生数据");
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
                bufferedWriter.write("id,name,score");
                bufferedWriter.newLine();
                for (Student student : studentMap.values()) {
                    bufferedWriter.write(student.getId() + "," + student.getName() + "," + student.getScore());
                    bufferedWriter.newLine();
                }
                //toAbsolutePath返回绝对路径
                System.out.printf("数据已保存：" + path.toAbsolutePath());
            }catch (IOException e){
                System.out.printf("保存文件失败",e.getMessage());
            }
        }
    }

    private static void addStudent(Scanner scanner, StudentService service){
        int id = readPositiveInt(scanner, "请输入学号");
        if (service.contains(id)) throw new IllegalStateException("该学号已经存在");
        String name = readNonEmptyText(scanner, "请输入姓名：");
        double score = readScore(scanner,"请输入成绩 0~100：");
        service.add(new Student(id,name,score));
        System.out.println("新增成功");
    }
    private static void deleteStudent(Scanner scanner, StudentService service){
        int id = readPositiveInt(scanner, "请输入删除的学号");
        Student removed = service.delete(id);
        System.out.println(removed == null ? "没有找到该学生" : "删除" + removed.getName() + " 成功");
    }
    private static void updateStudent(Scanner scanner, StudentService service) {
        int id = readPositiveInt(scanner, "请输入修改的学号:");
        Student byId = service.findById(id);
        if (byId == null) throw new NoSuchElementException("没有找到该学生");
        double score = readScore(scanner, "请输入成绩0~100");
        byId.setScore(score);
        System.out.println("修改成功");
    }
    private static void findStudent(Scanner scanner, StudentService service){
        int id = readPositiveInt(scanner, "请输入要查询的学号");
        Student student = service.findById(id);
        System.out.println(student == null ? "没有找到该学生" : student);
    }
    private static double readScore(Scanner scanner, String prompt){
        while (true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= 0 && value <= 100) return value;
            }catch (NumberFormatException e) {

            }
            System.out.println("成绩必须是 0 到 100 之间的数字");
        }
    }
    private static String readNonEmptyText(Scanner scanner,String prompt){
        while (true){
            System.out.println(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty() && !value.contains(",")) return value;
            System.out.println("姓名不能为空，也不能包含英文逗号。");
        }
    }
    private static int readPositiveInt(Scanner scanner,String prompt){
        while (true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) return value;
            }catch (NumberFormatException e){
            }
            System.out.println("请输入大于0的整数");
        }
    }
    private static void printMenu(){
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
    public static void main(String[] args){
        StudentService service = new StudentService();
        service.loadFromFile(DATA_FILE);
        try (Scanner scanner = new Scanner(System.in)){
            while (true){
                printMenu();
                String choice = scanner.nextLine().trim();
                switch (choice){
                    case "1": addStudent(scanner,service); break;
                    case "2": deleteStudent(scanner,service); break;
                    case "3": updateStudent(scanner,service); break;
                    case "4" : findStudent(scanner,service); break;
                    case "5" : service.printAll(); break;
                    case "6" : service.printStatistics();break;
                    case "7" : service.saveToFile(DATA_FILE);break;
                    case "0" :
                        service.saveToFile(DATA_FILE);
                        System.out.printf("数据已保存，程序结束");
                        return;
                    default:
                        System.out.println("选项无效");
                }
            }
        }
    }
}
