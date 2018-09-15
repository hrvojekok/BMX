package hr.etfos.mgrgic.bmxridesleep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaActionSound;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    Button button2;
    Uri imageURI;
    TextView textView;
    TextView textView1;
    TextView textView2;
    TextView textView3;

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

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String pictureName = "";
    String time = null;
    String value = "";
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
        textView3 = findViewById(R.id.locationTextView);
        button2 = findViewById(R.id.maps);




/*


            Glide.with(getApplicationContext())
                    .load(preferences.getString("realImage",""))
                    .into(imageView);
*/

            /*
        String pictureName = preferences.getString("pictureName", "");
        Toast.makeText(this, pictureName, Toast.LENGTH_LONG).show();
        Log.d("pictureName", pictureName);
        */
      /*  if(time == null){

            time = String.valueOf(System.currentTimeMillis());
            //preferences = PreferenceManager.getDefaultSharedPreferences(this);

            preferences = getPreferences(this.MODE_PRIVATE);
            editor = preferences.edit();
            editor.putString("timeKey", time);
            editor.commit();

            String imageName = preferences.getString("timeKey", "");
            Toast.makeText(this, imageName, Toast.LENGTH_LONG).show();

        } else{

            String imageName = preferences.getString("timeKey", "");
            //Toast.makeText(this, imageName, Toast.LENGTH_LONG).show();
        }*/


        progressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        Intent intentProfile = getIntent();
        spinnerValue1 = intent.getStringExtra("spinnerValue1");
        spinnerValue2 = intent.getStringExtra("spinnerValue2");
        String latitudeFromMaps = intentProfile.getStringExtra("latitude");
        String longitudeFromMaps = intentProfile.getStringExtra("longitude");



        loadUserInfo();
        if(spinnerValue1 != null && spinnerValue2 != null){

            textView2.setText("I am a " + spinnerValue1 + ", and I like riding " + spinnerValue2 + ".");
        }
        //Toast.makeText(getApplicationContext(), spinnerValue1, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), spinnerValue2, Toast.LENGTH_LONG).show();

        if(latitudeFromMaps != null && longitudeFromMaps != null) {
            textView3.setText("Your latitude is: " + latitudeFromMaps + ", your longitude is: " + longitudeFromMaps);
        }

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
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), mapsActivity.class));
                finish();
            }
        });

    }

    //loada u imageview dobro
    private void loadUserInfo() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        //String photoUrl = userInfo.getPhotoUrl().toString();


        if (user != null) {
            //Log.d("imageFromFirebaseTime", user.getPhotoUrl().toString());

            if (noviUrl != null) {
                Glide.with(this)
                        .load(noviUrl)
                        .into(imageView);
            }
            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());

                //String pictureName = editText.getText().toString();

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

            //uploadImageToFirebase();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please type in your nickname");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pictureName = input.getText().toString();
                    preferences = getPreferences(getApplication().MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putString("pictureName", pictureName);
                    editor.commit();

                    Log.d("pictureName", pictureName);
                    if(pictureName != "") {
                        uploadImageToFirebase();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }


    }


    //radi
    private void uploadImageToFirebase() {
        progressDialog.setMessage("Uploading..");
        progressDialog.show();


            final StorageReference profilePictureReference = FirebaseStorage.getInstance().getReference("profilepictures/" + pictureName + ".jpg");

            if (imageURI != null) {
                profilePictureReference.putFile(imageURI)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                profileImageUrl = taskSnapshot.getUploadSessionUri().toString();
                                Log.d("imageActualUriUpload", profileImageUrl);
                                progressDialog.dismiss();
                                Toast.makeText(profileActivity.this, "Image uploaded", Toast.LENGTH_LONG).show();

                                profilePictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("imageDownloadedUrl", uri.toString());
                                        noviUrl = uri.toString();
                                        Glide.with(getApplicationContext())
                                                .load(noviUrl)
                                                .into(imageView);
                                        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        editor = preferences.edit();
                                        editor.putString("realImage", noviUrl);
                                        editor.commit();

                                        /*String imageName = preferences.getString("realImage", "");
                                        Toast.makeText(getApplicationContext(), imageName, Toast.LENGTH_LONG).show();*/
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Log.d("imageFailed", "failed");
                                    }
                                });
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
/*


    @Override
    protected void onResume() {

        super.onResume();
        //Log.d("resumeNoviUrl", noviUrl);
        Glide.with(getApplicationContext())
                .load()
                .into(imageView);
    }
*/


    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, logInActivity.class));

        }

    }

}