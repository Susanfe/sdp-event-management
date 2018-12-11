package ch.epfl.sweng.eventmanager.repository.data;

/**
 * Represents all type of spot
 *
 * @author Robin Zbinden (274236)
 * @author Stanislas Jouven (260580)
 */
public enum SpotType {
    STAND("stand"), BAR("bar"), SCENE("scene"), ROOM("room"), WC("toilet"), NURSERY("nursery"), ATM("atm"), INFORMATION("information");

    String name;

    SpotType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
