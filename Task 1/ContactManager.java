import java.util.ArrayList;
import java.util.Scanner;

public class ContactManager {
    private ArrayList<Contact> contacts;
    private Scanner scanner;

    public ContactManager() {
        contacts = new ArrayList<>();
        scanner = new Scanner(System.in);
        addSampleContacts(); // Add some sample contacts for testing
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1" -> addContact();
                    case "2" -> viewContacts();
                    case "3" -> updateContact();
                    case "4" -> deleteContact();
                    case "5" -> running = false;
                    default -> System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        System.out.println("Thank you for using the Contact Management System!");
    }

    private void displayMenu() {
        System.out.println("""
            
            ===== Contact Management System =====
            1. Add New Contact
            2. View All Contacts
            3. Update Contact
            4. Delete Contact
            5. Exit
            
            Enter your choice (1-5):\s""");
    }

    private void addContact() {
        System.out.println("\n=== Add New Contact ===");
        
        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter phone number: ");
            String phoneNumber = scanner.nextLine();
            
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            
            System.out.print("Enter notes: ");
            String notes = scanner.nextLine();

            Contact contact = new Contact(name, phoneNumber, email, address, notes);
            contacts.add(contact);
            System.out.println("Contact added successfully!");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        System.out.println("\n=== Contact List ===");
        System.out.println("Name                 | Phone          | Email");
        System.out.println("-".repeat(65));
        
        for (int i = 0; i < contacts.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, contacts.get(i).toListString());
        }
    }

    private void updateContact() {
        viewContacts();
        if (contacts.isEmpty()) return;
        
        System.out.print("\nEnter the number of the contact to update: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= contacts.size()) {
                System.out.println("Invalid contact number!");
                return;
            }
            
            Contact contact = contacts.get(index);
            System.out.println("\nCurrent contact details:\n" + contact.toString());
            System.out.println("Enter new details (press Enter to keep current value):");
            
            System.out.print("Name [" + contact.getName() + "]: ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) contact.setName(name);
            
            System.out.print("Phone [" + contact.getPhoneNumber() + "]: ");
            String phone = scanner.nextLine();
            if (!phone.isEmpty()) contact.setPhoneNumber(phone);
            
            System.out.print("Email [" + contact.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) contact.setEmail(email);
            
            System.out.print("Address [" + contact.getAddress() + "]: ");
            String address = scanner.nextLine();
            if (!address.isEmpty()) contact.setAddress(address);
            
            System.out.print("Notes [" + contact.getNotes() + "]: ");
            String notes = scanner.nextLine();
            if (!notes.isEmpty()) contact.setNotes(notes);
            
            System.out.println("Contact updated successfully!");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteContact() {
        viewContacts();
        if (contacts.isEmpty()) return;
        
        System.out.print("\nEnter the number of the contact to delete: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= contacts.size()) {
                System.out.println("Invalid contact number!");
                return;
            }
            
            Contact contact = contacts.get(index);
            System.out.println("\nAre you sure you want to delete this contact?");
            System.out.println(contact.toString());
            System.out.print("Enter 'yes' to confirm: ");
            
            if (scanner.nextLine().toLowerCase().equals("yes")) {
                contacts.remove(index);
                System.out.println("Contact deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
        }
    }

    private void addSampleContacts() {
        contacts.add(new Contact("John Doe", "123-456-7890", "john@email.com", "123 Main St", "Friend"));
        contacts.add(new Contact("Jane Smith", "987-654-3210", "jane@email.com", "456 Oak Ave", "Colleague"));
        contacts.add(new Contact("Bob Wilson", "555-555-5555", "bob@email.com", "789 Pine Rd", "Family"));
    }

    public static void main(String[] args) {
        new ContactManager().start();
    }
} 