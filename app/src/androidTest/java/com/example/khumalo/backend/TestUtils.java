package com.example.khumalo.backend;


import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONException;

import java.util.List;

import static com.google.maps.android.PolyUtil.decode;

/**
 * Created by KHUMALO on 8/23/2016.
 */
public class TestUtils extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testUtils(){
        MainActivity main = new MainActivity();
        final String ORIGIN_PARAM = "origin";
        final String DESTINATION_PARAM = "destination";
        final String KEY_PARAM = "key";
        final String REGION_PARAM = "region";



        Uri builtUri = Uri.parse(main.FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(ORIGIN_PARAM, main.ORIGIN)
                .appendQueryParameter(DESTINATION_PARAM,main.DESTINATION)
                .appendQueryParameter(REGION_PARAM, main.REGION_PARAM_COUNTRY)
                .appendQueryParameter(KEY_PARAM, "AIzaSyA_cdmixH5iQvs5x4AD6LSjrCJ10WOM3bI").build();
        String result = main.DownloadDirections(builtUri.toString());
        List<LatLng> polyToTest = null;
        try {
            polyToTest = decode(main.getPolyLineCode(result));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertNotNull("This is should not be null",polyToTest);
        LatLng firtLocation = new LatLng(-33.921465,18.477669);

        LatLng lastLocation  = new LatLng(-33.892480,18.515379);

        assertTrue("This is should be True", Utils.isFoundAlongTheJourney(polyToTest, firtLocation, lastLocation));
    }



    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
