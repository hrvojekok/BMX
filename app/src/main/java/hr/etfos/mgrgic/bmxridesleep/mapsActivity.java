package hr.etfos.mgrgic.bmxridesleep;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.PendingIntent.getActivity;

public class mapsActivity extends AppCompatActivity {

    String latitudeToSend;
    String longitudeToSend;
    WebView webView;
    Button button;
    Button button2;
    Button button3;
    TextView textView;

    private String fileName = "map.html";

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceLatitude;
    private DatabaseReference databaseReferenceLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        textView = findViewById(R.id.textViewLocation);
        button = findViewById(R.id.getMyLocation);
        button2 = findViewById(R.id.confirmButton);
        button3 = findViewById(R.id.putAllUsersToMap);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + fileName);
        //webView.addJavascriptInterface(new webViewInterface(), "mapJavaScriptInterface");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("userInfo");

        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d("TAG","onAuthStateChanged:signed_in: " + user.getUid());
                    //Toast.makeText(getApplicationContext(), "Signed in: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("TAG", "onAuthStateChange_signed_out: ");
                }
            }
        };
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                Log.d("value", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("value", "Failed to read value.", error.toException());
            }
        });


        ActivityCompat.requestPermissions(mapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
                Location location = gpsTracker.getLocation();

                if(location != null){
                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();



                    //textView.setText("Latitude is: " + latitude + ", Longitude is: " + longitude);
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

                FirebaseUser user = firebaseAuth.getCurrentUser();
                String userID = user.getUid();

                latitudeToSend = String.valueOf(latitude);
                longitudeToSend = String.valueOf(longitude);

                if(latitudeToSend != null && longitudeToSend != null) {
                    Intent intentProfile = new Intent(getApplicationContext(), profileActivity.class);
                    //intentProfile.putExtra("latitude", latitudeToSend);
                    //intentProfile.putExtra("longitude", longitudeToSend);

                    databaseReference.child(userID).child("locationLatitude").setValue(latitudeToSend);
                    databaseReference.child(userID).child("locationLongitude").setValue(longitudeToSend);
                    Toast.makeText(getApplicationContext(),"Location is set", Toast.LENGTH_LONG).show();

                    finish();
                    startActivity(intentProfile);
                } else {
                    Toast.makeText(getApplicationContext(),"Location is null", Toast.LENGTH_LONG).show();
                }
            }
        });
/*
        databaseReferenceLatitude = firebaseDatabase.getReference().child("userInfo").child.child("locationLatitude");
        Log.d("databaseReferenc1", databaseReferenceLatitude.toString());
        databaseReferenceLongitude = firebaseDatabase.getReference().child("userInfo").child("locationLongitude");

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceLatitude.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Log.d("dataSnapshot1", dataSnapshot1.toString());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                databaseReferenceLongitude.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            Log.d("dataSnapshot2", dataSnapshot2.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });*/

    }



    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        if(authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
