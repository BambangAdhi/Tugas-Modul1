import java.util.Scanner;

public class LibraryLoginSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Data pengguna
        String adminUsername = "admin";
        String adminPassword = "admin123";

        boolean isLoggedIn = false;

        while (!isLoggedIn) {
            // Input dari pengguna
            System.out.print("Masukkan jenis pengguna (admin/mahasiswa): ");
            String userType = scanner.nextLine();

            if (userType.equalsIgnoreCase("admin")) {
                isLoggedIn = loginAdmin(scanner, adminUsername, adminPassword);

            } else if (userType.equalsIgnoreCase("mahasiswa")) {
                isLoggedIn = loginMahasiswa(scanner);

            } else {
                System.out.println("Jenis pengguna tidak valid. Silakan coba lagi.");
            }
        }

        scanner.close();
    }

    private static boolean loginAdmin(Scanner scanner, String adminUsername, String adminPassword) {
        System.out.print("Masukkan username admin: ");
        String usernameInput = scanner.nextLine();
        System.out.print("Masukkan password admin: ");
        String passwordInput = scanner.nextLine();

        if (usernameInput.equals(adminUsername) && passwordInput.equals(adminPassword)) {
            System.out.println("Login berhasil sebagai admin.");
            return true;
        } else {
            System.out.println("Login gagal. Username atau password salah. Silakan coba lagi.");
            return false;
        }
    }

    private static boolean loginMahasiswa(Scanner scanner) {
        System.out.print("Masukkan NIM mahasiswa (panjang 15 digit): ");
        String nimInput = scanner.nextLine();

        if (nimInput.length() == 15 && nimInput.matches("\\d+")) {
            System.out.println("Login berhasil sebagai mahasiswa.");
            return true;
        } else {
            System.out.println("Login gagal. Panjang NIM harus 15 digit dan hanya boleh berisi angka. Silakan coba lagi.");
            return false;
        }
    }
}
