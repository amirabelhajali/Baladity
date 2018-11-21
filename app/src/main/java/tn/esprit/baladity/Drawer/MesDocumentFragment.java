package tn.esprit.baladity.Drawer;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import tn.esprit.baladity.ListeEvenementActivity;
import tn.esprit.baladity.R;
import tn.esprit.baladity.Url;
import tn.esprit.baladity.entities.Pdf;
import tn.esprit.baladity.services.UserSessionManager;

import static android.app.Activity.RESULT_OK;



public class MesDocumentFragment extends Fragment {
    public static final String PDF_FETCH_URL = Url.URLL+"mescertif.php";

    TextView titrePdf;
    UserSessionManager session;

    //Image request code
    private int PICK_PDF_REQUEST = 2;
    String email="";
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 124;



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

    public MesDocumentFragment() {
        // Required empty public constructor
    }

    public static MesDocumentFragment newInstance(String param1, String param2) {
        MesDocumentFragment fragment = new MesDocumentFragment();


        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mes_document, container, false);

        session = new UserSessionManager(getActivity());

        listView = (ListView) view.findViewById(R.id.listMesdoc);
        //Requesting storage permission
        requestStoragePermission();
        //initializing ListView
        progressDialog = new ProgressDialog(this.getActivity());

        if (noData()){
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Alerte");
            alertDialog.setMessage("Vos documents ne sont pas encore traités");
            // alertDialog.setIcon(R.drawable.);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }
            });

            alertDialog.show();
        }
else {
            getPdfs();
        }
        System.out.println("get pdf");
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

        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
System.out.println("hello permission");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this.getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this.getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getPdfs() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, PDF_FETCH_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);
//        Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray = obj.getJSONArray("pdfs");


                            for (int i = 0; i < jsonArray.length(); i++) {

                                //Declaring a json object corresponding to every pdf object in our json Array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //Declaring a Pdf object to add it to the ArrayList  pdfList
                                Pdf pdf = new Pdf();

                                String pdfName = jsonObject.getString("nompdf");
                                System.out.println("pdfname" + pdfName);

                                String pdfUrl = jsonObject.getString("nompdf");
                                String pdfUrll = Url.pdf+ pdfUrl;

                                pdf.setTitre(pdfName);
                                pdf.setPdf(pdfUrll);


                                pdfList.add(pdf);

                            }

                            pdfAdapter = new PdfAdapter(getActivity(), R.layout.list_layout, pdfList);

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

                // parameters.put("username", nomText.getText().toString());


                HashMap<String, String> user = session.getUserDetails();
                email= user.get(UserSessionManager.KEY_EMAIL);
                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put("email", email);

                return HashMapParams;
            }
        };


        RequestQueue request = Volley.newRequestQueue(getActivity());
        request.add(stringRequest);
    }
    public boolean noData(){

        if (pdfList.isEmpty()){
            return true;
        }
        return false;
    }

}

