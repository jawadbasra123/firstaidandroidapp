package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

public class Hospital {
    private final String name;
    private final String address;
    private final double lat;
    private final double lng;
    private final double rating;
    private final String placeId;
    private final boolean openNow;
    private final float distanceKm;

    public Hospital(String name, String address, double lat, double lng,
                    double rating, String placeId, boolean openNow, float distanceKm) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.placeId = placeId;
        this.openNow = openNow;
        this.distanceKm = distanceKm;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public double getRating() { return rating; }
    public String getPlaceId() { return placeId; }
    public boolean isOpenNow() { return openNow; }
    public float getDistanceKm() { return distanceKm; }
}
