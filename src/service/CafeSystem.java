package service;

import java.io.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Booking;
import models.Table;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;

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
            Type type = new TypeToken<DataStorage>() {
            }.getType();
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

    // Пока просто метод для теста — вывести столики
    public void printTables() {
        System.out.println("\nСписок столиков:");
        for (Table t : tables) {
            System.out.println(t);
        }
    }

    // Метод, который вызовем из Main
    public void run() {
        printTables();
        // saveData();  // можно вызывать после каждого изменения
    }
}