package ch.epfl.sweng.eventmanager.repository.data;

/**
 * @author Louis Vialar
 */
public class EventTicketingConfiguration {
    /**
     * The URL of the endpoint used to login.<br>
     * If null, the API will be used without authentication.
     */
    private String loginUrl;

    /**
     * The URL of the endpoint listing the configurations.<br>
     * If null, the API will send all scans to the same endpoint
     */
    private String configurationsUrl;

    /**
     * The URL of the endpoint used to scan a ticket.<br>
     * If might contain `:config` in the places where the configuration ID should be
     * appended, if a configurationsUrl was provided.
     */
    private String scanUrl;

    public EventTicketingConfiguration() {
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getConfigurationsUrl() {
        return configurationsUrl;
    }

    public String getScanUrl() {
        return scanUrl;
    }
}
