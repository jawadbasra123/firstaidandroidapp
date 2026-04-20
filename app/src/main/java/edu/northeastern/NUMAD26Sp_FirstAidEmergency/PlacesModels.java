package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlacesModels {

    public static class NearbySearchResponse {
        @SerializedName("results")
        public List<PlaceResult> results;
        @SerializedName("status")
        public String status;
    }

    public static class PlaceResult {
        @SerializedName("name")
        public String name;
        @SerializedName("vicinity")
        public String vicinity;
        @SerializedName("geometry")
        public Geometry geometry;
        @SerializedName("rating")
        public double rating;
        @SerializedName("place_id")
        public String placeId;
        @SerializedName("opening_hours")
        public OpeningHours openingHours;
    }

    public static class Geometry {
        @SerializedName("location")
        public LatLngResult location;
    }

    public static class LatLngResult {
        @SerializedName("lat")
        public double lat;
        @SerializedName("lng")
        public double lng;
    }

    public static class OpeningHours {
        @SerializedName("open_now")
        public boolean openNow;
    }
}
