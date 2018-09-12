package hr.etfos.mgrgic.bmxridesleep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class
logInActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewSignIn;
    private Button loginButton;
    private EditText editTextEmailLogIn;
    private EditText editTextPasswordLogIn;
    private ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmailLogIn = findViewById(R.id.editTextEmailLogIn);
        editTextPasswordLogIn = findViewById(R.id.editTextPasswordLogIn);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.logInButton);
        loginButton.setOnClickListener(this);

        textViewSignIn = findViewById(R.id.textViewSignIn);
        textViewSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == loginButton){
            loginUser();
            progressDialog.setMessage("Logging you in..");
            progressDialog.show();
        }
        if(view == textViewSignIn){
            startActivity(new Intent(this, registerActivity.class));
            finish();
        }
    }


    public void loginUser(){
        String email = editTextEmailLogIn.getText().toString().trim();
        String password = editTextPasswordLogIn.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Intent intent = new Intent(logInActivity.this, frontPageActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//ne radi

                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, profileActivity.class));
        }
    }
}
