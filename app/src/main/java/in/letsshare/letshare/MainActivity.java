package in.letsshare.letshare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONObject;

import java.util.Locale;

import in.letsshare.letshare.utils.HttpGetRequest;
import in.letsshare.letshare.utils.HttpPostRequest;

import static android.R.attr.duration;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.util.Log.d;
import static in.letsshare.letshare.R.id.date;

public class MainActivity extends Activity {

    private static final int PLACE_PICKER_REQUEST = 1;
    private EditText mFrom;
    private EditText mTo;
    private EditText updateField;
    EditText dateField;
    EditText timeField;
    Calendar myCalendar ;
    DatePickerDialog.OnDateSetListener date;
    TextView message;
    Button submit;
    Spinner vehicleType;
    ProgressBar spinner;
    PopupWindow mPopupWindow;
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
        mFrom= (EditText) findViewById(R.id.from);
        mTo=(EditText) findViewById(R.id.to);
        myCalendar = Calendar.getInstance();
        vehicleType = (Spinner) findViewById(R.id.vehicle);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        mLinearLayout = (LinearLayout) findViewById(R.id.lr);


        mFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();

                    Intent intent = intentBuilder.build(MainActivity.this);
                    MainActivity.this.updateField=mFrom;
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();

                    Intent intent = intentBuilder.build(MainActivity.this);
                    MainActivity.this.updateField=mTo;
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        dateField = (EditText) findViewById(R.id.date);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeField = (EditText) findViewById(R.id.time);
        timeField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeField.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute,false);//Yes 24 hour time
                mTimePicker.show();

            }
        });
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Some url endpoint that you may have
                //spinner.setVisibility(View.VISIBLE);
                try {
                    String myUrl = "http://192.168.1.5:9000/rides";
                    String result;
                    HttpPostRequest postRequest = new HttpPostRequest();
                    JSONObject requestBody= new JSONObject();
                    requestBody.put("fromLocation",mFrom.getText());
                    requestBody.put("toLocation",mTo.getText());
                    requestBody.put("vehicleType","car");
                    requestBody.put("date",dateField.getText());
                    requestBody.put("time",timeField.getText());
                    requestBody.put("description","hello");
                    result = postRequest.execute(myUrl,requestBody.toString()).get();
                    if(result!=null)
                    {
                        String display_message= null;
                        if(result.contains("err"))
                        {
                          display_message = "OOPS!! SOMETHING WENT WRONG";

                        }
                        else{
                            display_message = "THANKS FOR SHARING RIDE";
                            mFrom.setText("");
                            mTo.setText("");
                            dateField.setText("");
                            timeField.setText("");

                        }
                        Toast toast = Toast.makeText(getApplicationContext(),display_message,Toast.LENGTH_LONG);
                        toast.show();

                    }




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });



    }

    private void updateLabel()
    {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateField.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, this);

                String place = selectedPlace.getName().toString();
                String address = selectedPlace.getAddress().toString();
                this.updateField.setText(address);
                // Do something with the place
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);

            }

        }
    }
}
