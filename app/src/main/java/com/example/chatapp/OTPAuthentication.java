package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPAuthentication extends AppCompatActivity {

    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;

    String enteredotp;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarforotpauth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpauthentication);


      mchangenumber = findViewById(R.id.changenumber);
      mverifyotp = findViewById(R.id.verify_otp);
      mgetotp = findViewById(R.id.get_otp);
      mprogressbarforotpauth = findViewById(R.id.progressbarfor_otp_auth);

      firebaseAuth = FirebaseAuth.getInstance();


      mchangenumber.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(OTPAuthentication.this, MainActivity.class);
              startActivity(intent);
          }
      });



        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredotp = mgetotp.getText().toString();
                if(enteredotp.isEmpty())
                {
                    Toast.makeText(OTPAuthentication.this, "Enter Your OTP First", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    mprogressbarforotpauth.setVisibility(View.VISIBLE);
                    String coderecieved = getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(coderecieved, enteredotp);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {


        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful())
           {
               mprogressbarforotpauth.setVisibility(View.INVISIBLE);
               Toast.makeText(OTPAuthentication.this, "Login Sucess", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OTPAuthentication.this, SetProfile.class);
                startActivity(intent);
                finish();
           }

           else {
               if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
               {
                   mprogressbarforotpauth.setVisibility(View.INVISIBLE);
                   Toast.makeText(OTPAuthentication.this, "Login Flailed!", Toast.LENGTH_SHORT).show();
               }
           }


            }
        });



    }


}


