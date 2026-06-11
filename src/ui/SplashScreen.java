package ui;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private JProgressBar progressBar;

    public SplashScreen() {
        // Главная панель заставки
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(33, 33, 33)); // Глубокий темный фон
        content.setBorder(BorderFactory.createLineBorder(new Color(25, 118, 210), 2)); // Синяя аккуратная рамка

        // Красивый заголовок с иконкой
        JLabel title = new JLabel("☕ Cafe Booking System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));

        // Текст статуса загрузки
        JLabel statusLabel = new JLabel("Инициализация модулей системы...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(170, 170, 170));

        // Настройка индикатора загрузки (Progress Bar)
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true); // Показывать проценты %
        progressBar.setForeground(new Color(25, 118, 210)); // Синий индикатор
        progressBar.setBackground(new Color(50, 50, 50));
        progressBar.setFont(new Font("Arial", Font.BOLD, 11));
        progressBar.setBorderPainted(false);

        // Компонуем нижнюю часть (текст + полоса загрузки)
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBackground(new Color(33, 33, 33));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 30, 25));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);

        content.add(title, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(content);
        setSize(450, 230); // Размер окошка заставки
        setLocationRelativeTo(null); // Строго по центру экрана
    }

    // Метод для запуска анимации загрузки
    public void runLoading(int durationMs) {
        setVisible(true);
        try {
            int steps = 100;
            int delay = durationMs / steps;
            for (int i = 0; i <= steps; i++) {
                Thread.sleep(delay);
                progressBar.setValue(i); // Заполняем полосу
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);
        dispose(); // Полностью уничтожаем окно заставки из памяти
    }
}