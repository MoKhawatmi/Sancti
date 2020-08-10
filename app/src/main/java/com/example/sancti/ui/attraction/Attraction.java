package com.example.sancti.ui.attraction;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sancti.R;
import com.example.sancti.adapters.PlacesAdapter;
import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.Coordinate;
import com.huawei.hms.site.api.model.HwLocationType;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.Site;
import com.huawei.hms.site.api.model.TextSearchRequest;
import com.huawei.hms.site.api.model.TextSearchResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Attraction extends Fragment {


    private SearchService searchService;
    String s;
    AutoCompleteTextView countrySearch;
    EditText keywordSearch;
    Spinner places;
    RecyclerView placesRecycler;
    ArrayList<Site> placesList;
    PlacesAdapter placeAdapter;

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data !=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject obj=new JSONObject(s);
                String results=obj.getString("results");

                //to handle multiple info as an array
                JSONArray arr=new JSONArray(results);
                String lat=arr.getJSONObject(0).getJSONObject("geometry").getString("lat");
                String lng=arr.getJSONObject(0).getJSONObject("geometry").getString("lng");
                String code=arr.getJSONObject(0).getJSONObject("components").getString("ISO_3166-1_alpha-2");

                Log.i("TAG lat",lat);
                Log.i("TAG lng",lng);
                Log.i("TAG code",code);

                findAttraction(Double.parseDouble(lat),Double.parseDouble(lng),code);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View v= inflater.inflate(R.layout.attraction_fragment, container, false);
        String[] countries = new String[]{"Albania", "Algeria", "Andorra", "Angola", "Anguilla", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana","Brazil","Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon","Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo","Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic","Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe","Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iraq", "Ireland", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Democratic People's Republic of Korea", "Republic of Korea", "Kuwait", "Kyrgyzstan", "Lao", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue","Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia ", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "Suriname", "Sweden", "Switzerland", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan","Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Yemen", "Zambia", "Zimbabwe"};

        countrySearch=v.findViewById(R.id.autoCompCountry);
        keywordSearch=v.findViewById(R.id.keywordSearch);
        placesRecycler=v.findViewById(R.id.placesRecycler);
        places=v.findViewById(R.id.placesSpinner);
        Button btn=v.findViewById(R.id.btn);
        placesList=new ArrayList<Site>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.select_dialog_item,countries);
        countrySearch.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get code and coordiante
                getCoCo(countrySearch.getText().toString());
                InputMethodManager m=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.hideSoftInputFromWindow(keywordSearch.getWindowToken(),0);
            }
        });

         placeAdapter=new PlacesAdapter(getContext(),placesList);
        LinearLayoutManager mng=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        placesRecycler.setLayoutManager(mng);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(placesRecycler.getContext(),
                mng.getOrientation()); //mng is the layout manager
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.divider));
        placesRecycler.addItemDecoration(dividerItemDecoration);
        placesRecycler.setAdapter(placeAdapter);

        return v;
    }

    public void getCoCo(String countryName){
        Log.i("country name",countryName);
        if(!placesList.isEmpty()){
            int count=placeAdapter.getItemCount();
            placesList.clear();
            placeAdapter.notifyItemRangeRemoved(0,count);
            placeAdapter.notifyDataSetChanged();
        }
        DownloadTask task=new DownloadTask();
        task.execute("https://api.opencagedata.com/geocode/v1/json?key=65f3f7d3cb834c3bbd5c62707ec9c326&q="+countryName);
    }

    //String place
    public void findAttraction(double lat,double lang,String countryCode){
        searchService = SearchServiceFactory.create(getContext(),  android.net.Uri.encode("CgB6e3x9sRVyS0oZJ74m/s8RFTewbGU7JWGO7J49f+UL8yKI3WvAjr2B+ze4LAmOKWSrLqZmgljI8ybqo8SA5pbk"));
        s="";
        TextSearchRequest request = new TextSearchRequest();
        request.setQuery(keywordSearch.getText().toString());

        Coordinate location = new Coordinate(lat, lang);
        request.setLocation(location);
        request.setRadius(50000);
        int position=places.getSelectedItemPosition();

        switch (position){
            case 0:
                request.setHwPoiType(HwLocationType.IMPORTANT_TOURIST_ATTRACTION);
                break;
            case 1:
                request.setHwPoiType(HwLocationType.ENTERTAINMENT_PLACE);
                break;
            case 2:
                request.setHwPoiType(HwLocationType.RESTAURANT);
                break;
            case 3:
                request.setHwPoiType(HwLocationType.HOTEL);
                break;
            case 4:
                request.setHwPoiType(HwLocationType.MUSEUM);
                break;
            case 5:
                request.setHwPoiType(HwLocationType.GENERAL_HOSPITAL);
                break;
            case 6:
                request.setHwPoiType(HwLocationType.AIRPORT);
                break;
            case 7:
                request.setHwPoiType(HwLocationType.BUS_STOP);
                break;
        }
        request.setCountryCode(countryCode);
        request.setLanguage("en");
        request.setPageIndex(1);
        request.setPageSize(5);

        SearchResultListener<TextSearchResponse> resultListener = new SearchResultListener<TextSearchResponse>() {
            // Return search results upon a successful search.
            @Override
            public void onSearchResult(TextSearchResponse results) {
                if (results == null || results.getTotalCount() <= 0) {
                    return;
                }
                List<Site> sites = results.getSites();
                if(sites == null || sites.size() == 0){
                    return;
                }
                for (Site site : sites) {
                    Log.i("TAG", String.format("siteId: '%s', name: %s\r\n", site.getSiteId(), site.getName()));
                    if(site.getAddress().getCountry().indexOf(countrySearch.getText().toString())!=-1) {
                        placesList.add(site);
                        placeAdapter.notifyItemInserted(placesList.size() - 1);
                    }
                }
                placeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onSearchError(SearchStatus status) {
                Log.i("TAG", "Error : " + status.getErrorCode() + " " + status.getErrorMessage());

            }
        };

        searchService.textSearch(request, resultListener);
    }


}