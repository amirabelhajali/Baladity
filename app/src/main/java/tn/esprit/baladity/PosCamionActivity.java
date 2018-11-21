package tn.esprit.baladity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PosCamionActivity  extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pos_camion);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        LatLng sydney = new LatLng(-34, 151);
        MarkerOptions markerOptions=new MarkerOptions().position(sydney).title("Marker").snippet("");
//        mMap.addMarker(markerOptions);
        CircleOptions circleOptions=new CircleOptions();
        circleOptions.center(new LatLng(-34, 151));
        circleOptions.radius(2000);
        circleOptions.strokeColor(ContextCompat.getColor(this,android.R.color.holo_orange_dark));
        circleOptions.fillColor(ContextCompat.getColor(this,android.R.color.holo_orange_light));

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

// Add a marker in Sydney and move the camera
        LatLng position = new LatLng(-34, 151);

        mMap.addMarker(new MarkerOptions().position(position).title("Marker in Tunisia"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

    }
}
