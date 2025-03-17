import java.util.Scanner;

public class Calculator {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    basicArithmetic();
                    break;
                case 2:
                    scientificCalculations();
                    break;
                case 3:
                    unitConversions();
                    break;
                case 4:
                    running = false;
                    System.out.println("Thank you for using the calculator!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=== Advanced Calculator ===");
        System.out.println("1. Basic Arithmetic");
        System.out.println("2. Scientific Calculations");
        System.out.println("3. Unit Conversions");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void basicArithmetic() {
        System.out.println("\n=== Basic Arithmetic ===");
        System.out.print("Enter first number: ");
        double num1 = getDoubleInput();
        System.out.print("Enter operator (+, -, *, /): ");
        String operator = scanner.nextLine();
        System.out.print("Enter second number: ");
        double num2 = getDoubleInput();

        try {
            double result = switch (operator) {
                case "+" -> num1 + num2;
                case "-" -> num1 - num2;
                case "*" -> num1 * num2;
                case "/" -> {
                    if (num2 == 0) throw new ArithmeticException("Division by zero!");
                    yield num1 / num2;
                }
                default -> throw new IllegalArgumentException("Invalid operator!");
            };
            System.out.printf("Result: %.2f %s %.2f = %.2f%n", num1, operator, num2, result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void scientificCalculations() {
        System.out.println("\n=== Scientific Calculations ===");
        System.out.println("1. Square Root");
        System.out.println("2. Power");
        System.out.println("3. Sine");
        System.out.println("4. Cosine");
        System.out.println("5. Tangent");
        System.out.print("Enter your choice (1-5): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            double num, result;

            switch (choice) {
                case 1:
                    System.out.print("Enter number: ");
                    num = getDoubleInput();
                    if (num < 0) throw new IllegalArgumentException("Cannot calculate square root of negative number!");
                    result = Math.sqrt(num);
                    System.out.printf("Square root of %.2f = %.2f%n", num, result);
                    break;
                case 2:
                    System.out.print("Enter base: ");
                    num = getDoubleInput();
                    System.out.print("Enter exponent: ");
                    double exponent = getDoubleInput();
                    result = Math.pow(num, exponent);
                    System.out.printf("%.2f raised to %.2f = %.2f%n", num, exponent, result);
                    break;
                case 3:
                    System.out.print("Enter angle in degrees: ");
                    num = getDoubleInput();
                    result = Math.sin(Math.toRadians(num));
                    System.out.printf("sin(%.2f°) = %.2f%n", num, result);
                    break;
                case 4:
                    System.out.print("Enter angle in degrees: ");
                    num = getDoubleInput();
                    result = Math.cos(Math.toRadians(num));
                    System.out.printf("cos(%.2f°) = %.2f%n", num, result);
                    break;
                case 5:
                    System.out.print("Enter angle in degrees: ");
                    num = getDoubleInput();
                    result = Math.tan(Math.toRadians(num));
                    System.out.printf("tan(%.2f°) = %.2f%n", num, result);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void unitConversions() {
        System.out.println("\n=== Unit Conversions ===");
        System.out.println("1. Celsius to Fahrenheit");
        System.out.println("2. Fahrenheit to Celsius");
        System.out.println("3. Kilometers to Miles");
        System.out.println("4. Miles to Kilometers");
        System.out.print("Enter your choice (1-4): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            double input, result;

            switch (choice) {
                case 1:
                    System.out.print("Enter temperature in Celsius: ");
                    input = getDoubleInput();
                    result = (input * 9/5) + 32;
                    System.out.printf("%.2f°C = %.2f°F%n", input, result);
                    break;
                case 2:
                    System.out.print("Enter temperature in Fahrenheit: ");
                    input = getDoubleInput();
                    result = (input - 32) * 5/9;
                    System.out.printf("%.2f°F = %.2f°C%n", input, result);
                    break;
                case 3:
                    System.out.print("Enter distance in kilometers: ");
                    input = getDoubleInput();
                    result = input * 0.621371;
                    System.out.printf("%.2f km = %.2f miles%n", input, result);
                    break;
                case 4:
                    System.out.print("Enter distance in miles: ");
                    input = getDoubleInput();
                    result = input * 1.60934;
                    System.out.printf("%.2f miles = %.2f km%n", input, result);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please enter a number: ");
            }
        }
    }
}
