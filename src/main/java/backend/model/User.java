package backend.model;
import java.util.List;

import backend.logic.InvoiceService;


public class User {
    private int id; //created automatically by database
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private UserState state;

    public User(String name, String email, String password, UserRole role, UserState state) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.state = state;
    }
    
    public String getName() {
    	return this.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UserRole getRole() {
        return role;

    public List<Invoice> viewCurrentReimbursement() {
        // Logik zur Anzeige aktueller Erstattungen
        return InvoiceService.getInvoices(this);

    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }
}