package domain.resource;

import money.Money;

import java.util.Objects;

public class Desk extends Resource {
    private static final Money FIXED_BASE_RATE = Money.of("25.00");
    private static final Money HOT_BASE_RATE = Money.of("25.00");

    private final DeskType type;

    public Desk(String name, DeskType type) {
        super(name);
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }

    public Desk(String name, Money customHourlyRate, DeskType type) {
        super(name, customHourlyRate);
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }

    @Override
    protected Money baseRatePerHour() {
        return type == DeskType.FIXED ? FIXED_BASE_RATE : HOT_BASE_RATE;
    }

    @Override
    public String describe() {
        return "Desk: " + getName() + ", type=" + type;
    }
}
