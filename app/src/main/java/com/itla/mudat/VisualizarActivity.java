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


        Bundle b = getIntent().getExtras();
        String value = "";

        if(b != null)
            value = b.getString(MainActivity.NOMBRE);

        TextView resultadoTextView = (TextView) findViewById(R.id.resultadoTextView);
        resultadoTextView.setText(value);
    }
}
