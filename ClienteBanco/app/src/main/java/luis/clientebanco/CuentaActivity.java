package luis.clientebanco;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import luis.clientebanco.OAuth.AppContext;
import luis.clientebanco.OAuth.GenericDialogListener;
import luis.clientebanco.OAuth.LOGGING;
import luis.clientebanco.OAuth.UrbankOAuthDialog;
import luis.clientebanco.OAuth.WebService;

/**
 * Created by Luis on 02/05/2015.
 * {"access_token":"WN7CVFH0bbM3xkBi2m52i5zyeazpMS4FXqtNmdtb","token_type":"Bearer","expires_in":3600,"refresh_token":"07dgEQimJq4jc4iztXFgBzCHgeUqBJ4votzV9OhE"}
 * {"owner_id":"2","owner_type":"user","access_token":{},"client_id":1,"scopes":{"permission_read_transaction":{"id":"permission_read_transaction","description":"Ver transacciones"}}}
 * {"uid":2,"username":"luis","name":"Luis","lastname":"Casabuena","dni":"Z98765432A","iban":"ES8023100001180000054321","balance":322.75}
 * [{"tid":1,"to_description":"TRANSFERENCIA DE LUIS CASABUENA","from_description":"TRANSFERENCIA A MARCOS BJORKELUND","quantity":53.25,"from_iban":"ES8023100001180000054321","to_iban":"ES8023100001180000012345","date":1432275041,"status":""},{"tid":5,"to_description":"COMPRA DE MARCOS BJORKELUND","from_description":"COMPRA EN DECATHLON","quantity":65,"from_iban":"ES8023100001180000054321","to_iban":"ES8023100001180000099996","date":1432275041,"status":""},{"tid":7,"to_description":"COMPRA DE LUIS CASABUENA","from_description":"COMPRA EN SUPERMERCADOS DIA","quantity":20,"from_iban":"ES8023100001180000054321","to_iban":"ES8023100001180000099994","date":1432275041,"status":""},{"tid":8,"to_description":"COMPRA DE LUIS CASABUENA","from_description":"COMPRA EN SUPERMERCADOS MAS","quantity":15.75,"from_iban":"ES8023100001180000054321","to_iban":"ES8023100001180000099993","date":1432275041,"status":""}]
 */

public class CuentaActivity extends Activity {

    private ListView lista;
    private static final Uri URI_CUENTAS = Uri.parse(
            "content://luis.contentprovider/cuentas");
    private static final Uri URI_TRANSACCIONES = Uri.parse(
            "content://luis.contentprovider/transacciones");
    private Uri uri;

    public Context mContext;
    public Activity mActivity;

    protected static final String TAG = "urbankAccessCode";
    public WebService webService;
    private int seleccionado;
    private String accessCode;
    private String accessToken;
    private String refreshToken;
    private int cuentaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;
        seleccionado = -1;
        int tam = 0;

        setContentView(R.layout.activity_cuenta);

        mostrarLista();

        lista = (ListView) findViewById(R.id.listacuentas);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                TextView textoNombre = (TextView) view.findViewById(R.id.textView_nombre);
                TextView textoID = (TextView) view.findViewById(R.id.textView_ID);

                CharSequence texto = "Seleccionado: " + textoNombre.getText();
                Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();

                seleccionado = Integer.parseInt(textoID.getText().toString());

                try{
                    for (int ctr=0;ctr<=lista.getCount() ;ctr++){
                        if(position==ctr){
                            lista.getChildAt(ctr).setBackgroundColor(Color.GRAY);
                        }else{
                            lista.getChildAt(ctr).setBackgroundColor(Color.parseColor("#C0C0C0"));
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private int mostrarLista(){

        ArrayList<Cuentas> lista_cuentas = recuperarCuentas();

        lista = (ListView) findViewById(R.id.listacuentas);
        lista.setAdapter(new Lista_adaptador(this, R.layout.cuenta, lista_cuentas) {

            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_nombre = (TextView) view.findViewById(R.id.textView_nombre);
                    if (texto_nombre != null)
                        texto_nombre.setText(((Cuentas) entrada).getNAME() + " " + ((Cuentas) entrada).getLASTNAME());

                    TextView texto_dni = (TextView) view.findViewById(R.id.textView_dni);
                    if (texto_dni != null)
                        texto_dni.setText(((Cuentas) entrada).getDNI());

                    TextView texto_iban = (TextView) view.findViewById(R.id.textView_iban);
                    if (texto_iban != null)
                        texto_iban.setText(((Cuentas) entrada).getIBAN());

                    TextView texto_ID = (TextView) view.findViewById(R.id.textView_ID);
                    if (texto_ID != null)
                        texto_ID.setText(Integer.toString(((Cuentas) entrada).getID()));

                }
            }
        });

        return lista_cuentas.size();

    }

    public ArrayList<Cuentas> recuperarCuentas() {

        ContentResolver CR = getContentResolver();
        Cursor c;

        ArrayList<Cuentas> lista_cuentas = new ArrayList<Cuentas>();

        String[] valores_recuperar = {"_id", "username", "name", "lastname", "dni", "iban",
                "balance", "permiso", "accessToken", "refreshToken"};
        c = CR.query(URI_CUENTAS, valores_recuperar, null, null, null);

        if(c!=null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                int _id = c.getInt(0);
                String _username = c.getString(1);
                String _name = c.getString(2);
                String _lastname = c.getString(3);
                String _dni = c.getString(4);
                String _iban = c.getString(5);
                float _balance = c.getFloat(6);
                int _permiso = c.getInt(7);
                String _accessToken = c.getString(8);
                String _refreshToken = c.getString(9);
                Cuentas cuentas = new Cuentas(_id, _username, _name, _lastname, _dni
                        , _iban, _balance, _permiso, _accessToken, _refreshToken);
                lista_cuentas.add(cuentas);
            } while (c.moveToNext());
        }

        return lista_cuentas;
    }

    private int insertarCuenta (JSONObject cuenta) throws JSONException {

        ContentResolver CR = getContentResolver();
        int res;
        cuentaActual = cuenta.getInt("uid");
        String _username = cuenta.getString("username");
        String _name = cuenta.getString("name");
        String _lastname = cuenta.getString("lastname");
        String _dni = cuenta.getString("dni");
        String _iban = cuenta.getString("iban");
        float _balance = (float) cuenta.getDouble("balance");
        int _permiso = 1;
        String _accessToken = accessToken;
        String _refreshToken = refreshToken;

        uri=CR.insert(URI_CUENTAS, setVALORESCUENTA(cuentaActual, _username, _name, _lastname, _dni
                , _iban, _balance, _permiso, _accessToken, _refreshToken));
        res=Integer.parseInt(uri.getLastPathSegment());

        if(res>0) {
            Toast.makeText(getApplicationContext(),
                    "Cuenta anadida correctamente", Toast.LENGTH_LONG).show();
            mostrarLista();
        }
        else
            Toast.makeText(getApplicationContext(),
                    "No se ha podido guardar la cuenta" ,   Toast.LENGTH_LONG).show();

        return res;
    }

    private int insertarTransacciones(JSONArray transacciones) throws  JSONException{

        ContentResolver CR = getContentResolver();
        int res = 0;

        for(int i=0;i<transacciones.length();i++) {

            JSONObject transaccion = transacciones.getJSONObject(i);

            String _to_descrip = transaccion.getString("to_description");
            String _from_descrip = transaccion.getString("from_description");
            float _quantity = (float) transaccion.getDouble("quantity");
            String _from_iban = transaccion.getString("from_iban");
            String _to_iban = transaccion.getString("to_iban");
            String _date = transaccion.getString("date");
            String _status = transaccion.getString("status");

            uri = CR.insert(URI_TRANSACCIONES, setVALORESTRANSACCIONES(0, cuentaActual, _to_descrip, _from_descrip, _quantity, _from_iban
                    , _to_iban, _date, _status));
            res = Integer.parseInt(uri.getLastPathSegment());
        }

        if(res>0) {
            Toast.makeText(getApplicationContext(),
                    "Transacciones anadidas correctamente", Toast.LENGTH_LONG).show();
            mostrarLista();
        }
        else
            Toast.makeText(getApplicationContext(),
                    "No se han podido guardar las transacciones" ,   Toast.LENGTH_LONG).show();

        return res;

    }

    private int borrarTransacciones(int cuentaid){

        ContentResolver CR = getContentResolver();
        Cursor c;
        int res = 0;

        String[] valores_recuperar = {"_id", "userId"};
        c = CR.query(URI_TRANSACCIONES, valores_recuperar, null, null, null);

        if(c!=null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                int _id = c.getInt(0);
                int _cuentaid = c.getInt(1);

                if(_cuentaid == cuentaid){

                    uri = Uri.parse("content://luis.contentprovider/transacciones/" + _id);
                    CR.delete(uri, null, null);

                    res = Integer.parseInt(uri.getLastPathSegment());
                }

            } while (c.moveToNext());
        }

        return res;
    }

    public void clicEnBoton_AnadirCuenta(View V) {

        urbankLogin();

    }

    public void clicEnBoton_IrBorrarCuenta (View view) {

        ContentResolver CR = getContentResolver();
        int res;

       if(seleccionado == -1)
           Toast.makeText(getApplicationContext(),
                   "Ninguna cuenta seleccionada", Toast.LENGTH_LONG).show();
        else {
           uri = Uri.parse("content://luis.contentprovider/cuentas/" + seleccionado);
           CR.delete(uri, null, null);

           res = Math.min(Integer.parseInt(uri.getLastPathSegment()), borrarTransacciones(seleccionado));
           if (res > 0) {
               Toast.makeText(getApplicationContext(),
                       "Cuenta " + seleccionado + " borrada correctamente", Toast.LENGTH_LONG).show();
                mostrarLista();
           }
           else
               Toast.makeText(getApplicationContext(),
                       "No se ha podido borrar la cuenta", Toast.LENGTH_LONG).show();
       }
    }

    public void urbankLogin(){

        webService = new WebService();

        String authRequestRedirect = AppContext.UB_APP_OAUTH_URL
                + "?client_id="+AppContext.UB_CLIENT_ID
                + "&redirect_uri="+AppContext.UB_APP_REDIRECT
                + "&response_type=code"
                + "&scope=permission_read_transaction"
                ;

        if(LOGGING.DEBUG)
            Log.d(TAG, "authRequestRedirect->" + authRequestRedirect);

        new UrbankOAuthDialog(mContext, authRequestRedirect, new GenericDialogListener() {

            public void onComplete(Bundle values) {
                if(LOGGING.DEBUG)
                    Log.d(TAG, "onComplete->"+values);

                accessCode = "";

                try{

                    accessCode = values.getString("code");
                    new urbankAccessToken().execute();

                }
                catch (Exception ex1){
                    Log.w(TAG, ex1.toString());
                    accessCode = null;
                }
                Log.v(TAG, "prueba");
            }
            public void onError(String e) {
                if(LOGGING.DEBUG)Log.d(TAG, "onError->"+e);
            }
            public void onCancel() {

                if(LOGGING.DEBUG)Log.d(TAG, "onCancel()");
            }
        }).show();

    }

    public class urbankAccessToken extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params){

            String URL = AppContext.UB_APP_TOKEN_URL
                    + "?client_id=" + AppContext.UB_CLIENT_ID
                    + "&redirect_uri=" + AppContext.UB_APP_REDIRECT
                    + "&grant_type=authorization_code"
                    + "&client_secret=" + AppContext.UB_CLIENT_SECRET
                    + "&code=" + accessCode;

            StringBuilder stringBuilder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                } else {
                    Log.d("JSON", "Failed to download file");
                }
            } catch (Exception e) {
                Log.d("readJSONFeed", e.getLocalizedMessage());
            }

            return stringBuilder.toString();
        }

        protected void onPostExecute(String result) {

            try {
                JSONObject tokens = new JSONObject(result);

                accessToken = tokens.getString("access_token");
                refreshToken = tokens.getString("refresh_token");

                Log.d("Access", "Access Token");

                new peticionTokenInfo().execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class peticionTokenInfo extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {

            String URL = "http://api.urbank.bjorkelund.me/tokeninfo"
                    + "?access_token=" + accessToken;

            StringBuilder stringBuilder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                } else {
                    Log.d("JSON", "Failed to download file");
                }
            } catch (Exception e) {
                Log.d("readJSONFeed", e.getLocalizedMessage());
            }

            return stringBuilder.toString();
        }

        protected void onPostExecute(String result) {

            try {
                JSONObject info = new JSONObject(result);

                new peticionUserInfo().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class peticionUserInfo extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {

            String URL = "http://api.urbank.bjorkelund.me/user"
                    + "?access_token=" + accessToken;

            StringBuilder stringBuilder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                } else {
                    Log.d("JSON", "Failed to download file");
                }
            } catch (Exception e) {
                Log.d("readJSONFeed", e.getLocalizedMessage());
            }

            return stringBuilder.toString();
        }

        protected void onPostExecute(String result) {

            try {
                JSONObject cuenta = new JSONObject(result);

                insertarCuenta(cuenta);

                new peticionTransactionInfo().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class peticionTransactionInfo extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {

            String URL = "http://api.urbank.bjorkelund.me/transaction"
                    + "?access_token=" + accessToken;

            StringBuilder stringBuilder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                } else {
                    Log.d("JSON", "Failed to download file");
                }
            } catch (Exception e) {
                Log.d("readJSONFeed", e.getLocalizedMessage());
            }

            return stringBuilder.toString();
        }

        protected void onPostExecute(String result) {

            try {
                JSONArray transactions = new JSONArray(result);

                insertarTransacciones(transactions);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private ContentValues setVALORESCUENTA(int id, String username, String name, String lastname,
                                     String dni,String iban, float balance,int permiso,
                                     String accessToken, String refreshToken) {
        ContentValues valores = new ContentValues();
        if(id!=0)
            valores.put("_id", id);
        valores.put("username", username);
        valores.put("name", name);
        valores.put("lastname", lastname);
        valores.put("dni", dni);
        valores.put("iban", iban);
        valores.put("balance", balance);
        valores.put("permiso", permiso);
        valores.put("accessToken", accessToken);
        valores.put("refreshToken", refreshToken);

        return valores;
    }

    private ContentValues setVALORESTRANSACCIONES(int id, int cuentaid,String to_descrip, String from_descrip, float quantity, String from_iban,
                                                  String to_iban, String date, String status) {
        ContentValues valores = new ContentValues();
        if(id!=0)
            valores.put("_id", id);
        valores.put("userId", cuentaid);
        valores.put("to_descr", to_descrip);
        valores.put("from_descr", from_descrip);
        valores.put("quantity", quantity);
        valores.put("from_iban", from_iban);
        valores.put("to_iban", to_iban);
        valores.put("date", date);
        valores.put("status", status);


        return valores;
    }

}
