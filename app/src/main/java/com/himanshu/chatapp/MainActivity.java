package com.himanshu.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText mgetphonenumber;
    android.widget.Button msendotp;
    CountryCodePicker mcountryCodePicker;
    String countrycode;
    String phonenumber;          // concatenate phone no with country code
    ProgressBar mprogressbarofmain;
    FirebaseAuth firebaseAuth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String codesent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        mcountryCodePicker = findViewById(R.id.countrycodepicker);
        msendotp = findViewById(R.id.sendotpbutton);
        mgetphonenumber = findViewById(R.id.getphonenumber);
        mprogressbarofmain = findViewById(R.id.progressbarofmain);

        firebaseAuth = FirebaseAuth.getInstance();

        countrycode = mcountryCodePicker.getSelectedCountryCodeWithPlus();             //to get default country

        mcountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {          // to change default country
            @Override
            public void onCountrySelected() {
                countrycode = mcountryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });

        msendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number;
                number = mgetphonenumber.getText().toString();
                if (number.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter your number", Toast.LENGTH_SHORT).show();
                } else if (number.length() < 10) {
                    Toast.makeText(MainActivity.this, "Please Enter correct number", Toast.LENGTH_SHORT).show();

                } else {
                    mprogressbarofmain.setVisibility(View.VISIBLE);
                    phonenumber = countrycode + number;

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(firebaseAuth)
                                    .setPhoneNumber(phonenumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(MainActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();


                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                Toast.makeText(MainActivity.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
                mprogressbarofmain.setVisibility(View.INVISIBLE);
                codesent = s;           //store the otp in variable to verify it in next activity

                Intent intent = new Intent(MainActivity.this, OTPauthentication.class);
                intent.putExtra("otp", codesent);
                startActivity(intent);


            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, chatactivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}