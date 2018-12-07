package ch.epfl.sweng.eventmanager.repository.data.MapEditionData;

public enum MarkerType {
    SPOT, OVERLAY_EDGE, TO_REMOVE;

    int id = 0;

    public MarkerType setId(int newId) {
        id = newId;
        return this;
    }

    public int getId() {
        return id;
    }
}
