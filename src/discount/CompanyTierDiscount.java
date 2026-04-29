package discount;

import domain.booking.Booking;
import money.Money;

import java.math.BigDecimal;
import java.util.Map;

public class CompanyTierDiscount implements Discount {
    private static final Map<Integer, BigDecimal> MULTIPLIERS = Map.of(
            1, BigDecimal.valueOf(0.95),
            2, BigDecimal.valueOf(0.90),
            3, BigDecimal.valueOf(0.85)
    );

    private final int tier;

    public CompanyTierDiscount(int tier) {
        if (tier < 1 || tier > 3) {
            throw new IllegalArgumentException("Invalid company tier: " + tier);
        }
        this.tier = tier;
    }

    @Override
    public Money applyDiscount(Money basePrice, Booking booking) {
        BigDecimal multiplier = MULTIPLIERS.get(tier);
        return basePrice.multiply(multiplier);
    }

}
