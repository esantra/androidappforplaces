package com.placesandplaces;

// code taken/modified from 
// https://github.com/gmarz/android-google-places/blob/master/src/org/gmarz/googleplaces/models/PlaceReview.java
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class PlacePhoto implements Parcelable {

    // Width of the Photo
    int mWidth=0;
 
    // Height of the Photo
    int mHeight=0;
 
    // Reference of the photo to be used in Google Web Services
    String mPhotoReference="";
 
    // Attributions of the photo
    // Attribution is a Parcelable class
    Attribution[] mAttributions={};
 
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
 
    /** Writing Photo object data to Parcel */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
        dest.writeString(mPhotoReference);
        dest.writeParcelableArray(mAttributions, 0);
    }
 
    public PlacePhoto(){
    }
 
    /** Initializing Photo object from Parcel object */
    private PlacePhoto(Parcel in){
        this.mWidth = in.readInt();
        this.mHeight = in.readInt();
        this.mPhotoReference = in.readString();
        this.mAttributions = (Attribution[])in.readParcelableArray(Attribution.class.getClassLoader());
    }
 
    /** Generates an instance of Place class from Parcel */
    public static final Parcelable.Creator<PlacePhoto> CREATOR = new Parcelable.Creator<PlacePhoto>() {
        @Override
        public PlacePhoto createFromParcel(Parcel source) {
            return new PlacePhoto(source);
        }
 
        @Override
        public PlacePhoto[] newArray(int size) {
            // TODO Auto-generated method stub
            return null;
        }
    };
}
