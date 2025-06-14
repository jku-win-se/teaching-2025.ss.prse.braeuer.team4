package uiTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import backend.logic.SessionManager;
import backend.model.User;
import backend.model.UserRole;
import backend.model.UserState;
import frontend.Main;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Tag("ui")
public class InvoiceUploadControllerTest extends ApplicationTest {
	
	@Override
	public void start(Stage stage) throws Exception {
		Main mainApp = new Main();
		mainApp.setTestMode(true);
		mainApp.start(stage);
	}

	@BeforeEach
	void initialize() {
		clickOn("#openInvoiceUploadButton");
	}

	@Test
	void testInitialUIElementsAreVisible() {
		verifyThat("#datePicker", isVisible());
		verifyThat("#amountField", isVisible());
		verifyThat("#reimbursementAmountField", isVisible());
		verifyThat("#categoryBox", isVisible());
		verifyThat("#uploadPane", isVisible());
		verifyThat("#submitButton", isVisible());
	}

	@Test
	void testBackNavigation() {
		SessionManager.setCurrentUser(new User (0,"dummy", "dummy@lunch.at", UserRole.ADMIN, UserState.ACTIVE));
		clickOn("#backButton");
	}

	@Test
	void testReimbursementAmountRestaurantMoreThanLimit() {
		clickOn("#amountField");
		write("4");
		clickOn("#categoryBox");
		clickOn("RESTAURANT");
		TextField field = lookup("#reimbursementAmountField").query();
		assertEquals("3.0", field.getText());
	}

	@Test
	void testReimbursementAmountRestaurantLessThanLimit() {
		clickOn("#amountField");
		write("2.50");
		clickOn("#categoryBox");
		clickOn("RESTAURANT");
		TextField field = lookup("#reimbursementAmountField").query();
		assertEquals("2.5", field.getText());
	}

	@Test
	void testReimbursementAmountSupermarketMoreThanLimit() {
		clickOn("#amountField");
		write("3");
		clickOn("#categoryBox");
		clickOn("SUPERMARKET");
		TextField field = lookup("#reimbursementAmountField").query();
		assertEquals("2.5", field.getText());
	}

	@Test
	void testReimbursementAmountSupermarketLessThanLimit() {
		clickOn("#amountField");
		write("1.50");
		clickOn("#categoryBox");
		clickOn("SUPERMARKET");
		TextField field = lookup("#reimbursementAmountField").query();
		assertEquals("1.5", field.getText());
	}

	@Test
	void testReimbursementChangeCategory() {
		clickOn("#amountField");
		write("2.75");
		clickOn("#categoryBox");
		clickOn("SUPERMARKET");
		TextField field = lookup("#reimbursementAmountField").query();
		assertEquals("2.5", field.getText());
		clickOn("#categoryBox");
		clickOn("RESTAURANT");
		field = lookup("#reimbursementAmountField").query();
		assertEquals("2.75", field.getText());
	}
	
	@Test
    void testInvalidAmountZero() {
        clickOn("#amountField");
        write("0");
        clickOn("#categoryBox");
        clickOn("RESTAURANT");

        TextField field = lookup("#reimbursementAmountField").query();
        assertEquals("0.0", field.getText());  // Beispielannahme: ungültige Beträge setzen Erstattung auf 0
    }
	
	
	@Test
	void testSelectFutureDate() {
	    DatePicker datePicker = lookup("#datePicker").query();
	    interact(() -> datePicker.setValue(LocalDate.of(2025, 12, 31)));

	    Label datePickerLabel = lookup("#datePickerLabel").query();
	    Assertions.assertThat(datePickerLabel.getText()).contains("Datum darf nicht in der Zukunft liegen");

	    verifyThat("#submitButton", (Button button) -> button.isDisabled());
	}
}
