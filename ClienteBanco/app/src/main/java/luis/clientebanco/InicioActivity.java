package luis.clientebanco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InicioActivity extends Activity {

    private final static int CUENTA = 0;
    private final static int BALANCE = 1;
    private final static int OPERACIONES = 2;
    private final static int AJUSTES = 3;

    public void clicEnBoton_IrCuenta (View view) {
        Intent intent = new Intent(this, CuentaActivity.class);
        startActivityForResult(intent, CUENTA);
    }
    public void clicEnBoton_IrBalance (View view) {
        Intent intent = new Intent(this, BalanceActivity.class);
        startActivityForResult(intent, BALANCE);
    }
    public void clicEnBoton_IrOperaciones (View view) {
        Intent intent = new Intent(this, OperacionesActivity.class);
        startActivityForResult(intent, OPERACIONES);
    }

    public void clicEnBoton_IrAjustes (View view) {
        //Intent intent = new Intent(this, AjustesActivity.class);
        Intent intent = new Intent(this, AjustesActivity.class);
        startActivityForResult(intent, AJUSTES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    */

}
