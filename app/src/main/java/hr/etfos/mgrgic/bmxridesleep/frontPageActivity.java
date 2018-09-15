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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    searchAdapter  searchAdapterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        Intent intent = getIntent();
        button = findViewById(R.id.profileButton);
        editText = findViewById(R.id.searchByUsername);
        recyclerView = findViewById(R.id.recyclerView);

       /* LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(searchAdapterAdapter);
        */

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        userNameList = new ArrayList<>();
        riderList = new ArrayList<>();
        ridingList = new ArrayList<>();


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
                recyclerView.removeAllViews();
                int counter = 0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String userID = snapshot.getKey();
                    String userName = (String) snapshot.child("nickname").getValue();
                    String rider = (String) snapshot.child("rider").getValue();
                    String riding = (String) snapshot.child("riding").getValue();

                    if(userName.toLowerCase().contains(searchedString.toLowerCase())){
                        userNameList.add(userName);
                        riderList.add(rider);
                        ridingList.add(riding);
                        counter++;
                    }else if (rider.toLowerCase().contains(searchedString.toLowerCase())){
                        userNameList.add(userName);
                        riderList.add(rider);
                        ridingList.add(riding);
                        counter++;
                    } else if (riding.toLowerCase().contains(searchedString.toLowerCase())){
                        userNameList.add(userName);
                        riderList.add(rider);
                        ridingList.add(riding);
                        counter++;
                    }

                    if(counter == 15){
                        break;
                    }

                    searchAdapterAdapter = new searchAdapter(frontPageActivity.this, userNameList, riderList, ridingList);
                    recyclerView.setAdapter(searchAdapterAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
