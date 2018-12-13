package ch.epfl.sweng.eventmanager.notifications;

/**
 * Represent a response from a pending notification of the event administrator.
 */
public final class NotificationRequestResponse {
    /**
     * Title of the notification
     */
    private final String title;
    /**
     * Body of the notification
     */
    private final String body;
    /**
     * Sender's information
     */
    private final String from;

    public NotificationRequestResponse(String title, String body, String from) {
        this.title = title;
        this.body = body;
        this.from = from;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getFrom() {
        return from;
    }
}
