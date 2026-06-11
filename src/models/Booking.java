package models;

public class Booking extends Person {
    private int id;
    private int tableId;
    private String date;       // "YYYY-MM-DD"
    private String startTime;  // "HH:MM"
    private String endTime;    // "не указано"

    // 🔥 1. Главный конструктор (используется, например, при загрузке из JSON со всеми полями)
    public Booking(int id, int tableId, String date, String startTime, String endTime,
                   String customerName, String phone) {
        super(customerName, phone);
        this.id = id;
        this.tableId = tableId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = (endTime == null || endTime.trim().isEmpty()) ? "не указано" : endTime;
    }

    // 🔥 2. Перегруженный конструктор (для создания новой брони из GUI, где endTime еще неизвестно)
    public Booking(int id, int tableId, String date, String startTime, String customerName, String phone) {
        this(id, tableId, date, startTime, "не указано", customerName, phone);
    }

    // --- ГЕТТЕРЫ ---
    public int getId() { return id; }
    public int getTableId() { return tableId; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    // Безопасное получение полей родительского класса Person
    public String getCustomerName() { return super.getName(); }
    public String getPhone() { return super.getPhone(); }

    // --- СЕТТЕРЫ (для редактирования админом) ---
    public void setTableId(int tableId) { this.tableId = tableId; }
    public void setDate(String date) { this.date = date; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    // Безопасное изменение полей родительского класса Person
    public void setName(String name) { super.setName(name); }
    public void setPhone(String phone) { super.setPhone(phone); }

    @Override
    public String toString() {
        return "Бронь #" + id +
                " | Столик " + tableId +
                " | " + date + " " + startTime + "–" + endTime +
                " | " + getCustomerName() + " (" + getPhone() + ")";
    }
}