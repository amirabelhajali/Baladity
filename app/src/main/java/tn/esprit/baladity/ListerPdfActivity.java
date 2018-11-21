package tn.esprit.baladity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tn.esprit.baladity.Adapter.PdfAdapter;

import tn.esprit.baladity.Drawer.MainActivity;
import tn.esprit.baladity.entities.Pdf;

public class ListerPdfActivity extends AppCompatActivity  {


public static final String PDF_FETCH_URL = Url.URLL+"AfficherListDocuments.php";


//Image request code
private int PICK_PDF_REQUEST = 1;

//storage permission code
private static final int STORAGE_PERMISSION_CODE = 123;



//Uri to store the image uri
private Uri filePath;

        //ListView to show the fetched Pdfs from the server
        ListView listView;

        //button to fetch the intiate the fetching of pdfs.
        Button buttonFetch;

        //Progress bar to check the progress of obtaining pdfs
        ProgressDialog progressDialog;

        //an array to hold the different pdf objects
        ArrayList<Pdf> pdfList= new ArrayList<Pdf>();

        //pdf adapter

        PdfAdapter pdfAdapter;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister_pdf);

        //Requesting storage permission
        requestStoragePermission();


        //initializing ListView
        listView = (ListView) findViewById(R.id.listView);


        //initializing progressDialog

        progressDialog = new ProgressDialog(this);

        //Position clicklistener
if (noData()) {

        AlertDialog alertDialog = new AlertDialog.Builder(ListerPdfActivity.this).create();
        alertDialog.setTitle("Alerte");
        alertDialog.setMessage("Liste des documents vide");
       // alertDialog.setIcon(R.drawable.);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ListerPdfActivity.this, MainActivity.class);
                        startActivity(i);
                }
        });

        alertDialog.show();

}else {
        getPdfs();
}

        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Pdf pdf = (Pdf) parent.getItemAtPosition(position);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(pdf.getPdf()));
        startActivity(intent);

        }
        });


        }



@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        filePath = data.getData();
        }
        }


//Requesting permission
private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        //If the user has denied the permission previously your code will come to this block
        //Here you can explain why you need this permission
        //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }


//This method will be called when the user will tap on allow or deny
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

        //If permission is granted
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        //Displaying a toast
        Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
        } else {
        //Displaying another toast if permission is not granted
        Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
        }
        }
        }

public boolean noData(){

        if (pdfList.isEmpty()){
          return true;
        }
        return false;
}



        private void getPdfs() {

               // progressDialog.setMessage("Fetching Pdfs... Please Wait");
          //  progressDialog.dismiss();
           //     progressDialog.dismiss();

             //   progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, PDF_FETCH_URL,

                        new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                        progressDialog.dismiss();
                                        try {
                                                JSONObject obj = new JSONObject(response);

                                                JSONArray jsonArray = obj.getJSONArray("pdfs");

                                                for (int i = 0; i < jsonArray.length(); i++) {

                                                        //Declaring a json object corresponding to every pdf object in our json Array
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        //Declaring a Pdf object to add it to the ArrayList  pdfList
                                                        Pdf pdf = new Pdf();

                                                        String pdfName = jsonObject.getString("titre");

                                                        String pdfUrl = jsonObject.getString("pdf");
                                                        String pdfUrll = Url.pdf + pdfUrl;

                                                        pdf.setTitre(pdfName);
                                                        pdf.setPdf(pdfUrll);

                                                        pdfList.add(pdf);

                                                }

                                                pdfAdapter = new PdfAdapter(ListerPdfActivity.this, R.layout.list_layout, pdfList);

                                                listView.setAdapter(pdfAdapter);

                                                pdfAdapter.notifyDataSetChanged();

                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }


                                }
                        },

                        new Response.ErrorListener() {

                                @Override

                                public void onErrorResponse(VolleyError error) {
                                }
                        }) {
                        @Override
                        protected Map<String, String> getParams() {
                                Map<String, String> parameters = new HashMap<String, String>();


                                return parameters;

                };
                };


                RequestQueue request = Volley.newRequestQueue(getApplicationContext());
                request.add(stringRequest);
        }
}
