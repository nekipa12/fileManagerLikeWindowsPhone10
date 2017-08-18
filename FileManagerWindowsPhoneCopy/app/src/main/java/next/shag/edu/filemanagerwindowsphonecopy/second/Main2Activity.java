package next.shag.edu.filemanagerwindowsphonecopy.second;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import layout.FragmentSDCardNoAction;
import next.shag.edu.filemanagerwindowsphonecopy.R;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentSDCardNoAction fSDC, fim;
    private FragmentTransaction ft;
    private boolean focusSD;
    private final String DEFAULT_VALUE_SD = "./sdcard";
    private final String DEFAULT_VALUE_INNER_MEMORY = "./data/data/next.shag.edu.filemanagerwindowsphonecopy";
    public String path;
    private ArrayList<String> als;
    private String prefInner = "VIEW1", prefSd = "VIEW";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fim = new FragmentSDCardNoAction(DEFAULT_VALUE_INNER_MEMORY, prefInner);
        fSDC = new FragmentSDCardNoAction(DEFAULT_VALUE_SD, prefSd);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent i = getIntent();
        path = i.getStringExtra("KEY_PATH");
        if(path == null) {
            als = i.getStringArrayListExtra("KEY");
            if(als == null) {
                als = i.getStringArrayListExtra("KEY_C");
            }
            chooseInnerMemoryFragment(fSDC);

            focusSD = true;

        } else {
            String[] pathSplit = path.split("/");
            if(pathSplit[1].equals("sdcard")){
                chooseInnerMemoryFragment(fSDC);
                focusSD = true;
            } else {
                chooseInnerMemoryFragment(fim);
                focusSD = false;
            }
        }
    }

    private void chooseInnerMemoryFragment(Fragment f) {
        ft = getFragmentManager().beginTransaction();
        ft.add(R.id.containerInnerMemory, f);
        ft.commit();
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_newFolder:
                    if (focusSD) {
                        LayoutInflater ltInflaterGivenNames = getLayoutInflater();
                        final View view = ltInflaterGivenNames.inflate(R.layout.edit_text, null, false);
                        final EditText et = (EditText) view.getRootView().findViewById(R.id.et);
                        et.setText("NewFolder");
                        final AlertDialog.Builder adb = new AlertDialog.Builder(Main2Activity.this);
                        adb.setTitle("Create new folder");
                        adb.setMessage("Folder name");
                        adb.setView(view);
                        adb.setCancelable(false);
                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String res = et.getText().toString();
                                File f = new File(fSDC.getPathMajor());
                                File[] fff = f.listFiles();
                                boolean t = false;
                                for(int i = 0; i < fff.length; i++) {
                                    if(fff[i].getName().equals(res)){
                                        t= true;
                                    }
                                }
                                if(t) {
                                    Toast.makeText(getBaseContext(), "This name exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    File folder = new File(fSDC.getPathMajor() + File.separator + res);
                                    folder.mkdir();
                                    fSDC.addInfo(fSDC.getPathMajor());
                                    if(fSDC.isFocusLvGv()) {
                                        fSDC.getIma().clear();
                                        fSDC.getIma().addAll(fSDC.getAlfo());
                                    } else {
                                        fSDC.getImagv().clear();
                                        fSDC.getImagv().addAll(fSDC.getAlfo());
                                    }

                                    fSDC.refreshMeth();

                                }
                            }
                        });
                        AlertDialog alertGivenNames = adb.create();
                        alertGivenNames.show();



                    } else {

                        LayoutInflater ltInflaterGivenNames = getLayoutInflater();
                        final View view = ltInflaterGivenNames.inflate(R.layout.edit_text, null, false);
                        final EditText et = (EditText) view.getRootView().findViewById(R.id.et);
                        et.setText("NewFolder");
                        final AlertDialog.Builder adb = new AlertDialog.Builder(Main2Activity.this);
                        adb.setTitle("Create new folder");
                        adb.setMessage("Folder name");
                        adb.setView(view);
                        adb.setCancelable(false);
                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String res = et.getText().toString();
                                File f = new File(fim.getPathMajor());
                                File[] fff = f.listFiles();
                                boolean t = false;
                                for(int i = 0; i < fff.length; i++) {
                                    if(fff[i].getName().equals(res)){
                                        t= true;
                                    }
                                }
                                if(t) {
                                    Toast.makeText(getBaseContext(), "This name exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    File folder = new File(fim.getPathMajor() + File.separator + res);
                                    folder.mkdir();
                                    fim.addInfo(fim.getPathMajor());
                                    if(fim.isFocusLvGv()) {
                                        fim.getIma().clear();
                                        fim.getIma().addAll(fim.getAlfo());
                                    } else {
                                        fim.getImagv().clear();
                                        fim.getImagv().addAll(fim.getAlfo());
                                    }

                                    fim.refreshMeth();

                                }
                            }
                        });
                        AlertDialog alertGivenNames = adb.create();
                        alertGivenNames.show();
                    }

                    return true;
                case R.id.navigation_accept:
                    if(focusSD) {
                        if(path == null) {
                            for(final String ress : als) {
                                if(new File(ress).isDirectory()) {
                                    CopyCatalog cc = new CopyCatalog(ress, fSDC.getPathMajor());
                                    setResult(RESULT_OK);
                                } else {
                                    CopyCatalog ccFile = new CopyCatalog();
                                    try {
                                        ccFile.copyFile(ress, fSDC.getPathMajor());
                                        setResult(RESULT_OK);
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        } else {
                            if(new File(path).isDirectory()) {
                                CopyCatalog cc = new CopyCatalog(path, fSDC.getPathMajor());
                                setResult(RESULT_OK);
                            } else {
                                CopyCatalog ccFile = new CopyCatalog();
                                try {
                                    ccFile.copyFile(path, fSDC.getPathMajor());
                                    setResult(RESULT_OK);
                                } catch (IOException e) {

                                }
                            }
                        }
                    } else {
                        if(path == null) {
                            for(final String ress : als) {
                                if(new File(ress).isDirectory()) {
                                    CopyCatalog cc = new CopyCatalog(ress, fim.getPathMajor());
                                    setResult(RESULT_OK);
                                } else {
                                    CopyCatalog ccFile = new CopyCatalog();
                                    try {
                                        ccFile.copyFile(ress, fim.getPathMajor());
                                        setResult(RESULT_OK);
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        } else {
                            if(new File(path).isDirectory()) {
                                CopyCatalog cc = new CopyCatalog(path, fim.getPathMajor());
                                setResult(RESULT_OK);
                            } else {
                                CopyCatalog ccFile = new CopyCatalog();
                                try {
                                    ccFile.copyFile(path, fim.getPathMajor());
                                    setResult(RESULT_OK);
                                } catch (IOException e) {

                                }
                            }
                        }

                    }
                    finish();
                    return true;
                case R.id.navigation_cancel:
                    finish();
                    return true;

            }
            return false;
        }
    };

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
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(focusSD) {
                if (fSDC.getPathMajor().equals(DEFAULT_VALUE_SD)) {
                    finish();
                } else {
                    String path = null;
                    String[] rebuild = fSDC.getPathMajor().split("/");
                    for (int i = 0; i < rebuild.length; i++) {
                        if (rebuild.length - 1 == i) {
                            break;
                        }
                        if(i == 0) {
                            path = rebuild[i] + "/";
                        } else if (rebuild.length - 2 == i) {
                            path = path + rebuild[i];
                        } else {
                            path = path +  rebuild[i] + "/";
                        }
                    }
                    fSDC.setPathMajor(path);
                    fSDC.addInfo(fSDC.getPathMajor());
                    if(fSDC.isFocusLvGv()) {
                        fSDC.getIma().clear();
                        fSDC.getIma().addAll(fSDC.getAlfo());
                    } else {
                        fSDC.getImagv().clear();
                        fSDC.getImagv().addAll(fSDC.getAlfo());
                    }
                    fSDC.refreshMeth();
                }
            } else {
                if (fim.getPathMajor().equals(DEFAULT_VALUE_INNER_MEMORY)) {
                    finish();
                } else {
                    String path = null;
                    String[] rebuild = fim.getPathMajor().split("/");
                    for (int i = 0; i < rebuild.length; i++) {
                        if (rebuild.length - 1 == i) {
                            break;
                        }
                        if(i == 0) {
                            path = rebuild[i] + "/";
                        } else if (rebuild.length - 2 == i) {
                            path = path + rebuild[i];
                        } else {
                            path = path +  rebuild[i] + "/";
                        }
                    }
                    fim.setPathMajor(path);
                    fim.addInfo(fim.getPathMajor());
                    if(fim.isFocusLvGv()) {
                        fim.getIma().clear();
                        fim.getIma().addAll(fim.getAlfo());
                    } else {
                        fim.getImagv().clear();
                        fim.getImagv().addAll(fim.getAlfo());
                    }
                    fim.refreshMeth();
                }

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(focusSD) {
                    if (fSDC.getPathMajor().equals(DEFAULT_VALUE_SD)) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.START);
                    } else {
                        String path = null;
                        String[] rebuild = fSDC.getPathMajor().split("/");
                        for (int i = 0; i < rebuild.length; i++) {
                            if (rebuild.length - 1 == i) {
                                break;
                            }
                            if(i == 0) {
                                path = rebuild[i] + "/";
                            } else if (rebuild.length - 2 == i) {
                                path = path + rebuild[i];
                            } else {
                                path = path +  rebuild[i] + "/";
                            }
                        }
                        fSDC.setPathMajor(path);

                        fSDC.addInfo(fSDC.getPathMajor());
                        if(fSDC.isFocusLvGv()) {
                            fSDC.getIma().clear();
                            fSDC.getIma().addAll(fSDC.getAlfo());
                        } else {
                            fSDC.getImagv().clear();
                            fSDC.getImagv().addAll(fSDC.getAlfo());
                        }
                        fSDC.refreshMeth();
                    }
                } else {
                    if (fim.getPathMajor().equals(DEFAULT_VALUE_INNER_MEMORY)) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.START);
                    } else {
                        String path = null;
                        String[] rebuild = fim.getPathMajor().split("/");
                        for (int i = 0; i < rebuild.length; i++) {
                            if (rebuild.length - 1 == i) {
                                break;
                            }
                            if(i == 0) {
                                path = rebuild[i] + "/";
                            } else if (rebuild.length - 2 == i) {
                                path = path + rebuild[i];
                            } else {
                                path = path +  rebuild[i] + "/";
                            }
                        }
                        fim.setPathMajor(path);

                        fim.addInfo(fim.getPathMajor());
                        if(fim.isFocusLvGv()) {
                            fim.getIma().clear();
                            fim.getIma().addAll(fim.getAlfo());
                        } else {
                            fim.getImagv().clear();
                            fim.getImagv().addAll(fim.getAlfo());
                        }
                        fim.refreshMeth();
                    }
                }
                return false;
            }
        });
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.containerInnerMemory, fim);
            ft.commit();
            focusSD = false;
        } else if (id == R.id.nav_gallery) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.containerInnerMemory, fSDC);
                ft.commit();
                focusSD = true;
            } else {
                Toast.makeText(getBaseContext(), "SD-card is not set", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
