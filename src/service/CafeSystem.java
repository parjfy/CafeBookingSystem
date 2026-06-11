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

            saveData(); // Сохраняем базовый набор один раз
        }
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
            System.out.println("Ошибка загрузки данных: " + e.getMessage());
        }
    }

    public void saveData() {
        new File("data").mkdirs();
        DataStorage data = new DataStorage(tables, bookings);

        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(data, writer);
        } catch (Exception e) {
            System.out.println("Ошибка сохранения данных: " + e.getMessage());
        }
    }

    // Логика бронирования (валидация перенесена сюда, а ошибки улетают в GUI)
    public void bookTable(int tableId, String dateStr, String startStr, String name, String phone) throws Exception {
        if (tableId < 1 || tableId > tables.size()) {
            throw new Exception("Такого столика нет!");
        }

        Table table = tables.get(tableId - 1);
        if (table.isBooked()) {
            throw new Exception("Столик уже забронирован!");
        }

        // Проверка на дублирующую бронь на эту дату
        for (Booking b : bookings) {
            if (b.getTableId() == tableId && b.getDate().equals(dateStr)) {
                throw new Exception("На эту дату уже есть бронь!");
            }
        }

        int bookingId = bookings.size() + 1;
        Booking newBooking = new Booking(bookingId, tableId, dateStr, startStr, "пока без конца", name, phone);
        bookings.add(newBooking);

        table.setBooked(true);
        saveData();
    }

    // Логика отмены бронирования
    public void cancelBooking(int bookingId) throws Exception {
        Booking toRemove = null;
        for (Booking b : bookings) {
            if (b.getId() == bookingId) {
                toRemove = b;
                break;
            }
        }

        if (toRemove == null) {
            throw new Exception("Бронирование с ID #" + bookingId + " не найдено!");
        }

        // Удаляем бронь из списка
        bookings.remove(toRemove);

        // 🔥 ВАЖНО: Освобождаем столик, который был закреплен за этой бронью
        for (Table t : tables) {
            if (t.getId() == toRemove.getTableId()) {
                t.setBooked(false);
                break;
            }
        }

        // Сохраняем обновленные данные в JSON файл
        saveData();
    }

    // Админ: добавить новый столик
    public void addTable(int seats) {
        int newId = tables.size() + 1;
        tables.add(new Table(newId, seats));
        saveData();
    }

    // 1. Метод для быстрого поиска брони по ID
    public Booking getBookingById(int id) {
        for (Booking b : bookings) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    // 2. Метод изменения данных бронирования
    public void updateBooking(int bookingId, int newTableId, String newDate, String newTime, String newName, String newPhone) throws Exception {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            throw new Exception("Бронирование с ID #" + bookingId + " не найдено!");
        }

        // Если админ решил поменять клиенту номер столика
        if (booking.getTableId() != newTableId) {
            Table newTable = null;
            for (Table t : tables) {
                if (t.getId() == newTableId) {
                    newTable = t;
                    break;
                }
            }
            if (newTable == null) {
                throw new Exception("Столик №" + newTableId + " не существует в кафе!");
            }
            if (newTable.isBooked()) {
                throw new Exception("Столик №" + newTableId + " уже занят кем-то другим!");
            }

            // Освобождаем старый столик
            for (Table t : tables) {
                if (t.getId() == booking.getTableId()) {
                    t.setBooked(false);
                    break;
                }
            }
            // Занимаем новый столик
            newTable.setBooked(true);
            booking.setTableId(newTableId);
        }

        // Обновляем остальные текстовые поля
        booking.setDate(newDate);
        booking.setStartTime(newTime);
        booking.setName(newName);
        booking.setPhone(newPhone);

        // Перезаписываем данные в JSON файл, чтобы ничего не пропало при перезапуске
        saveData();
    }

    // Геттер для получения списка столиков графическим интерфейсом
    public List<Table> getTables() {
        return tables;
    }

    // Геттер для получения списка бронирований панелью администратора
    public List<Booking> getBookings() {
        return bookings;
    }
}