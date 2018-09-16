package hr.etfos.mgrgic.bmxridesleep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class sendEmailActivity extends AppCompatActivity {
    private EditText editTextTo;
    private EditText editTextSubject;
    private EditText editTextMessage;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        editTextTo = findViewById(R.id.editTextTo);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        button = findViewById(R.id.buttonSend);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sendEmail();
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
