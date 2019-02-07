package com.example.user.ficherosordenar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText etFichero;
    private TextView etUltimo;
    private ListView lv;
    private ArrayList<String> textos = new ArrayList<String>();
    File file[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFichero = (EditText) findViewById(R.id.etFichero);
        etUltimo = (TextView) findViewById(R.id.etUltimo);
        lv = (ListView) findViewById(R.id.lv);


        //obtiene ruta donde se encuentran los archivos.
        String path = getFilesDir().getPath() + File.separator;
        File f = new File(path);

        //obtiene nombres de archivos dentro del directorio.
        file = f.listFiles();

        //recorre el directorio.
        for (int i = 0; i < file.length; i++)
        {
            Log.d("Files", "Archivo : " + file[i].getName());

            //Agrega nombres de archivos al list para ser agregado a adapter.
            textos.add(file[i].getName());
        }

        //Crea Adapter
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, textos);

        //Configura Adapter a ListView.
        lv.setAdapter(arrayAdapter);


        SharedPreferences preferences = this.getSharedPreferences("datos", Context.MODE_PRIVATE);

        etUltimo.setText(preferences.getString("nombre", "ningúno"));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), SegundaActivity.class);
                String texto = lv.getItemAtPosition(position).toString();
                i.putExtra("archivo", texto);
                startActivity(i);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int posicion = position;

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Elimina este archivo?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        textos.remove(posicion);
                        //borrachos el archivo
                        for(int i = 0; i < file.length; i++){

                            if(i == posicion){
                                file[i].delete();
                            }
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

                return true;
            }
        });
    }
    //creamos nuestro menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_despliegue, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemAlfa:
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, textos);
                Collections.sort(textos);//sort ordena alfabeticamente
                lv.setAdapter(adapter);
                break;
            case R.id.itemnoAlfa:
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, textos);
                Collections.reverse(textos);//reverse ordena al contrario que lo hace sort
                lv.setAdapter(adapter2);
                break;
        }
        return true;
    }
    public void crear(View v)
    {
        String fich = etFichero.getText().toString() + ".txt";
        File fichero = new File(fich);
        etFichero.setText("");

        textos.add(fich);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item , textos);
        lv.setAdapter(adapter);
    }
}
