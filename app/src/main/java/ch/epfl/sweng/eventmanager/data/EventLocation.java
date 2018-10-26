package ch.epfl.sweng.eventmanager.data;

/**
 * Represents the place where an event takes place
 *
 * @author Louis Vialar
 */
public class EventLocation {
    /**
     * The public name for the location (e.g. EPFL)
     */
    private String name;

    /**
     * The actual coordinates of the location
     */
    private Position position;

    public EventLocation(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public EventLocation() {
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }
}
