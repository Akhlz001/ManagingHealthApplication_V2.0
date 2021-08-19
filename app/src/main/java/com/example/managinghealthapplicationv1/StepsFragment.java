package com.example.managinghealthapplicationv1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StepsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false); //Step counter fragment called and inflated in layout view


        //It's empty here! Step counter class stored in 'WalkingActivity' (this is the main activity for non legacy devices)...

    }
}

