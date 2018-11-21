package tn.esprit.baladity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import tn.esprit.baladity.Drawer.MainActivity;
import tn.esprit.baladity.services.UserSessionManager;

public class ReclamationActivity extends Activity {

    String FinalData="";
    String ImageUploadPathOnSever =Url.URLL+"addReclamation.php" ;
    private static final int CAMERA_REQUEST = 1888;
    UserSessionManager session;
    Spinner spinner_reclamation;
    EditText description_reclamation;
    ImageView imageView_reclamation;
    Button btnReclamation, btnAjoutPhoto, btnGallery;
    ProgressDialog progressDialog ;
    Intent intent ;
    String email="";
    String spinnertext="";
    String description="";

    public  static final int RequestPermissionCode  = 1 ;

    Bitmap bitmap;

    boolean check = true;

    String GetImageNameFromEditText;

    // String ImageNameFieldOnServer = "image_name" ;

    String ImagePathFieldOnServer = "image_path" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamation);
        session = new UserSessionManager(getApplicationContext());

        EnableRuntimePermissionToAccessCamera();

        spinner_reclamation = (Spinner) findViewById(R.id.spinner_reclamation);
        description_reclamation = (EditText) findViewById(R.id.reclamation_description);
        imageView_reclamation = (ImageView) findViewById(R.id.image_reclamation);

        btnReclamation = (Button) findViewById(R.id.btn_envoyer_reclamation);
        btnGallery = (Button) findViewById(R.id.btn_gallery);


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });







                btnReclamation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

if (!validate()){
    AlertDialog alertDialog = new AlertDialog.Builder(ReclamationActivity.this).create();
    alertDialog.setTitle("Alerte");
    alertDialog.setMessage("Veuillez Remplir tous les champs");
    // alertDialog.setIcon(R.drawable.);

    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

            Intent i = new Intent(ReclamationActivity.this, ReclamationActivity.class);
            startActivity(i);
        }
    });

    alertDialog.show();
}
else {
    // GetImageNameFromEditText  = description_reclamation.getText().toString();
    spinnertext = spinner_reclamation.getSelectedItem().toString();
    description = description_reclamation.getText().toString();
    ImageUploadToServerFunction();
}
            }
        });

    }
    public boolean validate() {
        boolean valid = true;

        String description = description_reclamation.getText().toString();
        String type = spinner_reclamation.getSelectedItem().toString();




        if (description.isEmpty() ) {
            description_reclamation.setError("Ce champ ne doit pas être vide");
            valid = false;
        } else {
            description_reclamation.setError(null);
        }
        if (imageView_reclamation.getDrawable() == null ) {
            valid = false;

        } else {
            description_reclamation.setError(null);
        }

        if (type.isEmpty() ) {
          Toast.makeText(ReclamationActivity.this,"Veuillez choisir le type de reclamation",Toast.LENGTH_LONG).show();
            valid = false;
        } else {
        }


        return valid;
    }

    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageView_reclamation.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ReclamationActivity.this,
                Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(ReclamationActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ReclamationActivity.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    public void ImageUploadToServerFunction() {

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(ReclamationActivity.this,"Image is Uploading","Please Wait",false,false);
            }
            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(ReclamationActivity.this,string1,Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                imageView_reclamation.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String, String> user = session.getUserDetails();
                email= user.get(UserSessionManager.KEY_EMAIL);

                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put("typereclamation", spinnertext);

             //   HashMapParams.put("email", email);
                HashMapParams.put("descriptions", description);
                HashMapParams.put("photo", ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

}
