package test;

import money.Money;
import org.junit.jupiter.api.Test;
import payment.CardPayment;
import payment.PaymentStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PaymentTest {
    @Test
    void cardPaymentShouldCaptureSuccessfully() {
        CardPayment payment = new CardPayment(Money.of("100.00"), "PAY-1", "1234");
        payment.capture();
        assertEquals(PaymentStatus.CAPTURED, payment.getStatus());
    }

    @Test
    void shouldThrowWhenCaptureTwice() {
        CardPayment payment = new CardPayment(Money.of("100.00"), "PAY-2", "5678");
        payment.capture();
        assertThrows(IllegalStateException.class, payment::capture);
    }
}
