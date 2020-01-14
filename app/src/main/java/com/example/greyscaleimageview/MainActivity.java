package com.example.greyscaleimageview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOError;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    ImageView imageView;
    Uri imageUri;
    Bitmap greyBitmap, imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        OpenCVLoader.initDebug();
    }

    public void OpenGallary(View v){
        Intent myIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(myIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null ){
            imageUri = data.getData();

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void ConvertToGrey(View v){
        Mat RGBA = new Mat();
        Mat GREY = new Mat();
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDither = false;
        o.inSampleSize = 4;
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        greyBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);

        Utils.bitmapToMat(imageBitmap,RGBA);
        Imgproc.cvtColor(RGBA,GREY,Imgproc.COLOR_RGB2GRAY);
        Utils.matToBitmap(GREY,greyBitmap);

        imageView.setImageBitmap(greyBitmap);
    }
}
