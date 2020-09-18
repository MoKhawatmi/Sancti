package com.example.sancti.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sancti.R;
import com.example.sancti.classes.JournalLog;
import com.example.sancti.interfaces.OnIntentReceived;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.example.sancti.TripActivity.tableTitle;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyViewHolder> implements OnIntentReceived {

    ArrayList<JournalLog> items;
    Context context;
    SQLiteDatabase tourismBase;
    public PopupWindow popWindow;
    File mypath;
    ImageView iv;
    Bitmap bmp;
    boolean flag=false;

    public LogsAdapter(Context context,ArrayList<JournalLog> items){
        this.context=context;
        this.items=items;
    }

     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView text,date;
        ImageView image;
        public MyViewHolder(@NonNull View v) {
            super(v);
            text=v.findViewById(R.id.logText);
            date=v.findViewById(R.id.logDate);
            image=v.findViewById(R.id.logImage);
            tourismBase=context.openOrCreateDatabase("data", context.MODE_PRIVATE, null);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, items.get(getAdapterPosition()).getId()+"", Toast.LENGTH_LONG).show();
        }

         @Override
         public boolean onLongClick(final View view) {

             PopupMenu popUp=new PopupMenu(context,view);
             popUp.inflate(R.menu.logs_popup_menu);
             popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                 @Override
                 public boolean onMenuItemClick(MenuItem menuItem) {
                     if(menuItem.getItemId()==R.id.deleteLog){
                         delete();
                     }else if(menuItem.getItemId()==R.id.editLog){
                         edit(view);
                     }
                     return false;
                 }
             });
             popUp.show();
             return true;
         }

         public void delete(){
             new AlertDialog.Builder(context)
                     .setIcon(android.R.drawable.ic_dialog_alert)
                     .setTitle("Deleting Log")
                     .setMessage("Are you sure you want to delete this log?")
                     .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                     {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             int id=items.get(getAdapterPosition()).getId();
                             tourismBase.execSQL("DELETE FROM '" + tableTitle + "' WHERE " + "id" + "= '" + id + "'");
                             items.remove(getAdapterPosition());
                             notifyItemRemoved(getAdapterPosition());
                             notifyDataSetChanged();
                             Toast.makeText(context, "Log deleted successfully", Toast.LENGTH_LONG).show();
                         }
                     })
                     .setNegativeButton("No", null)
                     .show();
         }

         public void edit(View v){
             int mDeviceHeight=0;

             LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

             WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

             // inflate the custom popup layout
             final View inflatedView = layoutInflater.inflate(R.layout.edit_log_layout, null,false);

             final EditText et=inflatedView.findViewById(R.id.editLogText);
             iv=inflatedView.findViewById(R.id.editLogImage);
             Button editLog=inflatedView.findViewById(R.id.editLogButton);
             Button editCancel=inflatedView.findViewById(R.id.editLogCancel);



             iv.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     try {
                         //getImage(view);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });

             et.setText(items.get(getAdapterPosition()).getText());
             if(items.get(getAdapterPosition()).getImage()!=null){
                 if(!flag)
                 iv.setImageDrawable(Drawable.createFromPath(items.get(getAdapterPosition()).getImage()));
                 else{
                     iv.setImageBitmap(bmp);
                 }
             }


             // get device size
             Display display = wm.getDefaultDisplay();
             final Point size = new Point();
             display.getSize(size);
             mDeviceHeight = size.y;
             // set height depends on the device size
             popWindow = new PopupWindow(inflatedView, size.x - 50,size.y - 400, true );
             // set a background drawable with rounders corners
             // make it focusable to show the keyboard to enter in `EditText`
             popWindow.setFocusable(true);
             // make it outside touchable to dismiss the popup window
             popWindow.setOutsideTouchable(true);
             //animation
             popWindow.setAnimationStyle(R.style.PopupAnimation);
             // show the popup at bottom of the screen and set some margin at bottom ie,
             popWindow.showAtLocation(v, Gravity.CENTER, 0,100);


             editLog.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     String newText=et.getText().toString();
                     tourismBase.execSQL("UPDATE "+tableTitle+" set logText = '"+newText+"' where id = "+items.get(getAdapterPosition()).getId()+" ;");
                     items.get(getAdapterPosition()).setText(newText);
                     notifyItemChanged(getAdapterPosition());
                     notifyDataSetChanged();
                     popWindow.dismiss();
                 }
             });


             editCancel.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                    popWindow.dismiss();
                 }
             });


         }



         public void getImage(View view) throws IOException {
             ImageView v = (ImageView) view;
             Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
             photoPickerIntent.setType("image/*");
             ((Activity)context).startActivityForResult(photoPickerIntent, 2);
         }

     }

    @NonNull
    @Override
    public LogsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.logs_layout, parent, false);

        return new LogsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogsAdapter.MyViewHolder holder, int position) {
            holder.text.setText(items.get(position).getText());
            holder.date.setText(items.get(position).getDate());
            if(items.get(position).getImage()!=null){
                holder.image.setImageDrawable(Drawable.createFromPath(items.get(position).getImage()));
            }

    }

    @Override
    public void onIntent(Intent i, int resultCode) {
        Log.d("tag", "onIntent: .d");
        byte[] byteArray = i.getByteArrayExtra("image");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        flag=true;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
