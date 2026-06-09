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

        // Если после загрузки список столиков пустой, заполняем его дефолтными
        if (tables.isEmpty()) {
            tables.add(new Table(1, 2));  // маленький
            tables.add(new Table(2, 2));  // маленький
            tables.add(new Table(3, 4));  // средний
            tables.add(new Table(4, 4));  // средний
            tables.add(new Table(5, 4));  // средний
            tables.add(new Table(6, 6));  // большой
            tables.add(new Table(7, 6));  // большой
            tables.add(new Table(8, 8));  // очень большой
            tables.add(new Table(9, 8));  // очень большой
            tables.add(new Table(10, 4)); // средний
            tables.add(new Table(11, 12));// очень большой

            System.out.println("Созданы дефолтные столики.");
            saveData(); // Сохраняем базовый набор один раз
        }

        System.out.println("Система загружена. Столиков: " + tables.size());
        System.out.println("Бронирований загружено: " + bookings.size());
    }

    private void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            new File("data").mkdirs();
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
            System.out.println("Ошибка загрузки: " + e.getMessage());
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

    // Красивый вывод столиков в таблице
    public void printTables() {
        System.out.println("\nСтатус столиков:");
        System.out.printf("%-10s %-15s %-20s %-10s\n", "Номер", "Мест", "Для компании", "Статус");
        System.out.println("------------------------------------------------------------");
        for (Table t : tables) {
            String type = (t.getSeats() <= 2) ? "Маленькая" :
                    (t.getSeats() <= 4) ? "Средняя" :
                            (t.getSeats() <= 6) ? "Большая" : "Очень большая";
            System.out.printf("%-10d %-15d %-20s %-10s\n",
                    t.getId(), t.getSeats(), type, t.isBooked() ? "ЗАНЯТ" : "СВОБОДЕН");
        }
    }

    public void bookTable(Scanner scanner) {
        System.out.print("Введите номер столика (1-" + tables.size() + "): ");
        int tableId = readInt(scanner, "Ошибка: введите число!");

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

        // 🔥 Проверка на дублирующую бронь на эту дату
        for (Booking b : bookings) {
            if (b.getTableId() == tableId && b.getDate().equals(dateStr)) {
                System.out.println("На эту дату уже есть бронь!");
                return;
            }
        }

        System.out.print("Время начала (HH:MM): ");
        String startStr = scanner.nextLine().trim();

        System.out.print("ФИО клиента: ");
        String name = scanner.nextLine().trim();

        System.out.print("Телефон: ");
        String phone = scanner.nextLine().trim();

        int bookingId = bookings.size() + 1;
        Booking newBooking = new Booking(bookingId, tableId, dateStr, startStr, "пока без конца", name, phone);
        bookings.add(newBooking);

        table.setBooked(true);
        System.out.println("Столик забронирован! Бронь #" + bookingId);
        saveData();
    }

    public void cancelBooking(Scanner scanner) {
        System.out.print("Введите номер брони для отмены: ");
        int bookingId = readInt(scanner, "Ошибка: введите число!");

        Booking toRemove = null;
        for (Booking b : bookings) {
            if (b.getId() == bookingId) {
                toRemove = b;
                break;
            }
        }

        if (toRemove == null) {
            System.out.println("Бронь #" + bookingId + " не найдена.");
            return;
        }

        for (Table t : tables) {
            if (t.getId() == toRemove.getTableId()) {
                t.setBooked(false);
                break;
            }
        }

        bookings.remove(toRemove);
        System.out.println("Бронь #" + bookingId + " отменена.");
        saveData();
    }

    // Админ: добавить новый столик
    public void addTable(Scanner scanner) {
        System.out.print("Введите количество мест для нового столика: ");
        int seats = readInt(scanner, "Ошибка: введите число!");

        int newId = tables.size() + 1;
        tables.add(new Table(newId, seats));
        System.out.println("Добавлен столик #" + newId + " (" + seats + " мест)");
        saveData();
    }

    // Админ: показать все бронирования
    public void printAllBookings() {
        if (bookings.isEmpty()) {
            System.out.println("Бронирований пока нет.");
            return;
        }

        System.out.println("\nВсе бронирования:");
        System.out.println("-----------------------------------");

        for (Booking b : bookings) {
            System.out.println(b); // 🔥 теперь используется toString()
        }
    }

    // Админ: очистка старых броней (заглушка, можно доработать)
    public void clearOldBookings() {
        System.out.println("Очистка старых броней (пока не реализовано полностью).");
        // Здесь можно добавить логику сравнения дат с текущей
        saveData();
    }

    // Вспомогательный метод для чтения int с обработкой ошибок
    private int readInt(Scanner scanner, String errorMsg) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(errorMsg);
            return -1; // или можно зациклить запрос
        }
    }
}