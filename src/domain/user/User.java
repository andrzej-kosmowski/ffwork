package domain.user;

import discount.Discount;
import discount.Discountable;
import discount.NoDiscount;
import money.Money;
import payment.Wallet;

public abstract class User implements Discountable {
    private final String email;
    private final String displayName;
    private final Wallet wallet;

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
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
