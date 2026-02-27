import service.CafeSystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CafeSystem system = new CafeSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Система бронирования столиков в кафе ===");
        System.out.println("Добро пожаловать!");

        // Вход по паролю
        System.out.print("Вход: введите пароль (для админа — admin123, для обычного — Enter): ");
        String password = scanner.nextLine().trim();

        boolean isAdmin = password.equals("admin123");

        if (isAdmin) {
            System.out.println("Вход выполнен как АДМИНИСТРАТОР");
        } else {
            System.out.println("Вход выполнен как ОБЫЧНЫЙ ПОЛЬЗОВАТЕЛЬ");
        }

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Просмотреть все столики");

            if (isAdmin) {
                System.out.println("A. Добавить новый столик");
                System.out.println("B. Просмотреть все бронирования");
                System.out.println("C. Очистить старые брони (старше 30 дней)");
            }

            System.out.println("2. Забронировать столик");
            System.out.println("3. Отменить бронирование");
            System.out.println("4. Выход");

            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("1")) {
                system.printTables();
            } else if (choice.equals("2")) {
                system.bookTable(scanner);
            } else if (choice.equals("3")) {
                system.cancelBooking(scanner);
            } else if (isAdmin && choice.equals("A")) {
                system.addTable(scanner);
            } else if (isAdmin && choice.equals("B")) {
                system.printAllBookings();
            } else if (isAdmin && choice.equals("C")) {
                system.clearOldBookings();
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