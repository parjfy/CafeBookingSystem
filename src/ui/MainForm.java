package ui;

import models.Booking;
import models.Table;
import service.CafeSystem;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.DocumentFilter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainForm extends JFrame {
    private CafeSystem system;

    // 🔥 ЦВЕТОВАЯ ПАЛИТРА ДЛЯ ТЁМНОЙ ТЕМЫ
    private final Color BG_DARK = new Color(33, 33, 33);       // Глубокий тёмный фон
    private final Color BG_LIGHTER = new Color(43, 43, 43);    // Чуть светлее для панелей
    private final Color BG_FIELD = new Color(55, 55, 55);      // Для полей ввода и таблиц
    private final Color FG_LIGHT = new Color(240, 240, 240);    // Белый/светло-серый текст
    private final Color ACCENT_BLUE = new Color(25, 118, 210);  // Синий акцент

    // Компоненты для Клиентской зоны
    private JTextField txtTableId = new JTextField(10);
    private JTextField txtName = new JTextField(15);
    private JTextField txtPhone = new JTextField(15);

    private JSpinner spinnerDate;
    private JSpinner spinnerTime;

    private JButton btnBook = new JButton("Забронировать");
    private JButton btnReset = new JButton("Сбросить");

    private JTextField txtCancelBookingId = new JTextField(10);
    private JButton btnCancelBooking = new JButton("Отменить бронь");

    // 🔥 Новая кнопка выхода
    private JButton btnExit = new JButton("Выход");

    private DefaultTableModel tableModel;
    private JTable statusTable;

    // Компоненты для Панели администратора
    private JPanel adminContentPanel = new JPanel(new BorderLayout(10, 10));
    private JButton btnAdminLogin = new JButton("Войти как Администратор");
    private JTextField txtNewTableSeats = new JTextField(10);
    private JButton btnAddTable = new JButton("Добавить столик");
    private JTextArea txtAreaBookings = new JTextArea(15, 40);
    private JButton btnEditBooking = new JButton("Изменить данные брони");

    private boolean isAdminMode = false;

    public MainForm() {
        system = new CafeSystem();

        setTitle("Cafe Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 580);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        // Стилизация кнопок
        styleButton(btnBook, new Color(46, 125, 50)); // Зелёная
        styleButton(btnReset, new Color(117, 117, 117)); // Серая
        styleButton(btnCancelBooking, new Color(198, 40, 40)); // Красная
        styleButton(btnExit, new Color(90, 90, 90)); // Тёмно-серая для выхода
        styleButton(btnEditBooking, ACCENT_BLUE); // Синяя
        styleButton(btnAddTable, ACCENT_BLUE); // Синяя
        styleButton(btnAdminLogin, ACCENT_BLUE);

        // Инициализация спиннеров
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        spinnerDate = new JSpinner(dateModel);
        spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "yyyy-MM-dd"));
        styleSpinner(spinnerDate);

        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        spinnerTime = new JSpinner(timeModel);
        spinnerTime.setEditor(new JSpinner.DateEditor(spinnerTime, "HH:mm"));
        styleSpinner(spinnerTime);

        // Стилизация текстовых полей
        styleTextField(txtTableId);
        styleTextField(txtName);
        styleTextField(txtPhone);
        styleTextField(txtCancelBookingId);
        styleTextField(txtNewTableSeats);

        // Валидация полей
        setDigitsOnlyFilter(txtTableId);
        setDigitsOnlyFilter(txtPhone);
        setDigitsOnlyFilter(txtCancelBookingId);
        setDigitsOnlyFilter(txtNewTableSeats);
        setLettersOnlyFilter(txtName);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_DARK);
        tabbedPane.setForeground(FG_LIGHT);

        // 1. КЛИЕНТСКАЯ ВКЛАДКА
        JPanel clientPanel = new JPanel(new BorderLayout(15, 15));
        clientPanel.setBackground(BG_DARK);
        clientPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel clientLeftPanel = new JPanel();
        clientLeftPanel.setBackground(BG_DARK);
        clientLeftPanel.setLayout(new BoxLayout(clientLeftPanel, BoxLayout.Y_AXIS));

        JPanel bookGrid = new JPanel(new GridLayout(6, 2, 8, 8));
        bookGrid.setBackground(BG_LIGHTER);
        bookGrid.setBorder(createCustomBorder("Новое бронирование"));
        addLabel(bookGrid, "Номер столика:"); bookGrid.add(txtTableId);
        addLabel(bookGrid, "Дата:"); bookGrid.add(spinnerDate);
        addLabel(bookGrid, "Время:"); bookGrid.add(spinnerTime);
        addLabel(bookGrid, "ФИО Клиента:"); bookGrid.add(txtName);
        addLabel(bookGrid, "Телефон (цифры):"); bookGrid.add(txtPhone);
        bookGrid.add(btnReset); bookGrid.add(btnBook);

        JPanel cancelGrid = new JPanel(new GridLayout(2, 2, 8, 8));
        cancelGrid.setBackground(BG_LIGHTER);
        cancelGrid.setBorder(createCustomBorder("Отмена брони"));
        addLabel(cancelGrid, "ID Брони:"); cancelGrid.add(txtCancelBookingId);
        cancelGrid.add(new JLabel("")); cancelGrid.add(btnCancelBooking);

        clientLeftPanel.add(bookGrid);
        clientLeftPanel.add(Box.createVerticalStrut(15));
        clientLeftPanel.add(cancelGrid);
        clientLeftPanel.add(Box.createVerticalGlue());

        // Настройка таблицы мониторинга зала
        String[] columns = {"№ Стола", "Кол-во мест", "Текущий статус"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        statusTable = new JTable(tableModel);
        statusTable.setBackground(BG_FIELD);
        statusTable.setForeground(FG_LIGHT);
        statusTable.setGridColor(new Color(70, 70, 70));
        statusTable.setFillsViewportHeight(true);
        statusTable.setRowHeight(25);
        statusTable.setFont(new Font("Arial", Font.PLAIN, 13));

        // Стилизация шапки таблицы
        JTableHeader header = statusTable.getTableHeader();
        header.setBackground(BG_LIGHTER);
        header.setForeground(ACCENT_BLUE);
        header.setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane tableScrollPane = new JScrollPane(statusTable);
        tableScrollPane.getViewport().setBackground(BG_DARK);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));

        JPanel clientRightPanel = new JPanel(new BorderLayout());
        clientRightPanel.setBackground(BG_DARK);
        clientRightPanel.setBorder(createCustomBorder("Мониторинг зала кафе"));
        clientRightPanel.add(tableScrollPane, BorderLayout.CENTER);

        // 🔥 Размещаем кнопку Выход внизу справа под таблицей
        JPanel exitButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        exitButtonPanel.setBackground(BG_DARK);
        exitButtonPanel.add(btnExit);
        clientRightPanel.add(exitButtonPanel, BorderLayout.SOUTH);

        clientPanel.add(clientLeftPanel, BorderLayout.WEST);
        clientPanel.add(clientRightPanel, BorderLayout.CENTER);

        // 2. АДМИНСКАЯ ВКЛАДКА
        JPanel adminTabPanel = new JPanel(new BorderLayout(10, 10));
        adminTabPanel.setBackground(BG_DARK);
        adminTabPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BG_DARK);
        btnAdminLogin.setPreferredSize(new Dimension(250, 50));
        loginPanel.add(btnAdminLogin);
        adminTabPanel.add(loginPanel, BorderLayout.CENTER);

        adminContentPanel.setBackground(BG_DARK);
        JPanel adminLeftPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        adminLeftPanel.setBackground(BG_DARK);

        JPanel addTableGrid = new JPanel(new GridLayout(2, 2, 8, 8));
        addTableGrid.setBackground(BG_LIGHTER);
        addTableGrid.setBorder(createCustomBorder("Управление залом"));
        addLabel(addTableGrid, "Мест в столе:"); addTableGrid.add(txtNewTableSeats);
        addTableGrid.add(new JLabel("")); addTableGrid.add(btnAddTable);

        adminLeftPanel.add(addTableGrid);
        adminLeftPanel.add(btnEditBooking);

        JPanel adminRightPanel = new JPanel(new BorderLayout());
        adminRightPanel.setBackground(BG_DARK);
        adminRightPanel.setBorder(createCustomBorder("Журнал всех бронирований"));

        txtAreaBookings.setEditable(false);
        txtAreaBookings.setBackground(BG_FIELD);
        txtAreaBookings.setForeground(FG_LIGHT);
        txtAreaBookings.setCaretColor(FG_LIGHT);
        txtAreaBookings.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane areaScrollPane = new JScrollPane(txtAreaBookings);
        areaScrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        adminRightPanel.add(areaScrollPane, BorderLayout.CENTER);

        adminContentPanel.add(adminLeftPanel, BorderLayout.WEST);
        adminContentPanel.add(adminRightPanel, BorderLayout.CENTER);

        tabbedPane.addTab("👤 Клиентская зона", clientPanel);
        tabbedPane.addTab("🔑 Панель администратора", adminTabPanel);
        add(tabbedPane);

        refreshTablesList();

        // --- ОБРАБОТЧИКИ СОБЫТИЙ ---

        // 🔥 Обработчик для кнопки Выход (с красивым темным диалогом)
        btnExit.addActionListener(e -> {
            UIManager.put("OptionPane.background", BG_DARK);
            UIManager.put("Panel.background", BG_DARK);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Вы действительно хотите выйти из системы?",
                    "Завершение работы",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        btnReset.addActionListener(e -> {
            txtTableId.setText(""); txtName.setText(""); txtPhone.setText(""); txtCancelBookingId.setText("");
            spinnerDate.setValue(new Date()); spinnerTime.setValue(new Date());
        });

        btnBook.addActionListener(e -> {
            try {
                if (txtTableId.getText().trim().isEmpty()) throw new Exception("Введите номер столика!");
                int tableId = Integer.parseInt(txtTableId.getText().trim());
                String name = txtName.getText().trim();
                String phone = txtPhone.getText().trim();

                if(name.isEmpty() || phone.isEmpty()) throw new Exception("Заполните ФИО и Телефон!");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                String dateStr = dateFormat.format(spinnerDate.getValue());
                String timeStr = timeFormat.format(spinnerTime.getValue());

                system.bookTable(tableId, dateStr, timeStr, name, phone);

                JOptionPane.showMessageDialog(this, "Столик успешно забронирован!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                txtTableId.setText(""); txtName.setText(""); txtPhone.setText("");
                refreshTablesList();
                if (isAdminMode) refreshBookingsList();
            } catch (Exception ex) {
                showDarkMessage(ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelBooking.addActionListener(e -> {
            try {
                if (txtCancelBookingId.getText().trim().isEmpty()) throw new Exception("Введите ID брони для отмены!");
                int bookingId = Integer.parseInt(txtCancelBookingId.getText().trim());
                system.cancelBooking(bookingId);

                JOptionPane.showMessageDialog(this, "Бронирование #" + bookingId + " успешно отменено.", "Отмена", JOptionPane.INFORMATION_MESSAGE);
                txtCancelBookingId.setText("");
                refreshTablesList();
                if (isAdminMode) refreshBookingsList();
            } catch (Exception ex) {
                showDarkMessage(ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditBooking.addActionListener(e -> {
            try {
                UIManager.put("OptionPane.background", BG_DARK);
                UIManager.put("Panel.background", BG_DARK);
                String idStr = JOptionPane.showInputDialog(this, "Введите ID бронирования для изменения:", "Редактирование", JOptionPane.QUESTION_MESSAGE);
                if (idStr == null || idStr.trim().isEmpty()) return;

                int bookingId = Integer.parseInt(idStr.trim());
                Booking booking = system.getBookingById(bookingId);

                if (booking == null) {
                    throw new Exception("Бронирование с ID #" + bookingId + " не найдено!");
                }

                JPanel editPanel = new JPanel(new GridLayout(5, 2, 8, 8));
                editPanel.setBackground(BG_DARK);

                JTextField txtEditTable = new JTextField(String.valueOf(booking.getTableId()));
                JTextField txtEditName = new JTextField(booking.getName());
                JTextField txtEditPhone = new JTextField(booking.getPhone());

                styleTextField(txtEditTable);
                styleTextField(txtEditName);
                styleTextField(txtEditPhone);
                setDigitsOnlyFilter(txtEditTable);
                setDigitsOnlyFilter(txtEditPhone);
                setLettersOnlyFilter(txtEditName);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                Date currentDate = dateFormat.parse(booking.getDate());
                Date currentTime = timeFormat.parse(booking.getStartTime());

                JSpinner spinEditDate = new JSpinner(new SpinnerDateModel(currentDate, null, null, Calendar.DAY_OF_MONTH));
                spinEditDate.setEditor(new JSpinner.DateEditor(spinEditDate, "yyyy-MM-dd"));
                styleSpinner(spinEditDate);

                JSpinner spinEditTime = new JSpinner(new SpinnerDateModel(currentTime, null, null, Calendar.MINUTE));
                spinEditTime.setEditor(new JSpinner.DateEditor(spinEditTime, "HH:mm"));
                styleSpinner(spinEditTime);

                addLabel(editPanel, "Номер столика:"); editPanel.add(txtEditTable);
                addLabel(editPanel, "Дата:"); editPanel.add(spinEditDate);
                addLabel(editPanel, "Время:"); editPanel.add(spinEditTime);
                addLabel(editPanel, "ФИО Клиента:"); editPanel.add(txtEditName);
                addLabel(editPanel, "Телефон:"); editPanel.add(txtEditPhone);

                int result = JOptionPane.showConfirmDialog(this, editPanel, "Редактирование брони #" + bookingId,
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    if (txtEditTable.getText().trim().isEmpty()) throw new Exception("Укажите номер стола!");
                    int newTableId = Integer.parseInt(txtEditTable.getText().trim());
                    String newName = txtEditName.getText().trim();
                    String newPhone = txtEditPhone.getText().trim();
                    String newDate = dateFormat.format(spinEditDate.getValue());
                    String newTime = timeFormat.format(spinEditTime.getValue());

                    if (newName.isEmpty() || newPhone.isEmpty()) {
                        throw new Exception("Поля ФИО и Телефон не могут быть пустыми!");
                    }

                    system.updateBooking(bookingId, newTableId, newDate, newTime, newName, newPhone);

                    JOptionPane.showMessageDialog(this, "Данные успешно изменены!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                    refreshTablesList();
                    refreshBookingsList();
                }

            } catch (NumberFormatException ex) {
                showDarkMessage("Неверный формат ID или номера стола!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                showDarkMessage(ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAdminLogin.addActionListener(e -> {
            UIManager.put("OptionPane.background", BG_DARK);
            UIManager.put("Panel.background", BG_DARK);
            String password = JOptionPane.showInputDialog(this, "Введите пароль администратора:", "Авторизация", JOptionPane.QUESTION_MESSAGE);
            if (password != null && password.equals("admin123")) {
                isAdminMode = true;
                adminTabPanel.remove(loginPanel);
                adminTabPanel.add(adminContentPanel, BorderLayout.CENTER);
                adminTabPanel.revalidate();
                adminTabPanel.repaint();
                refreshBookingsList();
            } else if (password != null) {
                showDarkMessage("Неверный пароль!", "Отказ в доступе", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAddTable.addActionListener(e -> {
            try {
                int seats = Integer.parseInt(txtNewTableSeats.getText().trim());
                if (seats <= 0) throw new NumberFormatException();
                system.addTable(seats);
                JOptionPane.showMessageDialog(this, "Новый столик успешно добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                txtNewTableSeats.setText("");
                refreshTablesList();
            } catch (NumberFormatException ex) {
                showDarkMessage("Введите правильное количество мест!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // --- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ СТИЛИЗАЦИИ КОМПОНЕНТОВ ---

    private void styleButton(JButton button, Color bg) {
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
    }

    private void styleTextField(JTextField field) {
        field.setBackground(BG_FIELD);
        field.setForeground(FG_LIGHT);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setBackground(BG_FIELD);
        spinner.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));

        for (Component comp : spinner.getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(BG_LIGHTER);
                comp.setForeground(FG_LIGHT);
                ((JButton) comp).setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
            }
        }

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(BG_FIELD);
            tf.setForeground(FG_LIGHT);
            tf.setCaretColor(Color.WHITE);
            tf.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        }
    }

    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(FG_LIGHT);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(label);
    }

    private TitledBorder createCustomBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(65, 65, 65), 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                ACCENT_BLUE
        );
    }

    private void showDarkMessage(String msg, String title, int type) {
        UIManager.put("OptionPane.background", BG_DARK);
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", FG_LIGHT);
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    private void refreshTablesList() {
        tableModel.setRowCount(0);
        for (Table t : system.getTables()) {
            String status = t.isBooked() ? "❌ ЗАНЯТ" : "🟢 СВОБОДЕН";
            tableModel.addRow(new Object[]{t.getId(), t.getSeats(), status});
        }
    }

    private void refreshBookingsList() {
        if (system.getBookings().isEmpty()) {
            txtAreaBookings.setText("Бронирований пока нет.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Booking b : system.getBookings()) {
            sb.append(b.toString()).append("\n");
        }
        txtAreaBookings.setText(sb.toString());
    }

    private void setDigitsOnlyFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("\\d+")) super.insertString(fb, offset, string, attr);
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("\\d*")) super.replace(fb, offset, length, text, attrs);
            }
        });
    }

    private void setLettersOnlyFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[a-zA-Zа-яА-ЯёЁ\\s]+")) super.insertString(fb, offset, string, attr);
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("[a-zA-Zа-яА-ЯёЁ\\s]*")) super.replace(fb, offset, length, text, attrs);
            }
        });
    }
}