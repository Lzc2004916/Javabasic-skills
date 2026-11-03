import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

/**
 * 图书借阅系统：练习对象关系、集合、枚举和业务规则校验。
 */
public class LibraryBorrowingSystem {
    public static void main(String[] args) {
        LibraryService library = new LibraryService();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1":
                            addBook(scanner, library);
                            break;
                        case "2":
                            addUser(scanner, library);
                            break;
                        case "3":
                            borrowBook(scanner, library);
                            break;
                        case "4":
                            returnBook(scanner, library);
                            break;
                        case "5":
                            library.printBooks();
                            break;
                        case "6":
                            library.printUsers();
                            break;
                        case "7":
                            searchBooks(scanner, library);
                            break;
                        case "0":
                            System.out.println("程序结束。");
                            return;
                        default:
                            System.out.println("选项无效，请输入 0 到 7。");
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("操作失败：" + e.getMessage());
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== 图书借阅系统 =====");
        System.out.println("1. 新增图书");
        System.out.println("2. 新增用户");
        System.out.println("3. 借书");
        System.out.println("4. 还书");
        System.out.println("5. 查看全部图书");
        System.out.println("6. 查看全部用户及借阅情况");
        System.out.println("7. 按标题搜索图书");
        System.out.println("0. 退出");
        System.out.print("请选择：");
    }

    private static void addBook(Scanner scanner, LibraryService library) {
        int id = readPositiveInt(scanner, "请输入图书编号：");
        String title = readText(scanner, "请输入图书标题：");
        String author = readText(scanner, "请输入作者：");
        library.addBook(new Book(id, title, author));
        System.out.println("图书添加成功。");
    }

    private static void addUser(Scanner scanner, LibraryService library) {
        int id = readPositiveInt(scanner, "请输入用户编号：");
        String name = readText(scanner, "请输入用户姓名：");
        library.addUser(new User(id, name));
        System.out.println("用户添加成功。");
    }

    private static void borrowBook(Scanner scanner, LibraryService library) {
        int userId = readPositiveInt(scanner, "请输入用户编号：");
        int bookId = readPositiveInt(scanner, "请输入图书编号：");
        library.borrowBook(userId, bookId);
        System.out.println("借书成功。");
    }

    private static void returnBook(Scanner scanner, LibraryService library) {
        int userId = readPositiveInt(scanner, "请输入用户编号：");
        int bookId = readPositiveInt(scanner, "请输入图书编号：");
        library.returnBook(userId, bookId);
        System.out.println("还书成功。");
    }

    private static void searchBooks(Scanner scanner, LibraryService library) {
        String keyword = readText(scanner, "请输入标题关键词：");
        library.searchBooks(keyword);
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // 在循环末尾统一提示。
            }
            System.out.println("请输入大于 0 的整数。");
        }
    }

    private static String readText(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("内容不能为空。");
        }
    }

    enum BookStatus {
        AVAILABLE("可借"), BORROWED("已借出");

        private final String description;

        BookStatus(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    static class Book {
        private final int id;
        private final String title;
        private final String author;
        private BookStatus status;
        private Integer borrowerId;

        Book(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.status = BookStatus.AVAILABLE;
        }

        int getId() {
            return id;
        }

        String getTitle() {
            return title;
        }

        String getAuthor() {
            return author;
        }

        BookStatus getStatus() {
            return status;
        }

        Integer getBorrowerId() {
            return borrowerId;
        }

        void borrowBy(int userId) {
            status = BookStatus.BORROWED;
            borrowerId = userId;
        }

        void returnBook() {
            status = BookStatus.AVAILABLE;
            borrowerId = null;
        }

        @Override
        public String toString() {
            String borrower = borrowerId == null ? "无" : String.valueOf(borrowerId);
            return String.format("编号：%d，书名：%s，作者：%s，状态：%s，借阅人：%s",
                    id, title, author, status, borrower);
        }
    }

    static class User {
        private final int id;
        private final String name;
        private final Set<Integer> borrowedBookIds = new LinkedHashSet<>();

        User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        int getId() {
            return id;
        }

        String getName() {
            return name;
        }

        Set<Integer> getBorrowedBookIds() {
            return borrowedBookIds;
        }

        @Override
        public String toString() {
            return String.format("编号：%d，姓名：%s，已借图书：%s",
                    id, name, borrowedBookIds.isEmpty() ? "无" : borrowedBookIds);
        }
    }

    static class LibraryService {
        private final Map<Integer, Book> books = new LinkedHashMap<>();
        private final Map<Integer, User> users = new LinkedHashMap<>();

        void addBook(Book book) {
            if (books.containsKey(book.getId())) {
                throw new IllegalArgumentException("图书编号已经存在。");
            }
            books.put(book.getId(), book);
        }

        void addUser(User user) {
            if (users.containsKey(user.getId())) {
                throw new IllegalArgumentException("用户编号已经存在。");
            }
            users.put(user.getId(), user);
        }

        void borrowBook(int userId, int bookId) {
            User user = getUser(userId);
            Book book = getBook(bookId);
            if (book.getStatus() == BookStatus.BORROWED) {
                throw new IllegalStateException("这本书已经被借出，借阅人编号：" + book.getBorrowerId());
            }
            book.borrowBy(userId);
            user.getBorrowedBookIds().add(bookId);
        }

        void returnBook(int userId, int bookId) {
            User user = getUser(userId);
            Book book = getBook(bookId);
            if (book.getStatus() == BookStatus.AVAILABLE) {
                throw new IllegalStateException("这本书当前没有被借出。");
            }
            if (!Integer.valueOf(userId).equals(book.getBorrowerId())) {
                throw new IllegalStateException("这本书不是该用户借的，不能归还。");
            }
            book.returnBook();
            user.getBorrowedBookIds().remove(bookId);
        }

        void printBooks() {
            if (books.isEmpty()) {
                System.out.println("暂无图书数据。");
                return;
            }
            List<Book> sortedBooks = new ArrayList<>(books.values());
            sortedBooks.sort(Comparator.comparingInt(Book::getId));
            System.out.println("\n--- 图书列表 ---");
            for (Book book : sortedBooks) {
                System.out.println(book);
            }
        }

        void printUsers() {
            if (users.isEmpty()) {
                System.out.println("暂无用户数据。");
                return;
            }
            System.out.println("\n--- 用户列表 ---");
            for (User user : users.values()) {
                System.out.println(user);
            }
        }

        void searchBooks(String keyword) {
            boolean found = false;
            System.out.println("\n--- 搜索结果 ---");
            for (Book book : books.values()) {
                if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println(book);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("没有找到匹配的图书。");
            }
        }

        private Book getBook(int bookId) {
            Book book = books.get(bookId);
            if (book == null) {
                throw new IllegalArgumentException("图书编号不存在。");
            }
            return book;
        }

        private User getUser(int userId) {
            User user = users.get(userId);
            if (user == null) {
                throw new IllegalArgumentException("用户编号不存在。");
            }
            return user;
        }
    }
}
