package com.example.debatetracker.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debatetracker.MainActivity;
import com.example.debatetracker.R;
import com.example.debatetracker.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignIn;
    TextView textSignIn;
    FirebaseAuth mFirebaseAuth;
    Button register;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.login);
        textSignIn = findViewById(R.id.question1);
        register = findViewById(R.id.register);




        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this,"You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }

                else{
                    //Toast.makeText(LoginActivity.this,"Log in please", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener(){

            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {

                String email = emailId.getText().toString();
                String pass = password.getText().toString();

                if(email.isEmpty()){
                    emailId.setError("An Email is needed!");
                    emailId.requestFocus();
                }

                else if(pass.isEmpty()){
                    password.setError("A password is needed!");
                    password.requestFocus();
                }

                else if(!(email.isEmpty() && pass.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error,Try again,",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intToMain = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intToMain);
                            }
                        }
                    });
                }

                else{
                    Toast.makeText(LoginActivity.this,"Error Occurred !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        textSignIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}
