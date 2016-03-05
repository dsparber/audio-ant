package diplomarbeit.audioant.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import diplomarbeit.audioant.R;

/**
 * Created by Benjamin on 05.03.2016.
 */
public class ChooseSoundAlert extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.notification_choose_header));

        return builder.create();
    }
}
