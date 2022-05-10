package com.example.hw2updated.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw2updated.R;
import com.example.hw2updated.callbacks.CallBack_ListRecordClicked;
import com.example.hw2updated.data.Record;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Record> records;
    private Context context;
    private CallBack_ListRecordClicked callBack_listRecordClicked;

    public MyAdapter(Context context, List<Record> records, CallBack_ListRecordClicked callBack_listRecordClicked) {
        this.context = context;
        this.records = records;
        this.callBack_listRecordClicked = callBack_listRecordClicked;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.listView_LBL_Name.setText(records.get(holder.getAdapterPosition()).getName());
        holder.listView_LBL_Time.setText(records.get(holder.getAdapterPosition()).getTime());
        holder.listView_LBL_Score.setText(String.valueOf(records.get(holder.getAdapterPosition()).getScore()));
        holder.recycler_view_item.setOnClickListener(
                view -> callBack_listRecordClicked.recordClicked(
                        records.get(holder.getAdapterPosition()).getLatLng().latitude,
                        records.get(holder.getAdapterPosition()).getLatLng().longitude));

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView listView_LBL_Name;
        private MaterialTextView listView_LBL_Score;
        private MaterialTextView listView_LBL_Time;
        private LinearLayoutCompat recycler_view_item;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            listView_LBL_Name = itemView.findViewById(R.id.listView_LBL_Name);
            listView_LBL_Score = itemView.findViewById(R.id.listView_LBL_Score);
            listView_LBL_Time = itemView.findViewById(R.id.listView_LBL_Time);
            recycler_view_item = itemView.findViewById(R.id.recycler_view_item);
        }
    }
}