package ch.epfl.sweng.eventmanager.data;

/**
 * A class representing only a few properties of an event, to avoid retrieving tons
 * of data just to display an event list
 * @author Louis Vialar
 */
public final class ReducedEvent {
    /**
     * An internal id, identifying this event uniquely. Might have to be completed by an UUID in the future.
     */
    private int id;
    /**
     * The name of the event
     */
    private String name;

    public ReducedEvent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ReducedEvent() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
