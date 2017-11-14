package com.itla.mudat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VisualizarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        Intent intent = getIntent();
        String value = "";

        if(intent.getExtras() != null)
            value = intent.getExtras().getString(MainActivity.NOMBRE);

        TextView resultadoTextView = (TextView) findViewById(R.id.resultadoTextView);
        resultadoTextView.setText(value);
    }
}
