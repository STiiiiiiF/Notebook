package com.example.mynotebook.dataBase;

import com.example.mynotebook.adapter.ListItem;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ListItem> list);
}
