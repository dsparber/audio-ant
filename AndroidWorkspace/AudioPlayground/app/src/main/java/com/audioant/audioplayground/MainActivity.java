package com.audioant.audioplayground;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ConnectToRserveTask().execute();
    }

    private class ConnectToRserveTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            TextView text = (TextView) findViewById(R.id.textView);

            try{
                RConnection connection = new RConnection("192.168.7.145");
                REXP x = connection.eval("R.version.string");
                System.out.println(x.asString());
            }
            catch(RserveException e){
                e.printStackTrace();
            } catch (REXPMismatchException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
