package com.example.sancti.ui.objectName;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sancti.R;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.objects.MLObject;
import com.huawei.hms.mlsdk.objects.MLObjectAnalyzer;
import com.huawei.hms.mlsdk.objects.MLObjectAnalyzerSetting;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ObjectName extends Fragment {

    Button btn;
    ImageView img;
    TextView name;
    TextView translation;

    public void getImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Uri path = data.getData();
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                img.setImageBitmap(selectedImage);
                detectObject(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void detectObject(Bitmap bitmap){

        MLObjectAnalyzerSetting setting = new MLObjectAnalyzerSetting.Factory()
                .setAnalyzerType(MLObjectAnalyzerSetting.TYPE_PICTURE)
                .allowMultiResults()
                .allowClassification()
                .create();
        MLObjectAnalyzer analyzer = MLAnalyzerFactory.getInstance().getLocalObjectAnalyzer(setting);

        MLFrame frame = MLFrame.fromBitmap(bitmap);

        // Create a task to process the result returned by the object detector.
        final Task<List<MLObject>> task = analyzer.asyncAnalyseFrame(frame);
// Asynchronously process the result returned by the object detector.
        task.addOnSuccessListener(new OnSuccessListener<List<MLObject>>() {
            @Override
            public void onSuccess(List<MLObject> objects) {
                // Detection success.
                Log.i("TAG","detection success");
                String s="";
                for(MLObject i:objects){
                    Log.i("TAG", String.valueOf(i.getTypeIdentity()));
                    s+=i.toString();
                }
                name.setText(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Detection failure.
                Log.i("TAG","detection fail  "+e.toString());
                name.setText(e.toString());
            }
        });

        if (analyzer != null) {
            try {
                analyzer.stop();
            } catch (IOException e) {
                // Exception handling.
            }
        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.object_name_fragment, container, false);

            btn=v.findViewById(R.id.objectNameBtn);
            img=v.findViewById(R.id.objectImage);
            name=v.findViewById(R.id.objectName);
            translation=v.findViewById(R.id.objectTranslation);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getImage();
                }
            });


        return v;
    }


}