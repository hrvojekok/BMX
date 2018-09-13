package hr.etfos.mgrgic.bmxridesleep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaActionSound;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class profileActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    Button button;
    Button button1;
    Uri imageURI;
    TextView textView;
    TextView textView1;
    TextView textView2;

    String spinnerValue1;
    String spinnerValue2;
    private ProgressDialog progressDialog;
    String profileImageUrl;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    StorageReference storageReference;
/*    /////////////////////////
    StorageReference firebaseStorage;
    ////////////////////*/
    private static final int PICK_IMAGE = 100;

String noviUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editText = findViewById(R.id.personalInfo);
        imageView = findViewById(R.id.profilePicture);
        button = findViewById(R.id.saveButton);
        button1 = findViewById(R.id.customize);
        firebaseAuth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.verifyTextView);
        textView1 = findViewById(R.id.logOut);
        textView2 = findViewById(R.id.riderTextView);


        //////////////////////
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("profilepictures");
        storageReference = FirebaseStorage.getInstance().getReference().child("profilepictures/" + System.currentTimeMillis() + ".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("urlurl", uri.toString());
                noviUrl = uri.toString();
            }
        });
//////////////////

     /*   ////////////////
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        Log.d("blabla", firebaseStorage.toString());
///////////////////////////*/

        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        spinnerValue1 = intent.getStringExtra("spinnerValue1");
        spinnerValue2 = intent.getStringExtra("spinnerValue2");


        loadUserInfo();
        if(spinnerValue1 != null && spinnerValue2 != null){

            textView2.setText("I am a " + spinnerValue1 + ", and I like riding " + spinnerValue2 + ".");
        }
        //Toast.makeText(getApplicationContext(), spinnerValue1, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), spinnerValue2, Toast.LENGTH_LONG).show();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectProfilePicture();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), logInActivity.class));
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////pripazi na ovo
                startActivity(new Intent(getApplicationContext(), sign_up_screen2.class));

            }
        });

    }

    //loada u imageview dobro
    private void loadUserInfo() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        //String photoUrl = userInfo.getPhotoUrl().toString();


        if (user != null) {
            Log.d("bla", user.getPhotoUrl().toString());

            if (user.getPhotoUrl() != null) {
                //Picasso.get().load("gs://bmxrideandsleep.appspot.com/profilepictures/1536790148912.jpg").into(imageView);

                Glide.with(this)
                        //.load(noviUrl)
//                        .load(firebaseDatabase)
                        //.load("gs://bmxrideandsleep.appspot.com/profilepictures/1536790148912.jpg")
                        .load("https://firebasestorage.googleapis.com/v0/b/bmxrideandsleep.appspot.com/o/profilepictures%2F1536763546087.jpg?alt=media&token=25a85aea-cdd1-44d1-939f-e80e5e3d86bf")
                        //.load(userInfo.getPhotoUrl())
                        .into(imageView);
                //Glide.with(this).load(userInfo.getPhotoUrl()).placeholder(R.drawable.default_profile).dontAnimate().into(view);
               /* imageView.setImageURI(Uri.parse(userInfo.getPhotoUrl().toString()));

                imageView.setImageURI(imageURI);
*/
                //     Picasso.get().load(userInfo.getPhotoUrl()).into(imageView);
            }
            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }
        }


        if (user.isEmailVerified()) {
            textView.setText("Email is verified");

        } else {
            textView.setText("Email is not verified. Click here to verify.");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Verification email sent", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }


    //problem
    private void saveInfo() {
        String name = editText.getText().toString();

        progressDialog.setMessage("Saving info..");
        progressDialog.show();
        if (name.isEmpty()) {
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("success", "success");
                                progressDialog.dismiss();
                                Toast.makeText(profileActivity.this, "Profile info updated", Toast.LENGTH_LONG).show();

                            }
                            if (task.isCanceled()) {

                                Toast.makeText(profileActivity.this, "Task canceled", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(profileActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    //radi
    private void selectProfilePicture() {

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    //radi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
/////////////////////////
         /*   StorageReference filepath = firebaseStorage.child("Photos").child(imageURI.getLastPathSegment());
            filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(profileActivity.this, "Image uploaded", Toast.LENGTH_LONG).show();
                }
            });*/
            /////////////////////////
            uploadImageToFirebase();
        }
    }

    //radi
    private void uploadImageToFirebase() {
        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        final StorageReference profilePictureReference = FirebaseStorage.getInstance().getReference("profilepictures/" + System.currentTimeMillis() + ".jpg");

        if (imageURI != null) {
            profilePictureReference.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileImageUrl = taskSnapshot.getUploadSessionUri().toString();
                            Log.d("blablabla", profileImageUrl);
                            progressDialog.dismiss();
                            Toast.makeText(profileActivity.this, "Image uploaded", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, logInActivity.class));

        }
    }
}
