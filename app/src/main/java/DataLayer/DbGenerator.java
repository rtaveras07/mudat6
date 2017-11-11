package DataLayer;

/**
 * Created by Frandy Javier on 11/11/2017.
 */
public class DbGenerator {

    //region Scripts de creacion de tablas de base de datos
    public static final String CREATETABLE_Usuarios = "Create Table IF NOT EXISTS " +
            " Usuarios (IdUsuario integer primary key, " +
            " Fecha  Date not null, " +
            " Nombres  nvarchar(50) not null, " +
            " Email  nvarchar(50) not null," +
            " IdentificadorUnico nvarchar(50) default '' not null," +
            " UrlFoto nvarchar(500) null, " +
            " CoverFotoUrl nvarchar(500) null, " +
            " Suscrito bit null, " +
            " Autenticado bit null, " +
            " esNulo bit not null)";

    public static final String CREATEVIEW_getTurnoCobros = "Create View IF NOT EXISTS View_getTurnoCobros As " +
            " SELECT Sum(Recibido) as Monto,IdTurno, replace(Fecha ,'-','') as Fecha FROM eCobros" +
            " Where esNulo = 0 Group By IdTurno,Fecha";
    //endregion
}
