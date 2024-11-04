import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;

class Account {
    private final String accountNumber;
    private double balance;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            showReceipt("Deposit", amount);
        } else {
            JOptionPane.showMessageDialog(null, "Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            showReceipt("Withdraw", amount);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid withdraw amount or insufficient balance.");
        }
    }

    private void showReceipt(String type, double amount) {
        JOptionPane.showMessageDialog(null, type + " of ₱" + amount + " successful.\nCurrent Balance: ₱" + balance);
    }
}

@SuppressWarnings("unused")
class POS {
    public void makePurchase(Account account, double amount) {
        if (amount > 0 && amount <= account.getBalance()) {
            account.withdraw(amount);
            JOptionPane.showMessageDialog(null, "Purchase successful for ₱" + amount);
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient funds for this purchase.");
        }
    }
}

class BankingSystemGUI extends JFrame {
    private final Map<String, Account> accounts = new HashMap<>();

    public BankingSystemGUI() {
        setTitle("Online Banking & POS System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        JButton createAccountButton = new JButton("Create Account");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton openMenuButton = new JButton("Open Jollibee Menu");
        JButton openStardaksButton = new JButton("Open Stardaks Menu");
        JButton exitButton = new JButton("Exit");

        createAccountButton.addActionListener(e -> createAccount());
        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        checkBalanceButton.addActionListener(e -> checkBalance());
        openMenuButton.addActionListener(e -> openJollibeeMenu());
        openStardaksButton.addActionListener(e -> openStardaksMenu());
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(createAccountButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(checkBalanceButton);
        panel.add(openMenuButton);
        panel.add(openStardaksButton);
        panel.add(exitButton);

        add(panel);
    }

    private void createAccount() {
        String accountNumber = JOptionPane.showInputDialog("Enter account number:");
        if (accountNumber != null && !accountNumber.isEmpty()) {
            if (!accounts.containsKey(accountNumber)) {
                accounts.put(accountNumber, new Account(accountNumber));
                JOptionPane.showMessageDialog(null, "Account created successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Account already exists.");
            }
        }
    }

    private void deposit() {
        String accountNumber = JOptionPane.showInputDialog("Enter account number:");
        Account account = accounts.get(accountNumber);
        if (account != null) {
            String amountStr = JOptionPane.showInputDialog("Enter amount to deposit:");
            if (amountStr != null) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    account.deposit(amount);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid amount.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Account not found.");
        }
    }

    private void withdraw() {
        String accountNumber = JOptionPane.showInputDialog("Enter account number:");
        Account account = accounts.get(accountNumber);
        if (account != null) {
            String amountStr = JOptionPane.showInputDialog("Enter amount to withdraw:");
            if (amountStr != null) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    account.withdraw(amount);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid amount.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Account not found.");
        }
    }

    private void checkBalance() {
        String accountNumber = JOptionPane.showInputDialog("Enter account number:");
        Account account = accounts.get(accountNumber);
        if (account != null) {
            double balance = account.getBalance();
            JOptionPane.showMessageDialog(null, "Balance: ₱" + balance);
        } else {
            JOptionPane.showMessageDialog(null, "Account not found.");
        }
    }

    private void openJollibeeMenu() {
        JollibeeMenu jollibeeMenu = new JollibeeMenu(accounts);
        jollibeeMenu.setVisible(true);
    }

    private void openStardaksMenu() {
        StardaksPOSGUI stardaksPOSGUI = new StardaksPOSGUI(accounts);
        stardaksPOSGUI.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankingSystemGUI bankingSystemGUI = new BankingSystemGUI();
            bankingSystemGUI.setVisible(true);
        });

        try (Scanner scanner = new Scanner(System.in)) {
            new Thread(() -> {
                while (true) {
                    System.out.print("Enter a command (e.g., create account): ");
                    String input = scanner.nextLine();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting program.");
                        System.exit(0);
                    } else {
                        System.out.println("Command received: " + input);
                    }
                }
            }).start();
        }
    }
}
class JollibeeMenu extends JFrame {
    private final ArrayList<String> orderList = new ArrayList<>();
    private double totalAmount = 0.0;
    private final Map<String, Account> accounts;
    private Account currentAccount;

    public JollibeeMenu(Map<String, Account> accounts) {
        this.accounts = accounts;
        setTitle("Jollibee Menu");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JLabel jollibeeImage = new JLabel(new ImageIcon("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLbJzsXVi0GdTm5hEeegnVETAvC9xQTIQsmg&s"));
        headerPanel.add(jollibeeImage);
        add(headerPanel, BorderLayout.NORTH);

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
        JButton button = new JButton(itemName + " - ₱" + String.format("%.2f", price));
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

        String accountNumber = JOptionPane.showInputDialog("Enter account number for payment:");
        currentAccount = accounts.get(accountNumber);
        if (currentAccount == null) {
            JOptionPane.showMessageDialog(null, "Account not found.");
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
    private JLabel totalLabel;
    private double totalAmount = 0.0;
    private String selectedSize = "Tall";
    private Map<String, Double> sizePriceModifier;
    private final Map<String, Account> accounts;

    public StardaksPOSGUI(Map<String, Account> accounts) {
        this.accounts = accounts;
        initializeSizeModifiers();
        initializeGUI();
    }

    private void initializeSizeModifiers() {
        sizePriceModifier = new HashMap<>();
        sizePriceModifier.put("Short", 0.9);
        sizePriceModifier.put("Tall", 1.0);
        sizePriceModifier.put("Grande", 1.2);
        sizePriceModifier.put("Venti", 1.5);
        sizePriceModifier.put("Trenta", 1.7);
    }

    private void initializeGUI() {
        setTitle("Stardaks POS System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Stardaks", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JLabel dateTimeLabel = new JLabel(getCurrentDateTime(), SwingConstants.RIGHT);
        topPanel.add(dateTimeLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        addDrinkButton(menuPanel, "Espresso", 50, "espresso.png");
        addDrinkButton(menuPanel, "Cappuccino", 70, "cappuccino.png");
        addDrinkButton(menuPanel, "Latte", 80, "latte.png");
        addDrinkButton(menuPanel, "Americano", 60, "americano.png");
        addDrinkButton(menuPanel, "Mocha", 90, "mocha.png");
        addDrinkButton(menuPanel, "Whole Bean", 250, "wholebean.png");
        addDrinkButton(menuPanel, "Coffee Mug", 150, "mug.png");
        addDrinkButton(menuPanel, "Food/Pastry", 100, "pastry.png");
        addDrinkButton(menuPanel, "Snacks", 120, "snacks.png");
        add(menuPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        JPanel optionsPanel = new JPanel(new GridLayout(6, 1));
        JLabel sizeLabel = new JLabel("Size:");
        optionsPanel.add(sizeLabel);

        JButton shortButton = new JButton("Short");
        JButton tallButton = new JButton("Tall");
        JButton grandeButton = new JButton("Grande");
        JButton ventiButton = new JButton("Venti");
        JButton trentaButton = new JButton("Trenta");

        shortButton.addActionListener(e -> selectSize("Short"));
        tallButton.addActionListener(e -> selectSize("Tall"));
        grandeButton.addActionListener(e -> selectSize("Grande"));
        ventiButton.addActionListener(e -> selectSize("Venti"));
        trentaButton.addActionListener(e -> selectSize("Trenta"));

        optionsPanel.add(shortButton);
        optionsPanel.add(tallButton);
        optionsPanel.add(grandeButton);
        optionsPanel.add(ventiButton);
        optionsPanel.add(trentaButton);

        rightPanel.add(optionsPanel, BorderLayout.NORTH);

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        totalLabel = new JLabel("Total: ₱0.00");
        bottomPanel.add(totalLabel);
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> completeTransaction());
        bottomPanel.add(checkoutButton);

        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addDrinkButton(JPanel panel, String drinkName, double basePrice, String imagePath) {
        JButton button = new JButton("<html><center>" + drinkName + "<br>₱" + basePrice + "</center></html>");
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setPreferredSize(new Dimension(150, 150));

        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("Image not found: " + imagePath);
        }

        button.addActionListener(e -> addItemToReceipt(drinkName, basePrice));
        panel.add(button);
    }

    private void addItemToReceipt(String itemName, double basePrice) {
        double itemPrice = basePrice * sizePriceModifier.get(selectedSize);
        receiptArea.append(itemName + " (" + selectedSize + ") - ₱" + String.format("%.2f", itemPrice) + "\n");
        totalAmount += itemPrice;
        updateTotalLabel();
    }

    private void updateTotalLabel() {
        totalLabel.setText("Total: ₱" + String.format("%.2f", totalAmount));
    }

    private void selectSize(String size) {
        selectedSize = size;
    }

    private void completeTransaction() {
        String accountNumber = JOptionPane.showInputDialog("Enter account number for payment:");
        Account account = accounts.get(accountNumber);
        if (account == null) {
            JOptionPane.showMessageDialog(this, "Account not found.");
            return;
        }
        if (totalAmount > account.getBalance()) {
            JOptionPane.showMessageDialog(this, "Insufficient funds.");
            return;
        }
        account.withdraw(totalAmount);
        JOptionPane.showMessageDialog(this, "Thank you for your purchase! Total: ₱" + String.format("%.2f", totalAmount));
        totalAmount = 0;
        receiptArea.setText("");
        updateTotalLabel();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d\nh:mm a");
        return formatter.format(new Date());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankingSystemGUI bankingSystemGUI = new BankingSystemGUI();
            bankingSystemGUI.setVisible(true);
        });
    }
}
