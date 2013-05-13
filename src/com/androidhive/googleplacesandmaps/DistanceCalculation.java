package com.androidhive.googleplacesandmaps;

import java.lang.*;
import java.io.*;
import java.math.*;
public class DistanceCalculation {

public static void main(String[] args)
{
    double lon11 =-97.116121;
      double lat11=32.734642;

      double lon22=-97.111658;
      double lat22=32.731918;

    //   calculateDistance(lat11,lon11, lat22, lon22);
      distFrom(lat11, lon11, lat22, lon22);

}
public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
    double earthRadius = 3958.75;
    double dLat = Math.toRadians(lat2-lat1);
    double dLng = Math.toRadians(lng2-lng1);
    double sindLat = Math.sin(dLat / 2);
    double sindLng = Math.sin(dLng / 2);
    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    double dist = earthRadius * c;
    System.out.println(dist);
    return dist;
    }
}