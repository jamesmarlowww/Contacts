package contacts.contactsapptest_james;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import contacts.contactsapptest_james.R;

/**
 * Created by james on 8/11/2015.
 */
public class ListUsersArrayAdapter extends ArrayAdapter<ListUsers.User> {

    private List<ListUsers.User> list;
    private int resorurseID;
    private Context context;

    public ListUsersArrayAdapter(Context context, int resourseID, List<ListUsers.User> list) {
        super(context, resourseID, list);
        this.context = context;
        this.resorurseID = resourseID;
        this.list = list;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        View rowView = convertView;


//        if(rowView == null) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        rowView = inflater.inflate(resorurseID, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.username);
        TextView email = (TextView) rowView.findViewById(R.id.subTitle);

        name.setText(list.get(pos).name);
        email.setText(list.get(pos).email);
        return  rowView;

    }
}
