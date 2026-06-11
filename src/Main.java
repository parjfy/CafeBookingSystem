import ui.MainForm;
import ui.SplashScreen;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Локальные цвета для настройки UI
        Color bgDark = new Color(33, 33, 33);
        Color bgLighter = new Color(43, 43, 43);
        Color fgLight = new Color(240, 240, 240);
        Color accentBlue = new Color(25, 118, 210);
        Color bgField = new Color(55, 55, 55);

        try {
            // Глобальные настройки отображения для наших темных окошек
            UIManager.put("Label.foreground", fgLight);
            UIManager.put("Panel.background", bgDark);
            UIManager.put("OptionPane.background", bgDark);
            UIManager.put("OptionPane.messageForeground", fgLight);

            // Настройка вкладок
            UIManager.put("TabbedPane.background", bgDark);
            UIManager.put("TabbedPane.foreground", fgLight);
            UIManager.put("TabbedPane.selected", accentBlue);
            UIManager.put("TabbedPane.unselectedBackground", bgLighter);
            UIManager.put("TabbedPane.borderHighlightColor", bgDark);
            UIManager.put("TabbedPane.darkShadow", bgDark);
            UIManager.put("TabbedPane.light", bgDark);
            UIManager.put("TabbedPane.shadow", bgDark);

            UIManager.put("Button.opaque", true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1. Сначала крутим заставку
        SplashScreen splash = new SplashScreen();
        splash.runLoading(2500);

        // 2. Логика авторизации перед запуском основного интерфейса
        SwingUtilities.invokeLater(() -> {

            // Создаем красивую темную панельку для ввода кода
            JPanel authPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            JLabel label = new JLabel("Введите код доступа для запуска системы:");
            label.setFont(new Font("Arial", Font.PLAIN, 13));

            JPasswordField txtCode = new JPasswordField(10);
            txtCode.setBackground(bgField);
            txtCode.setForeground(fgLight);
            txtCode.setCaretColor(Color.WHITE);
            txtCode.setHorizontalAlignment(JTextField.CENTER);
            txtCode.setFont(new Font("Arial", Font.BOLD, 16));
            txtCode.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));

            authPanel.add(label);
            authPanel.add(txtCode);

            boolean accessGranted = false;
            String masterCode = "123"; // 🔥 Твой секретный код для старта системы

            while (!accessGranted) {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        authPanel,
                        "Вход в систему",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                // Если пользователь нажал ОК
                if (result == JOptionPane.OK_OPTION) {
                    String inputCode = new String(txtCode.getPassword());

                    if (inputCode.equals(masterCode)) {
                        accessGranted = true; // Код верный, выходим из цикла
                    } else {
                        // Показываем стильную ошибку
                        UIManager.put("OptionPane.background", bgDark);
                        UIManager.put("Panel.background", bgDark);
                        JOptionPane.showMessageDialog(
                                null,
                                "Неверный код доступа! Доступ отклонен.",
                                "Ошибка авторизации",
                                JOptionPane.ERROR_MESSAGE
                        );
                        txtCode.setText(""); // Сбрасываем поле ввода
                    }
                } else {
                    // Если нажали "Отмена" или закрыли крестиком — полностью тушим приложение
                    System.exit(0);
                }
            }

            // 3. Если код подошел — открываем главное окно
            MainForm form = new MainForm();
            form.setVisible(true);
        });
    }
}