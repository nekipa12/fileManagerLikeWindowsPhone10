package next.shag.edu.filemanagerwindowsphonecopy.second;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Val on 7/29/2017.
 */

public class CopyCatalog {

    private final int BUFFER_SIZE = 1024;


    public CopyCatalog(String src, String dst) {
        copyDir(src, dst);
    }

    public CopyCatalog() {
    }

    private boolean copyDir(final String src, final String dst) {
        final File srcFile = new File(src);
        final File dstFile = new File(dst + File.separator + srcFile.getName());
        if (srcFile.exists() && srcFile.isDirectory() && !dstFile.exists()) {
            dstFile.mkdir();
            File nextSrcFile;
            String nextSrcFilename, nextDstFilename;
            for (String filename : srcFile.list()) {
                nextSrcFilename = srcFile.getAbsolutePath()+ File.separator + filename;
                nextDstFilename = dstFile.getAbsolutePath()+ File.separator;
                nextSrcFile = new File(nextSrcFilename);
                if (nextSrcFile.isDirectory()) {
                    copyDir(nextSrcFilename, nextDstFilename);
                } else {
                    try {
                        copyFile(nextSrcFilename, nextDstFilename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void copyFile(String sourcePath, String destPath) throws IOException {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath + File.separator + sourceFile.getName());

        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
}
