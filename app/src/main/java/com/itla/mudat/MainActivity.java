package com.itla.mudat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public final static String NOMBRE = "nombre";
    EditText nombreEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreEditText = (EditText) findViewById(R.id.nombreEditText);
    }

    public void nombreButton_Click(View view) {
        if(nombreEditText.getText().length() <= 0){
            nombreEditText.setError("Valor Obligatorio....");
            return;
        }

        Intent intent = new Intent(this, VisualizarActivity.class);

        Bundle b = new Bundle();
        b.putString(MainActivity.NOMBRE, nombreEditText.getText().toString());
        intent.putExtras(b);

        startActivity(intent);
    }

    public void limpiarButton_Click(View view) {
        nombreEditText.setText("");
        Toast.makeText(this, "Limpio...", Toast.LENGTH_SHORT).show();
    }
}
