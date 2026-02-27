package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.Booking;
import models.Table;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CafeSystem {
    private List<Table> tables = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String FILE_PATH = "data/cafe_data.json";

    public CafeSystem() {
        loadData();
        if (tables.isEmpty()) {
            // Добавляем 10 столиков по умолчанию при первом запуске
            for (int i = 1; i <= 10; i++) {
                tables.add(new Table(i, 4));  // 4 места — можно менять
            }
            saveData();
        }
        System.out.println("Система загружена. Столиков: " + tables.size());
    }

    private void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            new File("data").mkdirs();  // создаём папку data, если нет
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<DataStorage>() {}.getType();
            DataStorage data = gson.fromJson(reader, type);
            if (data != null) {
                tables = (data.tables != null) ? data.tables : new ArrayList<>();
                bookings = (data.bookings != null) ? data.bookings : new ArrayList<>();
            }
        } catch (Exception e) {
            System.out.println("Ошибка загрузки данных: " + e.getMessage());
        }
    }

    public void saveData() {
        new File("data").mkdirs();
        DataStorage data = new DataStorage(tables, bookings);

        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(data, writer);
            System.out.println("Данные сохранены");
        } catch (Exception e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    // Вывод всех столиков
    public void printTables() {
        System.out.println("\nСписок столиков:");
        for (Table t : tables) {
            System.out.println(t);
        }
    }

    // Бронирование столика
    public void bookTable(Scanner scanner) {
        System.out.print("Введите номер столика (1-" + tables.size() + "): ");
        int tableId;
        try {
            tableId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число!");
            return;
        }

        if (tableId < 1 || tableId > tables.size()) {
            System.out.println("Такого столика нет!");
            return;
        }

        Table table = tables.get(tableId - 1);

        if (table.isBooked()) {
            System.out.println("Столик уже забронирован!");
            return;
        }

        System.out.print("Введите дату (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine().trim();

        System.out.print("Время начала (HH:MM): ");
        String startStr = scanner.nextLine().trim();

        System.out.print("ФИО клиента: ");
        String name = scanner.nextLine().trim();

        System.out.print("Телефон: ");
        String phone = scanner.nextLine().trim();

        // Создаём бронь (id = размер списка + 1)
        int bookingId = bookings.size() + 1;
        Booking newBooking = new Booking(bookingId, tableId, dateStr, startStr, "пока без конца", name, phone);
        bookings.add(newBooking);

        table.setBooked(true);

        System.out.println("Столик забронирован успешно! Бронь #" + bookingId);
        saveData();
    }

    // Пока пустой метод для отмены (добавим позже)
    public void cancelBooking(Scanner scanner) {
        System.out.println("Функция отмены бронирования пока в разработке.");
        // Здесь будет логика поиска и удаления брони
    }

    // Метод run — можно вызывать из Main для запуска меню
    public void run(Scanner scanner) {
        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Просмотреть все столики");
            System.out.println("2. Забронировать столик");
            System.out.println("3. Отменить бронирование");
            System.out.println("4. Выход");

            System.out.print("Выберите действие (1-4): ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                printTables();
            } else if (choice.equals("2")) {
                bookTable(scanner);
            } else if (choice.equals("3")) {
                cancelBooking(scanner);
            } else if (choice.equals("4")) {
                saveData();
                System.out.println("До свидания!");
                break;
            } else {
                System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }
}