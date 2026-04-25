package test;

import domain.user.IndividualUser;
import domain.user.User;
import money.Money;
import org.junit.jupiter.api.Test;
import payment.CardPayment;
import payment.Payment;
import payment.PaymentStatus;
import payment.WalletPayment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {
    private User userWithWallet(String initial) {
        User user = new IndividualUser("test@mail.com", "Test", null);
        user.getWallet().deposit(Money.of(initial));
        return user;
    }
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

    @Test
    void shouldWithdrawMoneyFromWallet() {
        User user = userWithWallet("100.00");
        Payment payment = new WalletPayment(
                Money.of("40.00"),
                "2",
                user
        );
        payment.capture();

        assertEquals(Money.of("60.00"), user.getWallet().getBalance());
        assertEquals(PaymentStatus.CAPTURED, payment.getStatus());
    }

    @Test
    void shouldThrowWhenAlreadyCaptured() {
        User user = userWithWallet("100.00");
        WalletPayment payment = new WalletPayment(
                Money.of("40.00"),
                "2",
                user
        );
        payment.capture();

        assertThrows(IllegalStateException.class, payment::capture);
    }

    @Test
    void shouldThrowWhenNotEnoughFunds() {
        User user = userWithWallet("20.00");
        WalletPayment payment = new WalletPayment(
                Money.of("40.00"),
                "2",
                user
        );

        assertThrows(IllegalStateException.class, payment::capture);
    }

    @Test
    void shouldReturnMoneyToWallet() {
        User user = userWithWallet("100.00");
        WalletPayment payment = new WalletPayment(
                Money.of("40.00"),
                "2",
                user
        );

        payment.capture();
        payment.refund();

        assertEquals(Money.of("100.00"), user.getWallet().getBalance());
        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
    }

    @Test
    void shouldThrowWhenNotCaptured() {
        User user = userWithWallet("100.00");
        WalletPayment payment = new WalletPayment(
                Money.of("40.00"),
                "2",
                user
        );

        assertThrows(IllegalStateException.class, payment::refund);
    }

    @Test
    void shouldThrowWhenAlreadyRefunded() {
        User user = userWithWallet("100.00");
        WalletPayment payment = new WalletPayment(
                Money.of("40.00"),
                "2",
                user
        );

        payment.capture();
        payment.refund();

        assertThrows(IllegalStateException.class, payment::refund);
    }
}
