import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class SaleRecordSystem {

     private static final String FILE_PATH = "Sales.csv";
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please enter a valid value.";

    public static void manageSale() {
        String input4 = JOptionPane.showInputDialog("Welcome to Sale Record Management!\n1.Add Sale\n2.Remove Sale\n3.Update Sale\n4.Display Sale\n5.Search Sale Record\n6.Main Page");
        int command4 = Integer.parseInt(input4);

        switch (command4) {
            case 1:
                addSale();
                break;
            case 2:
                removeSale();
                break;
            case 3:
                updateSale();
                break;
            case 4:
                displaySale();
                break;
            case 5:
                searchSale();
                break;
            case 6:
                InventoryManagementSystem.libertadPublicMarket();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid command");
        }
    }

    private static void addSale() {
        try (FileWriter saleWriter = new FileWriter(FILE_PATH, true)) {
            // Skip the header line
            try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
                if (scanner.hasNextLine()) {
                    scanner.nextLine(); // Read and discard the header line
                }
            }

            int numberOfSales = getNumberOfSalesInput();

            if (numberOfSales <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid number of Sales. Please enter a positive integer.");
                return;
            }

            for (int i = 0; i < numberOfSales; i++) {
                String name = JOptionPane.showInputDialog("Product Name:");
                String uppercaseString = name.toUpperCase();
                String customerId = JOptionPane.showInputDialog("Sold to:");
                String uppercaseCustomer = customerId.toUpperCase();
                String month = JOptionPane.showInputDialog("Month:");
                String uppercaseMonth = month.toUpperCase();
                int year = getValidIntegerInput("Year:");
                int saleId = getValidIntegerInput("Sale ID:");
                double price = getValidDoubleInput("Price:");
                int quantity = getValidIntegerInput("Quantity:");

                // Call subtractSaleFromInventory method from ProductManagementSystem
                subtractSaleFromInventory(uppercaseString, quantity);

                // Write sale details to sales record
                saleWriter.append(uppercaseString).append(",").append(uppercaseCustomer).append(",").append(uppercaseMonth).append(",").append(String.valueOf(year)).append(",").append(String.valueOf(saleId)).append(",").append(String.valueOf(price)).append(",").append(String.valueOf(quantity)).append("\n");
            }

            JOptionPane.showMessageDialog(null, "Sale(s) successfully added to Record!");
        } catch (IOException e) {
            showErrorDialog("Error occurred while adding Sale.");
        } finally {
            manageSale();
        }
    }
    

    private static void removeSale() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No Sales found.");
                return;
            }

            String saleNameToRemove = JOptionPane.showInputDialog("Enter the product name of the sale to remove:");
            String uppercaseNameToRemove = saleNameToRemove.toUpperCase();
            StringBuilder newData = new StringBuilder();

            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String SaleName = parts[0];
                    if (!SaleName.equals(uppercaseNameToRemove)) {
                        newData.append(line).append("\n");
                    }
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(newData.toString());
            }

            JOptionPane.showMessageDialog(null, "Sale removed successfully.");
        } catch (IOException e) {
            showErrorDialog("Error occurred while removing Sale.");
        } finally {
            manageSale();
        }
    }

    private static void updateSale() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No Sales found.");
                return;
            }

            StringBuilder SalesData = new StringBuilder();
            int SaleIndex = 1;

            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String SaleName = parts[0];
                    SalesData.append(SaleIndex).append(". ").append(SaleName).append("\n");
                    SaleIndex++;
                }
            }

            String SaleList = SalesData.toString();
            int SaleToUpdateIndex = getValidIntegerInput("Select a Sale to update:\n" + SaleList);

            StringBuilder newData = new StringBuilder();
            SaleIndex = 1;

            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (SaleIndex == SaleToUpdateIndex) {
                        String[] parts = line.split(",");
                        String SaleName = parts[0];
                        int choice = showUpdateOptionsDialog(SaleName);

                        String newName = parts[0];
                        String uppercaseName = newName.toUpperCase();
                        String newId = parts[1];
                        String uppercaseNewId = newId.toUpperCase();
                        String newMonth = parts[2];
                        String uppercaseMonth = newMonth.toUpperCase();
                        int newYear = Integer.parseInt(parts[3]);
                        int newSaleId = Integer.parseInt(parts[4]);
                        double newPrice = Double.parseDouble(parts[5]);
                        int newQuantity = Integer.parseInt(parts[6]);

                        switch (choice) {
                            case 0:
                                newName = JOptionPane.showInputDialog("Enter new product name for " + SaleName + ":");
                                break;
                            case 1:
                                newId = JOptionPane.showInputDialog("Enter new customer name for " + SaleName + ":");
                                break;
                            case 2:
                                newMonth = JOptionPane.showInputDialog("Enter new month date for " + SaleName + ":");
                                break;
                            case 3:
                                newYear = getValidIntegerInput("Enter new day date for " + SaleName + ":");
                                break;
                            case 4:
                                newSaleId = getValidIntegerInput("Enter new year date for " + SaleName + ":");
                                break;
                            case 5:
                                newPrice = getValidDoubleInput("Enter new price for " + SaleName + ":");
                                break;
                            case 6:
                                newQuantity = getValidIntegerInput("Enter new quantity for " + SaleName + ":");
                                break;
                            default:
                                showErrorDialog("Invalid choice");
                                break;
                        }

                        newData.append(uppercaseName).append(",").append(uppercaseNewId).append(",").append(uppercaseMonth).append(",").append(newYear).append(",").append(newSaleId).append(",").append(newPrice).append(",").append(newQuantity).append("\n");
                    } else {
                        newData.append(line).append("\n");
                    }
                    SaleIndex++;
                }
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(newData.toString());
            }

            JOptionPane.showMessageDialog(null, "Sale updated successfully.");
        } catch (IOException e) {
            showErrorDialog("Error occurred while updating Sale.");
        } finally {
            manageSale();
        }
    }

        private static void searchSale() {
        // Prompt the user to enter the keyword
        String searchKeyword = JOptionPane.showInputDialog("Enter the Sale ID to search:");

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
    
    private static void displaySale() {
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
            manageSale();
        }
    }
    

    private static int getNumberOfSalesInput() {
        int numberOfSales;
        do {
            numberOfSales = getValidIntegerInput("How many Sales would you like to input?");
        } while (numberOfSales <= 0);
        return numberOfSales;
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

    private static int showUpdateOptionsDialog(String SaleName) { 
        String[] options = {"Product Name", "Customer Name", "Month", "Day", "Year", "Price", "Quantity"};
        return JOptionPane.showOptionDialog(null, "Which data do you want to update for " + SaleName + "?", "Update Sale", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    private static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
 
    }

    private static void subtractSaleFromInventory(String productName, int quantitySold) {
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
                        int updatedQuantity = currentQuantity - quantitySold;
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