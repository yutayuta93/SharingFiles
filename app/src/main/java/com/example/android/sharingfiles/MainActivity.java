package com.example.android.sharingfiles;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Pick From:"),0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnIntent){
        if(requestCode == 0 && resultCode == RESULT_OK){
            ImageView iv = (ImageView)findViewById(R.id.image);

            Uri resultUri = returnIntent.getData();
            iv.setImageURI(resultUri);
            ParcelFileDescriptor mPFD = null;
            try {
                mPFD = getContentResolver().openFileDescriptor(resultUri,"r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileDescriptor descriptor = mPFD.getFileDescriptor();

            Cursor returnCursor = getContentResolver().query(resultUri,null,null,null,null);
            TextView titleText = (TextView)findViewById(R.id.title);
            TextView sizeText = (TextView)findViewById(R.id.size);
            TextView typeText  = (TextView) findViewById(R.id.type);

            int titleIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            titleText.setText(returnCursor.getString(titleIndex));
            sizeText.setText(Long.toString(returnCursor.getLong(sizeIndex)));
            typeText.setText(getContentResolver().getType(resultUri));

        }
    }
}
