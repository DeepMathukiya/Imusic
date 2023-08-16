package com.example.imusic;

import android.Manifest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ListView ls;
TextView tx;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ls = findViewById(R.id.listView);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (!Environment.isExternalStorageManager()) {
                specialPermission();
            }

        }else{
            permission();

        }

                        ArrayList<File> songs =  fetchsong(Environment.getExternalStorageDirectory());
                        String []items =  new String[songs.size()];
                        for (int i = 0 ; i<songs.size() ; i++){
                            items[i] = songs.get(i).getName().replace(".mp3" , "");
                        }
                    ArrayAdapter<String> ad = new ArrayAdapter<>(MainActivity.this , android.R.layout.simple_list_item_1,items);
                    ls.setAdapter(ad);
                ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                            String currentSong = ls.getItemAtPosition(i).toString();
                            Intent i1 = new Intent(MainActivity.this , play.class );
                            i1.putExtra("Songs",  songs);
                            i1.putExtra("Position" , i);
                            i1.putExtra("currentSong", currentSong);
                            startActivity(i1);
                        }
                    });



    }
    public ArrayList<File> fetchsong(File file){
        ArrayList arrayList = new ArrayList();
        File [] songs = file.listFiles();
        if(songs != null) {
            for (File myFile : songs) {
                if (!myFile.isHidden() && myFile.isDirectory()) {
                    arrayList.addAll(fetchsong(myFile));
                } else {
                    if (myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")) {
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }
    public void specialPermission(){


        try {

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
        catch (Exception e){

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(intent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                specialPermission();
            }
        }
    }
public void permission(){
    Dexter.withContext(MainActivity.this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                }
                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    permission();
                }
                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            })
            .check();

}

}
