package ir.hanzodev1375.ghostide.jgit.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import ir.hanzodev1375.ghostide.codeeditors.setting.PreferencesUtils;
import ir.hanzodev1375.ghostide.jgit.R;

public class CommitDialog extends DialogFragment implements TextWatcher {
  private EditText etMessage, etAuthor, etEmail;
  private OnCommitListener listener;
  private PreferencesUtils utils;

  public interface OnCommitListener {
    void onCommit(String message, String author, String email);
  }

  public void setOnCommitListener(OnCommitListener listener) {
    this.listener = listener;
  }

  @Override
  @MainThread
  public void onViewCreated(View arg0, Bundle arg1) {
    super.onViewCreated(arg0, arg1);
    utils = new PreferencesUtils(arg0.getContext());
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_commit, null);
    etMessage = view.findViewById(R.id.etCommitMessage);
    etAuthor = view.findViewById(R.id.etAuthor);
    etEmail = view.findViewById(R.id.etEmail);
    etAuthor.addTextChangedListener(this);
    etEmail.addTextChangedListener(this);
    if (!utils.getGitCommitName().isEmpty()) {
      etAuthor.setText(utils.getGitCommitName());
    }
    if (!utils.getGitCommitEmail().isEmpty()) {
      etAuthor.setText(utils.getGitHubName());
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

  @Override
  public void afterTextChanged(Editable e) {
    String result = e.toString();
    utils.setGitCommitName(result);
    utils.setGitLocalUserEmail(result);
  }

  @Override
  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

  @Override
  public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
}
