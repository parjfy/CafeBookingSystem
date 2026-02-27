import service.CafeSystem;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CafeSystem system = new CafeSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Система бронирования столиков в кафе ===");
        System.out.println("Добро пожаловать!");

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Просмотреть все столики");
            System.out.println("2. Забронировать столик");
            System.out.println("3. Отменить бронирование");
            System.out.println("4. Выход");

            System.out.print("Выберите действие (1-4): ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                system.printTables();
            } else if (choice.equals("2")) {
                system.bookTable(scanner);
            } else if (choice.equals("3")) {
                system.cancelBooking(scanner);
            } else if (choice.equals("4")) {
                system.saveData();
                System.out.println("До свидания!");
                break;
            } else {
                System.out.println("Неверный выбор, попробуйте снова.");
            }
        }

        scanner.close();
    }
}