package next.shag.edu.filemanagerwindowsphonecopy;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import layout.FragmentSDCard;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentSDCard fSDC, fim;
    private FragmentTransaction ft;
    private final String DEFAULT_VALUE_SD = "./sdcard";
    private final String DEFAULT_VALUE_INNER_MEMORY = "./data/data/next.shag.edu.filemanagerwindowsphonecopy";
    private boolean focusSD;
    private String prefInner = "VIEW1", prefSd = "VIEW";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            navigationView.getMenu().getItem(1).setVisible(false);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                fim = new FragmentSDCard(DEFAULT_VALUE_INNER_MEMORY, prefInner);
                chooseInnerMemoryFragment();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                fSDC = new FragmentSDCard(DEFAULT_VALUE_SD, prefSd);
            }
        }).start();
        //chooseInnerMemoryFragment();
        //Astask at = new Astask();
        //at.execute();
        /*fim = new FragmentSDCard(DEFAULT_VALUE_INNER_MEMORY, prefInner);
        fSDC = new FragmentSDCard(DEFAULT_VALUE_SD, prefSd);*/
        //chooseInnerMemoryFragment();

    }

    class Astask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            fim = new FragmentSDCard(DEFAULT_VALUE_INNER_MEMORY, prefInner);
            fSDC = new FragmentSDCard(DEFAULT_VALUE_SD, prefSd);
            chooseInnerMemoryFragment();
            return null;
        }
    }



    public void newFolder() {
        if(focusSD) {
            LayoutInflater ltInflaterGivenNames = getLayoutInflater();
            final View view = ltInflaterGivenNames.inflate(R.layout.edit_text, null, false);
            final EditText et = (EditText) view.getRootView().findViewById(R.id.et);
            et.setText("NewFolder");
            final AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
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
                            fSDC.getIma().checkAndSort();
                        } else {
                            fSDC.getImagv().clear();
                            fSDC.getImagv().addAll(fSDC.getAlfo());
                            fSDC.getImagv().checkAndSort();
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
            final AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
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
                            fim.getIma().checkAndSort();
                        } else {
                            fim.getImagv().clear();
                            fim.getImagv().addAll(fim.getAlfo());
                            fim.getImagv().checkAndSort();
                        }

                        fim.refreshMeth();

                    }
                }
            });
            AlertDialog alertGivenNames = adb.create();
            alertGivenNames.show();
        }
    }




    private void chooseInnerMemoryFragment() {
        ft = getFragmentManager().beginTransaction();
        ft.add(R.id.containerInnerMemory, fim);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(focusSD) {
                if (fSDC.getIma() != null && fSDC.getIma().isVisible()) {
                    fSDC.getIma().skipAll();
                    fSDC.getIma().goneCheckBox(false);
                    fSDC.registerForContextMenu(fSDC.getLv());
                    fSDC.getNavigation().setVisibility(View.VISIBLE);
                    fSDC.getNavigation2().setVisibility(View.GONE);
                } else if (fSDC.getImagv() != null && fSDC.getImagv().isVisible()) {
                    fSDC.getImagv().skipAll();
                    fSDC.getImagv().goneCheckBox(false);
                    fSDC.registerForContextMenu(fSDC.getGv());
                    fSDC.getNavigation().setVisibility(View.VISIBLE);
                    fSDC.getNavigation2().setVisibility(View.GONE);
                } else if(fSDC.getLl().getVisibility() == View.VISIBLE) {
                    fSDC.getLl().setVisibility(View.GONE);
                    fSDC.getEtSearch().setText("");
                    fSDC.addInfo(fSDC.getPathMajor());
                    fSDC.refreshMeth();
                    fSDC.getNavigation().getMenu().getItem(1).setEnabled(true);
                    fSDC.getNavigation().getMenu().getItem(3).setEnabled(true);
                }  else {
                    if (fSDC.getPathMajor().equals(DEFAULT_VALUE_SD)) {
                        finish();
                    } else {
                        if(fSDC.isFocusLvGv()) {
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
                            setTitle(fSDC.getPathMajor());
                            fSDC.addInfo(fSDC.getPathMajor());
                            fSDC.getIma().clear();
                            fSDC.getIma().addAll(fSDC.getAlfo());
                            fSDC.getIma().checkAndSort();
                            fSDC.refreshMeth();
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
                            setTitle(fSDC.getPathMajor());
                            fSDC.addInfo(fSDC.getPathMajor());
                            fSDC.getImagv().clear();
                            fSDC.getImagv().addAll(fSDC.getAlfo());
                            fSDC.getImagv().checkAndSort();
                            fSDC.refreshMeth();
                        }

                    }
                }

            } else {
                if (fim.getIma() != null && fim.getIma().isVisible()) {
                    fim.getIma().skipAll();
                    fim.getIma().goneCheckBox(false);
                    fim.registerForContextMenu(fim.getLv());
                    fim.getNavigation().setVisibility(View.VISIBLE);
                    fim.getNavigation2().setVisibility(View.GONE);
                } else if (fim.getImagv() != null && fim.getImagv().isVisible()) {
                    fim.getImagv().skipAll();
                    fim.getImagv().goneCheckBox(false);
                    fim.registerForContextMenu(fim.getGv());
                    fim.getNavigation().setVisibility(View.VISIBLE);
                    fim.getNavigation2().setVisibility(View.GONE);
                } else if(fim.getLl().getVisibility() == View.VISIBLE) {
                    fim.getLl().setVisibility(View.GONE);
                    fim.getEtSearch().setText("");
                    fim.addInfo(fim.getPathMajor());
                    fim.refreshMeth();
                    fim.getNavigation().getMenu().getItem(1).setEnabled(true);
                    fim.getNavigation().getMenu().getItem(3).setEnabled(true);
                }  else {
                    if (fim.getPathMajor().equals(DEFAULT_VALUE_INNER_MEMORY)) {
                        finish();
                    } else {
                        if(fim.isFocusLvGv()) {
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
                            setTitle(fim.getPathMajor());
                            fim.addInfo(fim.getPathMajor());
                            fim.getIma().clear();
                            fim.getIma().addAll(fim.getAlfo());
                            fim.getIma().checkAndSort();
                            fim.refreshMeth();
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
                            setTitle(fim.getPathMajor());
                            this.setTitle(R.id.tvTitle);
                            fim.addInfo(fim.getPathMajor());
                            fim.getImagv().clear();
                            fim.getImagv().addAll(fim.getAlfo());
                            fim.getImagv().checkAndSort();
                            fim.refreshMeth();
                        }

                    }
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.containerInnerMemory, fim);
            ft.commit();
            setTitle(fim.getPathMajor());
            focusSD = false;
        } else if (id == R.id.nav_gallery) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.containerInnerMemory, fSDC);
                ft.commit();
                setTitle(fSDC.getPathMajor());
                focusSD = true;
            } else {
                Toast.makeText(getBaseContext(), "SD-card is not set", Toast.LENGTH_SHORT).show();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
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
                         if(fSDC.isFocusLvGv()) {
                             if (fSDC.getIma().isVisible()) {
                                 fSDC.getIma().skipAll();
                                 fSDC.getIma().goneCheckBox(false);
                                 fSDC.getNavigation().setVisibility(View.VISIBLE);
                                 fSDC.getNavigation2().setVisibility(View.GONE);

                             }
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
                             setTitle(fSDC.getPathMajor());
                             fSDC.addInfo(fSDC.getPathMajor());
                             fSDC.getIma().clear();
                             fSDC.getIma().addAll(fSDC.getAlfo());
                             fSDC.getIma().checkAndSort();
                         } else {
                             if (fSDC.getImagv().isVisible()) {
                                 fSDC.getImagv().skipAll();
                                 fSDC.getImagv().goneCheckBox(false);
                                 fSDC.getNavigation().setVisibility(View.VISIBLE);
                                 fSDC.getNavigation2().setVisibility(View.GONE);

                             }
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
                             setTitle(fSDC.getPathMajor());
                             fSDC.addInfo(fSDC.getPathMajor());
                             fSDC.getImagv().clear();
                             fSDC.getImagv().addAll(fSDC.getAlfo());
                             fSDC.getImagv().checkAndSort();
                         }
                         fSDC.refreshMeth();
                     }
                 } else {
                     if (fim.getPathMajor().equals(DEFAULT_VALUE_INNER_MEMORY)) {
                         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                         drawer.openDrawer(GravityCompat.START);
                     } else {
                         if(fim.isFocusLvGv()) {
                             if (fim.getIma().isVisible()) {
                                 fim.getIma().skipAll();
                                 fim.getIma().goneCheckBox(false);
                                 fim.getNavigation().setVisibility(View.VISIBLE);
                                 fim.getNavigation2().setVisibility(View.GONE);

                             }
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
                             setTitle(fim.getPathMajor());
                             fim.addInfo(fim.getPathMajor());
                             fim.getIma().clear();
                             fim.getIma().addAll(fim.getAlfo());
                             fim.getIma().checkAndSort();
                         } else {
                             if (fim.getImagv().isVisible()) {
                                 fim.getImagv().skipAll();
                                 fim.getImagv().goneCheckBox(false);
                                 fim.getNavigation().setVisibility(View.VISIBLE);
                                 fim.getNavigation2().setVisibility(View.GONE);
                             }
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
                             setTitle(fim.getPathMajor());
                             fim.addInfo(fim.getPathMajor());
                             fim.getImagv().clear();
                             fim.getImagv().addAll(fim.getAlfo());
                             fim.getImagv().checkAndSort();
                         }
                         fim.refreshMeth();
                     }
                 }
                return false;
            }
        });
        return super.onOptionsItemSelected(item);
    }
}
