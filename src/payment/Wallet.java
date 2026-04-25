package payment;

import money.Money;

public class Wallet {
    private Money balance;

    public Wallet(Money balance) {
        this.balance = balance;
    }

    public void deposit(Money amount) {
        balance = balance.add(amount);
    }

    public void withdraw(Money amount) {
        if (balance.amount().compareTo(amount.amount()) < 0) {
            throw new IllegalStateException("Not enough funds");
        }
        balance = balance.subtract(amount);
    }

    public Money getBalance() {
        return balance;
    }
}
