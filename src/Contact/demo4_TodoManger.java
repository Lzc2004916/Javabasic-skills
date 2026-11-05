package Contact;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class demo4_TodoManger {
    private static final Path DATA_FILE = Paths.get("todo_tasks.csv");

    enum Priority {
        LOW(1, "低"), MEDIUM(2, "中"), HIGH(3, "高");
        private final int rank;
        private final String description;
        Priority(int rank, String description) {
            this.rank = rank;
            this.description = description;
        }
        int getRank() {
            return rank;
        }
        @Override
        public String toString() {
            return description;
        }
    }

    enum TaskStatus {
        PENDING("未完成"),
        DOME("已完成");
        private final String description;
        TaskStatus(String description) {
            this.description = description;
        }
        @Override
        public String toString() {
            return description;
        }
    }

    static class Task {
        private final int id;
        private final String title;
        private final String description;
        private final Priority priority;
        private TaskStatus status;
        private final LocalDate dueDate;

        public Task(int id, String title, String description, Priority priority, TaskStatus status, LocalDate dueDate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.status = status;
            this.dueDate = dueDate;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Priority getPriority() {
            return priority;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public TaskStatus getStatus() {
            return status;
        }

        void complete() {
            status = TaskStatus.DOME;
        }

        @Override
        public String toString() {
            return String.format("编号：%d，标题：%s，描述：%s，优先级：%s，状态：%s，截止：%s",
                    id, title, description, priority, status, dueDate);
        }
    }

    static class TodoService {
        private final Map<Integer, Task> tasks = new LinkedHashMap<>();
        private int nextId = 1;

        private Task add(String title, String description, Priority priority, LocalDate dueDate) {
            Task task = new Task(nextId++, title, description, priority, TaskStatus.PENDING, dueDate);
            tasks.put(task.getId(),task);
            return task;
        }
        private int complete(int id){
            Task task = getTask(id);
            if (task.getStatus() == TaskStatus.DOME) throw new IllegalStateException("该任务已完成");
            task.complete();
            return task.getId();
        }
        private String delete(int id){
            Task remove = tasks.remove(id);
            if (remove == null) throw new IllegalArgumentException("任务编号不存在");
            return remove.getTitle();
        }
        private Task getTask(int id){
            Task task = tasks.get(id);
            if (task == null) throw new IllegalArgumentException("任务编号不存在");
            return task;
        }
        void loadFromFile(Path path) {
            if (!Files.exists(path)) return;
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    if (lineNumber == 1 && line.equals("id,title,description,priority,status,dueDate")) continue;
                    String[] parts = line.split(",", -1);
                    if (parts.length != 6) continue;
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String title = parts[1].trim();
                        String description = parts[2].trim();
                        Priority priority = Priority.valueOf(parts[3].trim());
                        TaskStatus status = TaskStatus.valueOf(parts[4].trim());
                        LocalDate dueDate = parts[5].trim().isEmpty() ? null : LocalDate.parse(parts[5].trim());
                        if (id > 0 && !title.isEmpty()) {
                            tasks.put(id, new Task(id, title, description, priority, status, dueDate));
                            nextId = Math.max(nextId, id + 1);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("跳过格式错误的数据行：" + lineNumber);
                    }
                }
            } catch (IOException e) {
                System.out.println("读取数据文件失败");
            }
        }
        public void saveToFile(Path path) {
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path,StandardCharsets.UTF_8);){
                bufferedWriter.write("id,title,description,priority,status,dueDate");
                bufferedWriter.newLine();
                for (Task task : tasks.values()) {
                    bufferedWriter.write(task.getId() + "," + task.getTitle()
                            + "," + task.getDescription()
                            + "," + task.getPriority().name()
                            + "," + task.getStatus().name()
                            + "," + (task.getDueDate() == null ? "" : task.getDueDate())
                    );
                }
                System.out.println("数据已保存至——" + path.toAbsolutePath());
            }catch (IOException e){
                System.out.println("保存数据文件失败：" + e.getMessage());
            }
        }
        void printTasks(List<Task> tasks){
            if (tasks.isEmpty()){
                System.out.println("没有符合条件的任务");
                return;
            }
            System.out.println("\n--- 任务列表 ---");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
        public List<Task> sortedTasks() {
            ArrayList<Task> result = new ArrayList<>(tasks.values());
            result.sort(
                    Comparator.comparing((Task t) -> t.getStatus() == TaskStatus.DOME)
                    .thenComparing(Comparator.comparingInt((Task t) -> t.getPriority().getRank()).reversed())
                            .thenComparing(t -> t.getDueDate(), Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(Task :: getId));
            return result;
        }
        public List<Task> pendingTasks(){
            ArrayList<Task> result = new ArrayList<>(tasks.values());
            return result.stream().filter(t -> t.getStatus() == TaskStatus.PENDING).collect(Collectors.toList());
        }

        public List<Task> search(String keyword) {
            String lowerCase = keyword.toLowerCase(Locale.ROOT);
            ArrayList<Task> result = new ArrayList<>();
            for (Task task : sortedTasks()) {
                if (task.getTitle().toLowerCase(Locale.ROOT).contains(lowerCase)
                        || task.getDescription().toLowerCase(Locale.ROOT).contains(lowerCase)){
                    result.add(task);
                }
            }
            return result;
        }

        public void printStatistics() {
            int pending = 0;
            int done = 0;
            for (Task value : tasks.values()) {
                if (value.getStatus() == TaskStatus.PENDING)pending++;
                else done++;
            }
            System.out.printf("任务总数：%d%n未完成：%d%n已完成：%d%n", tasks.size(), pending, done);
        }


    }
    private static void printMenu() {
        System.out.println("\n===== 待办事项管理系统 =====");
        System.out.println("1. 新增待办");
        System.out.println("2. 完成待办");
        System.out.println("3. 删除待办");
        System.out.println("4. 查看全部待办");
        System.out.println("5. 查看未完成待办");
        System.out.println("6. 按关键词搜索");
        System.out.println("7. 任务统计");
        System.out.println("8. 手动保存");
        System.out.println("0. 保存并退出");
        System.out.print("请选择：");
    }

    public static void main(String[] args) {
        TodoService todoService = new TodoService();
        todoService.loadFromFile(DATA_FILE);
        try (Scanner scanner = new Scanner(System.in)){
            while (true){
                printMenu();
                String choice = scanner.nextLine().trim();
                try {
                    switch (choice){
                        case "1": addTask(scanner,todoService); break;
                        case "2": completeTask(scanner,todoService);break;
                        case "3": deleteTask(scanner, todoService); break;
                        case "4": todoService.printTasks(todoService.sortedTasks()); break;
                        case "5": todoService.printTasks(todoService.pendingTasks()); break;
                        case "6": searchTasks(scanner, todoService); break;
                        case "7": todoService.printStatistics(); break;
                        case "8": todoService.saveToFile(DATA_FILE); break;
                        case "0":
                            todoService.saveToFile(DATA_FILE);
                            System.out.println("数据已保存，程序结束。");
                            return;
                        default: System.out.println("选项无效，请输入 0 到 8。");
                    }
                }catch (IllegalArgumentException | IllegalStateException e){
                    System.out.println("操作失败：" + e.getMessage());
                }
            }
        }
    }

    private static void addTask(Scanner scanner, TodoService todoService) {
        String title = readText(scanner, "请输入任务标题");
        String description = readText(scanner, "请输入任务描述：");
        Priority priority = readPriority(scanner);
        LocalDate dueDate = readDueDate(scanner);
        Task add = todoService.add(title, description, priority, dueDate);
        System.out.println("新增成功:" + add);
    }
    private static void completeTask(Scanner scanner, TodoService todoService) {
        int readPositiveInt = readPositiveInt(scanner, "请输入要完成的任务编号");
        int id = todoService.complete(readPositiveInt);
        System.out.println(id + "任务已完成");
    }
    private static void deleteTask(Scanner scanner, TodoService todoService){
        int readPositiveInt = readPositiveInt(scanner,"请输入要删除的任务编号");
        String delete = todoService.delete(readPositiveInt);
        System.out.println(delete + "已删除");
    }
    private static void searchTasks(Scanner sc, TodoService todoService){
        String keyword = readText(sc, "请输入搜索关键词");
        todoService.printTasks(todoService.search(keyword));
    }
    private static Priority readPriority(Scanner scanner){
        while (true){
            System.out.println("请输入优先级（1-高，2-中，3-低）：");
            switch (scanner.nextLine().trim()){
                case "1": return Priority.HIGH;
                case "2": return Priority.MEDIUM;
                case "3": return Priority.LOW;
                default:
                    System.out.println("请输入1、2或3");
            }
        }
    }
    private static LocalDate readDueDate(Scanner scanner){
        while (true){
            System.out.println("请输入截止日期（yyyy-MM-dd，直接回车表示无截止日期）：");
            String trim = scanner.nextLine().trim();
            if (trim.isEmpty()) return null;
            try {
                return LocalDate.parse(trim);
            }catch (DateTimeException e){
                System.out.println("日期格式错误，例如：2026-12-31。");
            }
        }
    }
    private static String readText(Scanner sc,String sop){
        while (true){
            System.out.println(sop);
            String value = sc.nextLine().trim();
            if (!value.isEmpty() && !value.contains(",")) return value;
            System.out.println("内容不能为空，也不能包含英文逗号。");
        }
    }
    private static int readPositiveInt(Scanner sc,String sop){
        while (true){
            System.out.println(sop);
            try {
                int id = Integer.parseInt(sc.nextLine().trim());
                if (id > 0) return id;
            }catch (NumberFormatException e){
            }
            System.out.println("请输入大于 0 的整数。");
        }
    }
}