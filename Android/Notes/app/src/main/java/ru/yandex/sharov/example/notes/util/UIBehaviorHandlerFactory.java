package ru.yandex.sharov.example.notes.util;

import android.app.Activity;
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
                final int duration = 300;
                final float scaleHide = 0;
                final float scaleShow = 1;
                if (BottomSheetBehavior.STATE_DRAGGING == newState || BottomSheetBehavior.STATE_EXPANDED == newState) {
                    fab.animate().scaleX(scaleHide).scaleY(scaleHide).setDuration(duration).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    fab.animate().scaleX(scaleShow).scaleY(scaleShow).setDuration(duration).start();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                final int duration = 0;
                final float rotateAngle = 180;
                closeOpenBtn.animate().rotation(rotateAngle * v).setDuration(duration).start();
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
            final float rotateAngle = 180;
            compoundButton.setRotationX(isChecked ? 180 : 0);
            compoundButton.animate().rotationXBy(rotateAngle).start();
            noteListDataProvider.resortData(isChecked);
            noteListDataProvider.refreshData();

        };
    }

    public static View.OnFocusChangeListener createOnFocusChangeListener(@NonNull Activity activity) {
        return (view, onFocused) -> {
            if(!onFocused) {
                UIUtil.hideKeyTool(activity);
            }
        };
    }
}
