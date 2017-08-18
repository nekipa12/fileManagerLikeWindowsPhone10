package layout;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import next.shag.edu.filemanagerwindowsphonecopy.FileObject;
import next.shag.edu.filemanagerwindowsphonecopy.R;
import next.shag.edu.filemanagerwindowsphonecopy.second.FolderFileSize;



public class FragmentSDCardNoAction extends Fragment {

    private ListView lv;
    private View v;
    private GridView gv;
    final int img = R.drawable.folder_closed;
    final int imgFile = R.drawable.file_modern;
    private String pathMajor = "./sdcard";
    private ArrayList<FileObject> alfo;
    private File [] files;
    private boolean focusLvGv;
    private ItemMenuAdapter ima;
    private ItemMenuAdapterGrid imagv;
    private String key;


    public FragmentSDCardNoAction(String default_value_sd, String pref) {
        pathMajor = default_value_sd;
        key = pref;
    }

    public String getPathMajor() {
        return pathMajor;
    }
    public void setPathMajor(String pathMajor) {
        this.pathMajor = pathMajor;
    }
    public ArrayList<FileObject> getAlfo() {
        return alfo;
    }
    public boolean isFocusLvGv() {
        return focusLvGv;
    }
    public ItemMenuAdapter getIma() {
        return ima;
    }
    public ItemMenuAdapterGrid getImagv() {
        return imagv;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_fragment_sdcard_no_action, null);
        lv = (ListView) v.findViewById(R.id.lvFragmentSDCard);
        gv = (GridView) v.findViewById(R.id.gvFragmentSDCard);

        addInfo(pathMajor);
        if(loadPref(key).equals("list")){
            setList();
        } else {
            setGrid();
        }
        return v;
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

    private  String loadPref(String key) {
        SharedPreferences sp1 = this.getActivity().getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        String res = sp1.getString(key, "");
        return res;
    }

    private void setGrid() {
        focusLvGv = false;
        lv.setVisibility(View.GONE);
        registerForContextMenu(gv);
        gv.setVisibility(View.VISIBLE);
        imagv = new ItemMenuAdapterGrid(v.getContext(), alfo);
        gv.setAdapter(imagv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String values = imagv.getItem(position).getName();
                String path = pathMajor + "/" + values;
                File f = new File(path);
                if(f.isDirectory()) {
                    pathMajor = path;
                    addInfo(path);
                    imagv.clear();
                    imagv.addAll(alfo);
                    refreshMeth();
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
                String values = ima.getItem(position).getName();
                String path = pathMajor + "/" + values;
                File f = new File(path);
                if(f.isDirectory()) {
                    pathMajor = path;
                    addInfo(path);
                    ima.clear();
                    ima.addAll(alfo);
                    refreshMeth();
                }
            }
        });
    }

    public void refreshMeth () {
        if (alfo.isEmpty()) {
            ((TextView) v.findViewById(R.id.tvSd)).setText("Nothing to show");
        } else {
            ((TextView) v.findViewById(R.id.tvSd)).setText("Choose folder");
        }
    }

    private String getTypeOfFile (File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public class ItemMenuAdapter extends ArrayAdapter<FileObject> {

        private ArrayList<FileObject> alfo;
        private int mSize;
        private LruCache<String, Bitmap> mMemoryCache;
        private ImageView imageView;
        ViewHolder viewHolder;

        ItemMenuAdapter(Context context, ArrayList<FileObject> items) {
            super(context, 0, items);
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
            ((TextView)convertView.findViewById(R.id.textView4)).setText(String.valueOf(alfo.get(position).getSize()));
            ((ImageView)convertView.findViewById(R.id.iv)).setImageResource(alfo.get(position).getImg());
            imageView = (ImageView) convertView.findViewById(R.id.iv);
            if(alfo.get(position).getType().equalsIgnoreCase("jpg") || alfo.get(position).getType().equalsIgnoreCase("png")){
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

            return convertView;
        }

        private Bitmap getBitmap(int position) {
            String filePath = alfo.get(position).getFullPath();
            Bitmap bitmap = getBitmapFromMemCache(filePath);
            if (bitmap == null) {
                bitmap = FragmentSDCard.Utils.decodeSampledBitmapFromResource(filePath, mSize, mSize);
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
        private int mSize;
        private LruCache<String, Bitmap> mMemoryCache;

        ItemMenuAdapterGrid(Context context, ArrayList<FileObject> items) {
            super(context, 0, items);
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
            return convertView;
        }

        private Bitmap getBitmap(int position) {
            String filePath = alfo.get(position).getFullPath();
            Bitmap bitmap = getBitmapFromMemCache(filePath);
            if (bitmap == null) {
                bitmap = FragmentSDCard.Utils.decodeSampledBitmapFromResource(filePath, mSize, mSize);
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
