package com.example.khumalo.backend;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by KHUMALO on 8/23/2016.
 */
public class Utils {
    private static final double TOLERANCE_IN_METERS = 300;


    //Testing if the two points are found on the path
    public static boolean isFoundAlongTheJourney(List<LatLng> journey,LatLng origin, LatLng destination){

        return isInThePath(journey,origin) && isInThePath(journey,destination);
    }

    //Testing a single if a single point is found on the path
    public static boolean isInThePath(List<LatLng> journey, LatLng point) {
        Location location = convertLatLngToLocation(point);
        for(LatLng wayPoint: journey){
            Location pathPoint = convertLatLngToLocation(wayPoint);
            if(location.distanceTo(pathPoint)<=TOLERANCE_IN_METERS){
                return true;
            }
        }
        return false;
    }

    //Converting a LatLong to a Location Object
    public static Location convertLatLngToLocation(LatLng latLng) {
        Location loc = new Location("someLoc");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        return loc;
    }
}
