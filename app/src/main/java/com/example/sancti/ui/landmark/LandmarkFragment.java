package com.example.sancti.ui.landmark;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sancti.GMapActivity;
import com.example.sancti.MapActivity;
import com.example.sancti.R;
import com.google.android.gms.common.GoogleApiAvailability;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark;
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmarkAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class LandmarkFragment extends Fragment {

    public Button landmarkButton;
    public ImageView landmarkImage;
    public TextView landmarkText;
    public Button showOnMap;

    String landmarkName;
    double lat;
    double lng;

    public boolean  isGMS(){
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext()) == com.google.android.gms.common.ConnectionResult.SUCCESS;
    }
    public boolean  isHMS(){
        return HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(getContext()) == com.huawei.hms.api.ConnectionResult.SUCCESS;
    }

    public void getImage() throws IOException {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                landmarkImage.setImageBitmap(selectedImage);
                landmarkText.setText("");
                showOnMap.setVisibility(View.GONE);

                MLRemoteLandmarkAnalyzer analyzer = MLAnalyzerFactory.getInstance().getRemoteLandmarkAnalyzer();
                MLFrame mlFrame = new MLFrame.Creator().setBitmap(selectedImage).create();

                Task<List<MLRemoteLandmark>> task = analyzer.asyncAnalyseFrame(mlFrame);
                task.addOnSuccessListener(new OnSuccessListener<List<MLRemoteLandmark>>() {
                    public void onSuccess(List<MLRemoteLandmark> landmarkResults) {
                        // Processing logic for recognition success.
                        String s="";
                        landmarkName=landmarkResults.get(0).getLandmark();
                        lat=landmarkResults.get(0).getPositionInfos().get(0).getLat();
                        lng=+landmarkResults.get(0).getPositionInfos().get(0).getLng();
                        s=getResources().getString(R.string.name)+" "+landmarkName+"\n"+landmarkResults.get(0).getLandmarkIdentity()+"\n"+getResources().getString(R.string.coo)+" "+lat+" , "+lng;
                        landmarkText.setText(s);
                        showOnMap.setVisibility(View.VISIBLE);
                    }})
                        .addOnFailureListener(new OnFailureListener() {
                            public void onFailure(Exception e) {
                                // Processing logic for recognition failure.
                                // Recognition failure.
                                try {
                                    MLException mlException = (MLException)e;
                                    // Obtain the result code. You can process the result code and customize respective messages displayed to users.
                                    int errorCode = mlException.getErrCode();
                                    // Obtain the error information. You can quickly locate the fault based on the result code.
                                    String errorMessage = mlException.getMessage();
                                    Log.d("landmark onfail",errorMessage);
                                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                } catch (Exception error) {
                                    // Handle the conversion error.
                                    Log.d("landmark error",e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.landmark_fragment, container, false);

        landmarkButton=v.findViewById(R.id.landmarkButton);
        landmarkImage=v.findViewById(R.id.landamarkImage);
        landmarkText=v.findViewById(R.id.recResult);
        showOnMap=v.findViewById(R.id.landmarkMap);

        MLApplication.getInstance().setApiKey("CgB6e3x9sRVyS0oZJ74m/s8RFTewbGU7JWGO7J49f+UL8yKI3WvAjr2B+ze4LAmOKWSrLqZmgljI8ybqo8SA5pbk");

        landmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=null;
                try{

                    if(isGMS()) {
                        intent=new Intent(getActivity(), GMapActivity.class);
                    } else if(isHMS()) {
                        intent=new Intent(getActivity(), MapActivity.class);
                    }


                    intent.putExtra("lat",lat);
                    intent.putExtra("lng",lng);
                    Log.i("duh",lat+"");
                    Log.i("duh",lng+"");
                    getActivity().startActivity(intent);
                }catch (Exception e){
                    Log.i("duh",e.toString());
                }
            }
        });

        return v;
    }



}