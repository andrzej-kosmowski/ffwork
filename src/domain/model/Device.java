package domain.model;

import domain.value.Money;

public class Device extends Resource {
    private final int quantity;

    public Device(String name, int quantity) {
        super(name);
        this.quantity = quantity;
    }

    public Device(String name, Money customHourlyRate, int quantity) {
        super(name, customHourlyRate);
        this.quantity = quantity;
    }

    @Override
    protected Money baseRatePerHour() {
        return Money.of(5);
    }

    @Override
    public String describe() {
        return "Device: " + getName() + ", quantity=" + quantity;
    }
}
