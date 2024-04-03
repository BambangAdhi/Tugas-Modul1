import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<String> bookList = new ArrayList<>();
    private static Admin admin = new Admin();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Menambahkan data buku
        getBookList().add("Buku 1: Kancil Kecil - Penulis: Yanto - Seri: 12345");
        getBookList().add("Buku 2: Kancil Besar - Penulis: Adhi - Seri: 54321");

        menu();

        while (true) {
            int pilihan = input.nextInt();
            switch (pilihan) {
                case 1:
                    admin.login();
                    break;
                case 2:
                    String nim = inputNim();
                    if (admin.checkNim(nim)) {
                        menuStudent(nim);
                    } else {
                        System.out.println("NIM tidak valid!");
                    }
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan aplikasi!");
                    System.exit(0);
                    break;
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

    private static String inputNim() {
        Scanner input = new Scanner(System.in);
        System.out.print("Masukkan NIM: ");
        return input.nextLine();
    }

    public static void menuStudent(String nim) {
        System.out.println("Selamat datang " + admin.getNamaByNim(nim) + "!");
        System.out.println("1. Lihat Daftar Buku");
        System.out.println("2. Logout");
        System.out.print("Masukkan pilihan: ");

        Scanner input = new Scanner(System.in);
        int pilihan = input.nextInt();

        switch (pilihan) {
            case 1:
                displayBooks();
                askContinueToMainMenu();
                break;
            case 2:
                logout();
                break;
            default:
                System.out.println("Pilihan tidak valid!");
                askContinueToMainMenu();
        }
    }

    private static void askContinueToMainMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("Apakah ingin kembali ke Menu Utama?");
        System.out.println("1. Ya");
        System.out.println("2. Tidak (Keluar)");
        System.out.print("Masukkan pilihan: ");
        int choice = input.nextInt();

        if (choice == 1) {
            Main.menu();  // Kembali ke menu utama setelah menampilkan daftar buku
        } else if (choice == 2) {
            System.out.println("Terima kasih telah menggunakan aplikasi!");
            System.exit(0);
        } else {
            System.out.println("Pilihan tidak valid!");
            askContinueToMainMenu();
        }
    }

    private static void displayBooks() {
        for (String s : getBookList()) {
            System.out.println(s);
        }
    }

    private static void logout() {
        menu();
    }

    public static ArrayList<String> getBookList() {
        return bookList;
    }
}

class Admin {

    private static ArrayList<Student> userStudent = new ArrayList<>();

    public void login() {
        Scanner input = new Scanner(System.in);

        System.out.print("Masukkan username admin: ");
        String username = input.nextLine();

        System.out.print("Masukkan password admin: ");
        String password = input.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            menuAdmin();
        } else {
            System.out.println("Username atau password salah!");
            login();
        }
    }

    private void menuAdmin() {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("Selamat datang Admin!");
            System.out.println("1. Tambah Mahasiswa");
            System.out.println("2. Lihat Daftar Mahasiswa");
            System.out.println("3. Keluar");
            System.out.print("Masukkan pilihan: ");

            int pilihan = input.nextInt();

            switch (pilihan) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    if (userStudent.isEmpty()) {
                        System.out.println("Belum ada daftar mahasiswa. Pilih 1 untuk tambahkan daftar mahasiswa.");
                    } else {
                        displayStudent();
                    }
                    break;
                case 3:
                    Main.menu();
                    return;  // Kembali langsung dari metode setelah keluar dari menu admin
                default:
                    System.out.println("Pilihan tidak valid!");
            }

            System.out.println("Apakah ingin melanjutkan ke menu Admin?");
            System.out.println("1. Ya");
            System.out.println("2. Tidak (Keluar)");
            System.out.print("Masukkan pilihan: ");
            int choice = input.nextInt();

            if (choice != 1) {
                Main.menu();  // Kembali ke menu utama setelah keluar dari menu admin
                break;       // Keluar dari loop setelah kembali ke menu utama
            }
        }
    }
    public void addStudent() {
        Scanner input = new Scanner(System.in);

        System.out.print("Masukkan Nama: ");
        String nama = input.nextLine();

        String nim;
        do {
            System.out.print("Masukkan NIM (15 digit): ");
            nim = input.nextLine();

            if (nim.length() != 15) {
                System.out.println("NIM tidak valid! Masukkan 15 digit NIM.");
            }
        } while (nim.length() != 15);

        System.out.print("Masukkan fakultas: ");
        String fakultas = input.nextLine();

        System.out.print("Masukkan program studi: ");
        String prodi = input.nextLine();

        // Buat objek Mahasiswa baru dengan informasi yang dikumpulkan
        Student newStudent = new Student(nim, nama, fakultas, prodi);

        // Tambahkan mahasiswa baru ke dalam list userStudent
        userStudent.add(newStudent);

        System.out.println("Mahasiswa berhasil ditambahkan!");
    }


    public void displayStudent() {
        for (Student student : userStudent) {
            System.out.println("Nama: " + student.getNama());
            System.out.println("NIM: " + student.getNim());
            System.out.println("Fakultas: " + student.getFakultas());
            System.out.println("Prodi: " + student.getProdi());
            System.out.println();
        }
    }

    public String getNamaByNim(String nim) {
        for (Student student : userStudent) {
            if (student.getNim().equals(nim)) {
                return student.getNama();
            }
        }
        return null;
    }

    public boolean checkNim(String nim) {
        for (Student student : userStudent) {
            if (student.getNim().equals(nim)) {
                return true;
            }
        }
        return false;
    }
}

class Student {

    private String nim;
    private String nama;
    private String fakultas;
    private String prodi;

    public Student(String nim, String nama, String fakultas, String prodi) {
        this.nim = nim;
        this.nama = nama;
        this.fakultas = fakultas;
        this.prodi = prodi;
    }

    public String getNim() {
        return nim;
    }

    public String getNama() {
        return nama;
    }

    public String getFakultas() {
        return fakultas;
    }

    public String getProdi() {
        return prodi;
    }
}