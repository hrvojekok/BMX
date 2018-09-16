package hr.etfos.mgrgic.bmxridesleep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.iid.FirebaseInstanceId;

public class sendEmailActivity extends AppCompatActivity {
    private EditText editTextTo;
    private EditText editTextSubject;
    private EditText editTextMessage;
    Button button;

    String email = null;
    private FirebaseDatabase firebaseDatabaseNick;
    private FirebaseAuth firebaseAuthNick;
    private FirebaseAuth.AuthStateListener authStateListenerNick;
    private DatabaseReference databaseReferenceNick;


    String emailLogin = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        editTextTo = findViewById(R.id.editTextTo);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        button = findViewById(R.id.buttonSend);

        Intent intent = getIntent();

        //Log.d("email", emailLogin);

        firebaseAuthNick = FirebaseAuth.getInstance();
        firebaseDatabaseNick = FirebaseDatabase.getInstance();
        databaseReferenceNick = firebaseDatabaseNick.getReference();



        emailLogin = intent.getStringExtra("email");

        if(emailLogin!=null) {
            editTextTo.setText(emailLogin);
        }

        editTextSubject.setText(R.string.subject);
        editTextMessage.setText(R.string.message);

        databaseReferenceNick.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = firebaseAuthNick.getCurrentUser();
                String userID = user.getUid();

                if(dataSnapshot.child(userID).child("email").getValue() != "") {
                    email = (String) dataSnapshot.child(userID).child("email").getValue();

                    //editTextTo.setText(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendEmail();
            }
        });



    }

    private void sendEmail() {
        String recipientList = editTextTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = editTextSubject.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }
}
