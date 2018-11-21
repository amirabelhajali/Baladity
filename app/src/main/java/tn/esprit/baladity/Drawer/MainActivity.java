package tn.esprit.baladity.Drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tn.esprit.baladity.LoginActivity;
import tn.esprit.baladity.R;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout dl ;
    private ActionBarDrawerToggle toggle ;
    public static final String PREFS_USER = "prefs_user";

    public boolean deconnect=false;
    


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dl = (DrawerLayout) findViewById(R.id.dl);
        toggle = new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);
        dl.addDrawerListener(toggle);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        toggle.syncState();

        setupDrawerContent(nvDrawer);
        Home Home = new Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent,Home).commit();



    }




    public void selectItemDrawer(MenuItem menuItem) {
        Fragment myFragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.Home:
                fragmentClass = Home.class;
                break;

            case R.id.Position:
                fragmentClass = MesDocumentFragment.class;
                break;

            case R.id.Logout:
                SharedPreferences sp = getSharedPreferences(PREFS_USER, MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.remove("email");
                e.remove("password");
                e.apply();
            e.clear().commit();
                Intent i2=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i2);



                deconnect=true;
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            default:
             //   fragmentClass = Logout.class;
        }
        try {
            myFragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (!deconnect){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontent,myFragment).commit();
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            dl.closeDrawers();
        }


    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }

}


