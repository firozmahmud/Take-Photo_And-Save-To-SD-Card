package com.example.firoz.cameraapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;
    private Button button;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
    }


    public void photoButton(View view) {


        if (flag == 0) {
            selectPhoto();
        } else if (flag == 1) {
            savePhoto();
        }

    }

    private void savePhoto() {

        if (Build.VERSION.SDK_INT >= 23) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                // we have permission
                save();

            } else {

                // ask permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);

            }

        } else {

            save();
        }


    }

    private void save() {

        String photoName = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_sss").format(new Date());

        String root = Environment.getExternalStorageDirectory().toString();
        File folder = new File(root + "/MyPic");
        folder.mkdirs();

        File filePhoto = new File(folder, photoName + ".png");


        try {
            FileOutputStream stream = new FileOutputStream(filePhoto);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

            flag = 0;

            button.setText("Take Photo");

            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void selectPhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 12);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {


            bitmap = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(bitmap);

            flag = 1;
            button.setText("Save Photo");

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            save();

        } else {
            Toast.makeText(this, "You don't have permission", Toast.LENGTH_SHORT).show();
        }


    }
}
