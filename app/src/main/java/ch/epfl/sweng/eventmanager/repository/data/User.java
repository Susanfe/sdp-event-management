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
}