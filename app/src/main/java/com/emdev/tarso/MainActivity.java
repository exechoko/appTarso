package com.emdev.tarso;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_utilidades, R.id.nav_radio)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        
        //crearCarpeta();
    }

    /*private void crearCarpeta() {
        //crear carpeta en la raíz del almacenamiento interno
        File ruta_sd = Environment.getExternalStorageDirectory();
        File carpeta = new File(ruta_sd.getAbsolutePath(), "miCarpeta");

        //comprobar si la carpeta no existe, entonces crearla
        if(!carpeta.exists()) {
            carpeta.mkdirs(); // creará la carpeta en la ruta indicada al inicializar el objeto File
            if(carpeta.mkdirs())
                Toast.makeText(getApplicationContext(), "Carpeta creada : " + carpeta.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            //se ha creado la carpeta;
        }else
        {
            //la carpeta ya existe
            Toast.makeText(getApplicationContext(), "Carpeta existente : " + carpeta.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }

        Log.d("carpeta creada", carpeta.getAbsolutePath());

        //crear un archivo de texto en la carpeta creada
        try
        {
            File archivotxt = new File(carpeta, "miTexto.txt");
            FileWriter writer = new FileWriter(archivotxt);
            writer.append("Este es el contenido de mi archivo de texto.");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Archivo guardado", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            //mostrar en el Logcat el error
            Log.d("Error al crear archivo",e.getStackTrace().toString());
        }

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_noticias:
                //Hacer algo cuando presionen noticias
                //Toast.makeText(this, "IR A PANTALLA CON NOTICIAS", Toast.LENGTH_SHORT).show();
                Intent irNoticias = new Intent(MainActivity.this, NoticiasActivity.class);
                startActivity(irNoticias);
                break;

            case R.id.salir:
                //salir
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}