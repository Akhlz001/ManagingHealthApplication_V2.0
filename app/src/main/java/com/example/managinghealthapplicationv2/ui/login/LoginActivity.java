package com.example.managinghealthapplicationv2.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.managinghealthapplicationv2.R;
import com.example.managinghealthapplicationv2.WalkingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//User login, re-directed from Register class

public class LoginActivity extends AppCompatActivity {

    //all variables created

    EditText emailId, passwordId;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth; //Firebase authentication
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //set view to login xml layout file

        //all variables assigned values in onCreate method

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        passwordId = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.sign_in);
        tvSignUp = findViewById(R.id.altsign_up);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser(); //Firebase authentication status requested

                if(mFirebaseUser != null ){ //If user is logged in due to previous activity start WalkingActivity Class
                    Toast.makeText(LoginActivity.this, "Successfully logged in",Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(LoginActivity.this, WalkingActivity.class);
                    startActivity(login);
                }
                else{ //If user is not logged in display toast message:
                    Toast.makeText(LoginActivity.this, "Please login to start MHA",Toast.LENGTH_SHORT).show();
                }
            }
        };

        //when login button is clicked execute if statements
        btnSignIn.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(LoginActivity.this, "Both fields are empty, please fill them in", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && password.isEmpty())){//If both a username and a password has been typed in but one or more fields is incorrect display message:
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Username or password is incorrect, please try again", Toast.LENGTH_SHORT).show();
                            }
                            else{//If both fields are correct then execute the intent to lead to WalkingActivity class:
                                Intent intToMain = new Intent(LoginActivity.this, WalkingActivity.class);
                                startActivity(intToMain);
                            }
                        }
                    });
                }
                else{ //If there is an unknown problem outside the applications control, wifi is turned off or no internet is available display message:
                    Toast.makeText(LoginActivity.this, "An error occurred, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Sign up text is clicked, execute intent to lead to register activity
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intSignUp);
            }
        });


    }

    @Override
    protected void onStart() { //On starting the login activity listen for changes in firebase authentication
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
