import javax.swing.JOptionPane;

public class InventoryManagementSystem {

    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please enter a valid value.";
    
    public static void libertadPublicMarket() {
        int input3 = getValidIntegerInput("Welcome to Inventory Management System!\n1.Manage Products\n2.Manage Sale Records\n3.Manage Suppliers\n4.Generate Reports\n5.Exit");

        switch (input3) {
            case 1:
                ProductManagementSystem.manageProduct();
                break;
            case 2:
                SaleRecordSystem.manageSale();
                break;
            case 3:
                SupplierManagementSystem.manageSupplier();
                break;
            case 4:
                ReportSystem.generateReport();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid command");
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
