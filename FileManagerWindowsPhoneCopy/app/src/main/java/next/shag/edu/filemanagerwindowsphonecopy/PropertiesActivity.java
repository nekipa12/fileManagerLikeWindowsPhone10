package next.shag.edu.filemanagerwindowsphonecopy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.sql.Date;

import next.shag.edu.filemanagerwindowsphonecopy.second.FolderFileSize;

public class PropertiesActivity extends AppCompatActivity {

    private ImageView iv;
    private TextView tvNameProp, tvDateOfChangeRes, tvTypeOfFileRes, tvSizeFolderRes, tvContainsRes;
    private String path;
    private int countFolders;
    private int countFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);
        iv = (ImageView) findViewById(R.id.ivTypeOfFileProp);
        tvNameProp = (TextView) findViewById(R.id.tvNameProp);
        tvDateOfChangeRes = (TextView) findViewById(R.id.tvDateOfChangeRes);
        tvTypeOfFileRes = (TextView) findViewById(R.id.tvTypeOfFileRes);
        tvSizeFolderRes = (TextView) findViewById(R.id.tvSizeFolderRes);
        tvContainsRes = (TextView) findViewById(R.id.tvContainsRes);
        Intent i = getIntent();
        path = i.getStringExtra("KEY_PATH");
        File f = new File(path);
        tvNameProp.setText(f.getName());
        tvDateOfChangeRes.setText(String.valueOf(new Date(f.lastModified())));
        tvTypeOfFileRes.setText(getTypeOfFile(f));
        FolderFileSize ffs = new FolderFileSize();
        if(f.isDirectory()) {
            iv.setImageResource(R.drawable.normal_folder);
            tvSizeFolderRes.setText(ffs.converted(ffs.folderSize(f)));
        } else {
            if(getTypeOfFile(f).equalsIgnoreCase("jpg") || getTypeOfFile(f).equalsIgnoreCase("png")){
                iv.setImageBitmap(getBitmap(f));
            } else if(getTypeOfFile(f).equalsIgnoreCase("mp3")){
                iv.setImageResource(R.drawable.mp_3);
            } else if(getTypeOfFile(f).equalsIgnoreCase("txt") || getTypeOfFile(f).equalsIgnoreCase("rtf") || getTypeOfFile(f).equalsIgnoreCase("doc") || getTypeOfFile(f).equalsIgnoreCase("docx")){
                iv.setImageResource(R.drawable.text_document);
            } else if(getTypeOfFile(f).equalsIgnoreCase("html")){
                iv.setImageResource(R.drawable.html);
            } else if(getTypeOfFile(f).equalsIgnoreCase("pdf")){
                iv.setImageResource(R.drawable.pdf);
            } else {
                iv.setImageResource(R.drawable.file_un);
            }
            tvSizeFolderRes.setText(ffs.converted(f.length()));
            tvContainsRes.setVisibility(View.GONE);
        }
        tvContainsRes.setText("Folders: " + String.valueOf(ffs.getCountFolder()) + ", Files: " + String.valueOf(ffs.getCountFiles()));
    }
    private String getTypeOfFile (File file) {
        String fileName = file.getName();
        if (file.isDirectory()){
            return "folder";
        } else if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "file";
        }
    }

    private Bitmap getBitmap(File f) {
        String filePath = f.getAbsolutePath();
        Bitmap bitmap = decodeSampledBitmapFromResource(filePath, 100, 100);;
        return bitmap;
    }

    public Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
