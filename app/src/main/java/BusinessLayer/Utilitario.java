package BusinessLayer;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Frandy Javier on 11/11/2017.
 */
public class Utilitario {

    public static final int PantallaSmall = 1;
    public static final int PantallaNormal = 2;
    public static final int PantallaLarge = 3;
    public static final int PantallaxLarge = 4;
    public static final int PantallaNoDefinida = 5;

    public static Bitmap getBitmapFromURL(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        int IO_BUFFER_SIZE = 4 * 1024;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (IOException e) {
            Log.e("VentasMovilHD", "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }

    /**
     * Closes the specified stream.
     *
     * @param stream The stream to close.
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e("VentasMovilHD", "Could not close stream", e);
            }
        }
    }

    public static Bitmap getImage(String imageUrl, int desiredWidth, int desiredHeight) {
        Bitmap image = null;
        int inSampleSize = 0;


        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        options.inSampleSize = inSampleSize;

        try {
            URL url = new URL(imageUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream stream = connection.getInputStream();

            image = BitmapFactory.decodeStream(stream, null, options);

            int imageWidth = options.outWidth;
            int imageHeight = options.outHeight;

            if (imageWidth > desiredWidth || imageHeight > desiredHeight) {
                System.out.println("imageWidth:" + imageWidth + ", imageHeight:" + imageHeight);

                inSampleSize = inSampleSize + 2;

                //getImage(imageUrl,,);
            } else {
                options.inJustDecodeBounds = false;

                connection = (HttpURLConnection) url.openConnection();

                stream = connection.getInputStream();

                image = BitmapFactory.decodeStream(stream, null, options);

                return image;
            }
        } catch (Exception e) {
            Log.e("getImage", e.toString());
        }

        return image;
    }

    public static Bitmap getBitMapFromResorce(int Id) {
        return BitmapFactory.decodeResource(null, Id);
    }

    public static void downloadFile(String uRl, Activity activity, String Ruta, String fileName) {

        File sdCardDirectory = Environment.getExternalStorageDirectory();

        File dir = new File(sdCardDirectory.getAbsolutePath() + Ruta);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Ventas Movil HD")
                .setDescription("Descargando archivo")
                .setDestinationInExternalPublicDir(Ruta, fileName);
        mgr.enqueue(request);

    }

    public static String getIdentificadorUnico(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static int getScreenSize(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return Utilitario.PantallaSmall;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return Utilitario.PantallaNormal;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return Utilitario.PantallaLarge;
            case 4:
                return Utilitario.PantallaxLarge;
            default:
                return Utilitario.PantallaNoDefinida;
        }
    }


    public  static String RemoveMoneySimbol(String text){
        return text.replace("$","");
    }

    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    public static boolean CrearRutaEnSD(String ArchivoLog, String Ruta, String... Extencion) {
        File sdCard, directory, file = null;
        Boolean Retorno = false;
        try {
            // validamos si se encuentra montada nuestra memoria externa
            if (Environment.getExternalStorageState().equals("mounted")) {

                // Obtenemos el directorio de la memoria externa
                sdCard = Environment.getExternalStorageDirectory();


                // Clase que permite grabar texto en un archivo
                FileOutputStream fout = null;
                try {
                    // instanciamos un onjeto File para crear un nuevo
                    // directorio
                    // la memoria externa
                    directory = new File(sdCard.getAbsolutePath() + Ruta);

                    // se crea el nuevo directorio donde se cerara el
                    // archivo
                    directory.mkdirs();

                    // creamos el archivo en el nuevo directorio creado

                    if (Extencion.length > 0) {
                        file = new File(directory, ArchivoLog + Extencion[0]);
                    } else {
                        file = new File(directory, ArchivoLog + "Log.txt");
                    }
                    fout = new FileOutputStream(file);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Retorno = true;
            }


        } finally {

        }

        return Retorno;
    }

    public static Boolean GuardarFotoEnSD(String NombreImagen, String Ruta, Bitmap ImagenBitmap) {
        Boolean Retorno = false;

        if (ImagenBitmap != null) {

            File sdCardDirectory = Environment.getExternalStorageDirectory();

            File dir = new File(sdCardDirectory.getAbsolutePath() + Ruta);
            dir.mkdirs();

            File image = new File(dir, NombreImagen + ".png");

            // Encode the file as a PNG image.
            FileOutputStream outStream = null;
            try {

                outStream = new FileOutputStream(image);
                ImagenBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                /* 100 para mantener la calidad de la imagen */

                outStream.flush();
                outStream.close();
                Retorno = true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return Retorno;
    }

    public static Boolean BorrarFotoEnSD(String NombreImagen, String Ruta) {
        Boolean Retorno = false;
        File sdCardDirectory = Environment.getExternalStorageDirectory();

        File dir = new File(sdCardDirectory.getAbsolutePath() + Ruta);
        dir.mkdirs();


        File image = new File(dir, NombreImagen + ".png");

        try {

            if (image.delete()) {
                Retorno = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Retorno;
    }

    public static Integer TryParseInt(String someText) {
        int Retorno = 0;

        try {
            Retorno = Integer.parseInt(someText);
        } catch (NumberFormatException ex) {
            Retorno = 0;
        }

        return Retorno;
    }

    public static float TryParseFloat(String someText) {
        float Retorno = 0;

        if(someText == null){
            return 0;
        }

        try {
            Retorno = Float.parseFloat(someText);
        } catch (NumberFormatException ex) {
            Retorno = 0;
        }

        return Retorno;
    }

    public static long TryParseLong(String someText) {
        long Retorno = 0;

        try {
            Retorno = Long.parseLong(someText);
        } catch (NumberFormatException ex) {
            Retorno = 0;
        }

        return Retorno;
    }

    public static String getNewDateFormat() {
        String Retorno;

        try {
            Calendar cal = new GregorianCalendar();
            Date date = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Retorno = df.format(date);
        } catch (NumberFormatException ex) {
            Retorno = "";
        }

        return Retorno;
    }

    public static String FormatearFecha(Date Fecha) {
        String Retorno;

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Retorno = df.format(Fecha);
        } catch (NumberFormatException ex) {
            Retorno = "";
        }

        return Retorno;
    }


    public static boolean isNetworkAvailable(Context mContext) {

        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network Testing", "***Available***");
            return true;
        }
        Log.e("Network Testing", "***Not Available***");
        return false;
    }

    //Metodo usado para obtener la hora actual del sistema
    //@return Retorna un <b>STRING</b> con la hora actual formato "hh:mm:ss"
    public static String getHoraActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
        return formateador.format(ahora);
    }

    // Suma los días recibidos a la fecha
    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

    //Devuele un java.util.Date desde un String en formato dd-MM-yyyy
    //@param La fecha a convertir a formato date
    //@return Retorna la fecha en formato Date
    public static synchronized Date deStringToDate(String fecha) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaEnviar = null;
        try {
            fechaEnviar = formatoDelTexto.parse(fecha);
            return fechaEnviar;
        } catch (ParseException ex) {
            Log.e("VentasMovilHD", "Error en ObjectHelpers: " + ex.getMessage());
            return null;
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public static String getPath(Uri uri,Activity activity) {
        // just some safety built in
        if (uri == null) {
            return null;
        }

        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
