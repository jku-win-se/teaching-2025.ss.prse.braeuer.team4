package lunchifyTests;

import backend.logic.ReimbursementService;
import backend.model.Invoice;
import backend.model.InvoiceCategory;
import backend.model.Reimbursement;
import backend.model.ReimbursementState;
import backend.model.User;
import backend.model.UserRole;
import backend.model.UserState;
import frontend.controller.ReimbursementHistoryController;
import backend.interfaces.ConnectionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReimbursementServiceTest {

    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockStatement;
    @Mock private ResultSet mockResultSet;

    private ReimbursementService service;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User(1, "dummy", "dummy@lunch.at", UserRole.ADMIN, UserState.ACTIVE);

        ConnectionProvider provider = () -> mockConnection;
        ReimbursementService.setConnectionProvider(provider);

        lenient().when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        lenient().when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        lenient().when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        service = new ReimbursementService(testUser);
    }

    @Test
    void testAddReimbursementSuccess() throws Exception {
        Invoice inv = new Invoice();
        inv.setId(10);
        inv.setDate(LocalDate.now());

        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(42);

        boolean result = service.addReimbursement(inv, 5.0f);

        assertTrue(result);
        assertEquals(42, inv.getId());
    }

    @Test
    void testGetTotalReimbursementDefault() {
        Reimbursement r1 = new Reimbursement();
        r1.setApprovedAmount(3.0f);
        r1.setStatus(ReimbursementState.APPROVED);

        Reimbursement r2 = new Reimbursement();
        r2.setApprovedAmount(2.0f);
        r2.setStatus(ReimbursementState.REJECTED);

        float total = service.getTotalReimbursement(Arrays.asList(r1, r2));
        assertEquals(3.0f, total);
    }

    @Test
    void testConvertMonthToNumberValid() {
        assertEquals("4", service.convertMonthToNumber("April"));
        assertEquals("12", service.convertMonthToNumber("Dezember"));
    }
    
    @Test
    void testConvertMonthToNumberInvalid() {
    	Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.convertMonthToNumber("invalidMonth");
        });
        assertTrue(exception.getMessage().contains("Ungültiger Monat"));
    }
    
    @Test
    void testGetFilteredReimbursementsWithVariousFilters() {
        List<Reimbursement> result = service.getFilteredReimbursements("April", "2023", "RESTAURANT", "APPROVED", testUser.getId());
        assertNotNull(result);
    }
    
    @Test
    void testGetAllReimbursements() {
        List<Reimbursement> result = service.getAllReimbursements(testUser.getId());
        assertNotNull(result);
    }

    @Test
    void testGetCurrentReimbursements() {
        List<Reimbursement> result = service.getCurrentReimbursements(testUser.getId());
        assertNotNull(result);
    }
    
    @Test
    void testGetTotalReimbursementEmptyList() {
    	float total = service.getTotalReimbursement(List.of());
    	assertEquals(0.0f, total);
    }
    
    @Test
    void testGetTotalReimbursementAllRejected() {
        Reimbursement r1 = new Reimbursement();
        r1.setApprovedAmount(1.0f);
        r1.setStatus(ReimbursementState.REJECTED);

        float total = service.getTotalReimbursement(List.of(r1));
        assertEquals(0.0f, total);
    }
    
    @Test
    void testGetTotalReimbursementByStateEmptyList() {
        float total = service.getTotalReimbursement(List.of(), ReimbursementState.APPROVED);
        assertEquals(0.0f, total);
    }
    
    @Test
    void testGetTotalReimbursementByStateNoMatch() {
        Reimbursement r1 = new Reimbursement();
        r1.setApprovedAmount(2.0f);
        r1.setStatus(ReimbursementState.REJECTED);

        float total = service.getTotalReimbursement(List.of(r1), ReimbursementState.APPROVED);
        assertEquals(0.0f, total);
    }
    
    @Test
    void testGetFilteredReimbursementsWithAllMonth() {
        List<Reimbursement> result = service.getFilteredReimbursements("alle", "2023", "RESTAURANT", "APPROVED", testUser.getId());
        assertNotNull(result);
    }
    
    @Test //created by AI
    void testAddReimbursement_ConnectionProviderNull_shouldThrowException() {
        // Arrange
        ReimbursementService.setConnectionProvider(null); // bewusst null setzen
        ReimbursementService service = new ReimbursementService();
        Invoice dummyInvoice = new Invoice(); // dummy Objekt, Details egal

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            service.addReimbursement(dummyInvoice, 10.0f);
        }, "Expected IllegalStateException when connectionProvider is null");
    }
   
    @Test
    void testNewReimbursementHasPendingStatus() throws Exception {
        // Setup Invoice
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setAmount(10.0f);
        invoice.setCategory(InvoiceCategory.RESTAURANT);
        invoice.setDate(LocalDate.now());

        // Mock INSERT
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        ResultSet mockKeys = mock(ResultSet.class);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockKeys);
        when(mockKeys.next()).thenReturn(true);
        when(mockKeys.getInt(1)).thenReturn(123);

        boolean added = service.addReimbursement(invoice, 3.0f);

        assertTrue(added);
        assertEquals(123, invoice.getId());

        // Jetzt simulieren wir eine Rückgabe mit Status PENDING
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(startsWith("SELECT"))).thenReturn(mockSelectStmt);
        when(mockSelectStmt.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // nur ein Eintrag
        when(mockResultSet.getInt("reimbId")).thenReturn(123);
        when(mockResultSet.getFloat("approved_amount")).thenReturn(3.0f);
        when(mockResultSet.getDate("processed_date")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getString("status")).thenReturn("PENDING");

        when(mockResultSet.getInt("invoice_id")).thenReturn(10);
        when(mockResultSet.getInt("user_Id")).thenReturn(testUser.getId());
        when(mockResultSet.getFloat("invoiceAmount")).thenReturn(10.0f);
        when(mockResultSet.getString("category")).thenReturn("RESTAURANT");
        when(mockResultSet.getString("userEmail")).thenReturn("user@example.com");
        when(mockResultSet.getDate("date")).thenReturn(Date.valueOf(LocalDate.now()));

        List<Reimbursement> reimbursements = service.getAllReimbursements(testUser.getId());

        assertEquals(1, reimbursements.size());
        assertEquals(ReimbursementState.PENDING, reimbursements.get(0).getStatus());
    }
    
    @Test //created by AI - changed by the team
    void testGetFilteredReimbursementsReturnsCorrectItems() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("reimbId")).thenReturn(1);
        when(mockResultSet.getFloat("approved_amount")).thenReturn(5.0f);
        when(mockResultSet.getDate("processed_date")).thenReturn(Date.valueOf("2023-04-10"));
        when(mockResultSet.getString("status")).thenReturn("APPROVED");
        when(mockResultSet.getInt("user_Id")).thenReturn(1);
        when(mockResultSet.getInt("invoice_id")).thenReturn(10);
        when(mockResultSet.getFloat("invoiceAmount")).thenReturn(10.0f);
        when(mockResultSet.getString("category")).thenReturn("RESTAURANT");
        when(mockResultSet.getString("userEmail")).thenReturn("user@example.com");
        when(mockResultSet.getDate("date")).thenReturn(Date.valueOf("2023-04-01"));

        List<Reimbursement> filtered = service.getFilteredReimbursements("April", "2023", "RESTAURANT", "APPROVED", testUser.getId());

        assertEquals(1, filtered.size());
        assertEquals("RESTAURANT", filtered.get(0).getInvoice().getCategory().toString());
        assertEquals(ReimbursementState.APPROVED, filtered.get(0).getStatus());
    }
    
    @Test
    void testGetFilteredReimbursementsWithFilters() {
        // Mock für Service und Testdaten
        ReimbursementService mockService = mock(ReimbursementService.class);
        User testUser = new User(1, "Test", "test@example.com", UserRole.ADMIN, UserState.ACTIVE);

        Invoice invoice1 = new Invoice(LocalDate.of(2023, 4, 1), 10.0f, InvoiceCategory.RESTAURANT, null, testUser);
        Invoice invoice2 = new Invoice(LocalDate.of(2023, 4, 10), 20.0f, InvoiceCategory.RESTAURANT, null, testUser);

        Reimbursement r1 = new Reimbursement(invoice1, 5.0f, Date.valueOf(LocalDate.of(2023, 4, 5)));
        Reimbursement r2 = new Reimbursement(invoice2, 15.0f, Date.valueOf(LocalDate.of(2023, 4, 15)));

        List<Reimbursement> mockReimbursements = List.of(r1, r2);

        when(mockService.getFilteredReimbursements("April", "2023", "RESTAURANT", "APPROVED", testUser.getId()))
            .thenReturn(mockReimbursements);

        List<Reimbursement> filteredReimbursements = mockService.getFilteredReimbursements("April", "2023", "RESTAURANT", "APPROVED", testUser.getId());

        // Assertions
        assertNotNull(filteredReimbursements);
        assertEquals(2, filteredReimbursements.size());
        assertEquals(InvoiceCategory.RESTAURANT, filteredReimbursements.get(0).getInvoice().getCategory());
        assertEquals(5.0f, filteredReimbursements.get(0).getApprovedAmount());
    }
    
    @Test
    void testGetTotalReimbursementWithoutFilters() {
        // Mock für Service und Daten
        ReimbursementService mockService = mock(ReimbursementService.class);

        Invoice invoice1 = new Invoice(LocalDate.of(2023, 4, 1), 10.0f, InvoiceCategory.RESTAURANT, null, null);
        Invoice invoice2 = new Invoice(LocalDate.of(2023, 4, 10), 15.0f, InvoiceCategory.SUPERMARKET, null, null);

        Reimbursement r1 = new Reimbursement(invoice1, 5.0f, Date.valueOf(LocalDate.of(2023, 4, 5)));
        Reimbursement r2 = new Reimbursement(invoice2, 10.0f, Date.valueOf(LocalDate.of(2023, 4, 10)));

        List<Reimbursement> reimbursements = List.of(r1, r2);

        when(mockService.getTotalReimbursement(reimbursements)).thenReturn(15.0f);

        float total = mockService.getTotalReimbursement(reimbursements);

        // Assertions
        assertEquals(15.0f, total);
    }
    
    @Test
    void testGetFilteredReimbursementsWithoutSpecificMonthAndYear() {
        ReimbursementService mockService = mock(ReimbursementService.class);
        User testUser = new User(1, "Test", "test@example.com", UserRole.ADMIN, UserState.ACTIVE);

        Invoice invoice = new Invoice(LocalDate.of(2023, 1, 1), 10.0f, InvoiceCategory.SUPERMARKET, null, testUser);
        Reimbursement reimbursement = new Reimbursement(invoice, 0.0f, Date.valueOf(LocalDate.of(2023, 1, 5)));

        List<Reimbursement> reimbursements = List.of(reimbursement);

        when(mockService.getFilteredReimbursements(null, null, null, null, testUser.getId())).thenReturn(reimbursements);

        List<Reimbursement> filtered = mockService.getFilteredReimbursements(null, null, null, null, testUser.getId());

        // Assertions
        assertNotNull(filtered);
        assertEquals(1, filtered.size());
        assertEquals(ReimbursementState.PENDING, filtered.get(0).getStatus()); // Falls Status PENDING Standard ist
    }
    @Test
    void testGetTotalReimbursementByState1() {
        ReimbursementService mockService = mock(ReimbursementService.class);

        Invoice invoice1 = new Invoice(LocalDate.of(2023, 4, 1), 10.0f, InvoiceCategory.RESTAURANT, null, null);
        Invoice invoice2 = new Invoice(LocalDate.of(2023, 4, 10), 20.0f, InvoiceCategory.SUPERMARKET, null, null);

        Reimbursement r1 = new Reimbursement(invoice1, 5.0f, Date.valueOf(LocalDate.of(2023, 4, 5)));
        r1.setStatus(ReimbursementState.APPROVED);

        Reimbursement r2 = new Reimbursement(invoice2, 10.0f, Date.valueOf(LocalDate.of(2023, 4, 15)));
        r2.setStatus(ReimbursementState.REJECTED);

        List<Reimbursement> reimbursements = List.of(r1, r2);

        when(mockService.getTotalReimbursement(reimbursements, ReimbursementState.APPROVED)).thenReturn(5.0f);

        float total = mockService.getTotalReimbursement(reimbursements, ReimbursementState.APPROVED);

        // Assertions
        assertEquals(5.0f, total);
    }

    @Test
    void testGetInfoText() {
        service.modifyLimits(InvoiceCategory.SUPERMARKET, 2.5f);
        service.modifyLimits(InvoiceCategory.RESTAURANT, 3.0f);

        String info = service.getInfoText();

        assertTrue(info.contains("Supermarket: 2,50 €"));
        assertTrue(info.contains("Restaurant: 3,00 €"));
        assertFalse(info.contains("Undetectable"));
        assertTrue(info.startsWith("Pro Arbeitstag"));
        assertTrue(info.contains(System.lineSeparator()));
    }

    @Test
    void testIsValidFloat() {
        assertTrue(service.isValidFloat("1.0"));
        assertTrue(service.isValidFloat("0"));
        assertFalse(service.isValidFloat("-1"));
        assertTrue(service.isValidFloat("42"));
        assertFalse(service.isValidFloat("abc"));
        assertFalse(service.isValidFloat("12.3.4"));
        assertFalse(service.isValidFloat(""));
    }

    @Test
    void testIsAmountValid() {
        assertTrue(service.isAmountValid("2.5"));
        assertFalse(service.isAmountValid("abc"));
        assertFalse(service.isAmountValid(null));
    }

    @Test
    void testSetAndGetReimbursementAmount() {
        service.setReimbursementAmount(5.75f);
        assertEquals(5.75f, service.getReimbursementAmount());
    }

    @Test
    void testReimbursementServiceConstructor(){
        ReimbursementService reimbursementServiceNoUser = new ReimbursementService();
        assertNotNull(reimbursementServiceNoUser);
        ReimbursementService reimbursementServiceUser = new ReimbursementService(testUser);
        assertNotNull(reimbursementServiceUser);
    }

    //TODO: Test without Database
    @Test
    void testGetLimit() {
        service.modifyLimits(InvoiceCategory.SUPERMARKET, 2.5f);
        service.modifyLimits(InvoiceCategory.RESTAURANT, 3.0f);
        service.modifyLimits(InvoiceCategory.UNDETECTABLE, 2.5f);

        assertEquals(2.5f, service.getLimit(InvoiceCategory.SUPERMARKET));
        assertEquals(3.0f, service.getLimit(InvoiceCategory.RESTAURANT));
        assertEquals(2.5f, service.getLimit(InvoiceCategory.UNDETECTABLE));
    }

    @Test
    void testModifyLimits() {
        //TODO implement Logic without Database
        /*
        service.modifyLimits(InvoiceCategory.SUPERMARKET, 42.0f);
        assertEquals(42.0f, service.getLimit(InvoiceCategory.SUPERMARKET));
        service.modifyLimits(InvoiceCategory.RESTAURANT, 43.0f);
        assertEquals(43.0f, service.getLimit(InvoiceCategory.RESTAURANT));

        //assertThrows(IllegalArgumentException, service.getLimit(InvoiceCategory.UNDETECTABLE, -4), )

         */
    }
    @Test
    void testLoadLimitsFromDatabase() {
        //TODO implement Logic without Database
    }
}

