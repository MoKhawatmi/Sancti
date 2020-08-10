package com.example.sancti.ui.photoEnhance;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sancti.R;

public class PhotoEnhance extends Fragment {

    private PhotoEnhanceViewModel mViewModel;

    public static PhotoEnhance newInstance() {
        return new PhotoEnhance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_enhance_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PhotoEnhanceViewModel.class);
        // TODO: Use the ViewModel
    }

}