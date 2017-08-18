package next.shag.edu.filemanagerwindowsphonecopy.second;

import java.io.File;

/**
 * Created by Val on 7/29/2017.
 */

public class DeleteCatalog {

    public DeleteCatalog(File f) {
        recursiveDelete(f);
    }

    public static void recursiveDelete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
    }
}
