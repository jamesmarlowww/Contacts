package contacts.contactsapptest_james;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 8/11/2015.
 *
 * I used a list view instead of adding multiple TextView's. Because list view's are scalable
 * There is a inner class of MoreUserInfo. The ArrayAdapter users an ArrayList of MoreUserInfo
 *
 */
public class DisplayUserDetail extends AppCompatActivity {


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#303f97"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle data = getIntent().getExtras();
        User user = (User) data.getParcelable("user");

        ListView listView = (ListView) findViewById(R.id.listInfo);
        MoreInfoArrayAdapter arrayAdapter = new MoreInfoArrayAdapter(this, R.layout.row, createArrayFromUser(user));
        listView.setAdapter(arrayAdapter);

        getSupportActionBar().setTitle(user.name);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        finish();
        return super.onOptionsItemSelected(item);
    }

    //add each piece of row info to the List
    private List<MoreUserInfo> createArrayFromUser(User user) {
        List<MoreUserInfo> info = new ArrayList<>();
        info.add(new MoreUserInfo(user.username, "USERNAME"));
        info.add(new MoreUserInfo(user.phone, "PHONE"));
        info.add(new MoreUserInfo(user.address, "ADDRESS"));
        info.add(new MoreUserInfo(user.website, "WEBSITE"));
        info.add(new MoreUserInfo(user.company, "COMPANY"));
        return info;
    }

    //this class is needed to break the user info an arrayList, for the arrayAdapter
    public class MoreUserInfo {
        private String subtitle;
        private String info;

        public MoreUserInfo(String info, String subtitle) {
            this.info = info;
            this.subtitle = subtitle;
        }

    }

    public class MoreInfoArrayAdapter extends ArrayAdapter<MoreUserInfo> {

        private List<MoreUserInfo> list;
        private int resourceID;
        private Context context;

        public MoreInfoArrayAdapter(Context context, int resourseID, List<MoreUserInfo> list) {
            super(context, resourseID, list);
            this.context = context;
            this.resourceID = resourseID;
            this.list = list;
        }

        @Override
        public View getView(final int pos, View convertView, ViewGroup parent) {
            View rowView = convertView;

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(resourceID, parent, false);

            TextView name = (TextView) rowView.findViewById(R.id.mainMessage);
            TextView email = (TextView) rowView.findViewById(R.id.subTitle);

            name.setText(list.get(pos).info);
            email.setText(list.get(pos).subtitle);
            return rowView;
        }
    }


}
