package next.shag.edu.sort;

import java.util.Comparator;

import next.shag.edu.filemanagerwindowsphonecopy.FileObject;

/**
 * Created by Val on 7/26/2017.
 */

public class FileObjectSortByNameZ implements Comparator<FileObject> {
    @Override
    public int compare(FileObject o1, FileObject o2) {
        return o2.getName().compareTo(o1.getName());
    }
}
