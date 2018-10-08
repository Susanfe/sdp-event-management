package ch.epfl.sweng.eventmanager.ui.eventShowcase.scheduleUtils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TimeLineModel implements Parcelable {

    private String artist;
    private String date;
    private String genre;
    private String description;

    public TimeLineModel(String artist, Date date, String genre, String description) {
        this.artist = artist;
        this.date = date.toString();
        this.genre = genre;
        this.description = description;
    }

    String getArtist() {
        return artist;
    }

    public String getDate() {
        return date;
    }

    String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.artist);
        dest.writeString(date);
        dest.writeString(genre);
        dest.writeString(description);
    }

    private TimeLineModel(Parcel in) {
        this.artist = in.readString();
        this.date = in.readString();
        this.genre = in.readString();
        this.description = in.readString();
    }

    public static final Creator<TimeLineModel> CREATOR = new Creator<TimeLineModel>() {
        @Override
        public TimeLineModel createFromParcel(Parcel source) {
            return new TimeLineModel(source);
        }

        @Override
        public TimeLineModel[] newArray(int size) {
            return new TimeLineModel[size];
        }
    };
}