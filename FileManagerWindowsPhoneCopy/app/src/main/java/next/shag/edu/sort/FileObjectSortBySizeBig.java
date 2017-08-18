package next.shag.edu.sort;

import java.util.Comparator;

import next.shag.edu.filemanagerwindowsphonecopy.FileObject;

/**
 * Created by Val on 7/26/2017.
 */

public class FileObjectSortBySizeBig implements Comparator<FileObject> {
    @Override
    public int compare(FileObject o1, FileObject o2) {
        String s1 = o1.getSize();
        String [] ss1 = s1.split(" ");
        String s2 = o2.getSize();
        String [] ss2 = s2.split(" ");
        long sss1 = res(ss1[0], ss1[1]);
        long sss2 = res(ss2[0], ss2[1]);

        if (sss1 > sss2) {
            return 1;
        } else if (sss1 < sss2) {
            return -1;
        } else {
            return 0;
        }
    }

    public long res(String s, String s1) {
        if(s1.equals("KB")) {
            return (long)(Double.parseDouble(s) * 1000);
        } else if(s1.equals("MB")) {
            return  (long)((Double.parseDouble(s) * 1000) * 1000);
        } else if(s1.equals("GB")) {
            return (long)(((Double.parseDouble(s) * 1000) * 1000) * 1000);
        }
        return 0;
    }
}
