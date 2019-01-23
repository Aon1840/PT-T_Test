package com.example.ptt_test_mobile.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ptt_test_mobile.Adapter.ImageListAdapter;
import com.example.ptt_test_mobile.Database.DatabaseHelper;
import com.example.ptt_test_mobile.Model.Image;
import com.example.ptt_test_mobile.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<Image> list;
    ImageListAdapter adapter = null;
//    ImageView imageView;
    FloatingActionButton btnAdd;
    final int REQUEST_CODE_GALLERY = 999;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstance();
        databaseHelper = new DatabaseHelper(this, "PT-T_Database.db", null, 1);

//        databaseHelper = new DatabaseHelper(MainActivity.this);
    }

    private void initInstance() {
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
//        imageView = (ImageView) findViewById(R.id.imageView);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new ImageListAdapter(MainActivity.this,R.layout.image_items,list);
        gridView.setAdapter(adapter);

        int userId;
        Bundle extras = getIntent().getExtras();
        userId = extras.getInt("USER_ID");
        Log.d("TAG2222--------","User_id is ........ "+userId);
        String sql = "SELECT * FROM IMAGE WHERE user_id = "+userId;
        Cursor cursor = databaseHelper.getData(sql);
        list.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            byte[] image = cursor.getBlob(1);
            int uid = cursor.getInt(2);

            list.add(new Image(id,image,uid));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                int userId;
                Bundle extras = getIntent().getExtras();
                userId = extras.getInt("USER_ID");
                databaseHelper.addPhoto(imageViewToByte(bitmap),userId);
//                try{
//                    databaseHelper.addImage(
//                            imageViewToByte(bitmap),
//                            1
//                    );
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static byte[] imageViewToByte(Bitmap bitmap) {
//        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
