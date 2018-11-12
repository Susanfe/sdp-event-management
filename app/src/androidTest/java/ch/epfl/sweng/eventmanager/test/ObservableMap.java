package ch.epfl.sweng.eventmanager.test;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Louis Vialar
 */
public class ObservableMap<K, V> {
    private Map<K, V> map = new HashMap<>();
    private Map<K, MutableLiveData<V>> observers = new HashMap<>();
    private MutableLiveData<Collection<V>> valuesObserver = new MutableLiveData<>();

    public LiveData<V> get(K key) {
        return getObserverFor(key);
    }

    public V put(K key, V value) {
        V val = map.put(key, value);

        notifyChanged(key);

        return val;
    }

    public V remove(K key) {
        V val = map.remove(key);

        notifyChanged(key);

        return val;
    }

    public void clear() {
        map.clear();
        notifyChanged(null);
    }

    public Map<K, V> getMap() {
        return Collections.unmodifiableMap(map);
    }

    public LiveData<Collection<V>> values() {
        return valuesObserver;
    }

    private MutableLiveData<V> getObserverFor(K key) {
        if (!observers.containsKey(key)) {
            observers.put(key, new MutableLiveData<>());
            observers.get(key).postValue(map.get(key));
        }

        return observers.get(key);
    }

    public void notifyChanged(K key) {
        valuesObserver.postValue(map.values());

        if (key != null) {
            getObserverFor(key).postValue(map.get(key));
        }
    }


}
