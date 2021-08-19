package com.example.managinghealthapplicationv1;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MedicalIDFragment extends Fragment { //Fragment class which can be opened via the bottom navigation bar found in the version of MHA which supports stepcounters

    //variables created

    TextView name, age, height, weight, bloodtype, rcondition, reaction, medication;
    private CircleImageView userProfileImage; //Circle Image View called to show round user profile image
    DatabaseReference RootKey; //Real-time Firebase database used to allow medical ID data to be retrieved

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicalid, container, false); //medical ID fragment is inflated in layout view



        //Variables are declared, textviews are given layout file id's to allow data retrieval

        name = view.findViewById(R.id.rname);
        age = view.findViewById(R.id.rage);
        height = view.findViewById(R.id.rheight);
        weight = view.findViewById(R.id.rweight);
        bloodtype = view.findViewById(R.id.rbloodtype);
        rcondition = view.findViewById(R.id.rcondition);
        reaction = view.findViewById(R.id.rreaction);
        medication = view.findViewById(R.id.rmedication);

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

                    MedicalIDFragment.this.name.setText(name); //now these string values can be set to the medical ID fragment displaying user information which is in the database, a very secure method as no local data is stored
                    MedicalIDFragment.this.age.setText(age);
                    MedicalIDFragment.this.height.setText(height);
                    MedicalIDFragment.this.weight.setText(weight);
                    MedicalIDFragment.this.bloodtype.setText(bloodtype);
                    rcondition.setText(medcondition);
                    reaction.setText(medreaction);
                    medication.setText(medmedication);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        userProfileImage = view.findViewById(R.id.medical_image); //Circle Image View is allocated medical ID fragment image so once image is retrieved it is shown

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
        return view; //View can now be returned, as all code has been passed and information displayed, without this the view will not pass!
    }

}

