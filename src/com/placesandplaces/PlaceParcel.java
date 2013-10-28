package com.placesandplaces;

//code inspired by: http://wptrafficanalyzer.in/blog/showing-nearby-places-with-photos-at-any-location-in-google-maps-android-api-v2/
//taken just to procure places photos

import android.os.Parcel;
import android.os.Parcelable;
 
public class PlaceParcel implements Parcelable{
    // Latitude of the place
    String mLat="";
 
    // Longitude of the place
    String mLng="";
 
    // Place Name
    String mPlaceName="";
 
    // Vicinity of the place
    String mVicinity="";
 
    // Photos of the place
    // Photo is a Parcelable class
    static PlacePhoto[] mPhotos={};
 
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
 
    /** Writing Place object data to Parcel */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLat);
        dest.writeString(mLng);
        dest.writeString(mPlaceName);
        dest.writeString(mVicinity);
        dest.writeParcelableArray(mPhotos, 0);
    }
 
    public PlaceParcel(){
    }
 
    /** Initializing Place object from Parcel object */
    private PlaceParcel(Parcel in){
        this.mLat = in.readString();
        this.mLng = in.readString();
        this.mPlaceName = in.readString();
        this.mVicinity = in.readString();
        this.mPhotos = (PlacePhoto[])in.readParcelableArray(PlacePhoto.class.getClassLoader());
    }
 
    /** Generates an instance of Place class from Parcel */
    public static final Parcelable.Creator<PlaceParcel> CREATOR = new Parcelable.Creator<PlaceParcel>(){
        @Override
        public PlaceParcel createFromParcel(Parcel source) {
            return new PlaceParcel(source);
        }
 
        @Override
        public PlaceParcel[] newArray(int size) {
            // TODO Auto-generated method stub
            return null;
        }
    };
}