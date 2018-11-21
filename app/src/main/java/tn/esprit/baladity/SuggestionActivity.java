package tn.esprit.baladity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import tn.esprit.baladity.Drawer.MainActivity;
import tn.esprit.baladity.services.UserSessionManager;

import java.util.HashMap;
import java.util.Map;

public class SuggestionActivity extends AppCompatActivity {
    EditText edit_titre;
    EditText edit_description;
    Button btn_envoyer;
    UserSessionManager session;



    String URL = Url.URLL+"addSuggestion.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        session = new UserSessionManager(getApplicationContext());

        edit_titre = (EditText)findViewById(R.id.sugg_titre);
        edit_description= (EditText) findViewById(R.id.sugg_description);
        btn_envoyer=(Button)findViewById(R.id.btn_envoyer);






        btn_envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Intent intent = new Intent(getApplicationContext(), AccueilActivity.class);
                // startActivityForResult(intent, REQUEST_SIGNUP);
                if (edit_titre.getText().toString()!="" && edit_description.getText().toString()!="") {
                    addSuggestion();
                }
                else
                    Toast.makeText(getApplicationContext(),"Veuillez remlir les champs",Toast.LENGTH_LONG).show();
            }
        });
    }

    void addSuggestion()
    {
        if (!validate()) {
            edit_titre.setText("");
            edit_description.setText("");
            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("success")){
                    Toast.makeText(SuggestionActivity.this, "erreur envoie", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SuggestionActivity.this, "Envoie avec succées", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SuggestionActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                HashMap<String, String> user = session.getUserDetails();
                String email = user.get(UserSessionManager.KEY_EMAIL);
                // parameters.put("username", nomText.getText().toString());
                System.out.println( "hhhh"+email);
                parameters.put("titre", edit_titre.getText().toString());
                parameters.put("description", edit_description.getText().toString());
                parameters.put("email",email);


                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(SuggestionActivity.this);
        rQueue.add(request);




        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();


    }


    public boolean validate() {
        boolean valid = true;

        String titre = edit_titre.getText().toString();
        String description = edit_description.getText().toString();




        if (titre.isEmpty() || titre.length() < 3) {
            edit_titre.setError("au mois 3 caractères");
            valid = false;
        } else {
            edit_titre.setError(null);
        }

        if (description.isEmpty() || description.length() < 3) {
            edit_description.setError("au mois 3 caractères");
            valid = false;
        } else {
            edit_description.setError(null);
        }


        return valid;
    }


}

