package hr.etfos.mgrgic.bmxridesleep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import static java.lang.String.valueOf;


public class sendMessageActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    private FirebaseListAdapter<ChatMessage> adapter1;
    RelativeLayout sendMessageActivity;
    FloatingActionButton floatingActionButton;

    String message;
    EditText input;
    String email;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Intent intent = getIntent();

        email = intent.getStringExtra("email");
        Log.d("email", email);
        sendMessageActivity = findViewById(R.id.activity_send_message);
        floatingActionButton = findViewById(R.id.floatingButton);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Snackbar.make(sendMessageActivity, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();

            displayChatMessage();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = findViewById(R.id.inputEditText);


                DataSnapshot dataSnapshot = null;

                String userName = null;
                if (dataSnapshot != null) {
                    userName = (String) dataSnapshot.child("nickname").getValue();
                }

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String nicknameIs = prefs.getString("nickname", "nickname"); //no id: default value

                //String sender = FirebaseDatabase.getInstance().getReference(); ovak nije bilo lega
                FirebaseDatabase.getInstance().getReference().child("messages").child(valueOf(nicknameIs)).child(valueOf(email)).push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                FirebaseDatabase.getInstance().getReference().child("messages").child(valueOf(email)).child(valueOf(nicknameIs)).push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                input.setText("");



            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Snackbar.make(sendMessageActivity, "You are successfully signed in", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(sendMessageActivity, "Failed", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void displayChatMessage() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nicknameIs = prefs.getString("nickname", "nickname"); //no id: default value

        ListView listViewMessages = findViewById(R.id.messageList);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.list_tem,
                FirebaseDatabase.getInstance().getReference().child("messages").child(valueOf(nicknameIs)).child(valueOf(email))) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText, messageUser, messageTime;

                messageText = v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);


                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd--MM--yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        ///pogledaj kak se primaju sve poruke
        adapter1 = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.list_tem,
                FirebaseDatabase.getInstance().getReference().child("messages").child(valueOf(email)).child(valueOf(nicknameIs))) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText, messageUser, messageTime;

                messageText = v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);


                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd--MM--yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        listViewMessages.setAdapter(adapter);
        listViewMessages.setAdapter(adapter1);
    }
}
