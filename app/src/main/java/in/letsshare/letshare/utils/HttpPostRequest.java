package in.letsshare.letshare.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vishng on 26/11/17.
 */

public class HttpPostRequest extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "POST";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String urlParameters = params[1];
        String result=null;
        String inputLine;
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);


            // Send post request
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
        }
        catch (Exception e)
        {
          e.printStackTrace();
          result = e.getMessage();
        }
        finally {
            return result;
        }

    }
}