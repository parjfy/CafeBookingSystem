package service;

import models.Booking;
import models.Table;

import java.util.List;

public class DataStorage {
    public List<Table> tables;
    public List<Booking> bookings;

    // Пустой конструктор нужен для Gson
    public DataStorage() {}

    public DataStorage(List<Table> tables, List<Booking> bookings) {
        this.tables = tables;
        this.bookings = bookings;
    }
}