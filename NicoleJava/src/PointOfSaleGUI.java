import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Structure to represent a product
class Product {
    String name;
    float price;
    int quantity;
    String imagePath;

    public Product(String name, float price, int quantity, String imagePath) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }
}

public class PointOfSaleGUI {
    private JFrame frame;
    private Product[] inventory;

    public PointOfSaleGUI() {
        inventory = loadInventory();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Ordering System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("logopos.jpg"); // Change to your background image
                if (imageIcon.getIconWidth() == -1) {
                    g.setColor(Color.RED);
                    g.drawString("Image not found", getWidth() / 2, getHeight() / 2);
                } else {
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new GridLayout(4, 1, 10, 10));
        backgroundPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton viewProductsButton = new JButton("View Products");
        viewProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewProducts();
            }
        });
        backgroundPanel.add(viewProductsButton);

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });
        backgroundPanel.add(placeOrderButton);

        JButton modifyProductsButton = new JButton("Modify Products");
        modifyProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
        backgroundPanel.add(modifyProductsButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        backgroundPanel.add(exitButton);

        frame.add(backgroundPanel);
        frame.setVisible(true);
    }

    private void viewProducts() {
        displayProducts(frame);
    }

    private void displayProducts(JFrame parentFrame) {
        JFrame viewFrame = new JFrame("View Inventory");
        viewFrame.setSize(600, 400);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new GridLayout(inventory.length, 1, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (Product product : inventory) {
            JPanel productPanel = new JPanel(new BorderLayout(10, 10));
            JLabel nameLabel = new JLabel(product.name);
            JLabel priceLabel = new JLabel("Price: " + product.price);
            JLabel quantityLabel = new JLabel("Quantity: " + product.quantity);

            // Load and resize the image
            ImageIcon imageIcon = new ImageIcon(product.imagePath);
            if (imageIcon.getIconWidth() == -1) {
                imageIcon = new ImageIcon("path/to/default-image.jpg"); // Path to a default image if not found
            }
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

            JPanel textPanel = new JPanel(new GridLayout(3, 1));
            textPanel.add(nameLabel);
            textPanel.add(priceLabel);
            textPanel.add(quantityLabel);

            productPanel.add(imageLabel, BorderLayout.WEST);
            productPanel.add(textPanel, BorderLayout.CENTER);

            panel.add(productPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        viewFrame.add(scrollPane);
        viewFrame.setVisible(true);
    }

    private Product[] loadInventory() {
        return new Product[]{
                new Product("Graduation Cake 1", 725f, 10, "C:/Users/ASUS/IdeaProjects/NicoleJava/src/GradCake1.jpg"),
                new Product("Graduation Cake 2", 450f, 10, "C:/Users/ASUS/IdeaProjects/NicoleJava/src/GradCake2.jpg"),
                new Product("Graduation Cake 3", 325f, 10, "C:/Users/ASUS/IdeaProjects/NicoleJava/src/GradCake3.jpg")
        };
    }

    private Product[] addProduct(Product[] inventory, String name, float price, int quantity, String imagePath) {
        for (Product product : inventory) {
            if (product.name.equals(name)) {
                JOptionPane.showMessageDialog(frame, "Product already exists. Please enter a unique name.");
                return inventory;
            }
        }
        Product[] newInventory = new Product[inventory.length + 1];
        System.arraycopy(inventory, 0, newInventory, 0, inventory.length);
        newInventory[inventory.length] = new Product(name, price, quantity, imagePath);
        return newInventory;
    }

    private Product[] removeProduct(Product[] inventory, String name) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].name.equals(name)) {
                Product[] newInventory = new Product[inventory.length - 1];
                System.arraycopy(inventory, 0, newInventory, 0, i);
                System.arraycopy(inventory, i + 1, newInventory, i, inventory.length - i - 1);
                return newInventory;
            }
        }
        JOptionPane.showMessageDialog(frame, "Product not found.");
        return inventory;
    }

    private void changePrice(Product[] inventory, String name, float newPrice) {
        for (Product product : inventory) {
            if (product.name.equals(name)) {
                product.price = newPrice;
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Product not found.");
    }

    private void placeOrder() {
        JFrame orderFrame = new JFrame("Place Order");
        orderFrame.setSize(600, 400);
        orderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        orderFrame.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new GridLayout(inventory.length, 1, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (Product product : inventory) {
            JPanel productPanel = new JPanel(new BorderLayout(10, 10));
            JLabel nameLabel = new JLabel(product.name);
            JLabel priceLabel = new JLabel("Price: " + product.price);
            JLabel quantityLabel = new JLabel("Available: " + product.quantity);

            JTextField quantityField = new JTextField();
            JLabel orderQuantityLabel = new JLabel("Order Quantity:");

            JButton orderButton = new JButton("Order");
            orderButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int orderQuantity;
                    try {
                        orderQuantity = Integer.parseInt(quantityField.getText());
                        if (orderQuantity <= 0 || orderQuantity > product.quantity) {
                            JOptionPane.showMessageDialog(orderFrame, "Invalid order quantity.");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(orderFrame, "Please enter a valid number.");
                        return;
                    }

                    // Decrease the quantity in inventory
                    product.quantity -= orderQuantity;
                    JOptionPane.showMessageDialog(orderFrame, "Order placed for " + orderQuantity + " units of " + product.name);
                }
            });

            JPanel textPanel = new JPanel(new GridLayout(6, 1));
            textPanel.add(nameLabel);
            textPanel.add(priceLabel);
            textPanel.add(quantityLabel);
            textPanel.add(orderQuantityLabel);
            textPanel.add(quantityField);
            textPanel.add(orderButton);

            productPanel.add(textPanel, BorderLayout.CENTER);
            panel.add(productPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        orderFrame.add(scrollPane);
        orderFrame.setVisible(true);
    }

    private void authenticate() {
        JOptionPane.showMessageDialog(frame, "Authentication successful.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PointOfSaleGUI::new);
    }
}
