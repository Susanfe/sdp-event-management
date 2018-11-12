package ch.epfl.sweng.eventmanager.repository.data;

public interface User {
    /**
     * @return a string representation of the user's common name
     */
    public String getDisplayName();

    /**
     * @return a string representation of the user's email
     */
    public String getEmail();

    /**
     * @return a string representation of the user's unique ID
     */
    public String getUid();

    public boolean hasPermission(Permission permission);

    public static enum Permission {
        PUBLISH_NEWS,
        UPDATE_EVENT
    }
}