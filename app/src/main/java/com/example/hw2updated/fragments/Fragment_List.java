package com.example.hw2updated.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw2updated.R;
import com.example.hw2updated.callbacks.CallBack_ListRecordClicked;
import com.example.hw2updated.data.Record;
import com.example.hw2updated.data.RecordsDao;
import com.example.hw2updated.utils.MyAdapter;

import java.util.List;

public class Fragment_List extends Fragment {

    private RecyclerView recyclerView;
    private List<Record> records;
    private RecordsDao recordsDao;


    private CallBack_ListRecordClicked callBack_listRecordClicked;

    public Fragment_List(CallBack_ListRecordClicked callBack_listRecordClicked){
        this.callBack_listRecordClicked = callBack_listRecordClicked;
        recordsDao = new RecordsDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        readRecords();
        setRecyclerView(view);

        return view;
    }


    /**
     * set the recycler view
     * @param view the inflated view
     */
    private void setRecyclerView(View view) {
        MyAdapter myAdapter = new MyAdapter(view.getContext(), records, callBack_listRecordClicked);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager (view.getContext()));
    }

    /**
     * reads all the records data
     */
    private void readRecords() {
        records = recordsDao.readRecords();
        records.sort(null);

    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);

    }

}
