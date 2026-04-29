package domain.resource;

import money.Money;

public class Device extends Resource {
    private static final Money BASE_RATE = Money.of("5.00");

    private final int quantity;

    public Device(String name, int quantity) {
        super(name);
        this.quantity = quantity;
    }

    public Device(String name, Money customHourlyRate, int quantity) {
        super(name, customHourlyRate);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    protected Money baseRatePerHour() {
        return BASE_RATE;
    }

    @Override
    public String describe() {
        return "Device: " + getName() + ", quantity=" + quantity;
    }
}
