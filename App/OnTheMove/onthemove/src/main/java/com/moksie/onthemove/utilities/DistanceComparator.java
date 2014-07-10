package com.moksie.onthemove.utilities;

import android.content.Context;
import android.location.Location;

import com.moksie.onthemove.objects.Airport;

import java.util.Comparator;

/**
 * Esta classe que implementa Comparator, é usada para comparar distancias entre a posição atual e
 * a posição de dois aeroportos. Os valores devolvidas poderão ser usados para organizar um array
 * por ordem crescente de distancia.
 *
 * @author David Clemente
 * @author João Ladeiras
 * @author Ricardo Pedroso
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
        location1.setLatitude(a1.getLat());
        location1.setLongitude(a1.getLon());

        Location location2 = new Location("A2");
        location2.setLatitude(a2.getLat());
        location2.setLongitude(a2.getLon());

        distance1 = location0.distanceTo(location1);
        distance2 = location0.distanceTo(location2);

        if(distance1 < distance2)
            return -1;

        if(distance1 > distance2)
            return 1;

        return 0;
    }
}
