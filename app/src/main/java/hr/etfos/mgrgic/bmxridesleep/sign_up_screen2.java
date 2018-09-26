package hr.etfos.mgrgic.bmxridesleep;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sign_up_screen2 extends AppCompatActivity {

    public String spinnerValue1;
    public String spinnerValue2;
    Spinner spinner;
    Spinner spinner2;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    EditText editText1;
    EditText editText2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen2);
        Intent intent = getIntent();

        spinner = findViewById(R.id.spinner1);
        adapter = ArrayAdapter.createFromResource(this, R.array.rider, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("userInfo");

        editText1 = findViewById(R.id.chooseCity);
        editText2 = findViewById(R.id.editTextFamiliar);


        Button button = findViewById(R.id.nextButton2);

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
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d("value", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("value", "Failed to read value.", error.toException());
            }
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i)+" selected", Toast.LENGTH_LONG).show();
                spinnerValue1 = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner2 = findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.riding, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i)+"", Toast.LENGTH_LONG).show();
                spinnerValue2 = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(sign_up_screen2.this, profileActivity.class);


                String rider = spinnerValue1;
                String riding = spinnerValue2;
                String city = editText1.getText().toString();
                String knowSpots = editText2.getText().toString();

                FirebaseUser user = firebaseAuth.getCurrentUser();
                String userID = user.getUid();


                databaseReference.child(userID).child("rider").setValue(rider);
                databaseReference.child(userID).child("riding").setValue(riding);
                databaseReference.child(userID).child("city").setValue(city);
                databaseReference.child(userID).child("knowSpots").setValue(knowSpots);
                //Toast.makeText(getApplicationContext(),rider,Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),riding,Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();

            }
        });

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
