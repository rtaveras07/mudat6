package BusinessLayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import DataLayer.DbHelper;

/**
 * Created by Frandy Javier on 10/04/2015.
 */
public class Usuarios implements Registro {

    //region atributos
    private long IdUsuario;
    private String Fecha;
    private String Nombres;
    private String Email;
    private String IdentificadorUnico = "";
    private String UrlFoto;
    private String CoverFotoUrl;
    private boolean Suscrito;
    private boolean esNulo;
    public  boolean Autenticado;

    private Context context;
    private DbHelper db = new DbHelper(context);
    //endregion

    //region Constructor
    public Usuarios(Context context) {
        this.IdUsuario = 0;
        this.Nombres = "";
        this.IdentificadorUnico = "";
        this.Fecha = Utilitario.getNewDateFormat();
        this.Email = "";
        this.UrlFoto = "";
        this.CoverFotoUrl = "";
        this.esNulo = false;
        this.Suscrito = false;
        this.context = context;
    }

    public Usuarios(int pIdUsuario, String nombres, String descripcion, String pFecha, String pEmail,String pUrlFoto,String pCoverFotoUrl, boolean pesNulo, Context pcontext) {
        this.IdUsuario = pIdUsuario;
        this.Nombres = nombres;
        this.IdentificadorUnico = descripcion;
        this.Fecha = pFecha;
        this.Email = pEmail;
        this.UrlFoto = pUrlFoto;
        this.CoverFotoUrl = pCoverFotoUrl;
        this.esNulo = pesNulo;
        this.Suscrito = false;
        this.context = pcontext;
    }

    //region Getters y Setters
    public boolean isEsNulo() {
        return esNulo;
    }

    public void setEsNulo(boolean esNulo) {
        this.esNulo = esNulo;
    }

    public boolean isSuscrito() {
        return Suscrito;
    }

    public void setSuscrito(boolean suscrito) {
        Suscrito = suscrito;
    }

    public String getCoverFotoUrl() {
        return CoverFotoUrl;
    }

    public void setCoverFotoUrl(String coverFotoUrl) {
        CoverFotoUrl = coverFotoUrl;
    }

    public long getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getUrlFoto() {
        return UrlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        UrlFoto = urlFoto;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    //endregion

    public String getIdentificadorUnico() {
        return IdentificadorUnico;
    }

    public void setIdentificadorUnico(String identificadorUnico) {
        IdentificadorUnico = identificadorUnico;
    }
    //endregion

    //region Metodos
    @Override
    public ContentValues Valores() {
        ContentValues Values = new ContentValues();

        //key = campo de la bd, value el valor
        Values.put("IdUsuario", this.IdUsuario);
        Values.put("Nombres", this.Nombres);
        Values.put("IdentificadorUnico", this.IdentificadorUnico);
        Values.put("Fecha", this.Fecha);
        Values.put("Email", this.Email);
        Values.put("UrlFoto", this.UrlFoto);
        Values.put("CoverFotoUrl", this.CoverFotoUrl);
        Values.put("Suscrito", this.Suscrito);
        Values.put("esNulo", this.esNulo);
        return Values;
    }

    @Override
    public int getDbCount() {
        int Retorno = 0;

        db = new DbHelper(this.context);
        Cursor values = db.EjecutarSQL("Select Count(IdUsuario) as Cantidad From Usuarios where EsNulo = 0", null);

        if (values.getCount() > 0) {
            values.moveToFirst();
            Retorno = Integer.valueOf(values.getString(values.getColumnIndex("Cantidad")));
        }

        db.CloseDataBaseConnection();
        values.close();
        return Retorno;
    }

    @Override
    public boolean insertar() {
        db = new DbHelper(this.context);
        this.setSuscrito(true);

        this.IdUsuario = db.Insertar("Usuarios", "IdUsuario", Valores());
        Log.i("Usuarios", "Usuario insertado correctamente id: " + this.IdUsuario);
        return this.IdUsuario > 0;
    }

    @Override
    public boolean modificar() {
        db = new DbHelper(this.context);

        return db.Modificar("Usuarios", Valores(), "IdUsuario = ?", new String[]{(String.valueOf(IdUsuario))});
    }

    @Override
    public boolean eliminar() {
        db = new DbHelper(this.context);

        db.EjecutarSQL("DELETE FROM Usuarios",null);
        db.EjecutarSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME =  'Usuarios'",null);

        return db.Eliminar("Usuarios", "IdUsuario", String.valueOf(this.IdUsuario));
    }

    @Override
    public boolean buscar(String Id) {
        boolean retorno = false;
        db = new DbHelper(this.context);
        Log.i("idUsuario" + Id, "Este sera el que se buscara");
        Cursor values = db.Buscar("Usuarios", new String[]{"IdUsuario","Suscrito", "Fecha", "Nombres", "Email", "IdentificadorUnico","UrlFoto","CoverFotoUrl", "esNulo"}, "IdUsuario", Id,true);

        Log.i("Cursor de Busqueda", " valores: " + values.toString());
        Log.i("Cursor", "Count : " + values.getCount());
        if (values.getCount() > 0) {
            values.moveToFirst();
            Log.i("Cursor", "Count : " + values.getCount() + " primer dato: " + values.getString(values.getColumnIndex("IdUsuario")));
            Log.i("Cursor de Busqueda", "Cursor no es nulo");

            this.IdUsuario = Integer.valueOf(values.getString(values.getColumnIndex("IdUsuario")));
            this.Fecha = Utilitario.getNewDateFormat();
            this.Nombres = values.getString(values.getColumnIndex("Nombres"));
            this.Email = values.getString(values.getColumnIndex("Email"));
            this.UrlFoto = values.getString(values.getColumnIndex("UrlFoto"));
            this.CoverFotoUrl = values.getString(values.getColumnIndex("CoverFotoUrl"));
            this.IdentificadorUnico = values.getString(values.getColumnIndex("IdentificadorUnico"));
            this.Suscrito = Boolean.valueOf(values.getString(values.getColumnIndex("Suscrito")));
            retorno = true;
        }

        db.CloseDataBaseConnection();
        values.close();
        return retorno;
    }

    @Override
    public Cursor buscarLista(String[] Columns, String WhereClausure, String[] WhereArgs, String GroupBy, String Having, String OrderBy) {
        db = new DbHelper(this.context);
        Cursor Retorno = null;

        Retorno = db.BuscarLista("Usuarios", Columns, WhereClausure, WhereArgs, GroupBy, Having, OrderBy);
        Log.i("Cursor de Busqueda", "Listado Retorno: " + Retorno.getCount());
        db.CloseDataBaseConnection();
        return Retorno;
    }

    @Override
    public void insertarQuery(String Sql) {

    }


   /* public void InsertarNewLoginDb(final String pIdentificador, final String pNombre, final String pEmail, final String pFotoPerfil, final String pFotoPortada, final String pTipoCuenta, final String pIdTipoCuenta){

        Thread nt = new Thread(){
            String result;

            @Override
            public void run(){

                String NameSpace =  "http://ventasmovilhd.com/";
                String Url = "http://ventasmovilhd.com/services/Usuarios.asmx?op=SendNewMailToDb";
                String MethodName = "SendNewMailToDb";
                String Soap_Action = "http://ventasmovilhd.com/SendNewMailToDb";

                SoapObject request = new SoapObject(NameSpace,MethodName);
                request.addProperty("pIdentificador",pIdentificador);
                request.addProperty("pNombre",pNombre);
                request.addProperty("pEmail",pEmail);
                request.addProperty("pFotoPerfil",pFotoPerfil);
                request.addProperty("pFotoPortada",pFotoPortada);
                request.addProperty("pTipoCuenta",pTipoCuenta);
                request.addProperty("pIdTipoCuenta",pIdTipoCuenta);

                //asignamos el tipo de version que usamos del soap al evelope al hacer el new
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                //configuramos que usamos .net
                envelope.dotNet = true;

                //enviamos al soap el request anterior
                envelope.setOutputSoapObject(request);

                HttpTransportSE transporte = new HttpTransportSE(Url);

                try {
                    transporte.call(Soap_Action,envelope);
                    SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();

                    result = resultado_xml.toString();

                } catch (IOException | XmlPullParserException e) {
                    Log.i("Ventasmovilhd", "error:" + e.toString());
                }
            }
        };

        //utilizar el thead para que se llame
        nt.start();
    }*/
    //endregion


}
