package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private int id;
    private int tableId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String customerName;
    private String phone;

    public Booking(int id, int tableId, String date, String startTime, String endTime,
                   String customerName, String phone) {
        this.id = id;
        this.tableId = tableId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerName = customerName;
        this.phone = phone;
    }

    public Booking(int bookingId, int tableId, String dateStr, String startStr, String покаБезКонца, String name, String phone) {
    }

    // геттеры (добавь сам, если нужно больше)
    public int getTableId() { return tableId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }

    @Override
    public String toString() {
        return "Бронь #" + id + " | Столик " + tableId + " | " + date +
                " " + startTime + "–" + endTime + " | " + customerName;
    }
}