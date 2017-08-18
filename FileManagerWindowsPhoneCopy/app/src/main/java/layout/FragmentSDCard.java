package layout;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.util.LruCache;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

import next.shag.edu.dialog.DialogDelete;
import next.shag.edu.filemanagerwindowsphonecopy.FileObject;
import next.shag.edu.filemanagerwindowsphonecopy.MainActivity;
import next.shag.edu.filemanagerwindowsphonecopy.R;
import next.shag.edu.filemanagerwindowsphonecopy.second.DeleteCatalog;
import next.shag.edu.filemanagerwindowsphonecopy.second.FolderFileSize;
import next.shag.edu.sort.FileObjectSortByDateBig;
import next.shag.edu.sort.FileObjectSortByDateSmall;
import next.shag.edu.sort.FileObjectSortByNameA;
import next.shag.edu.sort.FileObjectSortByNameZ;
import next.shag.edu.sort.FileObjectSortBySizeBig;
import next.shag.edu.sort.FileObjectSortBySizeSmall;

public class FragmentSDCard extends Fragment {

    private ListView lv;
    private View v;
    private String pathMajor;
    private ArrayList<FileObject> alfo;
    private String [] items = {"Name (Begin with A)", "Name (Begin with Z)", "Size (Begin with small)", "Size(Begin with big)", "Date (Begin with old)", "Date (Begin with new)"};
    private Spinner spinner;
    private int positionSpinner;
    private File [] files;
    public static final int REQUEST_WEIGHT = 1;
    public static int posit;
    public static final int REQUEST_KEY = 2;
    private String pathDel;
    private EditText etSearch;
    private LinearLayout ll;
    private BottomNavigationView navigation, navigation2;
    private ItemMenuAdapter ima;
    private ItemMenuAdapterGrid imagv;
    private ArrayList<String> checkedResult;
    private  GridView gv;
    private boolean focusLvGv;
    private String key;
    private  LayoutInflater inflater;




    public FragmentSDCard(String default_value_sd, String pref) {
        pathMajor = default_value_sd;
        key = pref;

    }

    public String getPathMajor() {
        return pathMajor;
    }
    public void setPathMajor(String pathMajor) {
        this.pathMajor = pathMajor;
    }
    public boolean isFocusLvGv() {
        return focusLvGv;
    }
    public ItemMenuAdapterGrid getImagv() {
        return imagv;
    }
    public ItemMenuAdapter getIma() {
        return ima;
    }
    public BottomNavigationView getNavigation() {
        return navigation;
    }
    public BottomNavigationView getNavigation2() {
        return navigation2;
    }
    public ArrayList<FileObject> getAlfo() {
        return alfo;
    }
    public LinearLayout getLl() {
        return ll;
    }
    public EditText getEtSearch() {
        return etSearch;
    }
    public ListView getLv() {
        return lv;
    }
    public GridView getGv() {
        return gv;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fragment_sdcard, null);
        this.inflater = inflater;
        navigation = (BottomNavigationView) v.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation2 = (BottomNavigationView) v.findViewById(R.id.navigation2);
        navigation2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getActivity().setTitle(pathMajor);
        ll = (LinearLayout) v.findViewById(R.id.layoutVisible);
        ll.setVisibility(View.GONE);
        gv = (GridView) v.findViewById(R.id.gvFragmentSDCard);
        lv = (ListView) v.findViewById(R.id.lvFragmentSDCard);

        addInfo(pathMajor);
        if(loadPref(key).equals("list")){
            setList();
            navigation.getMenu().getItem(2).setIcon(R.drawable.icons);
        } else {
            setGrid();
            navigation.getMenu().getItem(2).setIcon(R.drawable.icons_list);
        }


        etSearch = (EditText) v.findViewById(R.id.editTextSerch);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setFocusable(true);
            }
        });
        ArrayAdapter<String> aa = new ArrayAdapter<String>(v.getContext(), R.layout.spinner_layout, items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) v.findViewById(R.id.spinnerSd);
        spinner.setAdapter(aa);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionSpinner = position;
                if(loadPref(key).equals("list")){
                    ima.checkAndSort();
                } else {
                    imagv.checkAndSort();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        return v;
    }

    private void setGrid() {
        focusLvGv = false;
        lv.setVisibility(View.GONE);
        registerForContextMenu(gv);
        gv.setVisibility(View.VISIBLE);
        imagv = new ItemMenuAdapterGrid(v.getContext(), alfo);
        gv.setAdapter(imagv);
        imagv.checkAndSort();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(imagv.isVisible()) {
                    navigation2.getMenu().setGroupEnabled(R.id.groups, true);
                    if(imagv.getItem(position).isChecked()) {
                        imagv.getItem(position).setChecked(false);
                        int counter = 0;
                        for(int i = 0; i < imagv.getCount(); i++) {
                            if(imagv.getItem(i).isChecked())
                                counter++;
                        }
                        if(counter == 0){
                            imagv.goneCheckBox(false);
                            registerForContextMenu(gv);
                            navigation2.setVisibility(View.GONE);
                            navigation.setVisibility(View.VISIBLE);
                        }
                        imagv.notifyDataSetChanged();
                    } else {
                        imagv.getItem(position).setChecked(true);
                        imagv.notifyDataSetChanged();
                    }
                } else {
                    ll.setVisibility(View.GONE);
                    etSearch.setText("");
                    String values = imagv.getItem(position).getName();
                    String path = pathMajor + "/" + values;
                    File f = new File(path);
                    if(f.isDirectory()) {
                        pathMajor = path;
                        getActivity().setTitle(pathMajor);
                        addInfo(path);
                        imagv.clear();
                        imagv.addAll(alfo);
                        imagv.checkAndSort();
                        refreshMeth();
                    } else {
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String mimeType = mime.getMimeTypeFromExtension(fileExt(imagv.getItem(position).getFullPath()));
                        i.setDataAndType(Uri.fromFile(new File(imagv.getItem(position).getFullPath())), mimeType);
                        try {
                            startActivity(i);
                        } catch (Exception e) {
                            Toast.makeText(v.getContext(), "Couldn`t open", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    public void setList() {
        focusLvGv = true;
        gv.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
        registerForContextMenu(lv);
        ima = new ItemMenuAdapter(v.getContext(), alfo);
        lv.setAdapter(ima);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ima.isVisible()) {
                    navigation2.getMenu().setGroupEnabled(R.id.groups, true);
                    if(ima.getItem(position).isChecked()) {
                        ima.getItem(position).setChecked(false);
                        int counter = 0;
                        for (int i = 0; i < ima.getCount(); i++) {
                            if (ima.getItem(i).isChecked())
                                counter++;
                        }
                        if (counter == 0) {
                            ima.goneCheckBox(false);
                            registerForContextMenu(lv);
                            navigation2.setVisibility(View.GONE);
                            navigation.setVisibility(View.VISIBLE);
                        }
                        ima.notifyDataSetChanged();
                    } else {
                        ima.getItem(position).setChecked(true);
                        ima.notifyDataSetChanged();
                    }
                } else {
                    ll.setVisibility(View.GONE);
                    etSearch.setText("");
                    String values = ima.getItem(position).getName();
                    String path = pathMajor + "/" + values;
                    File f = new File(path);
                    if(f.isDirectory()) {
                        pathMajor = path;
                        getActivity().setTitle(pathMajor);
                        addInfo(path);
                        ima.clear();
                        ima.addAll(alfo);
                        ima.checkAndSort();
                        refreshMeth();
                    } else {
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String mimeType = mime.getMimeTypeFromExtension(fileExt(ima.getItem(position).getFullPath()));
                        i.setDataAndType(Uri.fromFile(new File(ima.getItem(position).getFullPath())), mimeType);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Intent j = Intent.createChooser(i, "Choose the app");
                        try {
                            startActivity(j);
                        } catch (Exception e) {
                            Toast.makeText(v.getContext(), "Couldn`t open", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    public void meth(File f, String text) {
        FolderFileSize ffs = new FolderFileSize();
        File [] files = f.listFiles();
        for(int i = 0; i < files.length; i++) {
            if(files[i].isDirectory()) {
                meth(files[i], text);
            } else {
                if(files[i].getName().toLowerCase().contains(text.toLowerCase())) {
                    alfo.add(new FileObject(files[i].getName(), ffs.converted(files[i].length()), new Date(files[i].lastModified()), getTypeOfFile(files[i]), R.drawable.file_un, false, files[i].getPath()));
                }
            }
        }
        if(focusLvGv) {
            ima.clear();
            ima.addAll(alfo);
            ima.checkAndSort();
        } else {
            imagv.clear();
            imagv.addAll(alfo);
            imagv.checkAndSort();
        }
        refreshMeth();
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }


    public void addInfo(String path) {

        alfo = new ArrayList<>();
        File pathSDCard = new File(path);
        files = pathSDCard.listFiles();
        for(int i = 0; i < files.length; i++) {
            FolderFileSize ffs = new FolderFileSize();
            if (files[i].isDirectory()) {
                alfo.add(new FileObject(files[i].getName(), ffs.converted(ffs.folderSize(files[i])), new Date(files[i].lastModified()), getTypeOfFile(files[i]), R.drawable.normal_folder, false,files[i].getPath()));
            } else if (files[i].isFile()) {
                alfo.add(new FileObject(files[i].getName(), ffs.converted(files[i].length()), new Date(files[i].lastModified()), getTypeOfFile(files[i]), R.drawable.file_un, false, files[i].getPath()));
            }
        }
    }

    public void refreshMeth () {
        if (alfo.isEmpty()) {
            navigation.getMenu().getItem(0).setEnabled(false);
            navigation.getMenu().getItem(2).setEnabled(false);
            navigation.getMenu().getItem(3).setEnabled(false);
            ((TextView) v.findViewById(R.id.tvSd)).setText("Nothing to show");
            ((Spinner) v.findViewById(R.id.spinnerSd)).setVisibility(View.GONE);
        } else {
            navigation.getMenu().getItem(0).setEnabled(true);
            navigation.getMenu().getItem(2).setEnabled(true);
            navigation.getMenu().getItem(3).setEnabled(true);
            ((TextView) v.findViewById(R.id.tvSd)).setText(R.string.sortBy);
            ((Spinner) v.findViewById(R.id.spinnerSd)).setVisibility(View.VISIBLE);
        }

    }

    private void savePref(String key, String value) {
        SharedPreferences sp = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, value);
        e.commit();
    }

    private  String loadPref(String key) {
        SharedPreferences sp = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        String res = sp.getString(key, "");
        return res;
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_choose:
                    if(focusLvGv) {
                        ima.goneCheckBox(true);
                        unregisterForContextMenu(lv);
                    } else {
                        imagv.goneCheckBox(true);
                        unregisterForContextMenu(gv);
                    }
                    navigation.setVisibility(View.GONE);
                    navigation2.setVisibility(View.VISIBLE);
                    navigation2.getMenu().setGroupEnabled(R.id.groups, false);


                    return true;
                case R.id.navigation_newFolder:
                    MainActivity ma = (MainActivity) getActivity();
                    ma.newFolder();
                    return true;
                case R.id.navigation_icons:
                    if(loadPref(key).equalsIgnoreCase("list")){
                        setGrid();
                        item.setIcon(R.drawable.icons_list);
                        savePref(key, "grid");
                    } else {
                        setList();
                        item.setIcon(R.drawable.icons);
                        savePref(key, "list");
                    }
                    return true;
                case R.id.navigation_search:
                    ll.setVisibility(View.VISIBLE);
                    etSearch.requestFocus();
                    navigation.getMenu().getItem(1).setEnabled(false);
                    etSearch.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if(etSearch.getText().toString().equals("")) {
                                addInfo(pathMajor);
                                if(focusLvGv) {
                                    ima.clear();
                                    ima.addAll(alfo);
                                    ima.checkAndSort();
                                } else {
                                    imagv.clear();
                                    imagv.addAll(alfo);
                                    imagv.checkAndSort();
                                }
                                refreshMeth();
                            } else {
                                alfo = new ArrayList<>();
                                meth(new File(pathMajor), etSearch.getText().toString());
                            }

                            return false;
                        }
                    });
                    return true;
                case R.id.navigation2_choose_all:
                    if(focusLvGv) {
                        ima.chooseAll();
                    } else {
                        imagv.chooseAll();
                    }
                    navigation2.getMenu().setGroupEnabled(R.id.groups, true);

                    return true;
                case R.id.navigation2_delete:
                    final AlertDialog.Builder adbSurname = new AlertDialog.Builder(v.getContext());
                    adbSurname.setTitle("Delete");
                    adbSurname.setMessage("Are you shure&");
                    adbSurname.setCancelable(false);

                    adbSurname.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    adbSurname.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(focusLvGv) {
                                for(String path : ima.getChouseElecents()) {
                                    DeleteCatalog dc = new DeleteCatalog(new File(path));
                                }
                                navigation2.setVisibility(View.GONE);
                                navigation.setVisibility(View.VISIBLE);
                                addInfo(pathMajor);
                                ima.goneCheckBox(false);
                                ima.clear();
                                ima.addAll(alfo);
                                ima.checkAndSort();
                                refreshMeth();
                            } else {
                                for(String path : imagv.getChouseElecents()) {
                                    DeleteCatalog dc = new DeleteCatalog(new File(path));
                                }
                                navigation2.setVisibility(View.GONE);
                                navigation.setVisibility(View.VISIBLE);
                                addInfo(pathMajor);
                                imagv.goneCheckBox(false);
                                imagv.clear();
                                imagv.addAll(alfo);
                                imagv.checkAndSort();
                                refreshMeth();
                            }


                        }
                    });
                    AlertDialog alert = adbSurname.create();
                    alert.show();
                    return true;
                case R.id.navigation2_move_to:
                    if(focusLvGv)
                        checkedResult = ima.getChouseElecents();
                     else
                        checkedResult = imagv.getChouseElecents();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent("android.intent.action.Main2");
                            i.putStringArrayListExtra("KEY_C", checkedResult);
                            startActivityForResult(i, 23);
                        }
                    }).start();


                    navigation2.setVisibility(View.GONE);
                    navigation.setVisibility(View.VISIBLE);
                    addInfo(pathMajor);
                    if(focusLvGv) {
                        ima.goneCheckBox(false);
                        ima.clear();
                        ima.addAll(alfo);
                        ima.checkAndSort();
                    } else {
                        imagv.goneCheckBox(false);
                        imagv.clear();
                        imagv.addAll(alfo);
                        imagv.checkAndSort();
                    }
                    refreshMeth();

                    return true;
                case R.id.navigation2_copy_in:
                    if(focusLvGv)
                        checkedResult = ima.getChouseElecents();
                    else
                        checkedResult = imagv.getChouseElecents();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent ii = new Intent("android.intent.action.Main2");
                            ii.putStringArrayListExtra("KEY", checkedResult);
                            startActivity(ii);
                        }
                    }).start();

                    navigation2.setVisibility(View.GONE);
                    navigation.setVisibility(View.VISIBLE);
                    if(focusLvGv) {
                        ima.skipAll();
                        ima.goneCheckBox(false);
                    } else {
                        imagv.skipAll();
                        imagv.goneCheckBox(false);
                    }

                    return true;
            }
            return false;
        }
    };

    private String typeOfFile (File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else  if (file.isDirectory()){
            return "folder";
        } else {
            return "file";
        }
    }

    public String getTypeOfFile (File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.contextMenuItemDelete :
                posit = acmi.position;
                if(focusLvGv)
                    pathDel = ima.getItem(acmi.position).getFullPath();
                else
                    pathDel = imagv.getItem(acmi.position).getFullPath();

                DialogFragment d = new DialogDelete();
                d.setTargetFragment(this, REQUEST_WEIGHT);
                d.show(getFragmentManager(), d.getClass().getName());

                return true;
            case R.id.contextMenuItemMoveTo :
                Intent i = new Intent("android.intent.action.Main2");
                if(focusLvGv)
                    pathDel = ima.getItem(acmi.position).getFullPath();
                else
                    pathDel = imagv.getItem(acmi.position).getFullPath();
                i.putExtra("KEY_PATH", pathDel);
                startActivityForResult(i, REQUEST_KEY);
                return true;
            case R.id.contextMenuItemCopy :
                Intent i2 = new Intent("android.intent.action.Main2");
                if(focusLvGv)
                    pathDel = ima.getItem(acmi.position).getFullPath();
                else
                    pathDel = imagv.getItem(acmi.position).getFullPath();
                i2.putExtra("KEY_PATH", pathDel);
                startActivity(i2);
                addInfo(pathMajor);
                if(focusLvGv) {
                    ima.clear();
                    ima.addAll(alfo);
                    ima.checkAndSort();
                } else {
                    imagv.clear();
                    imagv.addAll(alfo);
                    imagv.checkAndSort();
                }
                refreshMeth();
                return true;
            case R.id.contextMenuItemRename :
                LayoutInflater ltInflaterGivenNames = getActivity().getLayoutInflater();
                final View view = ltInflaterGivenNames.inflate(R.layout.edit_text, null, false);
                final EditText et = (EditText) view.getRootView().findViewById(R.id.et);
                posit = acmi.position;
                if(focusLvGv)
                    et.setText(ima.getItem(acmi.position).getName());
                else
                    et.setText(imagv.getItem(acmi.position).getName());

                final AlertDialog.Builder adb = new AlertDialog.Builder(v.getRootView().getContext());
                adb.setTitle("Rename");
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
                        File f;
                        if(focusLvGv)
                            f = new File(pathMajor, ima.getItem(posit).getName());
                        else
                            f = new File(pathMajor, imagv.getItem(posit).getName());
                        File ff = new File(pathMajor, et.getText().toString());
                        if(f.exists() && !ff.exists()) {
                            f.renameTo(ff);
                        }
                        addInfo(pathMajor);
                        if(focusLvGv) {
                            ima.clear();
                            ima.addAll(alfo);
                            ima.checkAndSort();
                        } else {
                            imagv.clear();
                            imagv.addAll(alfo);
                            imagv.checkAndSort();
                        }
                        refreshMeth();
                    }
                });
                AlertDialog alertGivenNames = adb.create();
                alertGivenNames.show();
                return true;
            case R.id.contextMenuItemProperties :
                if(focusLvGv)
                    pathDel = ima.getItem(acmi.position).getFullPath();
                else
                    pathDel = imagv.getItem(acmi.position).getFullPath();
                Intent iProp = new Intent("android.intent.action.Prop");
                iProp.putExtra("KEY_PATH", pathDel);
                startActivity(iProp);
                return true;
            default: return super.onContextItemSelected(item);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_KEY) {
            if(resultCode == Activity.RESULT_OK) {
                DeleteCatalog dc = new DeleteCatalog(new File(pathDel));
                addInfo(pathMajor);
                if(focusLvGv) {
                    ima.clear();
                    ima.addAll(alfo);
                    ima.checkAndSort();
                } else {
                    imagv.clear();
                    imagv.addAll(alfo);
                    imagv.checkAndSort();
                }
                refreshMeth();
            }

        } else if (requestCode == REQUEST_WEIGHT) {
            if(resultCode == Activity.RESULT_OK) {
                DeleteCatalog dc = new DeleteCatalog(new File(pathDel));
                addInfo(pathMajor);
                if(focusLvGv) {
                    ima.clear();
                    ima.addAll(alfo);
                    ima.checkAndSort();
                } else {
                    imagv.clear();
                    imagv.addAll(alfo);
                    imagv.checkAndSort();
                }
                refreshMeth();
            }
        } else if (requestCode == 23) {
            if(resultCode == Activity.RESULT_OK) {
                for(String path : checkedResult) {
                    DeleteCatalog dc = new DeleteCatalog(new File(path));
                }
                addInfo(pathMajor);
                if(focusLvGv) {
                    ima.clear();
                    ima.addAll(alfo);
                    ima.checkAndSort();
                } else {
                    imagv.clear();
                    imagv.addAll(alfo);
                    imagv.checkAndSort();
                }
                refreshMeth();
            }
        }


    }



    public class ItemMenuAdapter extends ArrayAdapter<FileObject> {

        private ArrayList<FileObject> alfo;
        private ArrayList<ImageView> cbal;
        private boolean visible;
        private int mSize;
        private LruCache<String, Bitmap> mMemoryCache;
        private ImageView imageView;
        ViewHolder viewHolder;

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        ItemMenuAdapter(Context context, ArrayList<FileObject> items) {
            super(context, 0, items);
            cbal = new ArrayList<>();
            alfo = items;
            mSize = context.getResources().getDimensionPixelSize(R.dimen.image_size);
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
            final int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount();
                }
            };
        }

        public void goneCheckBox(boolean vis) {
            visible = vis;
            notifyDataSetChanged();
        }

        public void chooseAll() {
            for(FileObject fo : alfo) {
                fo.setChecked(true);
            }
            notifyDataSetChanged();
        }

        public void checkAndSort() {
            if (positionSpinner == 0) {
                Collections.sort(alfo, new FileObjectSortByNameA());
            } else if (positionSpinner == 1) {
                Collections.sort(alfo, new FileObjectSortByNameZ());
            } else if (positionSpinner == 2) {
                Collections.sort(alfo, new FileObjectSortBySizeBig());
            } else if (positionSpinner == 3) {
                Collections.sort(alfo, new FileObjectSortBySizeSmall());
            } else if (positionSpinner == 4) {
                Collections.sort(alfo, new FileObjectSortByDateBig());
            } else if (positionSpinner == 5) {
                Collections.sort(alfo, new FileObjectSortByDateSmall());
            }
            notifyDataSetChanged();
        }

        public void skipAll() {
            for(FileObject fo : alfo) {
                fo.setChecked(false);
            }
            notifyDataSetChanged();
        }
        public ArrayList<String> getChouseElecents(){
            ArrayList<String> paths = new ArrayList<>();
            for(FileObject fo : alfo) {
                if(fo.isChecked())
                    paths.add(fo.getFullPath());
            }
            return paths;
        }

        @Override
        public FileObject getItem(int position) {
            return super.getItem(position);
        }

        class ViewHolder {
            ImageView imageView;
            int path;
            Bitmap bitmap;

        }
        public class AsyncSetImageView extends AsyncTask<ViewHolder, Void, ViewHolder> {

            @Override
            protected ViewHolder doInBackground(ViewHolder... params) {
                ViewHolder viewHolder = params[0];
                viewHolder.bitmap = getBitmap(viewHolder.path);

                return viewHolder;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(ViewHolder res) {
                super.onPostExecute(res);
                res.imageView.setImageBitmap(res.bitmap);
            }
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            viewHolder = null;
            if (convertView == null) {
                convertView =getActivity().getLayoutInflater()
                        .inflate(R.layout.item_for_list_view, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();

            ((TextView)convertView.findViewById(R.id.tvFolderName)).setText(alfo.get(position).getName());
            ((TextView)convertView.findViewById(R.id.tvFolderDateOfCreate)).setText(String.valueOf(alfo.get(position).getDate()));
            ((TextView)convertView.findViewById(R.id.textView4)).setText(alfo.get(position).getSize());
            imageView = (ImageView) convertView.findViewById(R.id.iv);
            if(alfo.get(position).getType().equalsIgnoreCase("jpg") || alfo.get(position).getType().equalsIgnoreCase("png") || alfo.get(position).getType().equalsIgnoreCase("gif") || alfo.get(position).getType().equalsIgnoreCase("ico")){
                viewHolder.path = position;
                AsyncSetImageView a = new AsyncSetImageView();
                a.execute(viewHolder);
            } else if(alfo.get(position).getType().equalsIgnoreCase("mp3")){
                imageView.setImageResource(R.drawable.mp_3);
            } else if(alfo.get(position).getType().equalsIgnoreCase("txt") || alfo.get(position).getType().equalsIgnoreCase("rtf") || alfo.get(position).getType().equalsIgnoreCase("doc") || alfo.get(position).getType().equalsIgnoreCase("docx")){
                imageView.setImageResource(R.drawable.text_document);
            } else if(alfo.get(position).getType().equalsIgnoreCase("html")){
                imageView.setImageResource(R.drawable.html);
            } else if(alfo.get(position).getType().equalsIgnoreCase("pdf")){
                imageView.setImageResource(R.drawable.pdf);
            } else {
                imageView.setImageResource(alfo.get(position).getImg());
            }

            CheckBox cb = (CheckBox)convertView.findViewById(R.id.cbChecked);
            cb.setTag(position);
            cb.setChecked(alfo.get(position).isChecked());
            final View finalConvertView = convertView;
            if(visible) {
                cb.setVisibility(View.VISIBLE);
            } else {
                cb.setVisibility(View.GONE);
            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alfo.get((Integer) buttonView.getTag()).setChecked(isChecked);
                    if(isChecked) {
                        finalConvertView.setBackgroundColor(Color.rgb(97, 132, 220));
                    } else {
                        finalConvertView.setBackgroundColor(Color.rgb(0, 0, 0));
                    }
                }
            });

            return convertView;
        }

        private Bitmap getBitmap(int position) {
            String filePath = alfo.get(position).getFullPath();
            Bitmap bitmap = getBitmapFromMemCache(filePath);
            if (bitmap == null) {
                bitmap = Utils.decodeSampledBitmapFromResource(filePath, mSize, mSize);
                addBitmapToMemoryCache(filePath, bitmap);
            }
            return bitmap;
        }

        public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        public Bitmap getBitmapFromMemCache(String key) {
            return mMemoryCache.get(key);
        }
    }



    public static class Utils {

        public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(path, options);
        }

        public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                while ((halfHeight / inSampleSize) > reqHeight&& (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }
    }


    public class ItemMenuAdapterGrid extends ArrayAdapter<FileObject> {

        private ArrayList<FileObject> alfo;
        private ArrayList<CheckBox> cbal;
        private boolean visible;
        private int mSize;
        private LruCache<String, Bitmap> mMemoryCache;

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        ItemMenuAdapterGrid(Context context, ArrayList<FileObject> items) {
            super(context, 0, items);
            cbal = new ArrayList<>();
            alfo = items;
            mSize = context.getResources().getDimensionPixelSize(R.dimen.image_size_gv);
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
            final int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount();
                }
            };
        }

        public void goneCheckBox(boolean vis) {
            visible = vis;
            notifyDataSetChanged();
        }

        public void chooseAll() {
            for(FileObject fo : alfo) {
                fo.setChecked(true);
            }
            notifyDataSetChanged();
        }

        public void skipAll() {
            for(FileObject fo : alfo) {
                fo.setChecked(false);
            }
            notifyDataSetChanged();
        }
        public ArrayList<String> getChouseElecents(){
            ArrayList<String> paths = new ArrayList<>();
            for(FileObject fo : alfo) {
                if(fo.isChecked())
                    paths.add(fo.getFullPath());
            }
            return paths;
        }
        public void checkAndSort() {
            if (positionSpinner == 0) {
                Collections.sort(alfo, new FileObjectSortByNameA());
            } else if (positionSpinner == 1) {
                Collections.sort(alfo, new FileObjectSortByNameZ());
            } else if (positionSpinner == 2) {
                Collections.sort(alfo, new FileObjectSortBySizeBig());
            } else if (positionSpinner == 3) {
                Collections.sort(alfo, new FileObjectSortBySizeSmall());
            } else if (positionSpinner == 4) {
                Collections.sort(alfo, new FileObjectSortByDateBig());
            } else if (positionSpinner == 5) {
                Collections.sort(alfo, new FileObjectSortByDateSmall());
            }
            notifyDataSetChanged();
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView =getActivity().getLayoutInflater()
                        .inflate(R.layout.item_for_grid_view, parent, false);
            }

            ((TextView)convertView.findViewById(R.id.textView)).setText(alfo.get(position).getName());
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            if(alfo.get(position).getType().equalsIgnoreCase("jpg") || alfo.get(position).getType().equalsIgnoreCase("png")){
                Bitmap bitmap = getBitmap(position);
                imageView.setImageBitmap(bitmap);
            } else if(alfo.get(position).getType().equalsIgnoreCase("mp3")){
                imageView.setImageResource(R.drawable.mp_3);
            } else if(alfo.get(position).getType().equalsIgnoreCase("txt") || alfo.get(position).getType().equalsIgnoreCase("rtf") || alfo.get(position).getType().equalsIgnoreCase("doc") || alfo.get(position).getType().equalsIgnoreCase("docx")){
                imageView.setImageResource(R.drawable.text_document);
            } else if(alfo.get(position).getType().equalsIgnoreCase("html")){
                imageView.setImageResource(R.drawable.html);
            } else if(alfo.get(position).getType().equalsIgnoreCase("pdf")){
                imageView.setImageResource(R.drawable.pdf);
            } else {
                imageView.setImageResource(alfo.get(position).getImg());
            }
            CheckBox cb = (CheckBox)convertView.findViewById(R.id.checkBox2);
            cb.setTag(position);
            cb.setChecked(alfo.get(position).isChecked());
            if(visible) {
                cb.setVisibility(View.VISIBLE);
            } else {
                cb.setVisibility(View.GONE);
            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alfo.get((Integer) buttonView.getTag()).setChecked(isChecked);
                }
            });
            cbal.add(cb);

            return convertView;
        }

        private Bitmap getBitmap(int position) {
            String filePath = alfo.get(position).getFullPath();
            Bitmap bitmap = getBitmapFromMemCache(filePath);
            if (bitmap == null) {
                bitmap = Utils.decodeSampledBitmapFromResource(filePath, mSize, mSize);
                addBitmapToMemoryCache(filePath, bitmap);
            }
            return bitmap;
        }

        public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        public Bitmap getBitmapFromMemCache(String key) {
            return mMemoryCache.get(key);
        }
    }



}
