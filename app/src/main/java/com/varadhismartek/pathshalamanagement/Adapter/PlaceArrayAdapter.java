package com.varadhismartek.pathshalamanagement.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by varadhi5 on 1/12/17.
 */

public class PlaceArrayAdapter extends ArrayAdapter<PlaceArrayAdapter.PlaceAutoComplete> implements Filterable {


    private static final String TAG = "Addroute";
    private AutocompleteFilter mPlaceFilter;
    private LatLngBounds mBounds;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<PlaceAutoComplete> mResultList=new ArrayList<>();

    public PlaceArrayAdapter(Context context, int resource, LatLngBounds latLngBounds,AutocompleteFilter filter){
        super(context,resource);
        this.mPlaceFilter=filter;
        this.mBounds=latLngBounds;

    }

    public void setmGoogleApiClient(GoogleApiClient googleApiClient){

        if (googleApiClient==null||!googleApiClient.isConnected()){
            mGoogleApiClient=null;
        }
        else {
            mGoogleApiClient=googleApiClient;
        }

    }


    //Returns the number of results received in the last autocomplete query.
    @Override
    public int getCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    @Nullable
    @Override
    public PlaceAutoComplete getItem(int position) {
        return mResultList.get(position);
    }

    private ArrayList<PlaceAutoComplete> getPrediction(CharSequence constraint)
    {
        if (mGoogleApiClient!=null){
            Log.i(TAG, "Executing autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            // Wait for predictions, set the timeout.for a result of api
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error: " + status.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting place predictions: " + status
                        .toString());
                autocompletePredictions.release();
                return null;

            }


            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");


            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new PlaceAutoComplete(prediction.getPlaceId(),
                        prediction.getFullText(null)));
            }

            // Buffer release
            autocompletePredictions.release();
            return resultList;
        }

        Log.d(TAG,"Google api is not connected");
        return null;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {


            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults=new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (charSequence!=null){

                    // Query the autocomplete API for the (constraint) search string.
                    mResultList=getPrediction(charSequence);

                    if (mResultList!=null){
                        filterResults.values=mResultList;
                        filterResults.count=mResultList.size();
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                // The API returned at least one result, update the data.
                if (filterResults!=null&&filterResults.count>0){
                    notifyDataSetChanged();
                }
                else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetChanged();
                }

            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {

                // Override this method to display a readable result in the AutocompleteTextView when clicked.
                if (resultValue instanceof PlaceAutoComplete){
                    return ((PlaceAutoComplete)resultValue).Description;
                }
                else {
                    return super.convertResultToString(resultValue);
                }

            }
        };

        return filter;

    }

    public class PlaceAutoComplete {

        public CharSequence PlaceId;
        public CharSequence Description;

        PlaceAutoComplete(CharSequence placeId,CharSequence description){
            this.PlaceId=placeId;
            this.Description=description;
        }


        @Override
        public String toString()
        {
            return Description.toString();

        }
    }
}
