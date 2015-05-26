package luis.clientebanco;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
 *
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
    private String accessCode;
    private String accessToken;
    private String refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;

        setContentView(R.layout.activity_cuenta);

        mostrarLista();
    }

    private void mostrarLista(){

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
        int _id = cuenta.getInt("uid");
        String _username = cuenta.getString("username");
        String _name = cuenta.getString("name");
        String _lastname = cuenta.getString("lastname");
        String _dni = cuenta.getString("dni");
        String _iban = cuenta.getString("iban");
        float _balance = (float) cuenta.getDouble("balance");
        int _permiso = 1;
        String _accessToken = accessToken;
        String _refreshToken = refreshToken;

        uri=CR.insert(URI_CUENTAS, setVALORESCUENTA(_id, _username, _name, _lastname, _dni
                , _iban, _balance, _permiso, _accessToken, _refreshToken));
        res=Integer.parseInt(uri.getLastPathSegment());

        if(res>0) {
            Toast.makeText(getApplicationContext(),
                    "Cuenta añadida correctamente", Toast.LENGTH_LONG).show();
            mostrarLista();
        }
        else
            Toast.makeText(getApplicationContext(),
                    "No se ha podido guardar la cuenta" ,   Toast.LENGTH_LONG).show();

        return res;
    }

    public void clicEnBoton_AnadirCuenta(View V) {

        urbankLogin();

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

                Log.d("Access", info.toString());
                Log.d("Access", info.toString());

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

                Log.d("Access", cuenta.toString());
                Log.d("Access", cuenta.toString());

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

                Log.d("Access", transactions.toString());
                Log.d("Access", transactions.toString());

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



}
