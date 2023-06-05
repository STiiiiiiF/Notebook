package com.example.mynotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mynotebook.adapter.ListItem;
import com.example.mynotebook.adapter.MainAdapter;
import com.example.mynotebook.dataBase.AppExecutor;
import com.example.mynotebook.dataBase.MyDataBaseManager;
import com.example.mynotebook.dataBase.OnDataReceived;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataReceived {
    private MyDataBaseManager myDataBaseManager;
    private EditText idTitle;
    private EditText idDescription;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                readFromDataBase(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void init() {

        myDataBaseManager = new MyDataBaseManager(this);
        idTitle = findViewById(R.id.edTitle);
        idDescription = findViewById(R.id.edDescription);
        recyclerView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelper().attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDataBaseManager.openDataBase();
        readFromDataBase("");
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDataBaseManager.closeDataBase();
    }

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapter.removeItem(viewHolder.getAdapterPosition(), myDataBaseManager);

            }
        });
    }

    private void readFromDataBase(String text) {
        AppExecutor.getInstance().getSubIO().execute(() -> myDataBaseManager.getFromDataBase(text, MainActivity.this));
    }

    @Override
    public void onReceived(List<ListItem> list) {
        AppExecutor.getInstance().getMainIO().execute(() -> mainAdapter.updateAdapter(list));

    }
}