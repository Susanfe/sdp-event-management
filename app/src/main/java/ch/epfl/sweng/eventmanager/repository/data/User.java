package ch.epfl.sweng.eventmanager.repository.data;

public interface User {

    /**
     * @return an unique ID representing the user
     */
    public String getUid();

    /**
     * @return a string representation of the user's common name
     */
    public String getDisplayName();

    /**
     * @return a string representation of the user's email
     */
    public String getEmail();
}