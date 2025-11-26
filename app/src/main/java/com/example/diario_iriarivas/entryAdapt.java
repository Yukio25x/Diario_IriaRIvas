package com.example.diario_iriarivas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class entryAdapt extends RecyclerView.Adapter<entryAdapt.ViewHolder> {

    private List<entry> list;
    private static final int type_list = 0;
    private static final int type_grid = 1;

    private int currentType = type_list;

    public entryAdapt(List<entry> list) {
        this.list = list;
    }

    public void showListLayout() {
        currentType = type_list;
        notifyDataSetChanged();
    }

    public void showGridLayout() {
        currentType = type_grid;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return currentType;
    }

    //datos que va a recoger cada tarjeta
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate;
        ImageView imgMood;
        CheckBox ckbFavourite;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgMood = itemView.findViewById(R.id.imgMood);
            ckbFavourite = itemView.findViewById(R.id.ckbFavourite);
        }
    }

    //apariencia de la vista
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == type_grid) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.entry_grid, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.entry_list, parent, false);
        }
        return new ViewHolder(v);
    }

    //agregar datos a la lista
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        entry e = list.get(position);

        holder.tvTitle.setText(e.getTitle());
        holder.tvDate.setText(e.getDate());

        holder.imgMood.setImageResource(e.getMoodImage());

        holder.ckbFavourite.setChecked(e.isFavourite());
        holder.ckbFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                e.setFavourite(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<entry> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }
}
