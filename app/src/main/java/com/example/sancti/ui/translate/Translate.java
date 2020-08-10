package com.example.sancti.ui.translate;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sancti.R;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.langdetect.MLLangDetectorFactory;
import com.huawei.hms.mlsdk.langdetect.cloud.MLRemoteLangDetector;
import com.huawei.hms.mlsdk.model.download.MLLocalModelManager;
import com.huawei.hms.mlsdk.model.download.MLModelDownloadListener;
import com.huawei.hms.mlsdk.model.download.MLModelDownloadStrategy;
import com.huawei.hms.mlsdk.text.MLLocalTextSetting;
import com.huawei.hms.mlsdk.text.MLRemoteTextSetting;
import com.huawei.hms.mlsdk.text.MLText;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;
import com.huawei.hms.mlsdk.translate.MLTranslateLanguage;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator;
import com.huawei.hms.mlsdk.translate.local.MLLocalTranslateSetting;
import com.huawei.hms.mlsdk.translate.local.MLLocalTranslator;
import com.huawei.hms.mlsdk.translate.local.MLLocalTranslatorModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Translate extends Fragment {

    Bitmap bitmap;
    Button btn;
    TextView tx;
    TextView translated;
    TextView language;
    AutoCompleteTextView languagesTextView;
    String targetLanguage;

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                getText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getText() {
        MLApplication.getInstance().setApiKey("CgB6e3x9sRVyS0oZJ74m/s8RFTewbGU7JWGO7J49f+UL8yKI3WvAjr2B+ze4LAmOKWSrLqZmgljI8ybqo8SA5pbk");
        // Method 2: Use the default parameter settings to automatically detect languages for text recognition. This method is applicable to sparse text scenarios. The format of the returned text box is MLRemoteTextSetting.NGON.
        MLTextAnalyzer analyzer = MLAnalyzerFactory.getInstance().getRemoteTextAnalyzer();
        MLFrame frame = MLFrame.fromBitmap(bitmap);
        Task<MLText> task = analyzer.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<MLText>() {
            @Override
            public void onSuccess(MLText text) {
                // Recognition success.
                Toast.makeText(getContext(), "read text SUCCESS", Toast.LENGTH_LONG).show();
                tx.setText(text.getStringValue());
                detectTextLanguage(text.getStringValue());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // If the recognition fails, obtain related exception information.
                Toast.makeText(getContext(), "read text ERROR", Toast.LENGTH_LONG).show();

                try {
                    MLException mlException = (MLException)e;
                    // Obtain the result code. You can process the result code and customize respective messages displayed to users.
                    int errorCode = mlException.getErrCode();
                    // Obtain the error information. You can quickly locate the fault based on the result code.
                    String errorMessage = mlException.getMessage();
                    tx.setText(errorMessage+"   "+errorCode);
                } catch (Exception error) {
                    // Handle the conversion error.
                }
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

    public void detectTextLanguage(final String detectedText){

        MLApplication.getInstance().setApiKey("CgB6e3x9sRVyS0oZJ74m/s8RFTewbGU7JWGO7J49f+UL8yKI3WvAjr2B+ze4LAmOKWSrLqZmgljI8ybqo8SA5pbk");

        MLRemoteLangDetector mlRemoteLangDetector = MLLangDetectorFactory.getInstance()
                .getRemoteLangDetector();

        Task<String> firstBestDetectTask = mlRemoteLangDetector.firstBestDetect(detectedText);
        firstBestDetectTask.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                // Processing logic for detection success.
                language.setText(s);
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                translateText(detectedText,s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Processing logic for detection failure.
                // Recognition failure.
                try {
                    MLException mlException = (MLException)e;
                    // Obtain the result code. You can process the result code and customize respective messages displayed to users.
                    int errorCode = mlException.getErrCode();
                    // Obtain the error information. You can quickly locate the fault based on the result code.
                    String errorMessage = mlException.getMessage();
                    language.setText(errorMessage+"  "+errorCode);
                } catch (Exception error) {
                    // Handle the conversion error.
                }
            }
        });

        if (mlRemoteLangDetector != null) {
            mlRemoteLangDetector.stop();
        }

    }

    public void translateText(final String myText,String langaugeCode) {

        MLApplication.getInstance().setApiKey("CgB6e3x9sRVyS0oZJ74m/s8RFTewbGU7JWGO7J49f+UL8yKI3WvAjr2B+ze4LAmOKWSrLqZmgljI8ybqo8SA5pbk");
        // Create a text translator using custom parameter settings.
        MLRemoteTranslateSetting setting = new MLRemoteTranslateSetting
                .Factory()
                // Set the source language code. The ISO 639-1 standard is used. This parameter is optional. If this parameter is not set, the system automatically detects the language.
                .setSourceLangCode(langaugeCode)
                // Set the target language code. The ISO 639-1 standard is used.
                .setTargetLangCode(targetLanguage)
                .create();
        MLRemoteTranslator mlRemoteTranslator = MLTranslatorFactory.getInstance().getRemoteTranslator(setting);

        MLTranslateLanguage.getCloudAllLanguages().addOnSuccessListener(
                new OnSuccessListener<Set<String>>() {
                    @Override
                    public void onSuccess(Set<String> result) {
                        // Languages supported by on-cloud translation are successfully obtained.
                    }
                });

        // sourceText: text to be translated, with up to 5000 characters.
        final Task<String> task = mlRemoteTranslator .asyncTranslate(myText);
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String text) {
                // Processing logic for recognition success.
                translated.setText(text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Processing logic for recognition failure.
                try {
                    MLException mlException = (MLException)e;
                    // Obtain the result code. You can process the result code and customize respective messages displayed to users.
                    int errorCode = mlException.getErrCode();
                    // Obtain the error information. You can quickly locate the fault based on the result code.
                    String errorMessage = mlException.getMessage();
                    translated.setText(errorCode+"  "+errorMessage);
                } catch (Exception error) {
                    // Handle the conversion error.
                    translated.setText(error.toString());
                }
            }
        });

        if (mlRemoteTranslator!= null) {
            mlRemoteTranslator.stop();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.translate_fragment, container, false);
        btn = v.findViewById(R.id.translateBtn);
        tx = v.findViewById(R.id.translateTx);
        translated=v.findViewById(R.id.translatedTx);
        language=v.findViewById(R.id.lang);
        languagesTextView=v.findViewById(R.id.targetLanguage);
        String languagesSet="Simplified Chinese, English, French, Arabic, Thai, Spanish, Turkish, Portuguese, Japanese, German, Italian, Russian, Polish, Malay, Swedish, Finnish, Norwegian, Danish, Korean";
        final ArrayList<String> languageList=new ArrayList<String>();
        languageList.addAll(Arrays.asList(languagesSet.split(",")));
        for(String i:languageList){
            i=i.trim();
        }
        final String[] languageCodes=new String[]{"zh","en","fr","ar","th","es","tr","pt","ja","de","it","ru","pl","ms","sv","fi","no","da","ko"};

        final ArrayAdapter<String> langAdapt=new ArrayAdapter(getContext(),android.R.layout.select_dialog_item,languageList);
        languagesTextView.setAdapter(langAdapt);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                targetLanguage=languageCodes[languageList.indexOf(languagesTextView.getText().toString())];
                Log.i("target language",languagesTextView.getText().toString());
                Log.i("target language code",targetLanguage);
                selectImage();
            }
        });

        return v;
    }
}
