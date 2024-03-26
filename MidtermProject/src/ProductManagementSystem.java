import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class ProductManagementSystem {

    // Constants for file path and messages
    private static final String FILE_PATH = "products.csv";
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please enter a valid value.";

    public static void manageProduct() {
        int input4 = getValidIntegerInput("Welcome to Product Management!\n1.Add Product\n2.Remove Product\n3.Update Product\n4.Display Products\n5.Search Product\n6.Main Page");

        switch (input4) {
            case 1:
                addProduct();
                break;
            case 2:
                removeProduct();
                break;
            case 3:
                updateProduct();
                break;
            case 4:
                displayProduct();
                break;
            case 5:
                searchProduct();
                break;
            case 6:
                InventoryManagementSystem.libertadPublicMarket();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid command");
        }
    }

    private static void addProduct() {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            // Skip the header line
            try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // Read and discard the header line
                }
            }
            int numberOfProducts = getNumberOfProductsInput();
    
            if (numberOfProducts <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid number of products. Please enter a positive integer.");
                return;
            }
    
            for (int i = 0; i < numberOfProducts; i++) {
                String name = JOptionPane.showInputDialog("Name:");
                String uppercaseString = name.toUpperCase();
                String productId = JOptionPane.showInputDialog("Product ID:");
                double price = getValidDoubleInput("Price:");
                int quantity = getValidIntegerInput("Quantity:");
    
                writer.append(uppercaseString).append(",").append(productId).append(",").append(String.valueOf(price)).append(",").append(String.valueOf(quantity)).append("\n");
            }
    
            JOptionPane.showMessageDialog(null, "Product(s) successfully added to Inventory!");
        } catch (IOException e) {
            showErrorDialog("Error occurred while adding product.");
        } finally {
            manageProduct();
        }
    }

    private static void removeProduct() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No products found.");
                return;
            }

            String productNameToRemove = JOptionPane.showInputDialog("Enter the name of the product to remove:");
            String uppercaseProductNameToRemove = productNameToRemove.toUpperCase();
            StringBuilder newData = new StringBuilder();

            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String productName = parts[0];
                    if (!productName.equals(uppercaseProductNameToRemove)) {
                        newData.append(line).append("\n");
                    }
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(newData.toString());
            }

            JOptionPane.showMessageDialog(null, "Product removed successfully.");
        } catch (IOException e) {
            showErrorDialog("Error occurred while removing product.");
        } finally {
            manageProduct();
        }
    }

    private static void updateProduct() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No products found.");
                return;
            }

            StringBuilder productsData = new StringBuilder();
            int productIndex = 1;

            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String productName = parts[0];
                    productsData.append(productIndex).append(". ").append(productName).append("\n");
                    productIndex++;
                }
            }

            String productList = productsData.toString();
            int productToUpdateIndex = getValidIntegerInput("Select a product to update:\n" + productList);

            StringBuilder newData = new StringBuilder();
            productIndex = 1;

            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (productIndex == productToUpdateIndex) {
                        String[] parts = line.split(",");
                        String productName = parts[0];
                        int choice = showUpdateOptionsDialog(productName);

                        String newName = parts[0];
                        String uppercaseNewName = newName.toUpperCase();
                        String newId = parts[1];
                        double newPrice = Double.parseDouble(parts[2]);
                        int newQuantity = Integer.parseInt(parts[3]);

                        switch (choice) {
                            case 0:
                                newName = JOptionPane.showInputDialog("Enter new product name for " + productName + ":");
                                break;
                            case 1:
                                newId = JOptionPane.showInputDialog("Enter new product ID for " + productName + ":");
                                break;
                            case 2:
                                newPrice = getValidDoubleInput("Enter new price for " + productName + ":");
                                break;
                            case 3:
                                newQuantity = getValidIntegerInput("Enter new quantity for " + productName + ":");
                                break;
                            default:
                                showErrorDialog("Invalid choice");
                                break;
                        }

                        newData.append(uppercaseNewName).append(",").append(newId).append(",").append(newPrice).append(",").append(newQuantity).append("\n");
                    } else {
                        newData.append(line).append("\n");
                    }
                    productIndex++;
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(newData.toString());
            }

            JOptionPane.showMessageDialog(null, "Product updated successfully.");
        } catch (IOException e) {
            showErrorDialog("Error occurred while updating product.");
        } finally {
            manageProduct();
        }
    }

    private static void searchProduct() {
        // Prompt the user to enter the keyword
        String searchKeyword = JOptionPane.showInputDialog("Enter the Product ID to search:");

        try {
            File file = new File(FILE_PATH);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Split the line by comma since it's a CSV file
                String[] data = line.split(",");

                // Iterate through each field in the CSV
                for (String field : data) {
                    // Check if the field contains the search keyword
                    if (field.contains(searchKeyword)) {
                        JOptionPane.showMessageDialog(null, "Product found! " + line);
                        // You can also print out the specific field where it's found
                        break; // Once found, stop searching this line
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found: " + e.getMessage());
        }
    }

    private static void displayProduct() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No Sales found.");
                return;
            }
    
            StringBuilder content = new StringBuilder(); // StringBuilder to store all lines
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    content.append(line).append("\n"); // Append each line to StringBuilder
                }
            }
            
            // Display all sales in a single message dialog
            JOptionPane.showMessageDialog(null, content.toString());
        } catch (IOException e) {
            showErrorDialog("Error occurred while displaying Sales.");
        } finally {
            manageProduct();
        }
    }

    // Utility methods
    private static int getNumberOfProductsInput() {
        int numberOfProducts;
        do {
            numberOfProducts = getValidIntegerInput("How many products would you like to input?");
        } while (numberOfProducts <= 0);
        return numberOfProducts;
    }

    private static int getValidIntegerInput(String message) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(JOptionPane.showInputDialog(message));
                break;
            } catch (NumberFormatException e) {
                showErrorDialog(INVALID_INPUT_MESSAGE);
            }
        }
        return input;
    }

    private static double getValidDoubleInput(String message) {
        double input;
        while (true) {
            try {
                input = Double.parseDouble(JOptionPane.showInputDialog(message));
                break;
            } catch (NumberFormatException e) {
                showErrorDialog(INVALID_INPUT_MESSAGE);
            }
        }
        return input;
    }

    private static int showUpdateOptionsDialog(String productName) {
        String[] options = {"Name", "ID", "Price", "Quantity"};
        return JOptionPane.showOptionDialog(null, "Which data do you want to update for " + productName + "?", "Update Product", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    private static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}