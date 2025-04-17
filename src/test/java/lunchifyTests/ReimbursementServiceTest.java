package lunchifyTests;

import backend.logic.*;
import backend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//Created by AI
public class ReimbursementServiceTest {
    private ReimbursementService service;
    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(42);
        service = new ReimbursementService(mockUser);
    }

    @Test
    void testGetLimitForEachCategory() {
        assertEquals(3.0f, service.getLimit(InvoiceCategory.RESTAURANT));
        assertEquals(2.5f, service.getLimit(InvoiceCategory.SUPERMARKET));
        assertEquals(2.5f, service.getLimit(InvoiceCategory.UNDETECTABLE));
    }

    @Test
    void testSetAndGetReimbursementAmount() {
        service.setReimbursementAmount(5.5f);
        assertEquals(5.5f, service.getReimbursementAmount());
    }

    @Test
    void testIsValidFloat() {
        assertTrue(service.isValidFloat("10.5"));
        assertTrue(service.isValidFloat("7"));
        assertFalse(service.isValidFloat("abc"));
        assertFalse(service.isValidFloat("7.a"));
    }

    @Test
    void testIsAmountValid() {
        assertTrue(service.isAmountValid("10.2"));
        assertFalse(service.isAmountValid(null));
        assertFalse(service.isAmountValid(""));
        assertFalse(service.isAmountValid("xyz"));
    }

    @Test
    void testConvertMonthToNumber() {
        assertEquals("1", service.convertMonthToNumber("Jänner"));
        assertEquals("12", service.convertMonthToNumber("Dezember"));
        assertNull(service.convertMonthToNumber("alle"));
        assertThrows(IllegalArgumentException.class, () -> service.convertMonthToNumber("notAMonth"));
    }

    /*
    @Test
    void testGetTotalReimbursementAllStates() {
        List<Reimbursement> list = new ArrayList<>();

        Invoice invoice1 = new Invoice(LocalDate.now(), 10f, InvoiceCategory.RESTAURANT, ReimbursementState.APPROVED, null, mockUser);
        Invoice invoice2 = new Invoice(LocalDate.now(), 10f, InvoiceCategory.RESTAURANT, ReimbursementState.REJECTED, null, mockUser);
        Invoice invoice3 = new Invoice(LocalDate.now(), 10f, InvoiceCategory.RESTAURANT, ReimbursementState.PENDING, null, mockUser);

        list.add(new Reimbursement(invoice1, 2.5f, null));
        list.add(new Reimbursement(invoice2, 2.5f, null));
        list.add(new Reimbursement(invoice3, 2.0f, null));

        float total = service.getTotalReimbursement(list);
        assertEquals(4.5f, total);
    }

     */

    /*
    @Test
    void testGetTotalReimbursementByState() {
        List<Reimbursement> list = new ArrayList<>();

        Invoice invoice1 = new Invoice(LocalDate.now(), 10f, InvoiceCategory.RESTAURANT, ReimbursementState.APPROVED, null, mockUser);
        Invoice invoice2 = new Invoice(LocalDate.now(), 10f, InvoiceCategory.RESTAURANT, ReimbursementState.APPROVED, null, mockUser);
        Invoice invoice3 = new Invoice(LocalDate.now(), 10f, InvoiceCategory.RESTAURANT, ReimbursementState.PENDING, null, mockUser);

        list.add(new Reimbursement(invoice1, 2.5f, null));
        list.add(new Reimbursement(invoice2, 2.5f, null));
        list.add(new Reimbursement(invoice3, 2.0f, null));

        float approvedTotal = service.getTotalReimbursement(list, ReimbursementState.APPROVED);
        assertEquals(5.0f, approvedTotal);

        float pendingTotal = service.getTotalReimbursement(list, ReimbursementState.PENDING);
        assertEquals(2.0f, pendingTotal);
    }

     */

    @Test
    void testGetUserId() {
        assertEquals(42, service.getUserId());
    }
}
