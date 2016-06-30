package br.edu.ifspsaocarlos.sdm.asynctask;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btAcessarWs;
    private ProgressBar mProgress;
    private EditText edUrl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAcessarWs = (Button) findViewById(R.id.bt_acessar_ws);
        btAcessarWs.setOnClickListener(this);
        mProgress = (ProgressBar) findViewById(R.id.pb_carregando);
        edUrl = (EditText) findViewById(R.id.ed_url);
    }
    public void onClick(View v) {
        if (v == btAcessarWs) {
            buscarJson(edUrl.getText().toString());
        }
    }
    private void buscarJson(String url){
        AsyncTask<String, Void, JSONObject> tarefa = new AsyncTask<String, Void, JSONObject>() {
            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.setVisibility(View.VISIBLE);
            }
            protected JSONObject doInBackground(String... params) {
                JSONObject jsonObject = null;
                StringBuilder sb = new StringBuilder();
                try{
                    HttpURLConnection conexao =
                            (HttpURLConnection) (new URL(params[0])).openConnection();
                    if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = conexao.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String temp;
                        while ((temp = br.readLine()) != null) {
                            sb.append(temp);
                        }
                    }
                    jsonObject = new JSONObject(sb.toString());
                }
                catch (IOException ioe) {
                    Log.e("SDM", getString(br.edu.ifspsaocarlos.sdm.asynctask.R.string.msg_erro));
                }
                catch (JSONException jsone) {
                    Log.e("SDM", getString(br.edu.ifspsaocarlos.sdm.asynctask.R.string.msg_erro_json));
                }
                return jsonObject;
            }
            protected void onPostExecute(JSONObject s) {
                String texto;
                super.onPostExecute(s);

                if(s == null)
                    texto = getString(R.string.msg_erro_url);
                else
                    texto = s.toString();

                ((TextView) findViewById(R.id.tv_texto)).setText(texto);

                mProgress.setVisibility(View.GONE);
            }
        };
        tarefa.execute(url);
    }
}