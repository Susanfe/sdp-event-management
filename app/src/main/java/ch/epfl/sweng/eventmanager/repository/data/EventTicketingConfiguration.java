package ch.epfl.sweng.eventmanager.repository.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Louis Vialar
 */
public class EventTicketingConfiguration implements Parcelable {
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

    public EventTicketingConfiguration(String loginUrl, String configurationsUrl, String scanUrl) {
        this.loginUrl = loginUrl;
        this.configurationsUrl = configurationsUrl;
        this.scanUrl = scanUrl;
    }

    protected EventTicketingConfiguration(Parcel in) {
        loginUrl = in.readString();
        configurationsUrl = in.readString();
        scanUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(loginUrl);
        dest.writeString(configurationsUrl);
        dest.writeString(scanUrl);
    }

    public static final Creator<EventTicketingConfiguration> CREATOR = new Creator<EventTicketingConfiguration>() {
        @Override
        public EventTicketingConfiguration createFromParcel(Parcel in) {
            return new EventTicketingConfiguration(in);
        }

        @Override
        public EventTicketingConfiguration[] newArray(int size) {
            return new EventTicketingConfiguration[size];
        }
    };

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getConfigurationsUrl() {
        return configurationsUrl;
    }

    public String getScanUrl() {
        return scanUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventTicketingConfiguration)) return false;

        EventTicketingConfiguration that = (EventTicketingConfiguration) o;

        if (loginUrl != null ? !loginUrl.equals(that.loginUrl) : that.loginUrl != null) return false;
        if (configurationsUrl != null ? !configurationsUrl.equals(that.configurationsUrl) : that.configurationsUrl != null)
            return false;
        return scanUrl != null ? scanUrl.equals(that.scanUrl) : that.scanUrl == null;
    }

    @Override
    public int hashCode() {
        int result = loginUrl != null ? loginUrl.hashCode() : 0;
        result = 31 * result + (configurationsUrl != null ? configurationsUrl.hashCode() : 0);
        result = 31 * result + (scanUrl != null ? scanUrl.hashCode() : 0);
        return result;
    }
}
