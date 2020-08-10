package com.example.sancti.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.R;
import com.example.sancti.classes.Highlight;

import java.io.File;
import java.util.ArrayList;

public class HighlightsAdapter extends RecyclerView.Adapter<HighlightsAdapter.MyViewHolder> {

    ArrayList<Highlight> items;
    Context context;
    SQLiteDatabase tourismBase;
    String imagePath;

    public HighlightsAdapter(Context context,ArrayList<Highlight> items){
            this.context=context;
            this.items=items;
            this.tourismBase=context.openOrCreateDatabase("data", context.MODE_PRIVATE, null);
    }

     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        TextView title,description;
        ImageView image;

        public MyViewHolder(@NonNull View v) {
            super(v);
            title=v.findViewById(R.id.recylcer_highlight_title);
            description=v.findViewById(R.id.recylcer_highlight_description);
            image=v.findViewById(R.id.recylcer_highlight_image);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            final int id=items.get(getAdapterPosition()).getId();
            Log.d("in adapter position",getAdapterPosition()+"");
            Log.d("item long clicked",id+"");



            try{
            Cursor resultSet = tourismBase.rawQuery("Select image from Highlights Where id='"+id+"'",null);
            if (resultSet != null && resultSet.moveToFirst()){
                do {
                    imagePath=resultSet.getString(0);
                } while (resultSet.moveToNext());
            }
            }catch (Exception e){
                Log.d("error reading",e.toString());
            }

            PopupMenu pop=new PopupMenu(context,v);
            pop.inflate(R.menu.highlights_popup_menu);
            pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId()==R.id.delete){
                        Log.d("menu item clicked","delete clicked");
                        try{

                            //delete from database
                            tourismBase.execSQL("DELETE FROM Highlights WHERE id = '" + id + "';");
                            Log.d("db delete success","done");

                            //delete image file
                            try{
                                File fdelete = new File(imagePath);
                                fdelete.delete();
                                Log.d("file delete success","done");
                            }catch (Exception e){
                                Log.d("deletion failed",e.toString());
                            }

                            //delete from arraylist and update adapter
                            items.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyDataSetChanged();
                            Log.d("arr delete success","done");

                        }catch (Exception e){
                            Log.d("error",e.toString());
                        }
                    }
                    return false;
                }
            });
            pop.show();
            return false;
        }
    }


    @Override
    public HighlightsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.highlights, parent, false);

        return new HighlightsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HighlightsAdapter.MyViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.description.setText(items.get(position).getDescription());
        holder.image.setImageDrawable(Drawable.createFromPath(items.get(position).getImage()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
