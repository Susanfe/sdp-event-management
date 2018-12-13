package ch.epfl.sweng.eventmanager.notifications;

public final class NotificationRequestResponse {
    private final String title;
    private final String body;
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
