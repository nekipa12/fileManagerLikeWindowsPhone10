package next.shag.edu.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by Val on 7/27/2017.
 */

public class DialogDelete extends DialogFragment {

    String tagF;


    public DialogDelete() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Are you shure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent();
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        tagF = tag;
        super.show(manager, tag);
    }
}
