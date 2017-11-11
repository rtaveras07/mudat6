package BusinessLayer;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Frandy Javier on 11/11/2017.
 */
public interface Registro {

    //region Atributos
    public ContentValues Valores();
    //endregion

    //region Metodos
    public int getDbCount();

    public boolean insertar();

    public boolean modificar();

    public boolean eliminar();

    public boolean buscar(String Id);

    public Cursor buscarLista(String[] Columns, String WhereClausure, String[] WhereArgs, String GroupBy, String Having, String OrderBy);

    public void insertarQuery(String Sql);
    //endregion

}
