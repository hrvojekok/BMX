package hr.etfos.mgrgic.bmxridesleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

public class mapsActivity extends AppCompatActivity {

    WebView webView;
    Button button;
    TextView textView;

    private String fileName = "map.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        textView = findViewById(R.id.textViewLocation);
        button = findViewById(R.id.getMyLocation);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + fileName);


        ActivityCompat.requestPermissions(mapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
                Location location = gpsTracker.getLocation();

                if(location != null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Toast.makeText(getApplicationContext(), "Latitude: " + latitude + " Longitude: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Location is null", Toast.LENGTH_LONG).show();
                }
            }
        });

    }





}
