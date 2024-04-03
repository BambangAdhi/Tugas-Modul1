import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static Admin admin = new Admin();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        menu();

        while (true) {
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    admin.login();
                    break;
                case 2:
                    System.out.println("Masukkan NIM: ");
                    String nim = input.next();
                    Student student = admin.getStudentByNim(nim);
                    if (student != null) {
                        menuStudent(student);
                    } else {
                        System.out.println("NIM tidak terdaftar!");
                    }
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan aplikasi!");
                    System.exit(0);
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    public static void menu() {
        System.out.println("Selamat datang di aplikasi perpustakaan!");
        System.out.println("1. Login sebagai Admin");
        System.out.println("2. Login sebagai Mahasiswa");
        System.out.println("3. Keluar");
        System.out.print("Masukkan pilihan: ");
    }

    public static void menuStudent(Student student) {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Selamat datang, " + student.getName() + "!");
            System.out.println("1. Lihat Daftar Buku");
            System.out.println("2. Lihat Buku yang Dipinjam");
            System.out.println("3. Pinjam Buku");
            System.out.println("4. Kembalikan Buku");
            System.out.println("5. Logout");
            System.out.print("Masukkan pilihan: ");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    student.displayBooks();
                    break;
                case 2:
                    student.showBorrowedBooks();
                    break;
                case 3:
                    student.borrowBook();
                    break;
                case 4:
                    student.returnBook();
                    break;
                case 5:
                    System.out.println("Terima kasih telah menggunakan aplikasi!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}

class Student {
    private String name;
    private String nim;
    private String faculty;
    private String programStudi;
    private ArrayList<Book> borrowedBooks;

    public Student(String name, String nim, String faculty, String programStudi) {
        this.name = name;
        this.nim = nim;
        this.faculty = faculty;
        this.programStudi = programStudi;
        this.borrowedBooks = new ArrayList<>();
    }

    public void displayInfo() {
        System.out.println("Nama: " + name);
        System.out.println("NIM: " + nim);
        System.out.println("Fakultas: " + faculty);
        System.out.println("Program Studi: " + programStudi);
    }

    public void showBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("Anda tidak memiliki buku yang dipinjam.");
        } else {
            System.out.println("Daftar Buku yang Dipinjam:");
            for (Book book : borrowedBooks) {
                System.out.println(" - " + book.getTitle() + " (" + book.getDueDate() + ")");
            }
        }
    }

    public void displayBooks() {
        ArrayList<Book> bookList;
        bookList = Main.admin.getBookList();
        if (bookList.isEmpty()) {
            System.out.println("Tidak ada buku yang tersedia saat ini.");
            return;
        }

        System.out.println("Daftar Buku yang Tersedia:");
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            System.out.println((i + 1) + ". " + book.getTitle());
        }

        Scanner input = new Scanner(System.in);
        System.out.print("Masukkan nomor buku untuk dipinjam: ");
        int bookChoice = input.nextInt();

        if (bookChoice > 0 && bookChoice <= bookList.size()) {
            Book selectedBook = bookList.get(bookChoice - 1);
            System.out.print("Masukkan jumlah hari peminjaman: ");
            int days = input.nextInt();
            selectedBook.borrowBook(this, days);
            borrowedBooks.add(selectedBook);
        } else {
            System.out.println("Nomor buku tidak valid.");
        }
    }

    public void borrowBook() {
        Scanner input = new Scanner(System.in);
        System.out.println("Masukkan ID Buku: ");
        String bookId = input.nextLine();
        Book book = Main.admin.findBookById(bookId);
        if (book != null) {
            System.out.print("Masukkan jumlah hari peminjaman: ");
            int days = input.nextInt();
            book.borrowBook(this, days);
            borrowedBooks.add(book);
            System.out.println("Buku berhasil dipinjam. Tanggal jatuh tempo: " + book.getDueDate());
        } else {
            System.out.println("Buku tidak ditemukan.");
        }
    }

    public void returnBook() {
        Scanner input = new Scanner(System.in);
        System.out.println("Masukkan ID Buku: ");
        String bookId = input.nextLine();
        Admin admin = new Admin();
        Book book = admin.findBookById(bookId);
        if (book != null) {
            if (borrowedBooks.contains(book)) {
                book.returnBook(this);
                borrowedBooks.remove(book);
                System.out.println("Buku berhasil dikembalikan.");
            } else {
                System.out.println("Anda tidak meminjam buku ini.");
            }
        } else {
            System.out.println("Buku tidak ditemukan.");
        }
    }

    public String getName() {
        return name;
    }

    public String getNim() {
        return nim;
    }

    public LocalDate getDueDate(int days) {
        LocalDate currentDate = LocalDate.now();
        return currentDate.plusDays(days);
    }
}

class Admin {
    private static ArrayList<Student> studentList = new ArrayList<>();
    private static ArrayList<Book> bookList = new ArrayList<>();

    public void addStudent() {
        Scanner input = new Scanner(System.in);

        System.out.print("Masukkan Nama: ");
        String name = input.nextLine();

        String nim;
        do {
            System.out.print("Masukkan NIM (15 digit): ");
            nim = input.nextLine();

            if (nim.length() != 15) {
                System.out.println("NIM tidak valid! Masukkan 15 digit NIM.");
            }
        } while (nim.length() != 15);

        System.out.print("Masukkan fakultas: ");
        String faculty = input.nextLine();

        System.out.print("Masukkan program studi: ");
        String programStudi = input.nextLine();

        // Buat objek Mahasiswa baru dengan informasi yang dikumpulkan
        Student newStudent = new Student(name, nim, faculty, programStudi);

        // Tambahkan mahasiswa baru ke dalam list userStudent
        studentList.add(newStudent);

        System.out.println("Mahasiswa berhasil ditambahkan!");
    }

    public void inputBook() {
        Scanner input = new Scanner(System.in);

        System.out.println("Pilih jenis buku:");
        System.out.println("1. Story Book");
        System.out.println("2. History Book");
        System.out.println("3. Text Book");
        System.out.print("Masukkan pilihan: ");
        int choice = input.nextInt();
        input.nextLine(); // Consume newline character

        String category = "";
        switch (choice) {
            case 1:
                category = "Story Book";
                break;
            case 2:
                category = "History Book";
                break;
            case 3:
                category = "Text Book";
                break;
            default:
                System.out.println("Pilihan tidak valid.");
                return;
        }

        System.out.print("Masukkan judul buku: ");
        String title = input.nextLine();

        System.out.print("Masukkan penulis buku: ");
        String author = input.nextLine();

        System.out.print("Masukkan stok buku: ");
        int stock = input.nextInt();

        // Generate unique ID
        String bookId = generateId();

        // Create new Book object
        Book newBook = new Book(bookId, title, author, category, stock);

        // Add the book to the book list
        bookList.add(newBook);

        System.out.println("Buku berhasil ditambahkan!");
    }

    public void displayBooks() {
        if (bookList.isEmpty()) {
            System.out.println("Tidak ada buku yang tersedia saat ini.");
        } else {
            System.out.println("Daftar Buku yang Tersedia:");
            for (Book book : bookList) {
                book.displayInfo();
                System.out.println();
            }
        }
    }

    public void displayStudent() {
        for (Student student : studentList) {
            student.displayInfo();
            System.out.println();
        }
    }

    public boolean isAdmin(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }

    public String generateId() {
        // Generate unique ID logic
        return "B" + (bookList.size() + 1); // Example: B1, B2, B3, ...
    }

    public void login() {
        Scanner input = new Scanner(System.in);
        System.out.print("Masukkan username: ");
        String username = input.nextLine();
        System.out.print("Masukkan password: ");
        String password = input.nextLine();
        if (isAdmin(username, password)) {
            System.out.println("Login berhasil!");
            adminMenu();
        } else {
            System.out.println("Username atau password salah.");
        }
    }

    public void adminMenu() {
        Scanner input = new Scanner(System.in);
        boolean isAdminLoggedIn = true;
        while (isAdminLoggedIn) {
            System.out.println("Menu Admin:");
            System.out.println("1. Tambah Mahasiswa");
            System.out.println("2. Tambah Buku");
            System.out.println("3. Lihat Daftar Mahasiswa");
            System.out.println("4. Lihat Daftar Buku");
            System.out.println("5. Logout");
            System.out.print("Masukkan pilihan: ");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    inputBook();
                    break;
                case 3:
                    displayStudent();
                    break;
                case 4:
                    displayBooks();
                    break;
                case 5:
                    isAdminLoggedIn = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
        // Setelah keluar dari loop, tampilkan kembali menu utama
        Main.menu();
    }

    public Student getStudentByNim(String nim) {
        for (Student student : studentList) {
            if (student.getNim().equals(nim)) {
                return student;
            }
        }
        return null;
    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public Book findBookById(String bookId) {
        for (Book book : bookList) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }
}

class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int stock;
    private int duration; // Durasi peminjaman
    private Student borrower; // Peminjam buku

    public Book(String bookId, String title, String author, String category, int stock) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.stock = stock;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void borrowBook(Student student, int days) {
        if (stock > 0) {
            stock--;
            duration = days;
            borrower = student;
            System.out.println("Berhasil meminjam buku. Tanggal jatuh tempo: " + getDueDate());
        } else {
            System.out.println("Maaf, stok buku tidak mencukupi.");
        }
    }

    public void returnBook(Student student) {
        if (borrower != null && borrower.equals(student)) { // Pastikan buku dipinjam oleh student
            stock++;
            borrower = null; // Reset 'borrower'
            duration = 0; // Reset 'duration'
            System.out.println("Buku berhasil dikembalikan.");
        } else {
            System.out.println("Buku ini tidak dipinjam oleh Anda.");
        }
    }

    public void displayInfo() {
        System.out.println("ID Buku: " + bookId);
        System.out.println("Judul: " + title);
        System.out.println("Penulis: " + author);
        System.out.println("Kategori: " + category);
        System.out.println("Stok: " + stock);
        if (borrower != null) {
            System.out.println("Dipinjam oleh: " + borrower.getName());
            System.out.println("Tanggal jatuh tempo: " + getDueDate()); // Panggil method 'getDueDate'
        }
    }


    public String getDueDate() {
        // Implementasi untuk menghitung tanggal jatuh tempo
        // berdasarkan tanggal peminjaman dan durasi
        // Menggunakan java.time.LocalDate
        LocalDate today = LocalDate.now();
        return today.plusDays(duration).toString();
    }
}



