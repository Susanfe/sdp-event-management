package ch.epfl.sweng.eventmanager.notifications;

/**
 * Represent a pending notification from the event administrator. For now, this is only used when a sending messages to
 * users having joined a specific event.
 */
public final class NotificationRequest {
    /**
     * Title of the notification
     */
    private String title;
    /**
     * Body/Description of the notification
     */
    private String body;
    /**
     * Unique identifier of the event
     */
    private int eventId;

    /**
     * Empty constructor required by Firebase
     */
    public NotificationRequest() {
    }

    public NotificationRequest(String title, String body, int eventId) {
        this.title = title;
        this.body = body;
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String name) {
        this.body = name;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
