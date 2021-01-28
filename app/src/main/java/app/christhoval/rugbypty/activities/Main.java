package app.christhoval.rugbypty.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import app.christhoval.rugbypty.R;
import app.christhoval.rugbypty.activities.base.BaseActivity;
import app.christhoval.rugbypty.fragments.AboutFragment;
import app.christhoval.rugbypty.fragments.ChatFragment;
import app.christhoval.rugbypty.fragments.ComingSoonFragment;
import app.christhoval.rugbypty.fragments.FixtureFragment;
import app.christhoval.rugbypty.fragments.NewsFragment;
import app.christhoval.rugbypty.fragments.PositionFragment;
import app.christhoval.rugbypty.mvp.notifications.NotificationsFragment;
import app.christhoval.rugbypty.mvp.notifications.NotificationsPresenter;
import app.christhoval.rugbypty.utilities.Constant;
import app.christhoval.rugbypty.utilities.Debug;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Main extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private FragmentManager fragmentManager;

    private FirebaseMessaging firebaseMessaging;
    private FirebaseInstanceId firebaseInstanceId;

    /* INTEN DATA, EXTRAS, ACTTION*/
    private Uri dataIntent;
    private Bundle extrasIntent;
    private String actionIntent;
    private String app_link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseInstanceId = FirebaseInstanceId.getInstance();

        Intent intent = getIntent();
        if (null != intent) {
            actionIntent = intent.getAction();
            dataIntent = intent.getData();
            extrasIntent = intent.getExtras();
            if (extrasIntent != null) {
                if (extrasIntent.containsKey(Constant.APP_LINK)) {
                    app_link = extrasIntent.getString(Constant.APP_LINK);
                    openFragment(selectFragmentToOpen(app_link), app_link);
                }
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                    hideKeyboard();
                }
            };
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            customizeFragmentManager(toolbar, toggle);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }


    private void customizeFragmentManager(final Toolbar toolbar, final ActionBarDrawerToggle toggle) {
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = fragmentManager.getBackStackEntryCount();


                if (getSupportActionBar() != null) {
                    if (stackHeight > 0) {
                        getSupportActionBar().setHomeButtonEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getSupportFragmentManager().popBackStack();
                            }
                        });

                    } else {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        getSupportActionBar().setHomeButtonEnabled(false);
                        toggle.syncState();
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                drawer.openDrawer(GravityCompat.START);
                            }
                        });
                    }
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        int stackHeight = fragmentManager.getBackStackEntryCount();
        if (stackHeight > 0) {
            fragmentManager.popBackStack();

        } else {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);
            else
                new AlertDialog.Builder(Main.this).setMessage(getString(R.string.message))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //getPreferences().set(Constant.ACTUAL_DRAWER_TAG, NewsFragment.DEEP_LINK);
                                Main.super.onBackPressed();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String tag;

        switch (item.getItemId()) {
            case R.id.nav_news:
                tag = NewsFragment.DEEP_LINK;
                break;
            case R.id.nav_calendar:
                tag = FixtureFragment.DEEP_LINK;
                break;
            case R.id.nav_rankings:
                tag = PositionFragment.DEEP_LINK;
                break;
            case R.id.nav_notifications:
                tag = NotificationsFragment.DEEP_LINK;
                break;
            case R.id.nav_aboutme:
                tag = AboutFragment.DEEP_LINK;
                break;
            case R.id.nav_manage:
            case R.id.nav_facebook:
            case R.id.nav_instagram:
            default:
                tag = ComingSoonFragment.DEEP_LINK;
                break;
        }

        getPreferences().set(Constant.ACTUAL_DRAWER_TAG, tag);

        openFragment(selectFragmentToOpen(tag), tag);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment selectFragmentToOpen(String tag) {
        Fragment newFragment = null;
        Fragment actualFragment = fragmentManager.findFragmentById(R.id.content);


        switch (tag) {
            case NewsFragment.DEEP_LINK:
                if (actualFragment != null) {
                    if (actualFragment.getClass().getSimpleName().equals(NewsFragment.class.getSimpleName()))
                        newFragment = actualFragment;
                    else {
                        if (null != app_link) {
                            if (app_link.equals(tag))
                                newFragment = NewsFragment.newInstance(extrasIntent);
                        } else
                            newFragment = NewsFragment.newInstance();
                    }
                } else {
                    if (null != app_link) {
                        if (app_link.equals(tag))
                            newFragment = NewsFragment.newInstance(extrasIntent);
                    } else
                        newFragment = NewsFragment.newInstance();
                }
                break;
            case FixtureFragment.DEEP_LINK:
                if (actualFragment != null) {
                    if (actualFragment.getClass().getSimpleName().equals(FixtureFragment.class.getSimpleName()))
                        newFragment = actualFragment;
                    else {
                        if (null != app_link) {
                            if (app_link.equals(tag))
                                newFragment = FixtureFragment.newInstance(extrasIntent);
                        } else
                            newFragment = FixtureFragment.newInstance();
                    }
                } else {
                    if (null != app_link) {
                        if (app_link.equals(tag))
                            newFragment = FixtureFragment.newInstance(extrasIntent);
                    } else
                        newFragment = FixtureFragment.newInstance();
                }
                break;
            case PositionFragment.DEEP_LINK:
                if (actualFragment != null) {
                    if (actualFragment.getClass().getSimpleName().equals(PositionFragment.class.getSimpleName()))
                        newFragment = actualFragment;
                    else {
                        if (null != app_link) {
                            if (app_link.equals(tag))
                                newFragment = PositionFragment.newInstance(extrasIntent);
                        } else
                            newFragment = PositionFragment.newInstance();
                    }
                } else {
                    if (null != app_link) {
                        if (app_link.equals(tag))
                            newFragment = PositionFragment.newInstance(extrasIntent);
                    } else
                        newFragment = PositionFragment.newInstance();
                }
                break;
            case ChatFragment.DEEP_LINK:
                if (actualFragment != null) {
                    if (actualFragment.getClass().getSimpleName().equals(ChatFragment.class.getSimpleName()))
                        newFragment = actualFragment;
                    else {
                        if (null != app_link) {
                            if (app_link.equals(tag))
                                newFragment = ChatFragment.newInstance(extrasIntent);
                        } else
                            newFragment = ChatFragment.newInstance();
                    }
                } else {
                    if (null != app_link) {
                        if (app_link.equals(tag))
                            newFragment = ChatFragment.newInstance(extrasIntent);
                    } else
                        newFragment = ChatFragment.newInstance();
                }
                break;
            case NotificationsFragment.DEEP_LINK:
                if (actualFragment != null) {
                    if (actualFragment.getClass().getSimpleName().equals(NotificationsFragment.class.getSimpleName()))
                        newFragment = actualFragment;
                    else {
                        if (null != app_link) {
                            if (app_link.equals(tag))
                                newFragment = NotificationsFragment.newInstance(extrasIntent);
                        } else
                            newFragment = NotificationsFragment.newInstance();
                    }
                } else {
                    if (null != app_link) {
                        if (app_link.equals(tag))
                            newFragment = NotificationsFragment.newInstance(extrasIntent);
                    } else
                        newFragment = NotificationsFragment.newInstance();
                }
                new NotificationsPresenter((NotificationsFragment) newFragment, firebaseMessaging, null);
                break;

            case AboutFragment.DEEP_LINK:
                if (actualFragment != null) {
                    if (actualFragment.getClass().getSimpleName().equals(AboutFragment.class.getSimpleName()))
                        newFragment = actualFragment;
                    else {
                        if (null != app_link) {
                            if (app_link.equals(tag))
                                newFragment = AboutFragment.newInstance(extrasIntent);
                        } else
                            newFragment = AboutFragment.newInstance();
                    }
                } else {
                    if (null != app_link) {
                        if (app_link.equals(tag))
                            newFragment = AboutFragment.newInstance(extrasIntent);
                    } else
                        newFragment = AboutFragment.newInstance();
                }
                break;
            case ComingSoonFragment.DEEP_LINK:
                if (actualFragment != null) {
                    if (actualFragment.getClass().getSimpleName().equals(ComingSoonFragment.class.getSimpleName()))
                        newFragment = actualFragment;
                    else {
                        if (null != app_link) {
                            if (app_link.equals(tag))
                                newFragment = ComingSoonFragment.newInstance(extrasIntent);
                        } else
                            newFragment = ComingSoonFragment.newInstance();
                    }
                } else {
                    if (null != app_link) {
                        if (app_link.equals(tag))
                            newFragment = ComingSoonFragment.newInstance(extrasIntent);
                    } else
                        newFragment = ComingSoonFragment.newInstance();
                }
                break;
        }

        if (null != newFragment) {
            if (actualFragment != null) {

                Debug.i("selectFragmentToOpen(actualFragment): " + actualFragment.getClass().getSimpleName());
                Debug.i("selectFragmentToOpen(newFragment): " + newFragment.getClass().getSimpleName());

                if (actualFragment.getClass().getSimpleName().equals(newFragment.getClass().getSimpleName())) {
                    newFragment = null;
                }
            }
        } else {
            Debug.i("newFragment is null");
        }

        return newFragment;
    }

    private int getMenuIdFromTag(String tag) {
        int index = 0x00;
        switch (tag) {
            case NewsFragment.DEEP_LINK:
                index = R.id.nav_news;
                break;
            case FixtureFragment.DEEP_LINK:
                index = R.id.nav_calendar;
                break;
            case PositionFragment.DEEP_LINK:
                index = R.id.nav_rankings;
                break;
            case AboutFragment.DEEP_LINK:
                index = R.id.nav_aboutme;
                break;
            case ChatFragment.DEEP_LINK:
                index = R.id.nav_manage;
                break;
            case NotificationsFragment.DEEP_LINK:
                index = R.id.nav_notifications;
                break;
            case ComingSoonFragment.DEEP_LINK:
                index = R.id.nav_facebook;
                break;
        }
        return index;
    }

    private Fragment openFragment(Fragment fragment, String tag) {
        if (null != fragment) {
            if (fragmentManager.findFragmentByTag(tag) == null) {
                Debug.i("openFragment: " + fragment.getClass().getSimpleName());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment, tag);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        return fragment;
    }

    private void startActivity(Class activity) {
        Intent intent = new Intent(Main.this, activity);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getTokenFCM() {
        String token = getPreferences().get(Constant.FCM_TOKEN, "");
        Debug.i("InstanceID token: " + token);
        if (token.isEmpty()) {
            token = firebaseInstanceId.getToken();
            Debug.i("InstanceID token: " + token);
            getPreferences().set(Constant.FCM_TOKEN, token);
        }
    }

    public void setupNavigationView(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();


        // suscribe to topics

        firebaseMessaging.subscribeToTopic("rugbypty");

        MenuItem menuItem = menu.findItem(R.id.nav_push_rugby);
        View actionView = MenuItemCompat.getActionView(menuItem);

        SwitchCompat activeNotifications = (SwitchCompat) actionView.findViewById(R.id.notify_switch);
        activeNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((SwitchCompat) v).isChecked();

                getPreferences().set(Constant.FCM_NOTIFY, checked ? "true" : "false");
                //getTokenFCM();

                if (checked) {
                    firebaseMessaging.subscribeToTopic("rugbypty");
                } else {
                    firebaseMessaging.unsubscribeFromTopic("rugbypty");
                }
                Debug.i(Constant.FCM_NOTIFY + ": " + getPreferences().get(Constant.FCM_NOTIFY, "false"));
            }
        });


        Debug.i(Constant.FCM_NOTIFY + ": " + getPreferences().get(Constant.FCM_NOTIFY, "false"));
        activeNotifications.setChecked(Boolean.parseBoolean(getPreferences().get(Constant.FCM_NOTIFY, "false")));


        int _id = getMenuIdFromTag(getPreferences().get(Constant.ACTUAL_DRAWER_TAG, NewsFragment.DEEP_LINK));
        menuItem = menu.findItem(_id);
        if (menuItem != null) {
            menuItem.setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }

    @Override
    protected void onResume() {
        if (navigationView != null) {
            setupNavigationView(navigationView);
        }
        clearNotifications();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constant.REQUEST_CODE_PERMISSIONS:
                break;
            case Constant.REQUEST_CODE_PERMISSIONS_CHAT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }

    private void getExtras(Intent intent) {
        Bundle extras = intent.getExtras();
        if (null != extras) {
            Debug.i("Bundle: " + extras.toString());
            for (String key : extras.keySet()) {
                Debug.i(String.format("extras, %1$s: %2$s", key, extras.getString(key)));
            }

            // [START handle_data_extras]
            for (String key : intent.getExtras().keySet()) {

                Object value = extras.get(key);
                Debug.i(String.format("%s %s (%s)", key, value.toString(), value.getClass().getName()));
                //String value = intent.getExtras().getString(key);
                //Debug.i("Key: " + key + " Value: " + value);
            }
            // [END handle_data_extras]
        }
    }

    private void getDataUri(Intent intent) {
        String action = intent.getAction();
        if (action != null && !action.equals("")) {
            if (Intent.ACTION_VIEW.equals(action)) {
                Uri uri = intent.getData();
                Log.v("URI", "host: " + uri.getHost());
                switch (uri.getHost()) {
                    default:
                        break;
                }

                //String valueOne = uri.getQueryParameter("keyOne");
                //String valueTwo = uri.getQueryParameter("keyTwo");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPreferences().set(Constant.ACTUAL_DRAWER_TAG, NewsFragment.DEEP_LINK);
    }

    public FirebaseMessaging getFirebaseMessaging() {
        return firebaseMessaging;
    }

    public FirebaseInstanceId getFirebaseInstanceId() {
        return firebaseInstanceId;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void clearNotifications() {
        NotificationManager nManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        nManager.cancelAll();
    }

}


// http://stacktips.com
// https://guides.codepath.com/android/Fragment-Navigation-Drawer
// https://guides.codepath.com/android/Extended-ActionBar-Guide#adding-actionview-items

// http://stackoverflow.com/questions/37619694/how-do-i-animate-views-in-toolbar-when-user-scrolls?answertab=active#tab-top