package models;

public class Table {
    private int id;
    private int seats;
    private boolean booked;

    public Table(int id, int seats) {
        this.id = id;
        this.seats = seats;
        this.booked = false;
    }

    public int getId() { return id; }
    public int getSeats() { return seats; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }

    @Override
    public String toString() {
        return "Столик #" + id + " (" + seats + " мест) — " + (booked ? "ЗАНЯТ" : "СВОБОДЕН");
    }
}