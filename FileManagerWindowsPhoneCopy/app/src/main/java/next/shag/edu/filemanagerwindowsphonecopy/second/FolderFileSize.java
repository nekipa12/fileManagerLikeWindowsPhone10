package next.shag.edu.filemanagerwindowsphonecopy.second;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Val on 8/6/2017.
 */

public class FolderFileSize {

    private long length = 0;
    private int countFolder;
    private int countFiles;
    private long result;

    public int getCountFolder() {
        return countFolder;
    }

    public int getCountFiles() {
        return countFiles;
    }

    public long folderSize(File file) {

        File f1 = new File(file.getAbsolutePath());
        String s[] = f1.list();
        for (int i=0; i<s.length; i++) {
            File f2 = new File(file.getAbsolutePath() + "/" + s[i]);
            if (f2.isFile()) {
                length += f2.length();
                countFiles++;
            } else {
                length += folderSize(f2);
                countFolder++;
            }
        }
        return length;
    }

    public String converted(long ll){
        String res = null;
        long l = ll;
        if (l < 100) {
            double d = l;
            DecimalFormat df = new DecimalFormat("#.##");
            res = df.format(d) + " B";
        } else if (l < 100000) {
            double d = l;
            d /= 1000;
            DecimalFormat df = new DecimalFormat("#.##");
            res = df.format(d) + " KB";
        } else if (l < 100000000) {
            double d = l;
            d = (d / 1000) / 1000;
            DecimalFormat df = new DecimalFormat("#.##");
            res = df.format(d) + " MB";
        } else if (l > 100000000){
            double d = l;
            d = ((d / 1000) / 1000) / 1000;
            DecimalFormat df = new DecimalFormat("#.##");
            res = df.format(d) + " GB";
        }
        return  res;
    }
}
