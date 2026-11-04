package Contact;

import java.util.*;

public class demo3_LibraryBorrowingSystem {
    private static int readPositiveInt(Scanner scanner,String prompt){
        System.out.println(prompt);
        while (true){
            try {
                int bookId = Integer.parseInt(scanner.nextLine());
                if (bookId > 0) return bookId;
            }catch (NumberFormatException e){

            }
            System.out.println("请输入大于0的整数");
        }
    }
    private static String readText(Scanner scanner,String prompt){
        while (true){
            System.out.println(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("内容不能为空");
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
    private static void addBook(Scanner scanner,LibraryService library){
        int id = readPositiveInt(scanner, "请输入图书编号:");
        if (library.containsBook(id)) throw new IllegalArgumentException("图书编号已存在");
        String keyword = readText(scanner, "请输入图书标题");
        String author = readText(scanner, "请输入作者:");
        library.addBook(new Book(id,keyword,author));
    }
    private static void addUser(Scanner scanner, LibraryService library) {
        int id = readPositiveInt(scanner,"请输入用户编号");
        String name = readText(scanner, "请输入用户姓名");
        library.addUser(new User(id,name));
    }
    private static void returnBook(Scanner scanner, LibraryService library) {
        int userId = readPositiveInt(scanner, "请输入用户编号");
        int bookId = readPositiveInt(scanner, "请输入图书编号");
        library.returnBook(userId,bookId);
        System.out.println("还书成功");
    }

    private static void borrowBook(Scanner scanner, LibraryService library) {
        int userId = readPositiveInt(scanner, "请输入用户编号");
        int bookId = readPositiveInt(scanner, "请输入图书编号");
        library.borrowBook(userId,bookId);
        System.out.println("借书成功");
    }
    private static void searchBooks(Scanner scanner, LibraryService library) {
        String title = readText(scanner, "请输入标题关键词:");
        library.searchBooks(title);
    }
    public static void main(String[] args) {
        LibraryService library = new LibraryService();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true){
                printMenu();
                String choice = scanner.nextLine().trim();
                try {
                    switch (choice){
                        case "1" : addBook(scanner,library); break;
                        case "2" : addUser(scanner,library); break;
                        case "3" : borrowBook(scanner,library); break;
                        case "4" : returnBook(scanner,library); break;
                        case "5" : library.printBooks();break;
                        case "6" : library.printUsers();break;
                        case "7" : searchBooks(scanner,library); break;
                        case "0" :
                            System.out.println("程序结束");
                            return;
                        default:
                            System.out.println("选项无效");
                    }
                }catch (IllegalArgumentException | IllegalStateException e){
                    System.out.println("操作失败:"+ e.getMessage());
                }
            }
        }
    }




    enum BookStatus{
        AVAILABLE("可借"),
        BORROWED("已借出");
        private final String description;

        BookStatus(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
    static class Book{
        private final int id;
        private final String title;
        private final String author;
        private  BookStatus status;
        private Integer borrowerId;

        public Book(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.status = BookStatus.AVAILABLE;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public Integer getBorrowerId() {
            return borrowerId;
        }
        void borrowBy(int userId){
            status = BookStatus.BORROWED;
            borrowerId = userId;
        }
        void returnBook(){
            status = BookStatus.AVAILABLE;
        }
        @Override
        public String toString() {
            String borrower = borrowerId == null ? "无" : String.valueOf(borrowerId);
            return String.format("编号：%d，书名：%s，作者：%s，状态：%s，借阅人：%s",
                    id, title, author, status, borrower);
        }
    }
    static class User{
        private final int id;
        private final String name;
        private final Set<Integer> borrowedBookIds = new LinkedHashSet<>();

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
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
    static class LibraryService{
        private final Map<Integer,Book> books = new LinkedHashMap<>();
        private final Map<Integer,User> users = new LinkedHashMap<>();
        boolean containsBook(int id){
            return books.containsKey(id);
        }
        void addBook(Book book){
            books.put(book.getId(), book);
        }

        public void addUser(User user) {
            if (users.containsKey(user.getId())) throw new IllegalArgumentException("用户编号已存在");
            users.put(user.getId(), user);
        }

        public void borrowBook(int userId, int bookId) {
            User user = getUser(userId);
            Book book = getBook(bookId);
            if (book.status == BookStatus.BORROWED) throw new IllegalStateException("这本书已被借出");
            book.borrowBy(userId);
            user.getBorrowedBookIds().add(book.getId());
        }
        public void returnBook(int userId, int bookId) {
            User user = getUser(userId);
            Book book = getBook(bookId);
            if (!user.getBorrowedBookIds().contains(book.getId())) throw new IllegalArgumentException("该用户未借此书");
            user.getBorrowedBookIds().remove(book.getId());
            book.returnBook();
        }
        public void printBooks() {
            if (books.isEmpty()) {
                System.out.println("暂无图书数据");
                return;
            }
            ArrayList<Book> bookList = new ArrayList<>(books.values());
            bookList.sort(Comparator.comparing((Book b) -> b.status != BookStatus.BORROWED)
                    .thenComparing(Comparator.comparingInt(Book::getId).reversed()));
            for (Book book : bookList) {
                System.out.println(book);
            }
        }
        public void printUsers() {
            if (users.isEmpty()){
                System.out.println("暂无学生");
                return;
            }
            ArrayList<User> userList = new ArrayList<>(users.values());
            userList.sort(Comparator.comparingInt(User :: getId));
            for (User user : userList) {
                System.out.println(user);
            }
        }
        public void searchBooks(String title) {
            boolean found = false;
            System.out.println("\n--- 搜索结果 ---");
            for (Book book : books.values()) {
                if (book.getTitle().toLowerCase().contains(title.toLowerCase())){
                    System.out.println(book);
                    found = true;
                }
            }
            if (!found) System.out.println("没有找到匹配的图书");
        }
        private Book getBook(int bookId){
            Book book = books.get(bookId);
            if (book == null) throw new IllegalArgumentException("图书编号不存在");
            return book;
        }
        private User getUser(int userId){
            User user = users.get(userId);
            if (user == null) throw new IllegalArgumentException("用户编号不存在");
            return user;
        }



    }
}