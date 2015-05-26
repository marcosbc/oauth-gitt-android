package luis.clientebanco;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MiContentProvider extends ContentProvider {

    private MiBaseDatos MBD;
    SQLiteDatabase SQLDB;
    private static final String NOMBRE_CUENTAS = "luis.contentprovider";
    private static final String NOMBRE_TRANSACCIONES = "luis.contentprovider";

    private static final int CUENTAS = 1;
    private static final int CUENTAS_ID = 2;

    private static final int TRANSACCIONES = 3;
    private static final int TRANSACCIONES_ID = 4;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(NOMBRE_CUENTAS, "cuentas", CUENTAS);
        uriMatcher.addURI(NOMBRE_CUENTAS, "cuentas/#", CUENTAS_ID);
        uriMatcher.addURI(NOMBRE_TRANSACCIONES, "transacciones", TRANSACCIONES);
        uriMatcher.addURI(NOMBRE_TRANSACCIONES, "transacciones/#", TRANSACCIONES_ID);
    }



    @Override
    public String getType(Uri uri) {
        return null;
    }



    @Override
    public boolean onCreate() {
        MBD = new MiBaseDatos(getContext());
        return true;
    }



    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long registro = 0;
        String result = "";
        try {
            if (uriMatcher.match(uri) == CUENTAS) {
                SQLDB = MBD.getWritableDatabase();
                registro = SQLDB.insert("cuentas", null, values);
                result = "cuentas/";
            }
            else if (uriMatcher.match(uri) == TRANSACCIONES) {
                SQLDB = MBD.getWritableDatabase();
                registro = SQLDB.insert("transacciones", null, values);
                result = "transacciones/";
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        // Comprobar si se inserto bien el registro
        if (registro > 0) {
            Log.e("INSERT", "Registro creado correctamente");
        } else {
            Log.e("Error", "Al insertar registro: " + registro);
        }

        return Uri.parse(result + registro);
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String id = "";
        try {
            if (uriMatcher.match(uri) == CUENTAS_ID) {
                id = uri.getLastPathSegment();
                SQLDB = MBD.getWritableDatabase();
                SQLDB.update("cuentas", values, "_id=" + id, selectionArgs);
            }
            else if (uriMatcher.match(uri) == TRANSACCIONES_ID) {
                id = uri.getLastPathSegment();
                SQLDB = MBD.getWritableDatabase();
                SQLDB.update("transacciones", values, "_id=" + id, selectionArgs);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return Integer.parseInt(id);
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int registro = 0;
        try {
            if (uriMatcher.match(uri) == CUENTAS_ID) {
                String id = "_id=" + uri.getLastPathSegment();
                SQLDB = MBD.getWritableDatabase();
                registro = SQLDB.delete("cuentas", id, null);
            }
            else   if (uriMatcher.match(uri) == TRANSACCIONES_ID) {
                String id = "_id=" + uri.getLastPathSegment();
                SQLDB = MBD.getWritableDatabase();
                registro = SQLDB.delete("transacciones", id, null);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return registro;
    }



    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        String id = "";
        try {
            switch (uriMatcher.match(uri)) {
                case CUENTAS_ID:
                    id = "_id=" + uri.getLastPathSegment();
                    SQLDB = MBD.getReadableDatabase();
                    c = SQLDB.query("cuentas", projection, id, selectionArgs,
                            null, null, null, sortOrder);
                    break;
                case CUENTAS:
                    SQLDB = MBD.getReadableDatabase();
                    c = SQLDB.query("cuentas", projection, null, selectionArgs,
                            null, null, null, sortOrder);
                    break;
                case TRANSACCIONES_ID:
                    id = "_id=" + uri.getLastPathSegment();
                    SQLDB = MBD.getReadableDatabase();
                    c = SQLDB.query("transacciones", projection, id, selectionArgs,
                            null, null, null, sortOrder);
                    break;
                case TRANSACCIONES:
                    SQLDB = MBD.getReadableDatabase();
                    c = SQLDB.query("transacciones", projection, null, selectionArgs,
                            null, null, null, sortOrder);
                    break;
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return c;
    }



}