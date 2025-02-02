package vn.edu.stu.wine_gk;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class About_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView phoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupToolbar();
        addControls();
        addEvents();
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.about));
        }
    }
    private void addControls() {
        phoneTextView = findViewById(R.id.phone);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_wine_list) {
            startActivity(new Intent(this, WineList_Activity.class));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, About_Activity.class));
        } else if (id == R.id.action_db_main) {
            startActivity(new Intent(this, DB_MainActivity.class));
        } else if (id == R.id.action_manufacturer) {
            startActivity(new Intent(this, ManufacturerActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void addEvents() {
        phoneTextView.setOnClickListener(v -> makeCall());
    }

    private void makeCall() {
        String phoneNumber = getString(R.string.phone_number); // Lấy từ strings.xml
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(phoneNumber));
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng stu = new LatLng(10.738092944305777, 106.67783054363882);
        mMap.addMarker(new MarkerOptions()
                .position(stu)
                .title(getString(R.string.marker_title))
                .snippet(getString(R.string.marker_snippet))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stu, 17));
    }
}
