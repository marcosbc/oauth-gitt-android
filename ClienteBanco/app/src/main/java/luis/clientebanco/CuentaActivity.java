package luis.clientebanco;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Luis on 02/05/2015.
 */
public class CuentaActivity extends Activity {

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        ArrayList<Cuentas> lista_cuentas = new ArrayList<>();
        Cuentas cuenta1 = new Cuentas(1321, "Luis", "Luis",
                "Casabuena Gomez", "76650720T", "ES8023100001180012002356",302.83f,2);
        Cuentas cuenta2 = new Cuentas(1321, "Luis", "Luis",
                "Casabuena Gomez", "76650720T", "ES8023100001180012002341",1543.12f,2);

        lista_cuentas.add(cuenta1);
        lista_cuentas.add(cuenta2);

        lista = (ListView) findViewById(R.id.listacuentas);
        lista.setAdapter(new Lista_adaptador(this, R.layout.cuenta, lista_cuentas) {

            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_contacto = (TextView) view.findViewById(R.id.textView_nombre);
                    if (texto_contacto != null)
                        texto_contacto.setText(((Cuentas) entrada).getNAME() + " " + ((Cuentas) entrada).getLASTNAME());

                    TextView texto_telefono = (TextView) view.findViewById(R.id.textView_dni);
                    if (texto_telefono != null)
                        texto_telefono.setText(((Cuentas) entrada).getDNI());

                    TextView texto_email = (TextView) view.findViewById(R.id.textView_iban);
                    if (texto_email != null)
                        texto_email.setText(((Cuentas) entrada).getIBAN());

                    TextView texto_ID = (TextView) view.findViewById(R.id.textView_ID);
                    if (texto_ID != null)
                        texto_ID.setText(Integer.toString(((Cuentas) entrada).getID()));

                }
            }
        });
    }

}
