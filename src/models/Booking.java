package models;

public class Booking {
    private int id;
    private int tableId;
    private String date;       // "2025-12-31"
    private String startTime;  // "14:30"
    private String endTime;    // "16:00" или "не указано"
    private String customerName;
    private String phone;

    // Конструктор со строками (используем его)
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

    // Геттеры
    public int getId() { return id; }
    public int getTableId() { return tableId; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return "Бронь #" + id +
                " | Столик " + tableId +
                " | " + date + " " + startTime + "–" + endTime +
                " | " + customerName + " (" + phone + ")";
    }
}