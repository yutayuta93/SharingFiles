package com.example.android.sharingfiles;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //パーミッションの確認
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission
                    .WRITE_EXTERNAL_STORAGE},0);
        }

        Button btn_FileSelect = (Button)findViewById(R.id.btn_fileSelect);
        btn_FileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Pick From:"),0);
            }
        });
        Button btn_Document = (Button) findViewById(R.id.btn_document);
        btn_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent,1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent){
        if(requestCode == 0 && resultCode == RESULT_OK) {
            ImageView iv = (ImageView) findViewById(R.id.image);

            Uri resultUri = returnIntent.getData();
            iv.setImageURI(resultUri);
            ParcelFileDescriptor mPFD = null;
            try {
                mPFD = getContentResolver().openFileDescriptor(resultUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileDescriptor descriptor = mPFD.getFileDescriptor();

            Cursor returnCursor = getContentResolver().query(resultUri, null, null, null, null);
            TextView titleText = (TextView) findViewById(R.id.title);
            TextView sizeText = (TextView) findViewById(R.id.size);
            TextView typeText = (TextView) findViewById(R.id.type);

            int titleIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            titleText.setText(returnCursor.getString(titleIndex));
            sizeText.setText(Long.toString(returnCursor.getLong(sizeIndex)));
            typeText.setText(getContentResolver().getType(resultUri));
        }
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri resultUri = returnIntent.getData();
            ImageView iv = (ImageView)findViewById(R.id.image);
            iv.setImageURI(resultUri);
        }
    }
}
