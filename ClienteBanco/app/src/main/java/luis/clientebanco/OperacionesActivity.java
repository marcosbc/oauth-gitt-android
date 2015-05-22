package luis.clientebanco;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import luis.clientebanco.holographlibrary.PieGraph;
import luis.clientebanco.holographlibrary.PieSlice;

/**
 * Created by Luis on 02/05/2015.
 */
public class OperacionesActivity extends Activity {

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones);

        ArrayList<Transacciones> lista_transacciones = new ArrayList<>();
        Transacciones transaccion1 = new Transacciones(1321, "Alquiler", "",
                302.83f, "ES8023100001180012002356", "16-03-2015","Success");
        Transacciones transaccion2 = new Transacciones(1321, "Coche", "",
                1543.12f, "ES8023100001180012002341", "22-04-2015","Success");
        Transacciones transaccion3 = new Transacciones(1321, "Portatil", "",
                452.33f, "ES8023100001180012002341", "06-05-2015","Success");

        lista_transacciones.add(transaccion1);
        lista_transacciones.add(transaccion2);
        lista_transacciones.add(transaccion3);

        lista = (ListView) findViewById(R.id.lista_operaciones);
        lista.setAdapter(new Lista_adaptador(this, R.layout.operaciones, lista_transacciones) {

            int contador = 0;
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_descrip = (TextView) view.findViewById(R.id.textView_descrip);
                    if (texto_descrip != null)
                        texto_descrip.setText(((Transacciones) entrada).getTODESCRIP());

                    TextView texto_quantity = (TextView) view.findViewById(R.id.textView_quantity);
                    if (texto_quantity != null) {
                        float aux = ((Transacciones) entrada).getQUANTITY();
                        texto_quantity.setText("Cantidad: " + Float.toString(aux) + " euros");
                    }

                    TextView texto_iban = (TextView) view.findViewById(R.id.textView_fromiban);
                    if (texto_iban != null)
                        texto_iban.setText(((Transacciones) entrada).getFROMIBAN());

                    TextView texto_date = (TextView) view.findViewById(R.id.textView_date);
                    if (texto_date != null)
                        texto_date.setText(((Transacciones) entrada).getDATE());

                    Button boton  = (Button) view.findViewById(R.id.imageView_button);
                    if(contador == 0)
                        boton.setBackgroundColor(Color.parseColor("#AA66CC"));
                    else if(contador == 10)
                        boton.setBackgroundColor(Color.parseColor("#AA66CC"));
                    else if(contador == 20)
                        boton.setBackgroundColor(Color.parseColor("#AA66CC"));
                    contador = contador + 1;

                    TextView texto_ID = (TextView) view.findViewById(R.id.textView_ID);
                    if (texto_ID != null)
                        texto_ID.setText(Integer.toString(((Transacciones) entrada).getCUENTAID()));

                }
            }
        });

        PieGraph pg = (PieGraph) findViewById(R.id.piegraph);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        slice.setValue(2);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        slice.setValue(3);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#AA66CC"));
        slice.setValue(8);
        pg.addSlice(slice);

        pg.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener(){

            @Override
            public void onClick(int index) {

            }

        });
    }

}