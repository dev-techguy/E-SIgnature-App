package com.mvtechzone.signaturepad;

import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.Random;
import java.util.UUID;

public class SignaturePad extends AppCompatActivity {
    com.github.gcacace.signaturepad.views.SignaturePad mSignaturePad;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_pad);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = mSignaturePad.getSignatureBitmap();
                saveTempBitmap(bitmap);
                Toast.makeText(SignaturePad.this,
                        R.string.bitmap, Toast.LENGTH_LONG).show();
            }
        });

        mSignaturePad = findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                Toast.makeText(SignaturePad.this,
                        R.string.onStartSigning, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                Toast.makeText(SignaturePad.this,
                        R.string.onSigned, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                Toast.makeText(SignaturePad.this,
                        R.string.onClear, Toast.LENGTH_LONG).show();
            }
        });
    }


    public void saveTempBitmap(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            saveImage(bitmap);
        }  //prompt the user or do something

    }

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        Log.i("TAG", "Generated UUID " + randomUUIDString);

        String fname = "Bitmap_" + randomUUIDString + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
