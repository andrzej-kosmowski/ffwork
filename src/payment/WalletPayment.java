package payment;

import domain.user.User;
import money.Money;

public class WalletPayment extends Payment {
    private final User user;

    public WalletPayment(Money amount, String paymentId, User user) {
        super(amount, paymentId);
        this.user = user;
    }

    @Override
    public void capture() {
        if (getStatus() != PaymentStatus.INITIATED) {
            throw new IllegalStateException("Invalid state");
        }

        user.getWallet().withdraw(getAmount());
        setStatus(PaymentStatus.CAPTURED);
    }

    public void refund() {
        if (getStatus() != PaymentStatus.CAPTURED) {
            throw new IllegalStateException("Cannot refund");
        }
        user.getWallet().deposit(getAmount());
        setStatus(PaymentStatus.REFUNDED);
    }
}
