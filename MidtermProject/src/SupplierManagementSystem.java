import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SupplierManagementSystem {

    // Constants for file path and messages
    private static final String FILE_PATH = "Suppliers.csv";
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please enter a valid value.";

    public static void manageSupplier() {
        int input4 = getValidIntegerInput("Welcome to Supplier Management!\n1.Add Supplier\n2.Remove Supplier\n3.Update Supplier\n4.Display Suppliers\n5.Search Supplier\n6.Main Page");

        switch (input4) {
            case 1:
                addSupplier();
                break;
            case 2:
                removeSupplier();
                break;
            case 3:
                updateSupplier();
                break;
            case 4:
                displaySupplier();
                break;
            case 5:
                searchSupplier();
                break;
            case 6:
                InventoryManagementSystem.libertadPublicMarket();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid command");
        }
    }

   private static void addSupplier() {
    try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
        // Skip the header line
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Read and discard the header line
            }
        }

        int numberOfSuppliers = getNumberOfSuppliersInput();

        if (numberOfSuppliers <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid number of Suppliers. Please enter a positive integer.");
            return; // Return early if input is invalid
        }

        for (int i = 0; i < numberOfSuppliers; i++) {
            String name = JOptionPane.showInputDialog("Name:");
            String uppercaseString = name.toUpperCase();
            String contactNumber = getValidContactNumberInput("Contact Number:");
            String OrderedItem = JOptionPane.showInputDialog("Item Ordered:");
            String uppercaseOrderedItem = OrderedItem.toUpperCase();
            double price = getValidDoubleInput("Price:");
            int quantity = getValidIntegerInput("Quantity:");

            addProductToInventory(uppercaseOrderedItem, quantity);

            writer.append(uppercaseString).append(",").append(contactNumber).append(",").append(uppercaseOrderedItem).append(",").append(String.valueOf(price)).append(",").append(String.valueOf(quantity)).append("\n");
        }

        JOptionPane.showMessageDialog(null, "Supplier(s) successfully added to Inventory!");
    } catch (IOException e) {
        showErrorDialog("Error occurred while adding Supplier.");
    } finally {
        manageSupplier();
    }
}

    private static void removeSupplier() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No Suppliers found.");
                return;
            }

            String SupplierNameToRemove = JOptionPane.showInputDialog("Enter the name of the Supplier to remove:");
            String uppercaseSupplierNameToRemove = SupplierNameToRemove.toUpperCase();
            StringBuilder newData = new StringBuilder();

            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String SupplierName = parts[0];
                    if (!SupplierName.equals(uppercaseSupplierNameToRemove)) {
                        newData.append(line).append("\n");
                    }
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(newData.toString());
            }

            JOptionPane.showMessageDialog(null, "Supplier removed successfully.");
        } catch (IOException e) {
            showErrorDialog("Error occurred while removing Supplier.");
        } finally {
            manageSupplier();
        }
    }

    private static void updateSupplier() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No Suppliers found.");
                return;
            }
    
            StringBuilder suppliersData = new StringBuilder();
            int supplierIndex = 1;
    
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String supplierName = parts[0];
                    suppliersData.append(supplierIndex).append(". ").append(supplierName).append("\n");
                    supplierIndex++;
                }
            }
    
            String supplierList = suppliersData.toString();
            int supplierToUpdateIndex = getValidIntegerInput("Select a Supplier to update:\n" + supplierList);
    
            StringBuilder newData = new StringBuilder();
            supplierIndex = 1;
    
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (supplierIndex == supplierToUpdateIndex) {
                        String[] parts = line.split(",");
                        String supplierName = parts[0];
                        int choice = showUpdateOptionsDialog(supplierName);
    
                        String newName = parts[0];
                        String uppercaseNewName = newName.toUpperCase();
                        String newContactNumber = parts[1]; // Declare newContactNumber here
                        String newOrderedItem = parts[2]; // Declare newOrderedItem here
                        double newPrice = Double.parseDouble(parts[3]);
                        int newQuantity = Integer.parseInt(parts[4]);
    
                        switch (choice) {
                            case 0:
                                newName = JOptionPane.showInputDialog("Enter new Supplier name for " + supplierName + ":");
                                break;
                            case 1:
                                newContactNumber = JOptionPane.showInputDialog("Enter new Contact Number for " + supplierName + ":");
                                break;
                            case 2:
                                newOrderedItem = JOptionPane.showInputDialog("Enter new Ordered Item from " + supplierName + ":");
                                break;
                            case 3:
                                newPrice = getValidDoubleInput("Enter new price for " + newOrderedItem + ":");
                                break;
                            case 4:
                                newQuantity = getValidIntegerInput("Enter new quantity for " + newOrderedItem + ":");
                                break;
                            default:
                                showErrorDialog("Invalid choice");
                                break;
                        }
    
                        newData.append(uppercaseNewName).append(",").append(newContactNumber).append(",").append(newOrderedItem).append(",").append(newPrice).append(",").append(newQuantity).append("\n");
                    } else {
                        newData.append(line).append("\n");
                    }
                    supplierIndex++;
                }
            }
    
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(newData.toString());
            }
    
            JOptionPane.showMessageDialog(null, "Supplier updated successfully.");
        } catch (IOException e) {
            showErrorDialog("Error occurred while updating Supplier.");
        } finally {
            manageSupplier();
        }
    }
    
            private static void searchSupplier() {
        // Prompt the user to enter the keyword
        String searchKeyword = JOptionPane.showInputDialog("Enter the Supplier ID to search:");

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
                        JOptionPane.showMessageDialog(null, "Product record! " + line);
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

    private static void displaySupplier() {
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
            manageSupplier();
        }
    }

    // Utility methods
    private static int getNumberOfSuppliersInput() {
        int numberOfSuppliers;
        do {
            numberOfSuppliers = getValidIntegerInput("How many Suppliers would you like to input?");
        } while (numberOfSuppliers <= 0);
        return numberOfSuppliers;
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
    
    private static String getValidContactNumberInput(String message) {
        String contactNumber;
        do {
            try { 
                contactNumber = JOptionPane.showInputDialog(message);
                if (!contactNumber.matches("\\d+")) {
                    showErrorDialog(INVALID_INPUT_MESSAGE);
                }
            } catch (NumberFormatException e) {
                showErrorDialog(INVALID_INPUT_MESSAGE);
                contactNumber = null; // Reset contactNumber if an exception occurs
            }
        } while (contactNumber == null || !contactNumber.matches("\\d+"));
        return contactNumber;
    }    
    
    private static int showUpdateOptionsDialog(String SupplierName) {
        String[] options = {"Name","Contact Number","Item Ordered", "Price", "Quantity"};
        return JOptionPane.showOptionDialog(null, "Which data do you want to update for " + SupplierName + "?", "Update Supplier", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    private static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void addProductToInventory(String productName, int quantityOrdered) {
        try {
            File productsFile = new File("products.csv");
            if (!productsFile.exists()) {
                JOptionPane.showMessageDialog(null, "Products file not found.");
                return;
            }
    
            StringBuilder updatedData = new StringBuilder();
    
            try (Scanner fileScanner = new Scanner(productsFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String name = parts[0]; // Assuming product name is at index 0 in each line
                    int currentQuantity = Integer.parseInt(parts[3]); // Assuming quantity is at index 3 in each line
    
                    if (name.equals(productName)) {
                        int updatedQuantity = currentQuantity + quantityOrdered;
                        updatedQuantity = Math.max(updatedQuantity, 0);
                        parts[3] = String.valueOf(updatedQuantity); // Update the quantity in the parts array
                    }
    
                    updatedData.append(String.join(",", parts)).append("\n");
                }
            }
    
            try (FileWriter writer = new FileWriter(productsFile)) {
                writer.write(updatedData.toString());
            }
        } catch (IOException e) {
            showErrorDialog("Error occurred while updating inventory.");
        }
    }    
}