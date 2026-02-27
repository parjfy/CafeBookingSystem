import service.CafeSystem;

public class Main {
    public static void main(String[] args) {
        CafeSystem system = new CafeSystem();
        system.run();

        // Тест: забронируем первый столик вручную (потом сделаем меню)
        // system.tables.get(0).setBooked(true);
        // system.saveData();

        // Запусти 2 раза — второй раз должно загрузить данные из файла
    }
}