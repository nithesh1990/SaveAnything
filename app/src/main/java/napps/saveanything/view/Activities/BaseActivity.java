package napps.saveanything.view.Activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import napps.saveanything.Control.SaveClipService;
import napps.saveanything.Model.CustomGradientDrawable;
import napps.saveanything.Utilities.Constants;
import napps.saveanything.Utilities.Utils;
import napps.saveanything.R;
import napps.saveanything.view.Fragments.ClipsFragment;
import napps.saveanything.view.Fragments.CustomFragment;
import napps.saveanything.view.Fragments.ImageFragment;
import napps.saveanything.view.Fragments.NotesFragment;
import napps.saveanything.view.adapters.PageAdapter;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private TabLayout mTabLayout;

    private Context activityContext;

    private final int CLIPS_FRAGMENT_POSITION = 0;
    private final int NOTES_FRAMGENT_POSITION = 1;
    //private final int IMAGES_FRAGMENT_POSITION = 2;

    private Application mapp;
    FloatingActionButton fButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.clip_toolbar);
        setSupportActionBar(toolbar);
        activityContext = this;

        fButton = (FloatingActionButton) findViewById(R.id.fab);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNoteIntent = new Intent();
                createNoteIntent.setClass(activityContext, CreateNoteActivity.class);
                startActivity(createNoteIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        GradientDrawable navigationBackgroundDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{});
        CustomGradientDrawable.Builder gradientBuilder = new CustomGradientDrawable.Builder();
        gradientBuilder.Orientation(GradientDrawable.Orientation.LEFT_RIGHT);
        gradientBuilder.setStartColorAttribute(R.attr.colorPrimaryFallbackLow);
        gradientBuilder.setEndColorAttribute(R.attr.colorPrimaryFallbackMed);
        gradientBuilder.type(GradientDrawable.RECTANGLE);
        CustomGradientDrawable customGradientDrawable = gradientBuilder.build(this, gradientBuilder);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            navigationView.setBackgroundDrawable(customGradientDrawable);
        } else{
            navigationView.setBackground(customGradientDrawable);
        }

        navigationView.setNavigationItemSelectedListener(this);

        populatePages();

             //Here i was using context 'this' before so the service was stopped as soon as the activity was remove
            //1. For Services always use getApplicationContext() because this is initialized by Application class
            //2. For every application there will be only one application class which extends contextwrapper and is set during application initialization
            //3. For use cases which should be working irrespective of activities or UI use getApplicationContext()

        Utils.checkAndStartService(getApplicationContext(), SaveClipService.class);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.clips_menu, menu);
//        return true;
//    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //There is one issue with this way of setting fragments when used with FragmentStatePagerAdapter(FSPA)
        //Whenever FSPA is initialized it initialises the first 2 fragments.
        // If we just use sliding method, the fragments are created and destroyed accordingly and there is no issue
        //If there are 3 fragments, fragments 1 and 2 are initialized, If we navigate to fragment/tab 3 using the below method(i.e viewpager set)
        //The fragment 3 is not created prior because of FSPA and you navigate using mViewpager.setCurrentItem() which only recreates the view heirarchy(i.e layout views)
        //and not the whole fragment. So the fragment looks empty
        switch(id){
            case R.id.nav_notes:
                mViewPager.setCurrentItem(NOTES_FRAMGENT_POSITION);
                break;

            case R.id.nav_clips:
                mViewPager.setCurrentItem(CLIPS_FRAGMENT_POSITION);
                break;

//            case R.id.nav_images:
//                mViewPager.setCurrentItem(IMAGES_FRAGMENT_POSITION);
//                break;

            //case R.id.nav_database:
            //    saveDatabaseFile();
            //    break;
            default:
                mViewPager.setCurrentItem(NOTES_FRAMGENT_POSITION);
                break;
        }
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void saveDatabaseFile(){
        String diskState = Environment.getExternalStorageState();
        if(diskState.equals(Environment.MEDIA_MOUNTED)) {
            File dbFile = new File(Constants.DATABASE_PATH);
            if(dbFile.exists()){
                try{
                    File backUpDB = new File(Utils.getDatabaseStoragePath(), "backup.db");
                    FileChannel src = new FileInputStream(dbFile).getChannel();
                    FileChannel dst = new FileOutputStream(backUpDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }catch (Exception e){

                }
            }

        }
    }
    public void populatePages(){
        List<CustomFragment> fragmentList = new ArrayList<CustomFragment>();
        fragmentList.add(ClipsFragment.newInstance());
        fragmentList.add(NotesFragment.newInstance());
        mPageAdapter = new PageAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager = (ViewPager)findViewById(R.id.pages);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch(position){
            case NOTES_FRAMGENT_POSITION:
                fButton.setVisibility(View.VISIBLE);
                break;
            case CLIPS_FRAGMENT_POSITION:
                fButton.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
