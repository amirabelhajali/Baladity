package tn.esprit.baladity;

import android.content.Intent;
import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tn.esprit.baladity.Drawer.MainActivity;
import tn.esprit.baladity.services.UserSessionManager;


public class DemandesActivity extends AppCompatActivity {
    String URL = Url.URLL+"demandecertif.php";
    UserSessionManager session;

    EditText edtnom,edtprenom,edtdn,edtpere,edtmere;

    Button demandecertif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demandes);
        edtnom=(EditText)findViewById(R.id.certif_nom);
        edtprenom=(EditText)findViewById(R.id.certif_prenom);
        edtdn=(EditText)findViewById(R.id.certif_dn);
        edtpere=(EditText)findViewById(R.id.certif_prenom_pere);
        edtmere=(EditText)findViewById(R.id.certif_prenom_mere);
        demandecertif=(Button)findViewById(R.id.btn_envoyer_certif);

        session = new UserSessionManager(getApplicationContext());


        demandecertif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addCertifNaissance();
            }
        });



    }

    void addCertifNaissance()
    {
        if (!validate()) {

            Toast.makeText(getApplicationContext(), "Champs vides", Toast.LENGTH_LONG).show();

            return;
        }
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("vide") ){
                    Toast.makeText(getApplicationContext(), "Erreur envoie", Toast.LENGTH_LONG).show();



                }
                else{
                    Toast.makeText(getApplicationContext(), "Envoie avec succées", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Erreur -> "+volleyError, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                HashMap<String, String> user = session.getUserDetails();
                String email = user.get(UserSessionManager.KEY_EMAIL);
                // parameters.put("username", nomText.getText().toString());
                parameters.put("nom", edtnom.getText().toString());
                parameters.put("prenom", edtprenom.getText().toString());
                parameters.put("dateDeNaissance", edtdn.getText().toString());
                parameters.put("prenompere", edtpere.getText().toString());
                parameters.put("prenommere", edtmere.getText().toString());
                parameters.put("email",email);

                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.add(request);



        Intent intent = new Intent(getApplicationContext().getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();


    }


    public boolean validate() {
        boolean valid = true;

        String nom = edtnom.getText().toString();
        String prenom = edtprenom.getText().toString();
        String mere = edtmere.getText().toString();
        String pere = edtpere.getText().toString();
        String dn = edtdn.getText().toString();



        if (nom.isEmpty() || nom.length() < 3) {
            edtnom.setError("au mois 3 caractères");
            valid = false;
        } else {
            edtnom.setError(null);
        }

        if (prenom.isEmpty() || prenom.length() < 3) {
            edtprenom.setError("au mois 3 caractères");
            valid = false;
        } else {
            edtprenom.setError(null);
        }
        if (pere.isEmpty() || pere.length() < 3) {
            edtpere.setError("au mois 3 caractères");
            valid = false;
        } else {
            edtpere.setError(null);
        }
        if (mere.isEmpty() || mere.length() < 3) {
            edtmere.setError("au mois 3 caractères");
            valid = false;
        } else {
            edtmere.setError(null);
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(dn);
            if (!dn.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (dn.isEmpty()) {
            edtdn.setError("Date de naîssance invalide yy/mm/aaaa");
            valid = false;
        } else {
            edtdn.setError(null);
        }

        return valid;
    }

}
