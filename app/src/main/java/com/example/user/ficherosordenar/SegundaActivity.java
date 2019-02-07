package com.example.user.ficherosordenar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SegundaActivity extends AppCompatActivity {

    private TextView tv;
    private EditText et;
    private String texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        tv = (TextView) findViewById(R.id.tv);
        et = (EditText) findViewById(R.id.et);

        Bundle b = getIntent().getExtras();

        texto = b.getString("archivo");

        tv.setText(texto);

        File fichero = new File(texto);

        try {
            InputStreamReader archivo = new InputStreamReader(openFileInput(fichero.getName()));
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            String todo = "";
            while (linea != null) {
                todo = todo + linea + "\n";
                linea = br.readLine();
            }

            br.close();
            archivo.close();
            et.setText(todo);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "No se ha podido cargar el fichero", Toast.LENGTH_LONG).show();
        }
    }

    public void guardar(View v) {
        Toast t = null;
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(texto, Activity.MODE_PRIVATE));
            archivo.write(et.getText().toString());
            archivo.flush();
            archivo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            t = Toast.makeText(this, "Se produjo un error al guardar los datos", Toast.LENGTH_LONG);
            t.show();
        }

        t = Toast.makeText(this, "Los datos se guardaron correctamente", Toast.LENGTH_LONG);
        t.show();

        SharedPreferences preferences1 = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.putString("nombre", tv.getText().toString());
        editor.commit();

        Toast.makeText(getApplicationContext(), "El fichero " + tv.getText().toString() + " fué modificado", Toast.LENGTH_LONG).show();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}