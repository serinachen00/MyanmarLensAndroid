package com.example.myanmarlensandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button scan,words,favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Below are each buttons that perform action when clicked
        scan = (Button) findViewById(R.id.scans);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openScanActivity();
            }
        });

        words = (Button) findViewById(R.id.words);
        words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openwordsActivity();
            }
        });

        favorites = (Button) findViewById(R.id.favorites);
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfavoritesActivity();
            }
        });

    }


    public void openScanActivity(){

        Intent intent= new Intent(this, ScanActivity.class);
        startActivity(intent);

    }

    public void openwordsActivity(){

        Intent intent= new Intent(this, WordsActivity.class);
        startActivity(intent);

    }

    public void openfavoritesActivity(){

        Intent intent= new Intent(this, FavoritesActivity.class);
        startActivity(intent);

    }
}
