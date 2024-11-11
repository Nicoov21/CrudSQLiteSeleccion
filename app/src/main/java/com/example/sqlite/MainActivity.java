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

        Lista.setOnItemClickListener((adapterView, view, i, l) -> {//Metodo de click en la lista
            String seleccion = ListaUsuarios.get(i);//Posicion de la seleccion
            String idseleccionado = seleccion.split(",")[0].split(":")[1].trim();//Toma el id seleccionado
            BuscarTrabajador(idseleccionado);
        });
    }
    public void BuscarTrabajador(String id){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        if(!id.isEmpty()){
            Cursor fila = BaseDatos.rawQuery("SELECT ID_Usuario,NombreTrabajador, CargoTrabajador From Trabajadores WHERE ID_Usuario = "+id,null);
            if(fila.moveToFirst()){
                ID_Trabajador.setEnabled(false);
                do{
                    ID_Trabajador.setText(fila.getString(0));
                    NombreTrabajador.setText(fila.getString(1));
                    CargoTrabajador.setText(fila.getString(2));
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

    public void ActualizarTrabajador(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String id = ID_Trabajador.getText().toString();
        String nombre = NombreTrabajador.getText().toString();
        String cargo = CargoTrabajador.getText().toString();
        if(!nombre.isEmpty() || !cargo.isEmpty()){
            ContentValues datos = new ContentValues();
            datos.put("NombreTrabajador",nombre);
            datos.put("CargoTrabajador",cargo);
            int cantidad = BaseDatos.update("Trabajadores",datos,"ID_Usuario="+id,null);
            if(cantidad != 0){
                Toast.makeText(this, "La actualizacion se realizo correctamente", Toast.LENGTH_SHORT).show();
                ID_Trabajador.setText("");
                NombreTrabajador.setText("");
                CargoTrabajador.setText("");
                CargaTrabajadores();
            }else{
                Toast.makeText(this, "Hubo un error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Debes ingresar el ID del trabajador", Toast.LENGTH_SHORT).show();
        }
    }
    public void EliminarTrabajador(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String id = ID_Trabajador.getText().toString();
        if(!id.isEmpty()){
            int cantidad = BaseDatos.delete("Trabajadores","ID_Usuario="+id,null);
            if(cantidad != 0){
                Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                CargaTrabajadores();
                ID_Trabajador.setText("");
                NombreTrabajador.setText("");
                CargoTrabajador.setText("");
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
    public void VaciarCampos(View view){
        ID_Trabajador.setText("");
        NombreTrabajador.setText("");
        CargoTrabajador.setText("");
    }
}