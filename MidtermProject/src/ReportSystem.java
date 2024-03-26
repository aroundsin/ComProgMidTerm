import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReportSystem {

    private static final String PRODUCTS_FILE_PATH = "products.csv";
    private static final String SALES_FILE_PATH = "Sales.csv";
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please enter a valid value.";    

    public static void generateReport() {
        int input4 = getValidIntegerInput("Welcome to Report Management!\n1.Display Product Report\n2.Display Sale Report\n3.Main Page");

        switch (input4) {
            case 1:
                displayProduct();
                break;
            case 2:
                displaySale();
                break;
            case 3:
            InventoryManagementSystem.libertadPublicMarket();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid command");
        }
    }

    public static void displayProduct() {

        StringBuilder lowStockProducts = new StringBuilder("Low Stock Products:\n");
        StringBuilder highStockProducts = new StringBuilder("High Stock Products:\n");

        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCTS_FILE_PATH))) {

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] productData = line.split(",");
                String productName = productData[0];
                int quantity = Integer.parseInt(productData[3]); // Assuming quantity is at index 3

                if (quantity < 50) {
                    lowStockProducts.append(productName).append(": ").append(quantity).append("\n");
                } else {
                    highStockProducts.append(productName).append(": ").append(quantity).append("\n");
                }
            }

            // Display low stock products
            JOptionPane.showMessageDialog(null, lowStockProducts.toString(), "Low Stock Products", JOptionPane.INFORMATION_MESSAGE);

            // Display high stock products
            JOptionPane.showMessageDialog(null, highStockProducts.toString(), "High Stock Products", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            showErrorDialog("Error occurred while reading product data.");
        } finally {
            generateReport();
        }
    }

    private static void displaySale() {
        try (BufferedReader br = new BufferedReader(new FileReader(SALES_FILE_PATH))) {
            // Maps to store sales data by product name, customer name, and month
            Map<String, StringBuilder> productSales = new HashMap<>();
            Map<String, StringBuilder> customerSales = new HashMap<>();
            Map<String, StringBuilder> monthSales = new HashMap<>();

            // Read header line
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String productName = parts[0];
                String customerName = parts[1];
                String month = parts[2];

                // Add sale to product sales map
                if (!productSales.containsKey(productName)) {
                    productSales.put(productName, new StringBuilder());
                }
                productSales.get(productName).append(line).append("\n");

                // Add sale to customer sales map
                if (!customerSales.containsKey(customerName)) {
                    customerSales.put(customerName, new StringBuilder());
                }
                customerSales.get(customerName).append(line).append("\n");

                // Add sale to month sales map
                if (!monthSales.containsKey(month)) {
                    monthSales.put(month, new StringBuilder());
                }
                monthSales.get(month).append(line).append("\n");
            }

            // Display sales by product name
            StringBuilder productSalesMsg = new StringBuilder("Sales by Product Name:\n");
            for (Map.Entry<String, StringBuilder> entry : productSales.entrySet()) {
                productSalesMsg.append(entry.getKey()).append(":\n").append(entry.getValue()).append("\n");
            }
            JOptionPane.showMessageDialog(null, productSalesMsg.toString());

            // Display sales by customer name
            StringBuilder customerSalesMsg = new StringBuilder("Sales by Customer Name:\n");
            for (Map.Entry<String, StringBuilder> entry : customerSales.entrySet()) {
                customerSalesMsg.append(entry.getKey()).append(":\n").append(entry.getValue()).append("\n");
            }
            JOptionPane.showMessageDialog(null, customerSalesMsg.toString());

            // Display sales by month
            StringBuilder monthSalesMsg = new StringBuilder("Sales by Month:\n");
            for (Map.Entry<String, StringBuilder> entry : monthSales.entrySet()) {
                monthSalesMsg.append("Month: ").append(entry.getKey()).append("\n").append(entry.getValue()).append("\n");
            }
            JOptionPane.showMessageDialog(null, monthSalesMsg.toString());

        } catch (IOException e) {
            showErrorDialog("Error occurred while displaying Sales.");
        } finally {
            generateReport();
        }
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

    private static void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
