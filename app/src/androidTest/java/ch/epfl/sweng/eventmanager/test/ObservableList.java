package ch.epfl.sweng.eventmanager.test;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Louis Vialar
 */
public class ObservableList<T> extends MutableLiveData<List<T>> {
    private List<T> underlyingList = new ArrayList<>();

    public void add(T elem) {
        this.underlyingList.add(elem);
        this.notifyChanged();
    }

    public void notifyChanged() {
        this.postValue(underlyingList);
    }

    public List<T> getUnderlyingList() {
        return underlyingList;
    }

    public void clear() {
        this.underlyingList.clear();
        this.notifyChanged();
    }
}
