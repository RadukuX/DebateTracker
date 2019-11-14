package com.example.debatetracker.ui.register;

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
import com.example.debatetracker.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignIn;
    TextView textSignIn;
    FirebaseAuth mFirebaseAuth;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.login);
        textSignIn = findViewById(R.id.question1);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener(){

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

                else if(!(email.isEmpty() && pass.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                        @SuppressLint("ShowToast")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Register failed! Try again.",Toast.LENGTH_SHORT).show();
                            }

                            else{
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }
                        }
                    });
                }

                else{
                    Toast.makeText(RegisterActivity.this,"Error Occurred !",Toast.LENGTH_SHORT).show();
                }
            }

        });

        textSignIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
