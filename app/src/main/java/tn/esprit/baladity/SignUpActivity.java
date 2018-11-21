package tn.esprit.baladity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private ProgressDialog pDialog;

    EditText nomText;

    EditText prenomText;

    EditText emailText;

    EditText passwordText;

    EditText telText;

    Button signupButton;
    TextView loginLink;
    public static final  String USERNAME="USERNAME" ;
        String URL = Url.URLL+"adduser.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nomText=(EditText) findViewById(R.id.input_nom);
        prenomText=(EditText) findViewById(R.id.input_prenom);
        emailText=(EditText) findViewById(R.id.input_email);
        passwordText=(EditText) findViewById(R.id.input_password);

        telText=(EditText) findViewById(R.id.input_tel);
        signupButton=(Button) findViewById(R.id.btn_signup);

        loginLink=(TextView) findViewById(R.id.link_login);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity


                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            startActivity(new Intent(SignUpActivity.this,SignUpActivity.class));
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Création de compte...");

        progressDialog.show();


        // TODO: Implement your own signup logic here.


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("success")){
                    Toast.makeText(SignUpActivity.this, "Création avec succée", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Erreur Création de compte", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

               // parameters.put("username", nomText.getText().toString());
                parameters.put("nom", nomText.getText().toString());
               parameters.put("prenom", prenomText.getText().toString());
                parameters.put("email", emailText.getText().toString());
                parameters.put("password", passwordText.getText().toString());
                parameters.put("numtel", telText.getText().toString() );

                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(SignUpActivity.this);
        rQueue.add(request);




        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Compte ajouté avec succé", Toast.LENGTH_LONG).show();

        finish();


    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Erreur Login", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String nom = nomText.getText().toString();
        String prenom = prenomText.getText().toString();
        String email = emailText.getText().toString();
        String tel = telText.getText().toString();
        String motdepasse = passwordText.getText().toString();

        if (nom.isEmpty() || nom.length() < 3) {
            nomText.setError("au mois 3 caractères");
            valid = false;
        } else {
            nomText.setError(null);
        }

        if (prenom.isEmpty() || prenom.length() < 3) {
            prenomText.setError("au mois 3 caractères");
            valid = false;
        } else {
            prenomText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Adresse mail invalide");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (motdepasse.isEmpty() || motdepasse.length() < 4 || motdepasse.length() > 10) {
            passwordText.setError("entre 4 et 10 caractères");
            valid = false;
        } else {//passwordText.setError(null);
        }


        return valid;
    }


}