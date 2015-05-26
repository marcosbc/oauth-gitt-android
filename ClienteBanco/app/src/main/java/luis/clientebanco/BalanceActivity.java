package luis.clientebanco;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import luis.clientebanco.holographlibrary.Line;
import luis.clientebanco.holographlibrary.LineGraph;
import luis.clientebanco.holographlibrary.LinePoint;

/**
 * Created by Luis on 02/05/2015.
 */
public class BalanceActivity extends Activity {

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        ArrayList<Cuentas> lista_cuentas = new ArrayList<>();
        Cuentas cuenta1 = new Cuentas(1321, "Luis", "Luis",
                "Casabuena Gomez", "76650720T", "ES8023100001180012002356",302.83f,2,"asd","fgh");
        Cuentas cuenta2 = new Cuentas(1321, "Luis", "Luis",
                "Casabuena Gomez", "76650720T", "ES8023100001180012002341",1543.12f,2,"asd","fgh");

        lista_cuentas.add(cuenta1);
        lista_cuentas.add(cuenta2);

        lista = (ListView) findViewById(R.id.lista_balance);
        lista.setAdapter(new Lista_adaptador(this, R.layout.balance, lista_cuentas) {

            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_contacto = (TextView) view.findViewById(R.id.textView_nombre);
                    if (texto_contacto != null)
                        texto_contacto.setText(((Cuentas) entrada).getNAME() + " " + ((Cuentas) entrada).getLASTNAME());

                    TextView texto_telefono = (TextView) view.findViewById(R.id.textView_balance);
                    if (texto_telefono != null) {
                        float aux = ((Cuentas) entrada).getBALANCE();
                        texto_telefono.setText("Balance: " + Float.toString(aux) + " euros");
                    }

                    TextView texto_email = (TextView) view.findViewById(R.id.textView_iban);
                    if (texto_email != null)
                        texto_email.setText(((Cuentas) entrada).getIBAN());

                    TextView texto_ID = (TextView) view.findViewById(R.id.textView_ID);
                    if (texto_ID != null)
                        texto_ID.setText(Integer.toString(((Cuentas) entrada).getID()));

                }
            }
        });

        Line l = new Line();
        LinePoint p = new LinePoint();
        p.setX(0);
        p.setY(4);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(2);
        p.setY(8);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(4);
        p.setY(6);
        l.addPoint(p);
        l.setColor(Color.parseColor("#003366"));

        LineGraph li = (LineGraph) findViewById(R.id.linegraph);
        li.addLine(l);
        li.setRangeY(0, 10);
        li.setLineToFill(0);

        li.setOnPointClickedListener(new LineGraph.OnPointClickedListener(){

            @Override
            public void onClick(int lineIndex, int pointIndex) {
                // TODO Auto-generated method stub

            }

        });
    }

}