package com.example.android.sharingfiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

public class FileSelectActivity extends AppCompatActivity {
    private Intent intent = new Intent("com.example.android.sharingfiles.ACTION_RETURN_FILE");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileselect);

        ImageView imageView = (ImageView)findViewById(R.id.image);
        Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.android_image);
        imageView.setImageURI(imageUri);

        //Create file
        File imagePath = new File(getExternalFilesDir(null),"shareimages");
        if(!imagePath.isDirectory()){
            imagePath.mkdirs();
        }
        File newfile = new File(imagePath,"1mage_1.jpg");
        FileOutputStream fos;
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            fos = new FileOutputStream(newfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);
            fos.close();
        }catch(Exception e){

        }
        Log.d("FILE",newfile.toString());

        Uri fileUri = FileProvider.getUriForFile(this,"com.example.android.sharingfiles" +
                ".fileprovider",newfile);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri,getContentResolver().getType(fileUri));

        //このアクティビティを呼んだアクティビティに、OKフラグとともに、intentが返される

        setResult(RESULT_OK,intent);

    }

    public void select(View view){
        finish();
    }
}
