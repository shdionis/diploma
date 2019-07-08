package ru.yandex.sharov.example.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class ConfirmActionDialog extends DialogFragment {

    private static final String CONFIRM_BTN_TEXT_ARG = "confirm";
    private static final String MESSAGE_TEXT_ARG = "text";

    public static void showAlert(@NonNull String textMessage, @NonNull String yesBtnText, @NonNull Fragment fragment, int requestCode) {
        Bundle args = new Bundle();
        args.putString(CONFIRM_BTN_TEXT_ARG, yesBtnText);
        args.putString(MESSAGE_TEXT_ARG, textMessage);
        ConfirmActionDialog dialog = new ConfirmActionDialog();
        dialog.setArguments(args);
        dialog.setTargetFragment(fragment, requestCode);
        dialog.show(fragment.getFragmentManager(), null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("Context of dialog not found!");
        }
        String text = args.getString(MESSAGE_TEXT_ARG);
        String yesBtnText = args.getString(CONFIRM_BTN_TEXT_ARG);
        return new AlertDialog.Builder(requireContext())
                .setMessage(text)
                .setPositiveButton(
                        yesBtnText,
                        (dialogInterface, i) -> getTargetFragment().onActivityResult(
                                getTargetRequestCode(),
                                Activity.RESULT_OK,
                                null
                        )
                ).create();
    }
}
