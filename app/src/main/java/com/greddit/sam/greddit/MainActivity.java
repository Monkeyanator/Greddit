package com.greddit.sam.greddit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.greddit.sam.greddit.dummy.PostAdapter;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] defaults = {"AskReddit","BannedFromClubPenguin","DessertPorn","ExplainLikeImFive","GlobalOffensive","Hookah","MyLittlePony","NoSleep","Vinyl"};
    private String[] favorites = {"DessertPorn","GlobalOffensive","Hookah"};
    public String subredditString = null;

    RedditHttpRequestHelper helper;
    ListView subredditListView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    PostAdapter adapter;
    ArrayList<String> currentPostUrlList;
    TextView bottomToolbarText;
    Toolbar bottomToolbar;

    TextView titleText, secondaryText, commandText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleText = (TextView) findViewById(R.id.welcomeToGreddit);
        secondaryText = (TextView) findViewById(R.id.sloganText);
        commandText = (TextView) findViewById(R.id.commandText);

        bottomToolbar = (Toolbar) findViewById(R.id.bottomToolbar);
        bottomToolbarText = (TextView) findViewById(R.id.bottomToolbarText);

        subredditListView = (ListView) findViewById(R.id.subredditListView);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu subredditMenu = navigationView.getMenu();
        SubMenu favoriteSubreddits = subredditMenu.addSubMenu("Favorites");
        SubMenu defaultSubreddits = subredditMenu.addSubMenu("Default");

        for (String subs : favorites)
            favoriteSubreddits.add(subs);

        for (String subs : defaults)
            defaultSubreddits.add(subs);

        MenuItem menuItem = subredditMenu.getItem(subredditMenu.size() - 1);
        menuItem.setTitle(menuItem.getTitle());

        subredditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(currentPostUrlList.get(position)));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String id = (String)item.getTitle();
        subredditString = id.toLowerCase();

        bottomToolbarText.setText(subredditString);
        bottomToolbar.setVisibility(View.VISIBLE);

        titleText.setVisibility(View.GONE);
        secondaryText.setVisibility(View.GONE);
        commandText.setVisibility(View.GONE);

        helper = new RedditHttpRequestHelper(subredditString,10,this);
        helper.execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void renderListWithData(ArrayList<HashMap<String,String>> hashData){
        if(hashData == null) {
            Snackbar.make(this.findViewById(R.id.contentView), "Posts could not be loaded. Check connection.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            currentPostUrlList = new ArrayList<>();
            for(HashMap<String,String>hashes: hashData)
                currentPostUrlList.add(hashes.get("url"));

            adapter = new PostAdapter(this,hashData);
            subredditListView.setAdapter(adapter);
        }
    }

    public void setBottomToolbarText(String text){

    }
}

/*
            for(HashMap<String,String> hashes : hashData){
                System.out.println("Title:");
                System.out.println(hashes.get("title"));
                System.out.println("Comments:");
                System.out.println(hashes.get("num_comments"));
                System.out.println("URL:");
                System.out.println(hashes.get("url"));
                System.out.println("_________________________");
            }
 */