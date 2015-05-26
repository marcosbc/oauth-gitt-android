package luis.clientebanco;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MiBaseDatos extends SQLiteOpenHelper {

    private static final String TABLA_CUENTAS = "CREATE TABLE cuentas " +
            "(_id INTEGER PRIMARY KEY, username TEXT, name TEXT, lastname TEXT, dni TEXT " +
            ", iban TEXT, balance INTEGER, permiso INTEGER, accessToken TEXT, refreshToken TEXT)";

    private static final String TABLA_TRANSACCIONES = "CREATE TABLE transacciones " +
            "(_id INTEGER PRIMARY KEY, userId INTEGER, to_descr TEXT, from_descr TEXT, " +
            "quantity INTEGER, from_iban TEXT, date TEXT, status INTEGER)";


    public MiBaseDatos(Context context) {
        super(context, "mibasedatos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_CUENTAS);
        db.execSQL(TABLA_TRANSACCIONES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLA_CUENTAS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLA_TRANSACCIONES);
        onCreate(db);
    }
}