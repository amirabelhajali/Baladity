package tn.esprit.baladity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import tn.esprit.baladity.Drawer.MainActivity;
import tn.esprit.baladity.entities.Poubelle;

public class PassagePoubelleActivity extends AppCompatActivity {
    public static List<Poubelle> poubelleListe;

    RecyclerView recyclerView;


    ProgressBar progressBar;

    String HTTP_JSON_URL = Url.URLL+"GetListPassagePoubelle.php";
    String GET_JSON_FROM_SERVER_NAME_ADRESSE = "adresse";
    String GET_JSON_FROM_SERVER_NAME_TIME = "time";
    String GET_JSON_FROM_SERVER_NAME_LAT = "lat";
    String GET_JSON_FROM_SERVER_NAME_LONG = "longi";


    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    View ChildView ;
    public static Double a;
    public static Double l;
    int GetItemPosition ;
    RecyclerView.Adapter recyclerViewadapter;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;
    ArrayList<String> eventAdresse;
    ArrayList<String> eventTime;
    ArrayList<String> lat;
    ArrayList<String> longi;
    public static String longitude;
    public static String latitude;
    public List<Poubelle> listOfPoubelleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passage_poubelle);


        poubelleListe = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.poubelle_recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        recyclerView.setHasFixedSize(true);
        layoutManagerOfrecyclerView = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

        progressBar.setVisibility(View.VISIBLE);

        eventAdresse = new ArrayList<>();
        eventTime = new ArrayList<>();
        lat= new ArrayList<>();
        longi= new ArrayList<>();
        listOfPoubelleAdapter=new ArrayList<>();



        if (noData()){
            AlertDialog alertDialog = new AlertDialog.Builder(PassagePoubelleActivity.this).create();
            alertDialog.setTitle("Alerte");
            alertDialog.setMessage("Vos documents ne sont pas encore traités");
            // alertDialog.setIcon(R.drawable.);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(PassagePoubelleActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });

            alertDialog.show();
        }
        else {
            JSON_DATA_WEB_CALL();
        }
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(PassagePoubelleActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    Toast.makeText(PassagePoubelleActivity.this, lat.get(GetItemPosition), Toast.LENGTH_LONG).show();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });




    }
    public boolean noData(){

        if (listOfPoubelleAdapter.isEmpty()){
            return true;
        }
        return false;
    }
    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);

    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            Poubelle GetDataAdapter2 = new Poubelle();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setAdresse(json.getString(GET_JSON_FROM_SERVER_NAME_ADRESSE));
                GetDataAdapter2.setTime(json.getString(GET_JSON_FROM_SERVER_NAME_TIME));
                GetDataAdapter2.setAlt( json.getDouble(GET_JSON_FROM_SERVER_NAME_LAT));
                GetDataAdapter2.setLongi( json.getDouble(GET_JSON_FROM_SERVER_NAME_LONG));

                eventAdresse.add(json.getString(GET_JSON_FROM_SERVER_NAME_ADRESSE));
                eventTime.add(json.getString(GET_JSON_FROM_SERVER_NAME_TIME));
            //    latlong.add(json.getString(GET_JSON_FROM_SERVER_NAME_LATLONG));
                lat.add(json.getString(GET_JSON_FROM_SERVER_NAME_LAT));
                longi.add(json.getString(GET_JSON_FROM_SERVER_NAME_LONG));
for (int j=1 ; j<=poubelleListe.size();j++ ) {
    latitude = (String) lat.get(j);
    longitude = (String) longi.get(j);
    a = Double.parseDouble(latitude);
    l = Double.parseDouble(longitude);


}
            } catch (JSONException e) {

                e.printStackTrace();
            }
            poubelleListe.add(GetDataAdapter2);
        }

        recyclerViewadapter = new PoubelleAdapter(this,poubelleListe);

        recyclerView.setAdapter(recyclerViewadapter);

    }


}

class PoubelleAdapter extends RecyclerView.Adapter<PoubelleAdapter.ViewHolder>  {
    MapView mapView;

    Context context;
    List<Poubelle> lstPoubelle;
    public GoogleMap map;
    private final HashSet<MapView> mMaps = new HashSet<MapView>();

    public PoubelleAdapter( Context context, List<Poubelle> getDataAdapter) {
        super();
        this.lstPoubelle = getDataAdapter;

        this.context = context;
    }
    public HashSet<MapView> getMaps() {
        return mMaps;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_poubelle, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        System.out.print("ffffff");
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Poubelle getDataAdapter1 =  lstPoubelle.get(position);



        holder.eventAdresse.setText(getDataAdapter1.getAdresse());
        holder.eventTime.setText(getDataAdapter1.getTime());


        holder.initializeMapView();

        // Keep track of MapView
        mMaps.add(holder.mapView);
        // The map is already ready to be used



    }

    @Override
    public int getItemCount() {

        return lstPoubelle.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        public TextView eventAdresse;
        public TextView eventTime;
        public MapView mapView ;
        GoogleMap map;

        public ViewHolder(View itemView) {

            super(itemView);

            eventAdresse = (TextView) itemView.findViewById(R.id.txt_adresse) ;
            eventTime = (TextView) itemView.findViewById(R.id.txt_time) ;
            mapView = (MapView) itemView.findViewById(R.id.lite_list_row_map);

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            LatLng latLng = new LatLng( PassagePoubelleActivity.a,PassagePoubelleActivity.l);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
            googleMap.moveCamera(cameraUpdate);
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(6));
        }

        public void initializeMapView() {
            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }
        }

        private AbsListView.RecyclerListener mRecycleListener = new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {
                ViewHolder holder = (ViewHolder) view.getTag();
                GoogleMap map ;

                if (holder != null && holder.map != null) {
                    // Clear the map and free up resources by changing the map type to none
                    holder.map.clear();
                    holder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
                }

            }
        };


    }}
