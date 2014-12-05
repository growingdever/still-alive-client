package ssu.userinterface.stillalive.main.signin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ssu.userinterface.stillalive.R;
import ssu.userinterface.stillalive.R.id;
import ssu.userinterface.stillalive.R.layout;

public class PhoneCheckDialog extends DialogFragment {

    Runnable callback;
    int certification;
    EditText editTextCert;


    static PhoneCheckDialog NewInstance(Runnable callback, int cert) {
        PhoneCheckDialog dialog = new PhoneCheckDialog();
        dialog.callback = callback;

        Bundle args = new Bundle();
        args.putInt("cert", cert);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        certification = getArguments().getInt("cert");
    }

    public Dialog onCreateDialog(Bundle saveInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_phonecheck, null);
        editTextCert = (EditText) view.findViewById(id.dialog_phone_number_check);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Please enter your certification number")
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = editTextCert.getText().toString();
                        int cert = Integer.parseInt(content);

                        if( certification == cert ) {
                            callback.run();
                        }
                        else {
                            Toast.makeText(getActivity(), "wrong certification number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return builder.create();

    }


}
