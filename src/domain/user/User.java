package domain.user;

import discount.Discount;
import discount.Discountable;
import discount.NoDiscount;
import money.Money;
import payment.Wallet;

import java.util.Objects;

public abstract class User implements Discountable {
    private final String email;
    private final String displayName;
    private final Wallet wallet;

    public User(String email, String displayName) {
        this.email = Objects.requireNonNull(email, "email cannot be null");
        this.displayName = Objects.requireNonNull(displayName, "displayName cannot be null");
        this.wallet = new Wallet(Money.of("0.00"));
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public String toString() {
        return displayName + " (" + email + ")";
    }

    @Override
    public Discount getDiscount() {
        return new NoDiscount();
    }
}
