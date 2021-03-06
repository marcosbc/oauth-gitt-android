package luis.clientebanco;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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

    private static final Uri URI_CUENTAS = Uri.parse(
            "content://luis.contentprovider/cuentas");
    private static final Uri URI_TRANSACCIONES = Uri.parse(
            "content://luis.contentprovider/transacciones");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones);

        ArrayList<Transacciones> lista_transacciones = recuperarTransacciones();

        dibujarGrafico(lista_transacciones);

        lista = (ListView) findViewById(R.id.lista_operaciones);
        lista.setAdapter(new Lista_adaptador(this, R.layout.operaciones, lista_transacciones) {

            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    float _quantity = 0;
                    TextView texto_descrip = (TextView) view.findViewById(R.id.textView_descrip);
                    if (texto_descrip != null) {
                        texto_descrip.setText(((Transacciones) entrada).getFROMDESCRIP());

                        Button boton  = (Button) view.findViewById(R.id.imageView_button);
                        int categoria = determinarCategoria(((Transacciones) entrada).getFROMDESCRIP());
                        switch (categoria) {
                            case 0:
                                boton.setBackgroundColor(Color.CYAN);
                                break;
                            case 1:
                                boton.setBackgroundColor(Color.BLUE);
                                break;
                            case 2:
                                boton.setBackgroundColor(Color.GREEN);
                                break;
                            case 3:
                                boton.setBackgroundColor(Color.MAGENTA);
                                break;
                            case 4:
                                boton.setBackgroundColor(Color.YELLOW);
                                break;
                            case 5:
                                boton.setBackgroundColor(Color.RED);
                                break;
                        }

                    }
                    TextView texto_quantity = (TextView) view.findViewById(R.id.textView_quantity);
                    if (texto_quantity != null) {
                         _quantity= ((Transacciones) entrada).getQUANTITY();
                        texto_quantity.setText("Cantidad: " + Float.toString(_quantity) + " euros");
                    }

                    TextView texto_iban = (TextView) view.findViewById(R.id.textView_fromiban);
                    if (texto_iban != null)
                        texto_iban.setText(((Transacciones) entrada).getFROMIBAN());

                    TextView texto_date = (TextView) view.findViewById(R.id.textView_date);
                    if (texto_date != null)
                        texto_date.setText(((Transacciones) entrada).getDATE());

                    TextView texto_ID = (TextView) view.findViewById(R.id.textView_ID);
                    if (texto_ID != null)
                        texto_ID.setText(Integer.toString(((Transacciones) entrada).getCUENTAID()));

                }
            }
        });

    }

    public ArrayList<Transacciones> recuperarTransacciones() {

        ContentResolver CR = getContentResolver();
        Cursor c;

        ArrayList<Transacciones> lista_transacciones = new ArrayList<Transacciones>();

        String[] valores_recuperar = {"_id", "userId", "to_descr", "from_descr", "quantity", "from_iban",
                "to_iban", "date", "status"};
        c = CR.query(URI_TRANSACCIONES, valores_recuperar, null, null, null);

        if(c!=null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                int _id = c.getInt(0);
                int _userId = c.getInt(1);
                String _to_descr = c.getString(2);
                String _from_descr = c.getString(3);
                float _quantity = c.getFloat(4);
                String _from_iban = c.getString(5);
                String _to_iban = c.getString(6);
                String _date = c.getString(7);
                String _status = c.getString(8);

                Transacciones transaccion = new Transacciones(_id, _userId, _to_descr, _from_descr, _quantity
                        , _from_iban, _to_iban, _date, _status);

                lista_transacciones.add(transaccion);
            } while (c.moveToNext());
        }

        return lista_transacciones;
    }

    public void dibujarGrafico(ArrayList<Transacciones> lista_transacciones){

        PieGraph pg = (PieGraph) findViewById(R.id.piegraph);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.CYAN);
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.BLUE);
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.GREEN);
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.MAGENTA);
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.YELLOW);
        slice.setValue(0);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.RED);
        slice.setValue(0);
        pg.addSlice(slice);

        pg.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener(){

            @Override
            public void onClick(int index) {

            }

        });

        for(int i=0;i<lista_transacciones.size();i++) {
            String valorDescr = lista_transacciones.get(i).getFROMDESCRIP();
            int categoria = determinarCategoria(valorDescr);

            slice = pg.getSlice(categoria);
            float valorSlice = slice.getValue();
            slice.getColor();
            float valorTransaccion = lista_transacciones.get(i).getQUANTITY();
            slice.setValue(valorSlice + valorTransaccion);


        }
    }

    public int determinarCategoria(String desc) {
        int cat = 0; //otros    CIAN
        if(desc.contains("CARREFOUR") || desc.contains("MERCADONA") || desc.contains("DIA") || desc.contains("SUPERMERCADOS"))
            cat = 1; //comida   AZUL
        else if(desc.contains("DECATHLON"))
            cat = 2; //ropa VERDE
        else if(desc.contains("PEAJE"))
            cat = 3; // peajes  MAGENTA
        else if(desc.contains("APPLE"))
            cat = 4; // tecnologia  AMARILLO
        else if(desc.contains("TRANSFERENCIA"))
            cat = 5; //transferencias   ROJO

        return cat;
    }
}