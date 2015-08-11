package contacts.contactsapptest_james;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListUsers extends AppCompatActivity {

    private Object http;
    String stringUrl = "http://jsonplaceholder.typicode.com/users";
    private List<User> list;
    private ListView listView;
    private ListUsersArrayAdapter arrayAdapter;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //change the action bar and notification bar color
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#303f97"));


        list = new ArrayList<>();

        try {
            URL url = new URL("http://jsonplaceholder.typicode.com/users");
            AsyncTask user = new DownloadUsers().execute(url);
            user.getStatus();
            String str = (String) user.get();


            TextView tv = (TextView) findViewById(R.id.text);
            JSONArray ob = new JSONArray(str);

            for (int i = 0; i < ob.length(); i++) {
                JSONObject jsonObject = new JSONObject(ob.get(i).toString());
                this.list.add(new User(jsonObject.getString("name"), jsonObject.getString("username"), jsonObject.getString("email"), jsonObject.getString("phone"), jsonObject.getString("address"), jsonObject.getString("website"), jsonObject.getString("company")));

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        sortListAscending();


        listView = (ListView) findViewById(R.id.listUsers);
        arrayAdapter = new ListUsersArrayAdapter(this, R.layout.row, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
                //code for onClick item.
                //

                createArrayFromUser(position);
                Intent i = new Intent(getApplicationContext(), DisplayUserInfo.class);
                startActivity(i);

            }

        });

    }

    private List<UserInfo> createArrayFromUser(int i) {
        List<UserInfo> info = new ArrayList<>();
        User user = list.get(i);
        info.add(new UserInfo(user.username, "USERNAME"));
        info.add(new UserInfo(user.phone, "PHONE"));
        info.add(new UserInfo(user.address, "ADDRESS"));
        info.add(new UserInfo(user.website, "WEBSITE"));
        info.add(new UserInfo(user.company, "COMPANY"));
        return info;
    }

    private void sortListAscending() {
        if(list.size() >0) {
            Collections.sort(list, new Comparator<User>() {
                @Override
                public int compare(User object1, User object2) {
                    return object1.name.compareTo(object2.name);
                }
            });
        }
    }

    private void sortListDescending() {
        if(list.size() >0) {
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

    public class User {
        String username;
        String name;
        String email;
        String phone;
        String address;
        String website;
        String company;

        private User(String name, String username, String email, String phone, String address, String website, String company) {
            this.name = name;
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.website = website;
            this.company = company;
        }

    }

    class DownloadUsers extends AsyncTask<URL, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(ListUsers.this);


        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String responseStr = "";
            progressDialog.show();
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

                responseStr = response.toString();


            } catch (IOException e) {
                e.printStackTrace();
            }


            return responseStr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading Contacts...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    public class UserInfo implements Parcelable {
        private String subtitle;
        private String info;



        public UserInfo(String info, String subtitle) {
            this.info = info;
            this.subtitle = subtitle;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }


    }

}
