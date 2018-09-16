package hr.etfos.mgrgic.bmxridesleep;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class frontPageActivity extends AppCompatActivity {


    EditText editText;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    Button button1;
    Button button;
    ArrayList<String> userNameList;
    ArrayList<String> riderList;
    ArrayList<String> ridingList;
    ArrayList<String> emailList;
    ArrayList<String> locationList;
    searchAdapter  searchAdapterAdapter;
    String emailLogin = null;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        final Intent intent = getIntent();
        button = findViewById(R.id.profileButton);
        editText = findViewById(R.id.searchByUsername);
        recyclerView = findViewById(R.id.recyclerView);



        emailLogin = intent.getStringExtra("email");

        //Toast.makeText(this, emailLogin, Toast.LENGTH_LONG).show();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        userNameList = new ArrayList<>();
        riderList = new ArrayList<>();
        ridingList = new ArrayList<>();
        emailList = new ArrayList<>();
        locationList = new ArrayList<>();



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userID = user.getUid();

        if(emailLogin != null) {
            databaseReference.child(userID).child("email").setValue(emailLogin);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(frontPageActivity.this, profileActivity.class);
                startActivity(intent);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!editable.toString().isEmpty()){
                    setAdapter(editable.toString());
                }else{

                    userNameList.clear();
                    riderList.clear();
                    ridingList.clear();
                    emailList.clear();
                    locationList.clear();
                    recyclerView.removeAllViews();
                }
            }
        });


    }

    private void setAdapter(final String searchedString) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                userNameList.clear();
                riderList.clear();
                ridingList.clear();
                emailList.clear();
                locationList.clear();
                recyclerView.removeAllViews();
                int counter = 0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String userID = snapshot.getKey();
                    String userName = (String) snapshot.child("nickname").getValue();
                    String rider = (String) snapshot.child("rider").getValue();
                    String riding = (String) snapshot.child("riding").getValue();
                    String email = (String) snapshot.child("email").getValue();
                    String location = (String) snapshot.child("city").getValue();

                    if(userName.toLowerCase().contains(searchedString.toLowerCase())){
                        userNameList.add(userName);
                        riderList.add(rider);
                        ridingList.add(riding);
                        emailList.add(email);
                        locationList.add(location);
                        counter++;
                    }else if (rider.toLowerCase().contains(searchedString.toLowerCase())){
                        userNameList.add(userName);
                        riderList.add(rider);
                        ridingList.add(riding);
                        emailList.add(email);
                        locationList.add(location);
                        counter++;
                    } else if (riding.toLowerCase().contains(searchedString.toLowerCase())){
                        userNameList.add(userName);
                        riderList.add(rider);
                        ridingList.add(riding);
                        emailList.add(email);
                        locationList.add(location);
                        counter++;
                    }

                    if(counter == 15){
                        break;
                    }

                    searchAdapterAdapter = new searchAdapter(frontPageActivity.this, userNameList, riderList, ridingList, emailList, locationList);
                    recyclerView.setAdapter(searchAdapterAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
