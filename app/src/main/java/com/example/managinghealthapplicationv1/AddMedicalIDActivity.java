package com.example.managinghealthapplicationv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMedicalIDActivity extends AppCompatActivity {

    //all variables created

    private CircleImageView userProfileImage; //Circle Image View implemented and used to create a circular Medical ID image
    private String currentUserID;
    private FirebaseAuth mAuth; //Firebase Authentication used
    private DatabaseReference RootRef; //Firebase Real-time database used
    private static final int GalleryPicker = 1; //Allows files to be selected using androids stock gallery picker
    private StorageReference UserProfileImageRef; //Firebase Storage used
    private ProgressDialog loadingBar;
    private Button SaveMedID;
    private EditText mname, mage, mweight, mheight, mbloodtype, mcondition, mreaction, mmedication;
    private MedicalInfo medicalInfo; //getter and setter methods created in a different class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical); //set view to retrieve medical layout file

        //all variables declared in onCreate method

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid(); //user ID extracted and put into currentuserID
        RootRef = FirebaseDatabase.getInstance().getReference(); //given database reference
        userProfileImage = findViewById(R.id.profile_image); //profile image in xml file assigned to circle image view
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images"); //save directory for profile image
        loadingBar = new ProgressDialog(this);
        SaveMedID = findViewById(R.id.updateid);
        mname = findViewById(R.id.mname);
        mage = findViewById(R.id.mage);
        mweight = findViewById(R.id.mweight);
        mheight = findViewById(R.id.mheight);
        mbloodtype = findViewById(R.id.mbloodtype);
        mcondition = findViewById(R.id.mcondition);
        mreaction = findViewById(R.id.mreaction);
        mmedication = findViewById(R.id.mmedication);
        medicalInfo = new MedicalInfo(); //all above 'm' id's taken from Medical ID method for future use


        RootRef.child("Users").child(currentUserID) //real-time database directory moved accordingly
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) //if profile image exists get image download link and insert into circle image view
                        {

                            String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();
                            Picasso.get().load(retrieveProfileImage).into(userProfileImage);


                            userProfileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) { //if circle image view is clicked initiate below commands
                                    Intent galleryIntent = new Intent();
                                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT); //executes gallery picker
                                    galleryIntent.setType("image/*"); //only an image can be selected
                                    startActivityForResult(galleryIntent, GalleryPicker); //method started where crop image is utilised
                                }
                            });

                        }
                        else //if profile image doesn't exist allow user to select one as above but show below toast message:
                        {
                            Toast.makeText(AddMedicalIDActivity.this, "Please set a profile image when filling in the Medical ID", Toast.LENGTH_SHORT).show();

                            userProfileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) { //same as above if statement
                                    Intent galleryIntent = new Intent();
                                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    galleryIntent.setType("image/*");
                                    startActivityForResult(galleryIntent, GalleryPicker);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        //if Update Medical ID button is clicked execute below commands...
        SaveMedID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                int age = Integer.parseInt(mage.getText().toString().trim()); //data entered in activity is converted to string format, ready to be uploaded
                Float height = Float.parseFloat(mheight.getText().toString().trim());
                Float weight = Float.parseFloat(mweight.getText().toString().trim());

                medicalInfo.setName(mname.getText().toString().trim());
                medicalInfo.setAge(age);
                medicalInfo.setBloodtype(mbloodtype.getText().toString().trim());
                medicalInfo.setMedcondition(mcondition.getText().toString().trim());
                medicalInfo.setMedreaction(mreaction.getText().toString().trim());
                medicalInfo.setMedmedication(mmedication.getText().toString().trim());
                medicalInfo.setHeight(height);
                medicalInfo.setWeight(weight);
                RootRef.child("Users").child(currentUserID).child("User Medical Profile").setValue(medicalInfo); //all values are uploaded to the firebase real-time database
                Toast.makeText(AddMedicalIDActivity.this, "Medical ID information successfully updated", Toast.LENGTH_SHORT).show(); //user notified on upload
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //the picture taken from the gallery is copied to an image cropper...
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPicker && resultCode == RESULT_OK && data!=null) //if image has been selected from gallery picker then execute the image cropper
        {
            Uri ImageUri = data.getData(); //image data retrieved

            CropImage.activity(ImageUri) //Crop Image Activity being used to allow users to crop their medical ID image to size.
                    .setGuidelines(CropImageView.Guidelines.ON) //crop guidelines on
                    .setAspectRatio(1,1) //1:1 profile image ratio, typical for a profile image...
                    .start(this); //image cropper started
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) //if image has been successfully cropped and 'crop' clicked execute if statement
            {
                loadingBar.setTitle("Medical ID Image");
                loadingBar.setMessage("Please wait, your Medical ID image is updating...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show(); //loading animation to alleviate waiting time stress

                Uri resultUri = result.getUri(); //final crop data


                StorageReference filepath = UserProfileImageRef.child(currentUserID + ".jpg"); //Directory where the profile image will be uploaded in firebase storage

                final UploadTask uploadTask = filepath.putFile(resultUri); //Image successfully uploaded

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { //if uploaded successfully below code links image from storage to real-time database
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(AddMedicalIDActivity.this, "Medical ID Image Updated", Toast.LENGTH_SHORT).show(); //lets user know Medical ID image uploaded successfully

                                final String downloadUrl = uri.toString(); //picture link from storage added to variable

                                RootRef.child("Users").child(currentUserID).child("image")
                                        .setValue(downloadUrl); //picture link finally added to the real-time database where the circle image view can retrieve it and show the users their image
                                loadingBar.dismiss(); //loading animation stopped
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) { //on an unknown error display below message:
                                Toast.makeText(AddMedicalIDActivity.this, "An unknown error occurred, please check your internet connection", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        });
                    }
                });
            }
        }
    }
}
