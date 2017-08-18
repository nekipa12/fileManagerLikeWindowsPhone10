package next.shag.edu.filemanagerwindowsphonecopy;

import java.util.Date;

/**
 * Created by Val on 7/26/2017.
 */

public class FileObject  {

    private String name;
    private String size;
    private Date date;
    private String type;
    private int img;
    private boolean checked = false;
    private String fullPath;

    public FileObject(String name, String size, Date date, String type, int img, boolean checked, String fullPath) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.type = type;
        this.img = img;
        this.checked = checked;
        this.fullPath = fullPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullPath() {
        return fullPath;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public Date getDate() {
        return date;
    }

    public int getImg() {
        return img;
    }



}
