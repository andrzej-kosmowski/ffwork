package domain.model;

import domain.value.Money;

public class Desk extends Resource {
    public enum DeskType {
        HOT,
        FIXED
    }

    private final DeskType type;

    public Desk(String name, DeskType type) {
        super(name);
        this.type = type;
    }

    public Desk(String name, Money customHourlyRate, DeskType type) {
        super(name, customHourlyRate);
        this.type = type;
    }

    @Override
    protected Money baseRatePerHour() {
        return type == DeskType.FIXED ? Money.of(25) : Money.of(40);
    }

    @Override
    public String describe() {
        return "Desk: " + getName() + ", type=" + type;
    }
}
