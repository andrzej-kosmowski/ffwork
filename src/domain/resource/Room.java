package domain.resource;

import money.Money;

import java.util.Objects;
import java.util.Set;

public class Room extends Resource {
    private static final Money BASE_RATE = Money.of("15.00");

    private final int seats;
    private final Set<String> equipment;

    public Room(String name, int seats, Set<String> equipment) {
        super(name);
        if (seats <= 0) {
            throw new IllegalArgumentException("seats must be > 0");
        }
        this.seats = seats;
        this.equipment = Objects.requireNonNull(equipment, "equipment cannot be null");
    }

    public Room(String name, Money customHourlyRate, int seats, Set<String> equipment) {
        super(name, customHourlyRate);
        if (seats <= 0) {
            throw new IllegalArgumentException("seats must be > 0");
        }
        this.seats = seats;
        this.equipment = Objects.requireNonNull(equipment, "equipment cannot be null");;
    }

    @Override
    protected Money baseRatePerHour() {
        return BASE_RATE;
    }

    @Override
    public String describe() {
        return "Room: " + getName() + ", seats=" + seats + ", equipment=" + equipment;
    }
}
