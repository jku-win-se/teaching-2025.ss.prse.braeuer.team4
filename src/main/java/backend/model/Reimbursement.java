package backend.model;
import java.util.Date;

public class Reimbursement {
    private int id; //created automatically by database
    private Invoice invoice;
    private float approvedAmount;
    private Date processedDate;
    private ReimbursementState status;
    
    
    public Reimbursement() {
    	
    }
    
    public Reimbursement(Invoice invoice, float approvedAmount, Date processedDate, ReimbursementState status) {
        this.invoice = invoice;
        this.approvedAmount = approvedAmount;
        this.processedDate = processedDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public double getApprovedAmount() {
        return approvedAmount;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

}
