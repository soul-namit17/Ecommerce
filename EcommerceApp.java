import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class EcommerceApp5 {

    private ArrayList<Product> cart = new ArrayList<>();
    private JTextArea cartDetails;
    private JLabel totalLabel;
    private double total = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (authenticateUser()) {
                new EcommerceApp5().createAndShowGUI();
            } else {
                JOptionPane.showMessageDialog(null, "Authentication failed. Exiting application.");
                System.exit(0);
            }
        });
    }

    private static boolean authenticateUser() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            return username.equals("user") && password.equals("password");
        }
        return false;
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("E-Commerce Shopping Cart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 800);  // Increased frame size
        frame.setLayout(new BorderLayout());

        // Navbar
        JPanel navbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(Color.BLUE);
        JLabel appTitle = new JLabel("E-Commerce Platform");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("Poppins", Font.BOLD, 24));
        navbar.add(appTitle);

        // Category Panel
        JPanel categoryPanel = new JPanel(new GridLayout(5, 1, 5, 2));
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.setBorder(BorderFactory.createTitledBorder(null, "Categories", 0, 0, null, Color.WHITE));

        JButton electronicsBtn = new JButton("Electronics");
        JButton booksBtn = new JButton("Books");
        JButton clothingBtn = new JButton("Clothing");
        styleButton(electronicsBtn);
        styleButton(booksBtn);
        styleButton(clothingBtn);

        categoryPanel.add(electronicsBtn);
        categoryPanel.add(booksBtn);
        categoryPanel.add(clothingBtn);

        // Main Product Display
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 3, 10, 10)); // Reduced spacing
        mainPanel.setBackground(Color.BLACK);

        JScrollPane productScrollPane = new JScrollPane(mainPanel);
        productScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Cart Section
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder(null, "Cart", 0, 0, null, Color.WHITE));
        cartPanel.setBackground(Color.BLACK);

        cartDetails = new JTextArea(10, 20);
        cartDetails.setEditable(false);
        cartDetails.setBackground(Color.BLACK);
        cartDetails.setForeground(Color.WHITE);

        JScrollPane cartScrollPane = new JScrollPane(cartDetails);

        totalLabel = new JLabel("Total: Rs0.0");
        totalLabel.setForeground(Color.WHITE);

        JButton clearCart = new JButton("Clear Cart");
        JButton saveCart = new JButton("Save Cart");
        JButton checkout = new JButton("Checkout");
        styleButton(clearCart);
        styleButton(saveCart);
        styleButton(checkout);

        JPanel cartButtons = new JPanel();
        cartButtons.setBackground(Color.BLUE);
        cartButtons.add(clearCart);
        cartButtons.add(saveCart);
        cartButtons.add(checkout);

        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        cartPanel.add(totalLabel, BorderLayout.NORTH);
        cartPanel.add(cartButtons, BorderLayout.SOUTH);

        // Add components to frame
        frame.add(navbar, BorderLayout.NORTH);
        frame.add(categoryPanel, BorderLayout.WEST);
        frame.add(productScrollPane, BorderLayout.CENTER);
        frame.add(cartPanel, BorderLayout.EAST);

        // Button Actions
        electronicsBtn.addActionListener(e -> showProducts(mainPanel, "Electronics"));
        booksBtn.addActionListener(e -> showProducts(mainPanel, "Books"));
        clothingBtn.addActionListener(e -> showProducts(mainPanel, "Clothing"));

        clearCart.addActionListener(e -> clearCart());
        saveCart.addActionListener(e -> saveCartToFile());
        checkout.addActionListener(e -> checkout());

        // Default category
        showProducts(mainPanel, "Electronics");

        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void showProducts(JPanel mainPanel, String category) {
        mainPanel.removeAll();

        if (category.equals("Electronics")) {
            mainPanel.add(createProductPanel(new Product("Laptop", 60000)));
            mainPanel.add(createProductPanel(new Product("Smartphone", 25000)));
            mainPanel.add(createProductPanel(new Product("Headphones", 2000)));
            mainPanel.add(createProductPanel(new Product("Smartwatch", 5000)));
            mainPanel.add(createProductPanel(new Product("Tablet", 15000)));
            mainPanel.add(createProductPanel(new Product("Camera", 30000)));
            
        } else if (category.equals("Books")) {
            mainPanel.add(createProductPanel(new Product("Java Programming", 500)));
            mainPanel.add(createProductPanel(new Product("Design Patterns", 800)));
            mainPanel.add(createProductPanel(new Product("Data Structures", 600)));
            mainPanel.add(createProductPanel(new Product("Machine Learning", 1200)));
            mainPanel.add(createProductPanel(new Product("AI for Beginners", 1000)));
            mainPanel.add(createProductPanel(new Product("Python for All", 900)));
        } else if (category.equals("Clothing")) {
            mainPanel.add(createProductPanel(new Product("T-Shirt", 500)));
            mainPanel.add(createProductPanel(new Product("Jeans", 1200)));
            mainPanel.add(createProductPanel(new Product("Jacket", 3000)));
            mainPanel.add(createProductPanel(new Product("Sweater", 1500)));
            mainPanel.add(createProductPanel(new Product("Shirt", 800)));
            mainPanel.add(createProductPanel(new Product("Skirt", 1000)));
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel(new BorderLayout(3, 1)); // Reduced spacing
        productPanel.setBackground(Color.BLUE);
        productPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 0));

        // Product Info
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBackground(Color.WHITE);

        JLabel productName = new JLabel(product.getName());
        productName.setForeground(Color.BLACK);
        productName.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel productPrice = new JLabel("Rs" + product.getPrice());
        productPrice.setForeground(Color.BLACK);
        productPrice.setHorizontalAlignment(SwingConstants.CENTER);

        JButton addToCartBtn = new JButton("Add to Cart");
        styleButton(addToCartBtn);
        addToCartBtn.addActionListener(e -> addToCart(product));

        infoPanel.add(productName);
        infoPanel.add(productPrice);
        infoPanel.add(addToCartBtn);

        productPanel.add(infoPanel, BorderLayout.CENTER);

        return productPanel;
    }

    private void addToCart(Product product) {
        cart.add(product);
        total += product.getPrice();
        updateCartDetails();
    }

    private void clearCart() {
        cart.clear();
        total = 0;
        updateCartDetails();
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your cart is empty!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Checkout successful! Total: RS" + total, "Checkout", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveCartToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cart.txt"))) {
            for (Product product : cart) {
                writer.write(product.getName() + " - Rs" + product.getPrice() + "\n");
            }
            writer.write("Total: Rs" + total + "\n");
            JOptionPane.showMessageDialog(null, "Cart saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving cart: " + e.getMessage());
        }
    }

    private void updateCartDetails() {
        StringBuilder details = new StringBuilder();
        for (Product product : cart) {
            details.append(product.getName()).append(" - Rs").append(product.getPrice()).append("\n");
        }
        cartDetails.setText(details.toString());
        totalLabel.setText("Total: Rs" + total);
    }

    class Product {
        private final String name;
        private final double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }
}
