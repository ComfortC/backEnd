package com.example.khumalo.backend;

import android.net.Uri;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.google.maps.android.PolyUtil.decode;

public class MainActivity extends AppCompatActivity {


    public static final String FORECAST_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    public static final String REGION_PARAM_COUNTRY = "za";
    public static final String ORIGIN = "Kenilworth";
    public static final String DESTINATION = "Bellville";
    List<LatLng> polylineToDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadData();
    }

    public void downloadData() {
        new DownloadRawData().execute();
    }


    ///Async Task for the to destination

    public class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Tag", "AsyncTask Called");
            final String ORIGIN_PARAM = "origin";
            final String DESTINATION_PARAM = "destination";
            final String KEY_PARAM = "key";
            final String REGION_PARAM = "region";



            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(ORIGIN_PARAM, ORIGIN)
                    .appendQueryParameter(DESTINATION_PARAM, DESTINATION)
                    .appendQueryParameter(REGION_PARAM, REGION_PARAM_COUNTRY)
                    .appendQueryParameter(KEY_PARAM, getBaseContext().getString(R.string.ApiKey)).build();
            String Directions = DownloadDirections(builtUri.toString());

            return Directions;
        }


        @Override
        protected void onPostExecute(String res) {
            String thisLocationToDestination = "";

            Log.d("Tag", "The polyline returned is "+ res);
            try {
                thisLocationToDestination = getPolyLineCode(res);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            polylineToDestination = decode(thisLocationToDestination);


        }



    }


    public String DownloadDirections(String Uri) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL(Uri);
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return "";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return "";
            }

            return buffer.toString();


        } catch (IOException e) {
            Log.e("Tag", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return " ";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Tag", "Error closing stream", e);
                }
            }
        }
    }

    //Polyline
    public  String getPolyLineCode(String geoJson) throws JSONException {
        JSONObject parser = new JSONObject(geoJson);
        JSONArray contents = parser.getJSONArray("routes");
        JSONObject route = contents.getJSONObject(0);
        JSONObject polyline = route.getJSONObject("overview_polyline");

        return polyline.getString("points");
    }

}
