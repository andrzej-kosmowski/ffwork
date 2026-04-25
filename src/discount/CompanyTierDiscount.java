package discount;

import domain.booking.Booking;
import money.Money;

import java.math.BigDecimal;

public class CompanyTierDiscount implements Discount {
    private final int tier;

    public CompanyTierDiscount(int tier) {
        this.tier = tier;
    }

    @Override
    public Money applyDiscount(Money basePrice, Booking booking) {
        BigDecimal multiplier = switch (tier) {
            case 1 -> BigDecimal.valueOf(0.95);
            case 2 -> BigDecimal.valueOf(0.90);
            case 3 -> BigDecimal.valueOf(0.85);
            default -> BigDecimal.ONE;
        };

        return basePrice.multiply(multiplier);
    }
}
