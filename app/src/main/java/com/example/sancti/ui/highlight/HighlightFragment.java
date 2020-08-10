package com.example.sancti.ui.highlight;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.adapters.HighlightsAdapter;
import com.example.sancti.classes.Highlight;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class HighlightFragment extends Fragment {

    public FloatingActionButton fab;
    public PopupWindow popWindow;
    int mDeviceHeight = 0;
    public ImageView image;
    public SQLiteDatabase tourismBase;
    File mypath;
    RecyclerView highlightRecycler;
    HighlightsAdapter highlightAdapt;
    ArrayList<Highlight> highlightsList;
    int id;

    public void getImage(View view) throws IOException {
        ImageView v = (ImageView) view;
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
                image.setImageBitmap(selectedImage);

                ContextWrapper cw = new ContextWrapper(getContext());
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
                } catch (Exception e) {
                    Log.e("SAVE_IMAGE", e.getMessage(), e);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void onShowPopup(View v) {
        // inflate the custom popup layout
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatedView = layoutInflater.inflate(R.layout.popup_add_highlight, null, false);
        ;

        final EditText title = inflatedView.findViewById(R.id.highlightTitle);
        final EditText description = inflatedView.findViewById(R.id.highlightDescription);
        image = inflatedView.findViewById(R.id.highlightImage);
        Button addBtn = inflatedView.findViewById(R.id.addBtn);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getImage(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String highlightTitle = title.getText().toString();
                String highlightDescription = description.getText().toString();
                String highlightImage = mypath.getAbsolutePath();

                try {
                    tourismBase.execSQL("Insert into Highlights " + "(title, description, image)" + "  Values ('" + highlightTitle + "', '" + highlightDescription + "', '" + highlightImage + "');");
                    Log.d("success!", "insertion success!");

                    Fragment frg = getFragmentManager().findFragmentById(id);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(frg);
                    ft.attach(frg);
                    ft.commit();
                    popWindow.dismiss();
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                    Log.d("Exception", "insertion failed");
                }
            }
        });

        // get device size
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        mDeviceHeight = size.y;
        // set height depends on the device size
        popWindow = new PopupWindow(inflatedView, size.x - 50, size.y - 400, true);
        // set a background drawable with rounders corners
        // make it focusable to show the keyboard to enter in `EditText`
        popWindow.setFocusable(true);
        // make it outside touchable to dismiss the popup window
        popWindow.setOutsideTouchable(true);
        //animation
        popWindow.setAnimationStyle(R.style.PopupAnimation);
        // show the popup at bottom of the screen and set some margin at bottom ie,
        popWindow.showAtLocation(v, Gravity.CENTER, 0, 100);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_highlight, container, false);
        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopup(view);
            }
        });
        tourismBase = getActivity().openOrCreateDatabase("data", getActivity().MODE_PRIVATE, null);
        tourismBase.execSQL("CREATE TABLE IF NOT EXISTS Highlights(id integer primary key AUTOINCREMENT, title VARCHAR, description VARCHAR, image VARCHAR);");
        highlightsList = new ArrayList<>();

        Cursor resultSet = tourismBase.rawQuery("Select * from Highlights", null);
        if (resultSet != null && resultSet.moveToFirst()) {
            do {
                highlightsList.add(new Highlight(resultSet.getInt(0), resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            } while (resultSet.moveToNext());
        }
        id= this.getId();
        highlightRecycler = v.findViewById(R.id.highlightRecycler);
        highlightAdapt = new HighlightsAdapter(getContext(), highlightsList);
        highlightRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        highlightRecycler.setAdapter(highlightAdapt);
        return v;
    }
}