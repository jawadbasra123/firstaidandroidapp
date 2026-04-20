package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("maps/api/place/nearbysearch/json")
    Call<PlacesModels.NearbySearchResponse> getNearbyHospitals(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String apiKey
    );
}
