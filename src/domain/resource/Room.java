package domain.resource;

import money.Money;

import java.util.Set;

public class Room extends Resource {
    private final int seats;
    private final Set<String> equipment;

    public Room(String name, int seats, Set<String> equipment) {
        super(name);
        this.seats = seats;
        this.equipment = equipment;
    }

    public Room(String name, Money customHourlyRate, int seats, Set<String> equipment) {
        super(name, customHourlyRate);
        this.seats = seats;
        this.equipment = equipment;
    }

    @Override
    protected Money baseRatePerHour() {
        return Money.of(15);
    }

    @Override
    public String describe() {
        return "Room: " + getName() + ", seats=" + seats + ", equipment=" + equipment;
    }
}
