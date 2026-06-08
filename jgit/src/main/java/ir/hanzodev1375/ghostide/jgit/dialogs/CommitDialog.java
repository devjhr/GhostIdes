package ir.hanzodev1375.ghostide.jgit.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import ir.hanzodev1375.ghostide.codeeditors.setting.PreferencesUtils;
import ir.hanzodev1375.ghostide.jgit.R;

public class CommitDialog extends DialogFragment {
  private EditText etMessage, etAuthor, etEmail;
  private OnCommitListener listener;
  private PreferencesUtils utils;

  public interface OnCommitListener {
    void onCommit(String message, String author, String email);
  }

  public void setOnCommitListener(OnCommitListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    utils = new PreferencesUtils(requireContext());
    View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_commit, null);
    etMessage = view.findViewById(R.id.etCommitMessage);
    etAuthor = view.findViewById(R.id.etAuthor);
    etEmail = view.findViewById(R.id.etEmail);

    String savedName = utils.getGitCommitName();
    String savedEmail = utils.getGitCommitEmail();

    boolean hasData = !savedName.isEmpty() && !savedEmail.isEmpty();

    if (hasData) {
      etEmail.setVisibility(View.GONE);
      etAuthor.setVisibility(View.GONE);
      etAuthor.setText(savedName);
      etEmail.setText(savedEmail);
    } else {
      etAuthor.setVisibility(View.VISIBLE);
      etEmail.setVisibility(View.VISIBLE);
      etAuthor.addTextChangedListener(
          new TextWatcher() {
            public void afterTextChanged(Editable e) {
              utils.setGitCommitName(e.toString());
            }

            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}

            public void onTextChanged(CharSequence s, int a, int b, int c) {}
          });

      etEmail.addTextChangedListener(
          new TextWatcher() {
            public void afterTextChanged(Editable e) {
              utils.setGitLocalUserEmail(e.toString());
            }

            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}

            public void onTextChanged(CharSequence s, int a, int b, int c) {}
          });
    }

    return new MaterialAlertDialogBuilder(requireActivity())
        .setTitle("Commit Changes")
        .setView(view)
        .setPositiveButton(
            "Commit",
            (dialog, which) -> {
              if (listener != null) {
                listener.onCommit(
                    etMessage.getText().toString(),
                    etAuthor.getText().toString(),
                    etEmail.getText().toString());
              }
            })
        .setNegativeButton("Cancel", null)
        .setNeutralButton(
            "clear data",
            (cc, fff) -> {
              utils.setRemovedDataCommit();
              Snackbar.make(
                      requireActivity().findViewById(android.R.id.content),
                      "Data removed!",
                      Snackbar.LENGTH_LONG)
                  .show();
            })
        .create();
  }
}
