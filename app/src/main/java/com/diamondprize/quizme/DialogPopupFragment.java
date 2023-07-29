package com.diamondprize.quizme;

import android.app.AlertDialog;
        import android.app.Dialog;
        import android.os.Bundle;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.DialogFragment;

public class DialogPopupFragment extends DialogFragment {

    private String title;
    private String content;

    public DialogPopupFragment(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(content)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Tambahkan aksi yang ingin dilakukan ketika tombol "OK" diklik
                    dialog.dismiss();
                });

        return builder.create();
    }
}

