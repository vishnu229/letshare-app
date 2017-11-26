package in.letsshare.letshare;

import android.app.Activity;

import android.os.Bundle;

import org.json.JSONObject;

public class RideListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_list);
   
        //Read more: http://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz4y8ZPK48O
        //JSONObject rideListObject = new JSONObject("\{\"test\":\"test\"\}");

    }
}
