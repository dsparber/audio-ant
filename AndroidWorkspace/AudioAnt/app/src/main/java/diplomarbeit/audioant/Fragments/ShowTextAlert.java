package diplomarbeit.audioant.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Benjamin on 05.03.2016.
 */
public class ShowTextAlert extends DialogFragment {
    String text;
    String header;

    public void setText(String text)
    {
        this.text = text;
    }
    public void setHeader(String text)
    {
        this.header = text;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(header);
        builder.setMessage(text);
        return builder.create();
    }
}
