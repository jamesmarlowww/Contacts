package contacts.contactsapptest_james;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Object http;
    String stringUrl = "http://jsonplaceholder.typicode.com/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            URL url = new URL("http://jsonplaceholder.typicode.com/users");
            String str = new DownloadUsers().execute(url).get();


            TextView tv = (TextView) findViewById(R.id.text);
            JSONArray ob = new JSONArray(str);


            Toast.makeText(getApplicationContext(), "toast is working from here", Toast.LENGTH_LONG).show();
            tv.setText(ob.toString());
//            Toast.makeText(getApplicationContext(), ob.getJSONObject("1").toString() + " : ", Toast.LENGTH_LONG).show();




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    class DownloadUsers extends AsyncTask<URL, Void, String> {
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String s ="";
            try {

                StringBuilder response = new StringBuilder();

                HttpURLConnection httpconn = null;

                httpconn = (HttpURLConnection) url.openConnection();


                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                s = response.toString();



            } catch (IOException e){
                e.printStackTrace();
            }



            return s;
    }


}

}
