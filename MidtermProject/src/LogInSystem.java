    import javax.swing.JOptionPane;
    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.FileReader;
    import java.io.FileWriter;
    import java.io.IOException;

    public class LogInSystem {

        private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please enter a valid value.";
        
        public static void main(String[] args) {
            String filePath = "user_credentials.csv";
            showWelcomeMenu(filePath);
        }

        public static void showWelcomeMenu(String filePath) {
            int input = getValidIntegerInput("Welcome to Inventory Management System!\n1. Log in\n2. Sign up\n3. Exit");

            switch (input) {
                case 1:
                    login(filePath);
                    break;
                case 2:
                    signUp(filePath);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Exiting program...");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid command");
            }
        }

        public static void login(String filePath) {
            if (logInMethod(filePath)) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                InventoryManagementSystem.libertadPublicMarket();
            } else {
                JOptionPane.showMessageDialog(null, "Login failed. Try Again.");
            }
        }

        public static void signUp(String filePath) {
            if (signUpMethod(filePath)) {
                String input2 = JOptionPane.showInputDialog(null, "Account creation successful! User credentials saved.\n1. Log in\n2. Exit");
                int command2 = Integer.parseInt(input2);

                switch (command2) {
                    case 1:
                        login(filePath);
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "Exiting program...");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid command");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Account creation failed. Unable to save user credentials.");
            }
        }

        public static boolean logInMethod(String filePath) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String inputUsername = JOptionPane.showInputDialog("Enter username:");
                String inputPassword = JOptionPane.showInputDialog("Enter password:");

                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String username = parts[0];
                    String password = parts[1];

                    if (inputUsername.equals(username) && inputPassword.equals(password)) {
                        return true; // Login successful
                    }
                }

                return false; // Login failed

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading CSV file: " + e.getMessage());
                return false; // Login failed due to an error
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
                return false; // Login failed due to an unexpected error
            }
        }

        public static boolean signUpMethod(String filePath) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                // Prompt the user for username
                String username = JOptionPane.showInputDialog("Enter username:");
                // Ensure username is not empty
                if (username == null || username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username cannot be empty.");
                    return false;
                }

                // Prompt the user for password
                String password = JOptionPane.showInputDialog("Enter password:");
                // Ensure password is not empty
                if (password == null || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Password cannot be empty.");
                    return false;
                }

                // Write username and password to CSV file
                writer.write(username + "," + password);
                writer.newLine(); // Add newline after each input line

                return true; // Return true indicating successful signup
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error writing to CSV file: " + e.getMessage());
                return false; // Return false indicating signup failure
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e.getMessage());
                return false; // Signup failed due to an unexpected error
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