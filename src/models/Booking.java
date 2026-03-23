package models;

public class Booking extends Person {
    private int id;
    private int tableId;
    private String date;       // "YYYY-MM-DD"
    private String startTime;  // "HH:MM"
    private String endTime;    // "не указано"

    public Booking(int id, int tableId, String date, String startTime, String endTime,
                   String customerName, String phone) {
        super(customerName, phone); // наследуем
        this.id = id;
        this.tableId = tableId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() { return id; }
    public int getTableId() { return tableId; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    public String getCustomerName() { return name; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return "Бронь #" + id +
                " | Столик " + tableId +
                " | " + date + " " + startTime + "–" + endTime +
                " | " + name + " (" + phone + ")";
    }
}