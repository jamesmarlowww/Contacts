package contacts.contactsapptest_james;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by james on 8/11/2015.
 */
public class User implements Parcelable{
    public String username;
    public String name;
    public String email;
    public String phone;
    public String address;
    public String website;
    public String company;

    public User(String name, String username, String email, String phone, String address, String website, String company) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.website = website;
        this.company = company;
    }

    protected User(Parcel in) {
        username = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        website = in.readString();
        company = in.readString();
    }

    public final static Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(website);
        dest.writeString(company);
    }
}
