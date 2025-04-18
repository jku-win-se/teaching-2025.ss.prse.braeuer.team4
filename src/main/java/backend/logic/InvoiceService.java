package backend.logic;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import backend.interfaces.ConnectionProvider;
import backend.model.Invoice;
import backend.model.InvoiceCategory;
import backend.model.ReimbursementState;
import backend.model.User;
import database.DatabaseConnection;

//TODO not finished - still working on it


public class InvoiceService {
	public static ConnectionProvider connectionProvider;
	private User user;
	public List<Invoice> invoices;
	
	public static void setConnectionProvider(ConnectionProvider provider) {
	        connectionProvider = provider;
	}
	
	
	public InvoiceService () {
		this.invoices = new ArrayList<>();
	}
	
	public InvoiceService(User user) {
        this.user = user;
        if (connectionProvider != null) {
            this.invoices = getAllInvoices(user);
        } else {
            this.invoices = new ArrayList<>();
        }
    }
	
	public boolean invoiceDateAlreadyUsed (LocalDate date, User user) {
	   for (Invoice invoice: invoices) {
		   if (invoice.getDate().equals(date)) return true;
	   }
	   return false;
   }
	
	public boolean isValidDate(LocalDate date) {
		if(date == null) {
			return false;
		}
		  // Das heutige Datum
        LocalDate today = LocalDate.now();

        // Überprüfen, ob das Datum im gleichen Monat und Jahr wie heute ist
        boolean isSameMonth = date.getMonth() == today.getMonth() && date.getYear() == today.getYear();

        // Überprüfen, ob das Datum nicht in der Zukunft liegt
        boolean isNotInFuture = !date.isAfter(today);

        // Beide Bedingungen müssen erfüllt sein
        return isSameMonth && isNotInFuture && isWorkday(date);
    }

	public static boolean isWorkday (LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
	}
	
	public boolean isValidFloat(String text) { //created by AI (ChatGPT)
		return text.matches("^\\d+(\\.\\d+)?$");
	}
	
	public boolean isamaountValid(String text) {
		return (text!=null && isValidFloat(text));	
	}
	
	public static List<Invoice> getAllInvoices (User user) {
		List<Invoice> invoices = new ArrayList<>();
				
		String sql = "SELECT id, amount, category, date FROM invoices WHERE user_id = ?";
		try (Connection conn = connectionProvider.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, user.getId());
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                Invoice invoice = new Invoice();
	                invoice.setId(rs.getInt("id"));
	                invoice.setAmount(rs.getFloat("amount"));
	                invoice.setCategory(InvoiceCategory.valueOf(rs.getString("category")));
	                invoice.setDate(rs.getDate("date").toLocalDate());
	                invoices.add(invoice);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return invoices;
	}
	
	public List<Invoice> getInvoices (){
		return this.invoices;
	}
	
	public static boolean addInvoice(Invoice invoice) { //created with AI (ChatGPT)
	    String sql = "INSERT INTO invoices (date, amount, category, user_id, file, flagged) VALUES (?, ?, ?, ?, ?, ?)";

	    try (Connection conn = connectionProvider.getConnection();
	        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	    	stmt.setDate(1, Date.valueOf(invoice.getDate()));
	    	stmt.setFloat(2, invoice.getAmount());
	        stmt.setObject(3, invoice.getCategory(), Types.OTHER);
	        stmt.setInt(4, invoice.getUser().getId()); // Nutzer-ID setzen
			stmt.setBoolean(6, invoice.isFlagged());
	        
	        if (invoice.getFile() != null) { // Falls eine Datei vorhanden ist
	            try {
					stmt.setBinaryStream(5, new FileInputStream(invoice.getFile()), (int) invoice.getFile().length());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	        } else {
	            stmt.setNull(5, Types.BINARY); // Falls keine Datei da ist
			}

			int affectedRows = stmt.executeUpdate(); // SQL ausführen
	        if (affectedRows > 0) {
	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                invoice.setId(generatedKeys.getInt(1)); // Neue ID setzen
	            }
	            return true; // Erfolg
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // Falls etwas schiefgeht
	}

}
