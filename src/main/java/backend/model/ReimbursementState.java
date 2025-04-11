package backend.model;

public enum ReimbursementState {
    PENDING,       // Rückerstattungsantrag wurde hochgeladen, aber noch nicht verarbeitet
    APPROVED,      // Rückerstattungsantrag wurde geprüft und genehmigt
    REJECTED,     // Rückerstattungsantrag wurde abgelehnt
	FLAGGED;


    // Optional: Methode zur Beschreibung des Status
    public String getDescription() {
        switch (this) {
            case PENDING:
                return "Die Rechnung wartet auf Bearbeitung.";
            case APPROVED:
                return "Die Rechnung wurde genehmigt.";
            case REJECTED:
                return "Die Rechnung wurde abgelehnt.";
            case FLAGGED:
            		return "Die Rechnung liegt zur Prüfung bei einem Admin";
            default:
                return "Unbekannter Status.";
        }
    }
}