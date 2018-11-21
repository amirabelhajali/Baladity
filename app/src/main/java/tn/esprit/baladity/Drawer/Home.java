package tn.esprit.baladity.Drawer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import tn.esprit.baladity.AboutUsActivity;
import tn.esprit.baladity.CustomGridViewActivity;
import tn.esprit.baladity.DemandesActivity;
import tn.esprit.baladity.ListeEvenementActivity;
import tn.esprit.baladity.ListerPdfActivity;
import tn.esprit.baladity.NewsRSS.MainNewsActivity;
import tn.esprit.baladity.PassagePoubelleActivity;
import tn.esprit.baladity.R;
import tn.esprit.baladity.ReclamationActivity;
import tn.esprit.baladity.SuggestionActivity;
import tn.esprit.baladity.mapActivity.MapsActivity;


public class Home extends Fragment {
    GridView androidGridView;


    String[] gridViewString = {
            "Actualités", "Evenements", "Map",
            "Poubelle", "Nous Joindre", "Documents",
            "Reclamation", "Suggestions", "Demandes",


    };
    int[] gridViewImageId = {
            R.drawable.news, R.drawable.event, R.drawable.map,
            R.drawable.poubelle, R.drawable.push, R.drawable.documents,
            R.drawable.reclamation, R.drawable.suggestion, R.drawable.demande


    };
    public Home() {
    }


    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();


        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);


        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(getActivity().getApplicationContext(), gridViewString, gridViewImageId);
        androidGridView = (GridView) view.findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {



                if
                        (gridViewString[+i].equals("Map")){
                    Intent Map = new Intent(getActivity(),MapsActivity.class);
                    startActivity(Map);

                }
                if
                        (gridViewString[+i].equals("Evenements")){
                    Intent Evenements = new Intent(getActivity().getApplicationContext(),ListeEvenementActivity.class);
                    startActivity(Evenements);

                }

                if
                        (gridViewString[+i].equals("Reclamation")){
                    Intent Reclamations = new Intent(getActivity().getApplicationContext(),ReclamationActivity.class);
                    startActivity(Reclamations);

                }

                if
                        (gridViewString[+i].equals("Demandes")){
                    Intent Demande = new Intent(getActivity().getApplicationContext(),DemandesActivity.class);
                    startActivity(Demande);

                }
                if
                        (gridViewString[+i].equals("Documents")){
                    Intent Documents = new Intent(getActivity().getApplicationContext(),ListerPdfActivity.class);
                    startActivity(Documents);

                }
                if
                        (gridViewString[+i].equals("Actualités")){
                    Intent Actualités = new Intent(getActivity().getApplicationContext(),MainNewsActivity.class);
                    startActivity(Actualités);

                }

                if
                        (gridViewString[+i].equals("Poubelle")){
                    Intent Poubelle = new Intent(getActivity().getApplicationContext(),PassagePoubelleActivity.class);
                    startActivity(Poubelle);

                }

                if
                        (gridViewString[+i].equals("Suggestions")){
                    Intent suggestion = new Intent(getActivity().getApplicationContext(),SuggestionActivity.class);
                    startActivity(suggestion);

                }
    if
                        (gridViewString[+i].equals("Nous Joindre")){
                    Intent propos = new Intent(getActivity().getApplicationContext(),AboutUsActivity.class);
                    startActivity(propos);

                }



            }
        });
        return  view;

    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
