package com.example.android.sharingfiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        File imagePath = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_PICTURES);
        if(!imagePath.isDirectory()){
            imagePath.mkdirs();
        }
        File newfile = new File(imagePath,"image_1.jpg");
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
        //MediaStoreへの登録
        addMediaStoreByMediaScanner(newfile);

        //addMediaStoreByIntentの確認　ここから

        //Rawから共有ストレージへ保存
        //Uriの生成
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.image_2);
        //共有ストレージへ保存
        File newFile2 = new File(imagePath,"image_2.jpg");
        FileOutputStream fos2;
        try{
            //Bitmapの生成
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            fos2 = new FileOutputStream(newFile2);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos2);
            fos2.close();
        }catch (IOException e){

        }
        //MediaStoreへの登録
        addMediaStoreByIntent(newFile2);

        //addMediaStoreByIntentの確認　ここまで

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri,getContentResolver().getType(fileUri));

        //このアクティビティを呼んだアクティビティに、OKフラグとともに、intentが返される

        setResult(RESULT_OK,intent);

    }

    public void select(View view){
        finish();
    }

    //MediaStoreへのファイル登録(1)
    private void addMediaStoreByMediaScanner(File file){
        MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, new
                MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("addMediaStoreByScanner",path+" is added to MediaStore");
                    }
                });

    }

    //MediaStoreへのファイル登録(2)
    private void addMediaStoreByIntent(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        this.sendBroadcast(intent);



    }

}
