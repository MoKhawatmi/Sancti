package com.example.sancti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sancti.adapters.LogsAdapter;
import com.example.sancti.classes.JournalLog;
import com.example.sancti.interfaces.OnIntentReceived;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TripActivity extends AppCompatActivity {
    int tripId;
    public static String tableTitle;
    ArrayList<JournalLog> logs;
    RecyclerView logsRecycler;
    SQLiteDatabase tourismBase;
    EditText logText;
    ImageView logImage;
    Button logButton;
    File mypath;
    Bitmap selectedImage;
    LogsAdapter adapter;
    private OnIntentReceived mIntentListener;

    public void addLog(View view){

        JournalLog log=null;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(calendar.getTime());

        if(selectedImage!=null) {
            ContextWrapper cw = new ContextWrapper(this);
            File directory = cw.getDir("highlights", Context.MODE_PRIVATE);
            if (!directory.exists()) {
                directory.mkdir();
            }
            int fileName = new Random().nextInt(10000000);
            mypath = new File(directory, fileName + ".png");


            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Log.d("path is ", mypath.getAbsolutePath());
                fos.close();

                tourismBase.execSQL("Insert into '"+tableTitle+"' (logText,logImage,logDate) Values ('" + logText.getText().toString() + "', '" + mypath.getAbsolutePath() + "', '" + date+ "');");
                Cursor resultSet = tourismBase.rawQuery("Select id from '"+tableTitle+"';",null);
                resultSet.moveToLast();
                int id = resultSet.getInt(0);
                log=new JournalLog(id,logText.getText().toString(),mypath.getAbsolutePath(),date+"");
            } catch (Exception e) {
                Log.e("SAVE_IMAGE", e.getMessage(), e);
            }
        }else{
            tourismBase.execSQL("Insert into '"+tableTitle+"' (logText,logImage,logDate) Values ('" + logText.getText().toString() + "', '" + null + "', '" + date+ "');");
            Cursor resultSet = tourismBase.rawQuery("Select id from '"+tableTitle+"';",null);
            resultSet.moveToLast();
            int id = resultSet.getInt(0);
            log=new JournalLog(id,logText.getText().toString(),null,date+"");
        }

        logs.add(log);
        adapter.notifyDataSetChanged();
        logText.setText("");

        InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(logText.getWindowToken(),0);
        mypath=null;
        selectedImage=null;
        logImage.setImageResource(R.drawable.cam);
    }

    public void addImage(View view) throws IOException {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if(reqCode==1){
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                logImage.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        }else{

            if (resultCode == RESULT_OK) {
                if (mIntentListener != null) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        data.putExtra("image",byteArray);
                        mIntentListener.onIntent(data, resultCode);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Intent intent=getIntent();
        tableTitle=intent.getStringExtra("TripTitle");
        this.getSupportActionBar().setTitle(tableTitle);
        tableTitle=tableTitle.replaceAll("\\s","_");
        tripId=intent.getIntExtra("TripId",-1);
        Log.d("tripId",tripId+"");


        logText=findViewById(R.id.logText);
        logImage=findViewById(R.id.logImage);
        logButton=findViewById(R.id.logButton);

        tourismBase=openOrCreateDatabase("data", MODE_PRIVATE, null);
        logs=new ArrayList<JournalLog>();

        Cursor resultSet = tourismBase.rawQuery("Select * from '"+tableTitle+"';",null);
        if (resultSet != null && resultSet.moveToFirst()){
            do {
                logs.add(new JournalLog(resultSet.getInt(0),resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
            } while (resultSet.moveToNext());
        }





        logsRecycler=findViewById(R.id.logs);
        adapter=new LogsAdapter(this,logs);
        logsRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        logsRecycler.setAdapter(adapter);


        logText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(logText.getText().toString().length()==0){
                    logButton.setEnabled(false);
                }else{
                    logButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}