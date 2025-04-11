package backend.model;
import java.util.Date;

public class Reimbursement {
    private int id; //created automatically by database
    private Invoice invoice;
    private float approvedAmount;
    private Date processedDate;
    private ReimbursementState state;
    
    
    public Reimbursement() {
    	
    }
    
    public Reimbursement(Invoice invoice, float approvedAmount, Date processedDate) {
        this.invoice = invoice;
        this.approvedAmount = approvedAmount;
        this.processedDate = processedDate;
    }

    public int getId() {
        return this.id;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public double getApprovedAmount() {
        return this.approvedAmount;
    }

    public Date getProcessedDate() {
        return this.processedDate;
    }
    
    public ReimbursementState getState() {
    	return this.state;
    }

}
