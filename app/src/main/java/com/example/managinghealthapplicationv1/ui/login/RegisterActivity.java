package com.example.managinghealthapplicationv1.ui.login;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.managinghealthapplicationv1.R;
import com.example.managinghealthapplicationv1.WalkingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//User can register via Firebase, no credentials are stored locally

public class RegisterActivity extends AppCompatActivity {

    //all variables created

    EditText emailId, passwordId;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); //set view to register xml layout file

        //all variables assigned values in onCreate method

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        emailId = findViewById(R.id.susername);
        passwordId = findViewById(R.id.spassword);
        btnSignUp = findViewById(R.id.register);
        tvSignIn = findViewById(R.id.sign_in);

        //Register button clicked execute if statements
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString(); //get text entered into string format to match firebase authentication
                String password = passwordId.getText().toString();
                if (email.isEmpty()){ //If email is left empty display the below toast message:
                    emailId.setError("Please enter a valid email address");
                    emailId.requestFocus();
                }
                else if(password.isEmpty()){ //If password is left empty display message:
                    passwordId.setError("Please enter a password");
                }
                else if(email.isEmpty() && password.isEmpty()){ //If both username and password are empty display message:
                    Toast.makeText(RegisterActivity.this, "Both fields are empty, please fill them in", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && password.isEmpty())){ //If both a username and a password has been typed in but special characters have been used e.g in username display message:
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Unable to Sign you up, please make sure you are using an appropriate username and make sure the password is between 5-7 characters", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(RegisterActivity.this, WalkingActivity.class));
                            }
                        }
                    });
                }
                else{ //If there is an unknown problem outside the applications control, wifi is turned off or no internet is available display message:
                    Toast.makeText(RegisterActivity.this, "An error occurred, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //If Sign In text is clicked execute intent to lead to login activity
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignIn = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intSignIn);
            }
        });

    }
}
