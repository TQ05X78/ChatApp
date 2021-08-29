package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetProfile extends AppCompatActivity {

    private CardView mgetuserimage;
    private ImageView mgetuserImageinImageView;
    private static int PICK_IMAGE = 123;
    private Uri imagepath;

    EditText mgetusername;

    private android.widget.Button msaveprofile;
    private FirebaseAuth firebaseAuth;
    private String name;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private String ImageUriAcessToken;
    private FirebaseFirestore firebaseFirestore;


    ProgressBar mprogressbarforsetprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


       mgetuserimage = findViewById(R.id.getuserImage);
       mgetuserImageinImageView = findViewById(R.id.getuserimageinImageView);
       msaveprofile = findViewById(R.id.saveProfile);
       mprogressbarforsetprofile = findViewById(R.id.progressbarfor_save_Profile);
       mgetusername = findViewById(R.id.getuser_name);


       mgetuserimage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
               startActivityForResult(intent, PICK_IMAGE);
           }
       });



       msaveprofile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               name = mgetusername.getText().toString();
               if(name.isEmpty())
               {
                   Toast.makeText(SetProfile.this, "Name is Empty", Toast.LENGTH_SHORT).show();
               }

               else if(imagepath == null)
               {
                   Toast.makeText(SetProfile.this, "Image is Empty", Toast.LENGTH_SHORT).show();
               }

               else {
                   mprogressbarforsetprofile.setVisibility(View.VISIBLE);
                   sendDataForNewUser();
                   mprogressbarforsetprofile.setVisibility(View.INVISIBLE);
                   Intent intent = new Intent(SetProfile.this, ChatActivity.class);
                   startActivity(intent);
                   finish();
               }

           }
       });


    }

    private void sendDataForNewUser() {

       sendDataToRealTimeDatabase();


    }

    private void sendDataToRealTimeDatabase() {


        name = mgetusername.getText().toString().trim();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        userProfile muserprofile = new userProfile(name, firebaseAuth.getUid());
        databaseReference.setValue(muserprofile);
        Toast.makeText(SetProfile.this, "User Profile Added Successfully!", Toast.LENGTH_SHORT).show();
        sendImagetoStorage();


    }

    private void sendImagetoStorage() {

        StorageReference imageref = storageReference.child("Images").child(firebaseAuth.getUid())
                .child("Profile Pic");

        //Image Compression

        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();


        //putting image to storage


        UploadTask uploadTask = imageref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        ImageUriAcessToken = uri.toString();
                        Toast.makeText(SetProfile.this, "URI get sucess", Toast.LENGTH_SHORT).show();
                        sendDataTocloudFirebase();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SetProfile.this, "URI get Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(SetProfile.this, "Image is upload", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SetProfile.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void sendDataTocloudFirebase() {

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", name);
        userdata.put("image", ImageUriAcessToken);
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "online");



        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(SetProfile.this, "Data on cloud Firestore send sucess", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath = data.getData();
            mgetuserImageinImageView.setImageURI(imagepath);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
