package contacts.contactsapptest_james;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This is the main class. It downloads json from the server, puts it into java User objects
 * these user objects are put into an array list. This array list is used in the arrayAdapter for the list view
 *
 */
public class ListUsers extends AppCompatActivity implements Serializable {

    private List<User> list;
    private ListView listView;
    private JSONArray jsonArray;
    private ListUsersArrayAdapter arrayAdapter;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //change the action and notification bar colour
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#303f97"));

        //empty list of java objects, of the downloaded users
        list = new ArrayList<>();

        try {
            //start download
            URL url = new URL("http://jsonplaceholder.typicode.com/users");
            new DownloadUsers().execute(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void handleJsonResult(JSONArray arr) {
        try {
            jsonArray = arr;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                //format the address and company text
                JSONObject add = new JSONObject(jsonObject.getString("address"));
                String address = add.getString("street") + ", " + add.getString("suite") + "\n" + add.getString("city") + ", " + add.getString("zipcode");
                JSONObject com = new JSONObject(jsonObject.getString("company"));
                String company = com.getString("name") + "\n" + com.getString("catchPhrase") + "\n" + com.getString("bs");

                list.add(new User(jsonObject.getString("name"), jsonObject.getString("username"), jsonObject.getString("email"), jsonObject.getString("phone"), address, jsonObject.getString("website"), company));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        sortListAscending();

        //create new array adapter and assign it to the list view
        listView = (ListView) findViewById(R.id.listUsers);
        arrayAdapter = new ListUsersArrayAdapter(this, R.layout.row, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,long id) {

                Intent i = new Intent(getApplicationContext(), DisplayUserDetail.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("user", list.get(position));
                i.putExtras(bundle);

                startActivity(i);

            }

        });

    }


    private void sortListAscending() {
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<User>() {
                @Override
                public int compare(User object1, User object2) {
                    return object1.name.compareTo(object2.name);
                }
            });
        }
    }

    private void sortListDescending() {
        if (list.size() > 0) {
            Collections.sort(list, new Comparator<User>() {
                @Override
                public int compare(User object1, User object2) {
                    return object2.name.compareTo(object1.name);
                }
            });
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

        if (id == R.id.az) {
            sortListAscending();
            arrayAdapter.notifyDataSetChanged();
        }
        if (id == R.id.za) {
            sortListDescending();
            arrayAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }


    class DownloadUsers extends AsyncTask<URL, Void, JSONArray> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ListUsers.this);
            progressDialog.setMessage("Loading Contacts...");
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONArray doInBackground(URL... urls) {
            URL url = urls[0];
            StringBuilder response = new StringBuilder();


            try {

                HttpURLConnection httpConnection = null;
                httpConnection = (HttpURLConnection) url.openConnection();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()), 8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }

                    input.close();
                }

                jsonArray = new JSONArray(response.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;

        }


        @Override
        protected void onPostExecute(JSONArray arr) {
            progressDialog.dismiss();
            handleJsonResult(arr);

        }
    }


}
