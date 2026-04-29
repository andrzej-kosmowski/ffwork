package domain.resource;

import money.Money;

import java.util.Objects;
import java.util.Optional;

public abstract class Resource {
    private final String name;
    private final Optional<Money> customHourlyRate;

    public Resource(String name) {
        this.name = name;
        this.customHourlyRate = Optional.empty();
    }

    public Resource(String name, Money customHourlyRate) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.customHourlyRate = Optional.ofNullable(customHourlyRate);
    }

    public String getName() {
        return name;
    }

    public Optional<Money> getCustomHourlyRate() {
        return customHourlyRate;
    }

    protected abstract Money baseRatePerHour();

    public abstract String describe();

    public Money hourlyRate() {
        return customHourlyRate
                .orElseGet(this::baseRatePerHour);
    }
}
