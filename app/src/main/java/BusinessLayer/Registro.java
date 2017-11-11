package BusinessLayer;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Frandy Javier on 12/04/2015.
 */
public interface Registro {

    //region Atributos
    public ContentValues Valores();
    //endregion

    //region Metodos
    public int getDbCount();

    public boolean Insertar();

    public boolean Modificar();

    public boolean Eliminar();

    public boolean Buscar(String Id);

    public Cursor BuscarLista(String[] Columns, String WhereClausure, String[] WhereArgs, String GroupBy, String Having, String OrderBy);

    public void InsertarQuery(String Sql);
    //endregion

}
