package com.wposs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView tvMadre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btSuMdre);
        tvMadre = findViewById(R.id.tvSuMadre);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("lalalalal", "*****************************************");
                intent();
            }
        });

        if (getCallingActivity() != null) {
            tvMadre.setText(getCallingActivity().getPackageName());
        }

    }

    private void intent() {
        Intent i = getPackageManager().getLaunchIntentForPackage("com.wposs.bancard");
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(i, 0);

        /*Intent i = new Intent(getApplicationContext(), this.getClass());
        i.setPackage("com.wposs.bancard");
        i.setClassName("com.wposs.bancard", "com.bancard.actividades.StartAppBANCARD");
        startActivityForResult(i, 0);*/
    }

}