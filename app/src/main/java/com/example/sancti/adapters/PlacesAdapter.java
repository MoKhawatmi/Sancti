package com.example.sancti.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sancti.MapActivity;
import com.example.sancti.R;
import com.huawei.hms.site.api.model.AddressDetail;
import com.huawei.hms.site.api.model.Site;

import java.util.ArrayList;
import java.util.Arrays;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder>{

    ArrayList<Site> items;
    Context context;


    public PlacesAdapter(Context context,ArrayList<Site> items){
        this.context=context;
        this.items=items;
    }

      class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,address;
        Button mapButton;
        public MyViewHolder(@NonNull View v) {
            super(v);
            title=v.findViewById(R.id.placeTitle);
            address=v.findViewById(R.id.placeAddress);
            mapButton=v.findViewById(R.id.openMapBtn);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClick(view,getAdapterPosition());
                }
            });
        }
      }


    @Override
    public PlacesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_layout, parent, false);

        return new PlacesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( PlacesAdapter.MyViewHolder holder, int position) {
        AddressDetail detail=items.get(position).getAddress();
        holder.title.setText(items.get(position).getName());
        holder.address.setText(detail.getCountry()+" - "+(detail.getAdminArea()==null?"":detail.getAdminArea())+" - "+(detail.getLocality()==null?"":detail.getLocality())+"\n"+(detail.getThoroughfare()==null?"":detail.getThoroughfare())+" - "+(detail.getStreetNumber()==null?"":detail.getStreetNumber()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void buttonClick(View view,int pos){
        try{
            Log.i("duh",pos+"");
            Intent intent=new Intent(context, MapActivity.class);
            intent.putExtra("lat",items.get(pos).getLocation().getLat());
            intent.putExtra("lng",items.get(pos).getLocation().getLng());
            Log.i("duh",items.get(pos).getLocation().getLat()+"");
            Log.i("duh",items.get(pos).getLocation().getLng()+"");
            context.startActivity(intent);
        }catch (Exception e){
            Log.i("duh",e.toString());
        }

    }

}

