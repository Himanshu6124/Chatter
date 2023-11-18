package com.himanshu.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPauthentication extends AppCompatActivity {

    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;
    String enteredotp;
    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarofauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpauthentication);
//        getSupportActionBar().hide();
        mchangenumber = findViewById(R.id.changenumber);
        mverifyotp = findViewById(R.id.verifyotp);
        mgetotp= findViewById(R.id.getotp);
        mprogressbarofauth=findViewById(R.id.progressbarofotpauth);

        firebaseAuth=FirebaseAuth.getInstance();

        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OTPauthentication.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredotp=mgetotp.getText().toString();
                if(enteredotp.isEmpty())
                {
                    Toast.makeText(OTPauthentication.this, "Please Enter your OTP", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    mprogressbarofauth.setVisibility(View.VISIBLE);
                    String codereceived = getIntent().getStringExtra("otp");

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codereceived,enteredotp);
                    signInWithPhoneAuthCredential(credential);
                    Intent intent = new Intent(OTPauthentication.this,setprofile.class);
                    startActivity(intent);


                }
            }
        });




    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(OTPauthentication.this, "Login successfull", Toast.LENGTH_SHORT).show();
                            mprogressbarofauth.setVisibility(View.INVISIBLE);

                            FirebaseUser user = task.getResult().getUser();
                            Intent intent =new Intent(OTPauthentication.this,chatactivity.class);
                            startActivity(intent);
                            finish();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mprogressbarofauth.setVisibility(View.INVISIBLE);
                                Toast.makeText(OTPauthentication.this, "Login failed", Toast.LENGTH_SHORT).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    }