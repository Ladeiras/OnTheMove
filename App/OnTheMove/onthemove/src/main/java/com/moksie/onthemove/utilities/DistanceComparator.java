package com.moksie.onthemove.utilities;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.moksie.onthemove.objects.Airport;

import java.util.Comparator;

/**
 * Created by belh0 on 30-04-2014.
 */
public class DistanceComparator implements Comparator<Airport>
{
    Context context;
    double latitude, longitude;

    public DistanceComparator(Context context, double latitude, double longitude)
    {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int compare(Airport a1, Airport a2)
    {
        float distance1, distance2;

        Location location0 = new Location("Current");
        location0.setLatitude(latitude);
        location0.setLongitude(longitude);

        Location location1 = new Location("A1");
        location1.setLatitude(a1.getLatitude());
        location1.setLongitude(a1.getLatitude());

        Location location2 = new Location("A2");
        location2.setLatitude(a2.getLatitude());
        location2.setLongitude(a2.getLatitude());

        distance1 = location0.distanceTo(location1);
        distance2 = location0.distanceTo(location2);

        if(distance1 < distance2)
            return -1;

        if(distance1 > distance2)
            return 1;

        Log.w("FLAG", "4");
        return 0;
    }
}
