package com.example.sqlite;

import android.content.ClipData;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText ID_Trabajador, NombreTrabajador, CargoTrabajador;
    ListView Lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ID_Trabajador = findViewById(R.id.txtID);
        NombreTrabajador = findViewById(R.id.txtNombre);
        CargoTrabajador = findViewById(R.id.txtCargo);
        //aqui error.
        Lista = findViewById(R.id.ListaTrabajadores);
        CargaTrabajadores();
    }

    public void CrearTrabajador(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String ID = ID_Trabajador.getText().toString();
        String Nombre = NombreTrabajador.getText().toString();
        String Cargo = CargoTrabajador.getText().toString();
        if (ID.isEmpty() || Nombre.isEmpty() || Cargo.isEmpty()){
            Toast.makeText(this, "No se permiten campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("ID_Usuario", ID);
            DatosUsuario.put("NombreTrabajador", Nombre);
            DatosUsuario.put("CargoTrabajador", Cargo);
            BaseDatos.insert("Trabajadores", null, DatosUsuario);
            BaseDatos.close();
            ID_Trabajador.setText("");
            NombreTrabajador.setText("");
            CargoTrabajador.setText("");
            Toast.makeText(this, "Trabajador registrado exitosamente", Toast.LENGTH_SHORT).show();
            CargaTrabajadores();
        }
    }

    public void CargaTrabajadores(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        Cursor fila = BaseDatos.rawQuery("Select * from Trabajadores", null);
        ArrayList<String> ListaUsuarios = new ArrayList<>();
        if(fila.moveToFirst()){
            do {
                String ID = fila.getString(0);
                String Nombre = fila.getString(1);
                String Cargo = fila.getString(2);
                String UserInfo = "ID: "+ID+", Nombre: "+Nombre+", Cargo: "+Cargo;
                ListaUsuarios.add(UserInfo);
            } while (fila.moveToNext());
        }
        BaseDatos.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListaUsuarios);
        Lista.setAdapter(adapter);
    }
    public void BuscarTrabajador(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String id = ID_Trabajador.getText().toString();
        if(!id.isEmpty()){
            Cursor fila = BaseDatos.rawQuery("SELECT NombreTrabajador, CargoTrabajador From Trabajadores WHERE ID_Usuario = "+id,null);
            if(fila.moveToFirst()){
                do{
                    NombreTrabajador.setText(fila.getString(0));
                    CargoTrabajador.setText(fila.getString(1));
                }while (fila.moveToNext());
            }else{
                Toast.makeText(this, "El ID: "+id+" no se encuentra dentro de la base de datos", Toast.LENGTH_SHORT).show();
                ID_Trabajador.setText("");
                NombreTrabajador.setText("");
                CargoTrabajador.setText("");
            }BaseDatos.close();
        }else{
            Toast.makeText(this, "Debes ingresar el ID del trabajador", Toast.LENGTH_SHORT).show();
        }
    }
    public void EliminarTrabajador(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String id = ID_Trabajador.getText().toString();
        if(!id.isEmpty()){
            Cursor fila = BaseDatos.rawQuery("DELETE From Trabajadores WHERE ID_Usuario = "+id,null);
            if(fila.moveToFirst()){
                BaseDatos.delete("Trabajadores","ID_Trabajador="+id,null);
            }else{
                Toast.makeText(this, "El ID: "+id+" no se encuentra dentro de la base de datos", Toast.LENGTH_SHORT).show();
                ID_Trabajador.setText("");
                NombreTrabajador.setText("");
                CargoTrabajador.setText("");
            }BaseDatos.close();
        }else{
            Toast.makeText(this, "Debes ingresar el ID del trabajador", Toast.LENGTH_SHORT).show();
        }
    }
}