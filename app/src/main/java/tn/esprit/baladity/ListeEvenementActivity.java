package tn.esprit.baladity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.baladity.Adapter.EvenementAdapter;
import tn.esprit.baladity.Drawer.MainActivity;
import tn.esprit.baladity.entities.Evenement;

public class ListeEvenementActivity extends AppCompatActivity {
    List<Evenement> evenementsListe;

    RecyclerView recyclerView;





    String HTTP_JSON_URL = Url.URLL+"AfficherListEvenement.php";
    String GET_JSON_FROM_SERVER_NAME_TITRE = "titre";
    String GET_JSON_FROM_SERVER_NAME_DESCRIPTION = "description";
    String Image_URL_JSON = "image";

ProgressBar progressBar;
    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    View ChildView ;

    int GetItemPosition ;
    RecyclerView.Adapter recyclerViewadapter;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;
    ArrayList<String> eventImage;
    ArrayList<String> eventDescriptions;
    ArrayList<String> eventTitres;

    List<Evenement> listOfEventAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_evenement);

        evenementsListe = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.evenement_recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        recyclerView.setHasFixedSize(true);
        layoutManagerOfrecyclerView = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

      //  progressBar.setVisibility(View.VISIBLE);

        eventTitres = new ArrayList<>();
        eventDescriptions = new ArrayList<>();
        eventImage=new ArrayList<>();
        listOfEventAdapter=new ArrayList<>();
if (noData())
{
    AlertDialog alertDialog = new AlertDialog.Builder(ListeEvenementActivity.this).create();
    alertDialog.setTitle("Alerte");
    alertDialog.setMessage("Pas d'événements");
    // alertDialog.setIcon(R.drawable.);

    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            finish();
            Intent i = new Intent(ListeEvenementActivity.this, MainActivity.class);
            startActivity(i);
        }
    });

    alertDialog.show();
}
else {
    JSON_DATA_WEB_CALL();
}
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(ListeEvenementActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    Toast.makeText(ListeEvenementActivity.this, eventTitres.get(GetItemPosition), Toast.LENGTH_LONG).show();
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
    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                     //   progressBar.setVisibility(View.GONE);


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

            Evenement GetDataAdapter2 = new Evenement();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setTitre(json.getString(GET_JSON_FROM_SERVER_NAME_TITRE));
                GetDataAdapter2.setDescription(json.getString(GET_JSON_FROM_SERVER_NAME_DESCRIPTION));
                GetDataAdapter2.setImage(Url.imagee+json.getString(Image_URL_JSON));

                eventTitres.add(json.getString(GET_JSON_FROM_SERVER_NAME_TITRE));
                eventDescriptions.add(json.getString(GET_JSON_FROM_SERVER_NAME_DESCRIPTION));
                eventImage.add(Url.imagee+json.getString(Image_URL_JSON));

            } catch (JSONException e) {

                e.printStackTrace();
            }
            evenementsListe.add(GetDataAdapter2);
        }

        recyclerViewadapter = new EvenementAdapter(this,evenementsListe);

        recyclerView.setAdapter(recyclerViewadapter);

    }
    public boolean noData(){

        if (evenementsListe.isEmpty()){
            return true;
        }
        return false;
    }


}
