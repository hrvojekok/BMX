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
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

public class mapsActivity extends AppCompatActivity {

    String latitudeToSend;
    String longitudeToSend;
    WebView webView;
    Button button;
    Button button2;
    TextView textView;

    private String fileName = "map.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        textView = findViewById(R.id.textViewLocation);
        button = findViewById(R.id.getMyLocation);
        button2 = findViewById(R.id.confirmButton);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + fileName);
        //webView.addJavascriptInterface(new webViewInterface(), "mapJavaScriptInterface");


        ActivityCompat.requestPermissions(mapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
                Location location = gpsTracker.getLocation();

                if(location != null){
                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();


                    textView.setText("Latitude is: " + latitude + ", Longitude is: " + longitude);
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:setMarker('" + latitude + "', '" + longitude + "')");
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Location is null", Toast.LENGTH_LONG).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
                Location location = gpsTracker.getLocation();

                final double latitude =  location.getLatitude();
                final double longitude = location.getLongitude();

                latitudeToSend = String.valueOf(latitude);
                longitudeToSend = String.valueOf(longitude);

                Intent intentProfile = new Intent(getApplicationContext(), profileActivity.class);
                intentProfile.putExtra("latitude", latitudeToSend);
                intentProfile.putExtra("longitude", longitudeToSend);
                startActivity(intentProfile);
            }
        });
    }




   /* public class webViewInterface{

        @JavascriptInterface
        public void showToast(){
            GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
            Location location = gpsTracker.getLocation();

            if(location != null){
                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();

                textView.setText("Latitude is: " + latitude + ", Longitude is: " + longitude);

                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:setMarker('" + latitude + "', '" + longitude + "')");
                    }
                });

            }else{
                Toast.makeText(getApplicationContext(),"Location is null", Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(getApplicationContext(), "javascript webinterface", Toast.LENGTH_LONG).show();
        }
    }*/


}
