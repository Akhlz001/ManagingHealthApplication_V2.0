package com.example.managinghealthapplicationv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class LegacyMedicalID extends AppCompatActivity {

    // Legacy version of 'Fragment_medicalID same code except it is not written in view context //

    //variables created

    TextView name, age, height, weight, bloodtype, condition, reaction, medication;
    private CircleImageView userProfileImage; //Circle Image View called to show round user profile image
    DatabaseReference RootKey; //Real-time Firebase database used to allow medical ID data to be retrieved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legacy_medical);

        //Variables are declared, textviews are given layout file id's to allow data retrieval

        name = findViewById(R.id.legacy_rname);
        age = findViewById(R.id.legacy_rage);
        height = findViewById(R.id.legacy_rheight);
        weight = findViewById(R.id.legacy_rweight);
        bloodtype = findViewById(R.id.legacy_rbloodtype);
        condition = findViewById(R.id.legacy_rcondition);
        reaction = findViewById(R.id.legacy_rreaction);
        medication = findViewById(R.id.legacy_rmedication);

        //notice in this class each id starts with 'r' which stands for retrieve, this stops confusion between both medical id methods


        RootKey = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("User Medical Profile"); //Real-time firebase database directory is set
        RootKey .addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) //data can now be retrieved from the if statement below
            {
                if (dataSnapshot.exists())
                {

                    String name = dataSnapshot.child("name").getValue().toString(); //each value is taken from the realtime database and added to local variables in string format
                    String age = dataSnapshot.child("age").getValue().toString();
                    String height = dataSnapshot.child("height").getValue().toString();
                    String weight = dataSnapshot.child("weight").getValue().toString();
                    String bloodtype = dataSnapshot.child("bloodtype").getValue().toString();
                    String medcondition = dataSnapshot.child("medcondition").getValue().toString();
                    String medreaction = dataSnapshot.child("medreaction").getValue().toString();
                    String medmedication = dataSnapshot.child("medmedication").getValue().toString();

                    LegacyMedicalID.this.name.setText(name); //now these string values can be set to the medical ID fragment displaying user information which is in the database, a very secure method as no local data is stored
                    LegacyMedicalID.this.age.setText(age);
                    LegacyMedicalID.this.height.setText(height);
                    LegacyMedicalID.this.weight.setText(weight);
                    LegacyMedicalID.this.bloodtype.setText(bloodtype);
                    condition.setText(medcondition);
                    reaction.setText(medreaction);
                    medication.setText(medmedication);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        userProfileImage = findViewById(R.id.legacy_medical_image); //Circle Image View is allocated medical ID fragment image so once image is retrieved it is shown

        RootKey = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()); //Real-time firebase database directory is changed to allow image sourcing
        RootKey .addValueEventListener(new ValueEventListener() //PLEASE NOTE IMAGE AND MEDICAL DATA ARE STORED UNDER THE CURRENT ID UNDER DIFFERENT SUB CATEGORIES MAKING IT HARDER TO INFILTRATE AND STEAL DATA
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String retrieveProfileImage = dataSnapshot.child("image").getValue().toString(); //image download link is saved to string format to allow input into Circle Image View
                    Picasso.get().load(retrieveProfileImage).into(userProfileImage); //Image url loaded, image displayed...
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
