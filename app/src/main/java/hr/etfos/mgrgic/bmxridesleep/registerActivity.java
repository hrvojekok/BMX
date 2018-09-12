package hr.etfos.mgrgic.bmxridesleep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class registerActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewLogIn;
    private ProgressDialog progressDialog;
    public static Activity firstActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        textViewLogIn = findViewById(R.id.textViewSignIn);

        buttonRegister = findViewById(R.id.registerButton);


        buttonRegister.setOnClickListener(this);
        textViewLogIn.setOnClickListener(this);

        firstActivity = this;
    }


    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            registerUser();
        }
        if(view == textViewLogIn){
           startActivity(new Intent(this, logInActivity.class));
           finish();
        }
    }



    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

                        if(task.isSuccessful()){
                            finish();
                            Intent intent = new Intent(registerActivity.this, frontPageActivity.class);
                            progressDialog.dismiss();

                            startActivity(intent);
                            //Toast.makeText(registerActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(registerActivity.this, "Registration failed, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, frontPageActivity.class));
        }
    }

}
