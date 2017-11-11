package DataLayer;

/**
 * Created by Frandy Javier on 10/04/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static final String BD_Name = "VentasMovilHDDb";
    public static final int BD_Version = 26;
    SQLiteDatabase database;

    public DbHelper(Context context) {
        super(context, BD_Name, null, BD_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DbGenerator.CREATETABLE_Usuarios);
        Log.i("DbHelper", "Tabla Usuarios creada");
        Log.i("DbHelper", "Base de datos creada completamente bien");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //region Metodos de base de datos
    public void InsertarQuery(String Query) {

        database = this.getWritableDatabase();
        try {
            this.getWritableDatabase();
            database.execSQL(Query);
        } catch (Exception ex) {
            Log.e("VentasMovilHD", "Error Importando: " + ex.getMessage());
        }
    }

    public Cursor EjecutarSQL(String sql, String[] SelectionArgs) {
        Cursor Retorno = null;
        database = this.getWritableDatabase();
        try {
            this.getWritableDatabase();
            //rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});
            Retorno = database.rawQuery(sql, SelectionArgs);
        } catch (Exception ex) {

        }

        Log.i("EjecutarSQL", "SQL Ejecutado Correctamente: " + sql);
        return Retorno;
    }

    public long Insertar(String Table, String NullColums, ContentValues Values) {
        long retorno = -1;

        database = this.getWritableDatabase();
        try {
            this.getWritableDatabase();
            Log.i("Insert", "Tabla: " + Table + " NullColums: " + NullColums + Values.size());
            retorno = database.insert(Table, NullColums, Values);
            database.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.close();
        }

        return retorno;
    }

    public boolean Modificar(String Table, ContentValues Values, String WhereClausure, String[] WhereArgs) {
        long retorno = -1;

        database = this.getWritableDatabase();
        try {
            this.getWritableDatabase();
            Log.i("Update", "Tabla: " + Table + " Where: " + WhereClausure);
            retorno = database.update(Table, Values, WhereClausure, WhereArgs);
            database.close();
        } catch (Exception ex) {

        } finally {
            this.close();
        }

        return retorno > 0;
    }

    public Cursor Buscar(String Table, String[] columns, String CampoIdentificador, String Id, Boolean UsaEsNulo) {
        Cursor Retorno = null;
        database = this.getReadableDatabase();
        try {
            this.getReadableDatabase();
            if (UsaEsNulo) {
                Retorno = database.query(Table, columns, CampoIdentificador + " = ? And EsNulo = ?", new String[]{Id, "0"}, null, null, null);
            } else {
                Retorno = database.query(Table, columns, CampoIdentificador + " = ?", new String[]{Id}, null, null, null);
            }

            //database.close();
        } catch (Exception ex) {

        }

        return Retorno;
    }

    public Cursor BuscarLista(String Table, String[] Columns, String WhereClausure, String[] WhereArgs, String GroupBy, String Havimg, String OrderBy) {
        Cursor retorno = null;

        database = this.getWritableDatabase();
        try {
            this.getReadableDatabase();
            retorno = database.query(Table, Columns, WhereClausure, WhereArgs, GroupBy, Havimg, OrderBy);
            //  database.close();
        } catch (Exception ex) {

        }

        return retorno;
    }

    public boolean Eliminar(String Table, String CampoIdentificador, String Id) {
        int retorno = 0;

        database = this.getWritableDatabase();
        try {
            this.getReadableDatabase();

            retorno = database.delete(Table, CampoIdentificador + " = ?", new String[]{Id});
            database.close();
        } catch (Exception ex) {

        } finally {
            this.close();
        }

        return retorno > 0;
    }

    public void CloseDataBaseConnection() {
        database.close();
        this.close();
    }
    //endregion
}
