package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.NUMAD26Sp_FirstAidEmergency.databinding.FragmentHospitalsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HospitalsFragment extends Fragment implements OnMapReadyCallback {

    // Replace with your actual Places API key (same key with Places API enabled)
    private static final String PLACES_API_KEY = BuildConfig.MAPS_API_KEY;
    private static final int SEARCH_RADIUS_METERS = 5000;

    private FragmentHospitalsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private HospitalAdapter hospitalAdapter;
    private CancellationTokenSource cancellationTokenSource;
    private ActivityResultLauncher<String[]> locationPermLauncher;

    public HospitalsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationPermLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean granted =
                            Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION))
                            || Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));
                    if (granted) {
                        initMap();
                    } else {
                        showPermissionDenied();
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHospitalsBinding.inflate(inflater, container, false);

        hospitalAdapter = new HospitalAdapter(this::openNavigation);
        binding.hospitalsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.hospitalsRecycler.setAdapter(hospitalAdapter);

        if (hasLocationPermission()) {
            initMap();
        } else {
            locationPermLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

        return binding.getRoot();
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void initMap() {
        SupportMapFragment mapFrag = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_container);
        if (mapFrag == null) {
            mapFrag = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map_container, mapFrag)
                    .commitNow();
        }
        mapFrag.getMapAsync(this);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        loadNearbyHospitals();
    }

    @SuppressLint("MissingPermission")
    private void loadNearbyHospitals() {
        showLoading(true);
        cancellationTokenSource = new CancellationTokenSource();

        fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken()
        ).addOnSuccessListener(location -> {
            if (!isAdded() || binding == null) return;
            if (location != null) {
                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 13f));
                fetchNearbyHospitals(location.getLatitude(), location.getLongitude());
            } else {
                showLoading(false);
                showError("Unable to get your location.\nPlease ensure GPS is enabled and try again.");
            }
        }).addOnFailureListener(e -> {
            if (!isAdded() || binding == null) return;
            showLoading(false);
            showError("Location error: " + e.getMessage());
        });
    }

    private void fetchNearbyHospitals(double lat, double lng) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlacesApiService service = retrofit.create(PlacesApiService.class);
        service.getNearbyHospitals(lat + "," + lng, SEARCH_RADIUS_METERS, "hospital", PLACES_API_KEY)
                .enqueue(new Callback<PlacesModels.NearbySearchResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PlacesModels.NearbySearchResponse> call,
                                           @NonNull Response<PlacesModels.NearbySearchResponse> response) {
                        if (!isAdded() || binding == null) return;
                        showLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            String status = response.body().status;
                            if ("OK".equals(status)) {
                                displayResults(response.body().results, lat, lng);
                            } else if ("ZERO_RESULTS".equals(status)) {
                                showStatus("No hospitals found within 5 km.");
                            } else {
                                showError("Places API error: " + status);
                            }
                        } else {
                            showError("Failed to fetch hospital data.");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PlacesModels.NearbySearchResponse> call,
                                          @NonNull Throwable t) {
                        if (!isAdded() || binding == null) return;
                        showLoading(false);
                        showError("Network error. Check your connection.");
                    }
                });
    }

    private void displayResults(List<PlacesModels.PlaceResult> results, double userLat, double userLng) {
        if (results == null || results.isEmpty()) {
            showStatus("No hospitals found within 5 km.");
            return;
        }

        googleMap.clear();
        List<Hospital> hospitals = new ArrayList<>();

        for (PlacesModels.PlaceResult r : results) {
            if (r.geometry == null || r.geometry.location == null) continue;

            double pLat = r.geometry.location.lat;
            double pLng = r.geometry.location.lng;

            float[] distResult = new float[1];
            Location.distanceBetween(userLat, userLng, pLat, pLng, distResult);
            float distKm = distResult[0] / 1000f;

            hospitals.add(new Hospital(
                    r.name,
                    r.vicinity,
                    pLat, pLng,
                    r.rating,
                    r.placeId,
                    r.openingHours != null && r.openingHours.openNow,
                    distKm
            ));

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pLat, pLng))
                    .title(r.name)
                    .snippet(String.format("%.1f km away", distKm)));
        }

        hospitalAdapter.setHospitals(hospitals);
    }

    private void openNavigation(Hospital hospital) {
        // Use place_id for exact destination matching — raw lat/lng can snap to nearby buildings
        Uri navUri = Uri.parse(
                "https://www.google.com/maps/dir/?api=1"
                + "&destination=" + Uri.encode(hospital.getName())
                + "&destination_place_id=" + hospital.getPlaceId()
                + "&travelmode=driving");
        startActivity(new Intent(Intent.ACTION_VIEW, navUri));
    }

    private void showLoading(boolean loading) {
        if (binding == null) return;
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (loading) binding.statusText.setVisibility(View.GONE);
    }

    private void showError(String msg) {
        if (binding == null) return;
        binding.statusText.setText(msg);
        binding.statusText.setTextColor(Color.parseColor("#B71C1C"));
        binding.statusText.setVisibility(View.VISIBLE);
    }

    private void showStatus(String msg) {
        if (binding == null) return;
        binding.statusText.setText(msg);
        binding.statusText.setTextColor(Color.GRAY);
        binding.statusText.setVisibility(View.VISIBLE);
    }

    private void showPermissionDenied() {
        if (binding == null) return;
        binding.progressBar.setVisibility(View.GONE);
        binding.statusText.setText(
                "Location permission is required to find nearby hospitals.\n\n"
                + "Please go to Settings \u2192 App Permissions \u2192 Location.");
        binding.statusText.setTextColor(Color.GRAY);
        binding.statusText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cancellationTokenSource != null) {
            cancellationTokenSource.cancel();
        }
        binding = null;
    }
}
