package domain.resource;

import money.Money;

public abstract class Resource {
    private final String name;
    private final Money customHourlyRate;

    public Resource(String name) {
        this.name = name;
        this.customHourlyRate = null;
    }

    public Resource(String name, Money customHourlyRate) {
        this.name = name;
        this.customHourlyRate = customHourlyRate;
    }

    public String getName() {
        return name;
    }

    public Money getCustomHourlyRate() {
        return customHourlyRate;
    }

    protected abstract Money baseRatePerHour();

    public abstract String describe();

    public Money hourlyRate() {
        return customHourlyRate != null? customHourlyRate : baseRatePerHour();
    }
}
