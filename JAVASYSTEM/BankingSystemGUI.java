import java.awt.*; 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

class BankingSystemGUI extends JFrame {
    private final Map<String, Account> accounts = new HashMap<>();
    private Account loggedInAccount;

    public BankingSystemGUI() {
        setTitle("Online Banking & POS System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel("Bank-Account.png");
        backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 1, 10, 10));
        buttonPanel.setOpaque(false);

        JButton loginButton = new JButton("Log In");
        JButton createAccountButton = new JButton("Create Account");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton openMenuButton = new JButton("Open Jollibee Menu");
        JButton openStardaksButton = new JButton("Open Stardaks Menu");
        JButton exitButton = new JButton("Exit");

        loginButton.addActionListener(e -> logIn());
        createAccountButton.addActionListener(e -> createAccount());
        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        checkBalanceButton.addActionListener(e -> checkBalance());
        openMenuButton.addActionListener(e -> openJollibeeMenu());
        openStardaksButton.addActionListener(e -> openStardaksMenu());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(checkBalanceButton);
        buttonPanel.add(openMenuButton);
        buttonPanel.add(openStardaksButton);
        buttonPanel.add(exitButton);

        backgroundPanel.add(buttonPanel);
        add(backgroundPanel);
    }

    private class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel(String filePath) {
            try {
                backgroundImage = ImageIO.read(new File(filePath));
            } catch (IOException e) {
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void logIn() {
        String accountNumber = JOptionPane.showInputDialog("Enter account number:");
        String password = JOptionPane.showInputDialog("Enter password:");

        if (accountNumber != null && password != null) {
            loggedInAccount = accounts.get(accountNumber);
            if (loggedInAccount != null && loggedInAccount.getPassword().equals(password)) {
                JOptionPane.showMessageDialog(null, "Logged in successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid account number or password.");
            }
        }
    }

    private void createAccount() {
        String accountNumber = JOptionPane.showInputDialog("Enter account number:");
        String password = JOptionPane.showInputDialog("Enter password:");
        if (accountNumber != null && !accountNumber.isEmpty() && password != null) {
            if (!accounts.containsKey(accountNumber)) {
                accounts.put(accountNumber, new Account(accountNumber, password));
                JOptionPane.showMessageDialog(null, "Account created successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Account already exists.");
            }
        }
    }

    private void deposit() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(null, "Log in first.");
            return;
        }
        String amountStr = JOptionPane.showInputDialog("Enter amount to deposit:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                loggedInAccount.deposit(amount);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid amount.");
            }
        }
    }

    private void withdraw() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(null, "Log in first.");
            return;
        }
        String amountStr = JOptionPane.showInputDialog("Enter amount to withdraw:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                loggedInAccount.withdraw(amount);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid amount.");
            }
        }
    }

    private void checkBalance() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(null, "Log in first.");
            return;
        }
        double balance = loggedInAccount.getBalance();
        JOptionPane.showMessageDialog(null, "Balance: ₱" + balance);
    }

    private void openJollibeeMenu() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(null, "Log in first.");
            return;
        }
        JollibeeMenu jollibeeMenu = new JollibeeMenu(accounts, loggedInAccount);
        jollibeeMenu.setVisible(true);
    }

    private void openStardaksMenu() {
        if (loggedInAccount == null) {
            JOptionPane.showMessageDialog(null, "Log in first.");
            return;
        }
        StardaksPOSGUI stardaksPOSGUI = new StardaksPOSGUI(accounts, loggedInAccount);
        stardaksPOSGUI.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankingSystemGUI bankingSystemGUI = new BankingSystemGUI();
            bankingSystemGUI.setVisible(true);
        });
    }
}

class JollibeeMenu extends JFrame {
    private final ArrayList<String> orderList = new ArrayList<>();
    private double totalAmount = 0.0;
    private final Account currentAccount;

    public JollibeeMenu(Map<String, Account> accounts, Account currentAccount) {
        this.currentAccount = currentAccount;
        setTitle("Jollibee Menu");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel jollibeeImage = new JLabel(new ImageIcon("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLbJzsXVi0GdTm5hEeegnVETAvC9xQTIQsmg&s"));
        add(jollibeeImage, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        add(menuPanel, BorderLayout.CENTER);

        menuPanel.add(createMenuItem("Burger", 75.00));
        menuPanel.add(createMenuItem("Spaghetti", 50.00));
        menuPanel.add(createMenuItem("Chicken Joy", 120.00));
        menuPanel.add(createMenuItem("Fries", 35.00));
        menuPanel.add(createMenuItem("Jolly Hotdog", 60.00));
        menuPanel.add(createMenuItem("Sundae", 30.00));

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> checkout());
        add(checkoutButton, BorderLayout.SOUTH);
    }

    private JButton createMenuItem(String itemName, double price) {
        JButton button = new JButton(itemName);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> {
            orderList.add(itemName + " - ₱" + String.format("%.2f", price));
            totalAmount += price;
            JOptionPane.showMessageDialog(null, itemName + " added to order. Total: ₱" + String.format("%.2f", totalAmount));
        });

        return button;
    }

    private void checkout() {
        if (orderList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No items ordered. Please add items to your order.");
            return;
        }

        if (totalAmount > currentAccount.getBalance()) {
            JOptionPane.showMessageDialog(null, "Insufficient funds. Please deposit money to your account.");
            return;
        }

        currentAccount.withdraw(totalAmount);
        JOptionPane.showMessageDialog(null, "Thank you for your purchase! Total: ₱" + String.format("%.2f", totalAmount));
        orderList.clear();
        totalAmount = 0.0;
    }
}

class StardaksPOSGUI extends JFrame {
    private JTextArea receiptArea;
    private double totalAmount = 0.0;
    private Map<String, Double> sizePriceModifier;
    private final Account currentAccount;

    public StardaksPOSGUI(Map<String, Account> accounts, Account currentAccount) {
        this.currentAccount = currentAccount;
        initializeSizeModifiers();
        initializeGUI();
    }

    private void initializeSizeModifiers() {
        sizePriceModifier = new HashMap<>();
        sizePriceModifier.put("Small", 0.8); 
        sizePriceModifier.put("Medium", 1.0);
        sizePriceModifier.put("Large", 1.2); 
    }

    private void initializeGUI() {
        setTitle("Stardaks POS");
        setSize(700, 1000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel stardaksImage = new JLabel(new ImageIcon("stardaks_image.png"));
        add(stardaksImage, BorderLayout.NORTH);

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new GridLayout(0, 2));

        addOrderItem(orderPanel, "Cafe", 50.00);
        addOrderItem(orderPanel, "Tea", 50.00);
        addOrderItem(orderPanel, "Hot Chocolate", 60.00);
        addOrderItem(orderPanel, "C2Gin", 100.00);
        addOrderItem(orderPanel, "Ice Cream yummy", 50.00);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> checkout());
        orderPanel.add(checkoutButton);

        add(orderPanel, BorderLayout.SOUTH);
    }

    private void addOrderItem(JPanel orderPanel, String itemName, double basePrice) {
        JLabel label = new JLabel(itemName);
        JTextField quantityField = new JTextField("0");
        JComboBox<String> sizeBox = new JComboBox<>(new String[]{"Small", "Medium", "Large"});
        orderPanel.add(label);
        orderPanel.add(quantityField);
        orderPanel.add(sizeBox);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            int quantity = Integer.parseInt(quantityField.getText());
            double priceModifier = sizePriceModifier.get(sizeBox.getSelectedItem());
            double totalPrice = quantity * basePrice * priceModifier;

            if (quantity > 0) {
                totalAmount += totalPrice;
                receiptArea.append(itemName + " x" + quantity + " - ₱" + String.format("%.2f", totalPrice) + "\n");
                quantityField.setText("0");
            }
        });
        orderPanel.add(addButton);
    }

    private void checkout() {
        if (totalAmount > currentAccount.getBalance()) {
            JOptionPane.showMessageDialog(null, "Insufficient funds. Please deposit money to your account.");
            return;
        }

        currentAccount.withdraw(totalAmount);
        JOptionPane.showMessageDialog(null, "Thank you for your purchase! Total: ₱" + String.format("%.2f", totalAmount));
        totalAmount = 0.0;
        receiptArea.setText("");
    }
}

class Account {
    private final String accountNumber;
    private final String password;
    private double balance;

    public Account(String accountNumber, String password) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            JOptionPane.showMessageDialog(null, "Deposited: ₱" + String.format("%.2f", amount) + ". New balance: ₱" + String.format("%.2f", balance));
        } else {
            JOptionPane.showMessageDialog(null, "Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            JOptionPane.showMessageDialog(null, "Withdrew: ₱" + String.format("%.2f", amount) + ". New balance: ₱" + String.format("%.2f", balance));
        } else {
            JOptionPane.showMessageDialog(null, "Invalid withdrawal amount or insufficient funds.");
        }
    }
}
