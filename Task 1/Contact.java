public class Contact {
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String notes;

    public Contact(String name, String phoneNumber, String email, String address, String notes) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.notes = notes;
    }

    // Getters
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}|\\d{3}[-.]\\d{3}[-.]\\d{4}|\\(\\d{3}\\)\\d{3}-\\d{4}")) {
            throw new IllegalArgumentException("Invalid phone number format. Use: 1234567890, 123-456-7890, 123.456.7890, or (123)456-7890");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = (address != null) ? address.trim() : "";
    }

    public void setNotes(String notes) {
        this.notes = (notes != null) ? notes.trim() : "";
    }

    @Override
    public String toString() {
        return String.format("""
            Name: %s
            Phone: %s
            Email: %s
            Address: %s
            Notes: %s
            """, name, phoneNumber, email, address, notes);
    }

    // For displaying in list format
    public String toListString() {
        return String.format("%-20s | %-15s | %-25s", name, phoneNumber, email);
    }
} 