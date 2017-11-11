package com.itla.mudat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText nombreEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreEditText = (EditText) findViewById(R.id.nombreEditText);
    }

    public void nombreButton_Click(View view) {
        TextView textView = (TextView) findViewById(R.id.resultadoTextView);
        textView.setText(nombreEditText.getText());
        Toast.makeText(this, "Listo...", Toast.LENGTH_SHORT).show();
    }

    public void limpiarButton_Click(View view) {
        nombreEditText.setText("");
        Toast.makeText(this, "Limpio...", Toast.LENGTH_SHORT).show();
    }
}
