package tn.esprit.baladity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tn.esprit.baladity.Dao.UserDao;
import tn.esprit.baladity.Drawer.MainActivity;
import tn.esprit.baladity.entities.User;
import tn.esprit.baladity.services.UserSessionManager;
import tn.esprit.baladity.utils.DialogError;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private UserSessionManager session;
    String   newString;
    String URLTOKEN = Url.URLL+"addusertoken.php";

    String URL = Url.URLL+"loginNew.php";
    public static final String PREFS_USER = "prefs_user";
    EditText emailText;
   EditText passwordText;
    Button loginButton;
    TextView signupLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new UserSessionManager(getApplicationContext());


        emailText=(EditText) findViewById(R.id.login);
        passwordText=(EditText) findViewById(R.id.password);
        loginButton=(Button) findViewById(R.id.loginBtm);

        signupLink=(TextView) findViewById(R.id.signup_link);






        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Start the Signup activity
                login();





            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authentification en cours...");
        progressDialog.show();
        // Open SharedPrefs File in Edit Mode
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_USER, MODE_PRIVATE).edit();
        editor.putString("email", emailText.getText().toString());
        editor.putString("password", passwordText.getText().toString());
        editor.apply();
        //INSERT INTO DATABASE
        UserDao dao = new UserDao(getApplicationContext());
        User user = new User();
        user.setEmail(emailText.getText().toString());
        user.setPassword(passwordText.getText().toString());

        // TODO: Implement your own authentication logic here.

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response)
                    {

                        if(response.equals("Wrongpassword")){
                            Toast.makeText(LoginActivity.this, "Mot de passe erronée", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else if(response.equals("Invalidemail")){
                            Toast.makeText(LoginActivity.this, "Email erroné", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else{

                            //response json
                       try {
                           JSONObject connectedUserData = new JSONObject(response);
                            User user = new User(connectedUserData.getString("nom"),
                                    connectedUserData.getString("prenom"),
                                    connectedUserData.getString("email"),
                                    connectedUserData.getString("numtel"),
                                    connectedUserData.getString("adresse"),
                                    connectedUserData.getString("codePostale")
                                    );
                           session.createUserLoginSession(user.getNom(),user.getEmail(),user.getAdresse(),user.getCodePostale(),user.getNumTel(),user.getPrenom());

                           Intent i = new Intent(getApplicationContext(), MainActivity.class);
                           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           // Add new Flag to start new Activity
                           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                           final String tokenuser=sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
                           StringRequest stringRequesttoken= new StringRequest(Request.Method.POST, URLTOKEN,
                                   new Response.Listener<String>() {
                                       @Override
                                       public void onResponse(String response) {

                                       }

                                   }, new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {

                               }
                           })
                           {
                               @Override
                               protected Map<String, String> getParams() throws AuthFailureError {
                                   Map<String,String> paramss=new HashMap<String,String>();
                                   paramss.put("token",tokenuser);
                                   //paramss.put("id",token);
                                   return paramss;
                               }

                           };
                           RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                           rQueue.add(stringRequesttoken);
                           startActivity(i);

                           // finish();

                       } catch (JSONException e) {
                        }
                    }}
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                      //  VolleyLog.e("Error: "+error.toString(), error);
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";

                        } else if (error instanceof ServerError) {

                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        DialogError alert = new DialogError();

//                        alert.showDialog(LoginActivity.this, message);
                        AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);






                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", emailText.getText().toString());
                params.put("password", passwordText.getText().toString());
                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();


    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Erreur Login", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String motdepasse = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Entrer une adresse mail valide");
            valid = false;

        } else {
            emailText.setError(null);
        }

        if (motdepasse.isEmpty() || motdepasse.length() < 4 || motdepasse.length() > 10) {
            passwordText.setError("Mot de passe trop faible");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences(PREFS_USER, MODE_PRIVATE);
        String emailPrefs = preferences.getString("email", null);
        String passwordPrefs = preferences.getString("password", null);

        if (emailPrefs != null && passwordPrefs!=null ) {

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    }}
}





