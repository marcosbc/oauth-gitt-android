package luis.clientebanco;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import luis.clientebanco.holographlibrary.Bar;
import luis.clientebanco.holographlibrary.BarGraph;

/**
 * Created by Luis on 02/05/2015.
 */
public class BalanceActivity extends Activity {

    private static final Uri URI_CUENTAS = Uri.parse(
            "content://luis.contentprovider/cuentas");
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        ArrayList<Cuentas> lista_cuentas = recuperarCuentas();

        lista = (ListView) findViewById(R.id.lista_balance);
        lista.setAdapter(new Lista_adaptador(this, R.layout.balance, lista_cuentas) {

            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_nombre = (TextView) view.findViewById(R.id.textView_nombre);
                    if (texto_nombre != null)
                        texto_nombre.setText(((Cuentas) entrada).getNAME() + " " + ((Cuentas) entrada).getLASTNAME());

                    TextView texto_balance = (TextView) view.findViewById(R.id.textView_balance);
                    if (texto_balance != null) {
                        float aux = ((Cuentas) entrada).getBALANCE();
                        texto_balance.setText("Balance: " + Float.toString(aux) + " euros");
                    }

                    TextView texto_iban = (TextView) view.findViewById(R.id.textView_iban);
                    if (texto_iban != null)
                        texto_iban.setText(((Cuentas) entrada).getIBAN());

                    TextView texto_ID = (TextView) view.findViewById(R.id.textView_ID);
                    if (texto_ID != null)
                        texto_ID.setText(Integer.toString(((Cuentas) entrada).getID()));

                }
            }
        });

        dibujarGrafico(lista_cuentas);
    }

    public void dibujarGrafico(ArrayList<Cuentas> lista_cuentas){
        ArrayList<Bar> points = new ArrayList<Bar>();
        Bar d;

        float balance = 0;
        float balanceTotal = 0;
        for(int i=0;i<lista_cuentas.size();i++){

            balance = lista_cuentas.get(i).getBALANCE();
            balanceTotal += balance;


            d = new Bar();

            d.setValue(balance);
            d.setName(lista_cuentas.get(i).getNAME() + " " + lista_cuentas.get(i).getLASTNAME());
            d.setColor(Color.parseColor("#003366"));

            points.add(d);

        }

        TextView texto_balanceTotal = (TextView) findViewById(R.id.textViewBalanceTotal2);
        texto_balanceTotal.setText(Float.toString(balanceTotal) + " euros");


        BarGraph g = (BarGraph) findViewById(R.id.bargraph);
        assert g != null;
        g.setUnit(" Euros");
        g.appendUnit(true);
        g.setBars(points);

        g.setOnBarClickedListener(new BarGraph.OnBarClickedListener() {

            @Override
            public void onClick(int index) {

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

}