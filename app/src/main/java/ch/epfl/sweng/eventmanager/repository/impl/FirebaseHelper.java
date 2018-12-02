package ch.epfl.sweng.eventmanager.repository.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Louis Vialar
 */
public class FirebaseHelper {
    public static <T> LiveData<List<T>> getList(DatabaseReference dbRef, Class<T> classOfT) {
        return getList(dbRef, classOfT, Mapper.unit());
    }

    public static <T> LiveData<List<T>> getList(DatabaseReference dbRef, Class<T> classOfT, Mapper<T> mapper) {
        final MutableLiveData<List<T>> data = new MutableLiveData<>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> events = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren())
                    events.add(mapper.map(child.getValue(classOfT), child));

                data.postValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseHelper", "Error when getting data list.", databaseError.toException());
            }
        });

        return data;
    }

    public static LiveData<Bitmap> getImage(StorageReference ref) {
        final MutableLiveData<Bitmap> img = new MutableLiveData<>();

        final long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            img.setValue(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options));
        }).addOnFailureListener(exception -> {
            Log.w("FirebaseHelper", "Could not load image " + img.toString());
            img.setValue(null);
        });
        return img;
    }

    public static LiveData<String> getImageURL(StorageReference ref) {
        final MutableLiveData<String> imgURL = new MutableLiveData<>();

        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            imgURL.postValue(uri.toString());
        }).addOnFailureListener(exception -> {
            Log.w("FirebaseHelper", "Could not load image URL " + imgURL.toString());
            imgURL.setValue(null);
        });
        return imgURL;
    }

    public interface Mapper<T> {
        static <T> Mapper<T> unit() {
            return (in, snapshot) -> in;
        }

        T map(T value, DataSnapshot snapshot);

        default Mapper<T> andThen(Mapper<T> next) {
            Mapper<T> self = this;
            return (in, snap) -> next.map(self.map(in, snap), snap);
        }
    }

    public static <T> Task<Void> publishElement(int eventId, String ref, T elem){
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference(ref)
                .child("event_" + eventId)
                .push(); // Create a new key in the list

        return dbRef.setValue(elem);
    }
}
