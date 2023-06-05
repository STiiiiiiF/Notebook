package com.example.mynotebook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mynotebook.adapter.ListItem;
import com.example.mynotebook.dataBase.AppExecutor;
import com.example.mynotebook.dataBase.MyConstants;
import com.example.mynotebook.dataBase.MyDataBaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EditActivity extends AppCompatActivity {

    private final int PICK_IMAGE_CODE = 123;
    private ImageView imNewImage;
    private ConstraintLayout imageContainer;
    private ImageButton imEditImage, imDeleteImage;
    private FloatingActionButton fbAddImage;
    private String tempUri = "empty";
    private boolean isEditState = true;
    private EditText edTitle;
    private EditText edDescription;
    private MyDataBaseManager myDataBaseManager;
    private ListItem item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getMyIntents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDataBaseManager.openDataBase();
    }


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            tempUri = intent.getData().toString();
                            imNewImage.setImageURI(intent.getData());
                            getContentResolver().takePersistableUriPermission(intent.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                }
            });


    private void init() {
        myDataBaseManager = new MyDataBaseManager(this);
        edTitle = findViewById(R.id.edTitle);
        imNewImage = findViewById(R.id.imNewImage);
        imageContainer = findViewById(R.id.imageContainer);
        fbAddImage = findViewById(R.id.fbAddImage);
        edDescription = findViewById(R.id.edDescription);
        imEditImage = findViewById(R.id.imEditImage);
        imDeleteImage = findViewById(R.id.imDeleteImage);
    }

    private void getMyIntents() {
        Intent intent = getIntent();
        if (intent != null) {
            item = (ListItem) intent.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);

            isEditState = intent.getBooleanExtra(MyConstants.EDIT_STATE, true);
            if (!isEditState) {
                edTitle.setText(item.getTitle());
                edDescription.setText(item.getDescription());
                if (!item.getUri().equals("empty")) {
                    tempUri = item.getUri();
                    imageContainer.setVisibility(View.VISIBLE);
                    imNewImage.setImageURI(Uri.parse(item.getUri()));
                    imEditImage.setVisibility(View.GONE);
                    imDeleteImage.setVisibility(View.GONE);
                }
            }
        }
    }

    public void onClickSave(View view) {
        String title = edTitle.getText().toString();
        String description = edDescription.getText().toString();

        if (title.equals("") || description.equals("")) {
            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();
        } else {
            if (isEditState) {
                AppExecutor.getInstance().getSubIO().execute(() -> myDataBaseManager.insertToDataBAse(title, description, tempUri));
                Toast.makeText(this, R.string.text_save, Toast.LENGTH_SHORT).show();
            } else {
                myDataBaseManager.updateFromDataBase(title, description, tempUri, item.getId());
                Toast.makeText(this, R.string.text_save, Toast.LENGTH_SHORT).show();
            }
            myDataBaseManager.closeDataBase();
            finish();
        }
    }

    public void onClickDeleteImage(View view) {
        imNewImage.setImageResource(R.drawable.baseline_image_defoult);
        tempUri = "empty";
        imageContainer.setVisibility(view.GONE);
        fbAddImage.setVisibility(View.VISIBLE);

    }

    public void onClickAddImage(View view) {
        imageContainer.setVisibility(view.VISIBLE);
        view.setVisibility(View.GONE);

    }

    public void onClickChooseImage(View view) {
        Intent chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooser.setType("image/*");
        mStartForResult.launch(chooser);


    }


}