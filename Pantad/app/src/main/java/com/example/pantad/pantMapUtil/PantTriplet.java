package com.example.pantad.pantMapUtil;

public class PantTriplet<T, U, V> {

    private final T lat;
    private final U lng;
    private final V address;

    public PantTriplet(T lat, U lng, V address) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public T getLat() { return lat; }
    public U getLng() { return lng; }
    public V getAddress() { return address; }

    @Override
    public String toString() {
        return "{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", adress=" + address +
                '}' + "\n";
    }
}