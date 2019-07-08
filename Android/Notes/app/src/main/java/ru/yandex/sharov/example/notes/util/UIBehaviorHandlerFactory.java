package ru.yandex.sharov.example.notes.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import ru.yandex.sharov.example.notes.viewmodel.NoteListDataProvider;

public class UIBehaviorHandlerFactory {
    @NonNull
    public static BottomSheetBehavior.BottomSheetCallback createBottomSheetCallback(@NonNull View fab, @NonNull View closeOpenBtn) {
        return new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                final int DURATION = 300;
                final float SCALE_HIDE = 0;
                final float SCALE_SHOW = 1;
                if (BottomSheetBehavior.STATE_DRAGGING == newState || BottomSheetBehavior.STATE_EXPANDED == newState) {
                    fab.animate().scaleX(SCALE_HIDE).scaleY(SCALE_HIDE).setDuration(DURATION).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    fab.animate().scaleX(SCALE_SHOW).scaleY(SCALE_SHOW).setDuration(DURATION).start();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                final int DURATION = 0;
                final float ROTATE_ANGLE = 180;
                closeOpenBtn.animate().rotation(ROTATE_ANGLE * v).setDuration(DURATION).start();
            }
        };
    }

    @NonNull
    public static View.OnClickListener createCloseOpenBtnOnClickListener(@NonNull BottomSheetBehavior bottomSheetBehavior) {
        return new View.OnClickListener() {
            boolean toUp = true;

            @Override
            public void onClick(View view) {
                if (toUp) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                toUp = !toUp;
            }
        };

    }

    @NonNull
    public static TextWatcher createTextChangedListener(@NonNull NoteListDataProvider noteListDataProvider) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                noteListDataProvider.setFilterData(charSequence.toString());
                noteListDataProvider.refreshData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @NonNull
    public static CompoundButton.OnCheckedChangeListener createOnCheckedChangeListener(@NonNull NoteListDataProvider noteListDataProvider) {
        return (compoundButton, isChecked) -> {
            final float ROTATE_ANGLE = 180;
            compoundButton.animate().rotationXBy(ROTATE_ANGLE).start();
            noteListDataProvider.resortData(isChecked);
            noteListDataProvider.refreshData();
        };
    }
}
