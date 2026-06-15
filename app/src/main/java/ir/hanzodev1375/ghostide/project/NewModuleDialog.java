package ir.hanzodev1375.ghostide.project;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ir.hanzodev1375.ghostide.R;

public class NewModuleDialog {

  public interface OnModuleCreated {
    void onCreated(String modulePath);
  }

  private final Context context;
  private final String projectRootPath;
  private final OnModuleCreated callback;
  private ModuleCreator mod;

  public NewModuleDialog(Context context, String projectRootPath, OnModuleCreated callback) {
    this.context = context;
    this.projectRootPath = projectRootPath;
    this.callback = callback;
  }

  public void show() {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_module, null);

    TextInputLayout tilName = view.findViewById(R.id.tilModuleName);
    TextInputEditText etName = view.findViewById(R.id.etModuleName);
    TextInputLayout tilPkg = view.findViewById(R.id.tilModulePackage);
    TextInputEditText etPkg = view.findViewById(R.id.etModulePackage);
    TextView tvRoot = view.findViewById(R.id.tvProjectRoot);
    CheckBox box = view.findViewById(R.id.box);

    tvRoot.setText(context.getString(R.string.module_project_root, projectRootPath));

    // track if user manually edited package
    final boolean[] pkgEditedByUser = {false};

    etPkg.addTextChangedListener(
        new android.text.TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int st, int c, int a) {}

          @Override
          public void afterTextChanged(Editable s) {}

          @Override
          public void onTextChanged(CharSequence s, int st, int b, int c) {
            // only mark as manual if the user typed (count > 0 or deletion happened)
            if (etPkg.hasFocus()) pkgEditedByUser[0] = true;
          }
        });

    etName.addTextChangedListener(
        new android.text.TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int st, int c, int a) {}

          @Override
          public void onTextChanged(CharSequence s, int st, int b, int c) {}

          @Override
          public void afterTextChanged(android.text.Editable s) {
            if (pkgEditedByUser[0]) return;
            String slug =
                s.toString()
                    .trim()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9_]", "_")
                    .replaceAll("^_+|_+$", "");
            etPkg.setText("com.example." + slug);
          }
        });

    var dialog =
        new MaterialAlertDialogBuilder(context)
            .setTitle(R.string.module_new_title)
            .setView(view)
            .setPositiveButton(R.string.module_create, null)
            .setNegativeButton(R.string.cancel, null)
            .create();

    dialog.setOnShowListener(
        d -> {
          dialog
              .getButton(AlertDialog.BUTTON_POSITIVE)
              .setOnClickListener(
                  v -> {
                    String name =
                        etName.getText() != null ? etName.getText().toString().trim() : "";
                    if (name.isEmpty()) {
                      tilName.setError(context.getString(R.string.module_error_name_empty));
                      return;
                    }
                    if (!name.matches("[a-zA-Z][a-zA-Z0-9_-]*")) {
                      tilName.setError(context.getString(R.string.module_error_name_invalid));
                      return;
                    }
                    tilName.setError(null);

                    String pkg = etPkg.getText() != null ? etPkg.getText().toString().trim() : "";
                    if (pkg.isEmpty() || !isValidPackage(pkg)) {
                      tilPkg.setError(context.getString(R.string.module_error_pkg_invalid));
                      return;
                    }
                    tilPkg.setError(null);

                    dialog.dismiss();

                    var progress =
                        new MaterialAlertDialogBuilder(context)
                            .setMessage(R.string.module_creating)
                            .setCancelable(false)
                            .create();
                    progress.show();

                    mod = new ModuleCreator(context);
                    mod.create(
                        projectRootPath,
                        name,
                        pkg,
                        new ModuleCreator.OnModuleResult() {
                          @Override
                          public void onSuccess(String modulePath) {
                            progress.dismiss();
                            if (callback != null) callback.onCreated(modulePath);
                          }

                          @Override
                          public void onError(String message) {
                            progress.dismiss();
                            new MaterialAlertDialogBuilder(context)
                                .setTitle(R.string.module_error_title)
                                .setMessage(message)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                          }
                        });
                    mod.setDslType(box.isChecked() ? DslType.KOTLIN : DslType.GROOVY);
                  });
        });

    dialog.show();
  }

  private boolean isValidPackage(String pkg) {
    if (!pkg.contains(".")) return false;
    for (String seg : pkg.split("\\.", -1)) {
      if (seg.isEmpty() || !Character.isLetter(seg.charAt(0))) return false;
      if (!seg.matches("[a-zA-Z][a-zA-Z0-9_]*")) return false;
    }
    return true;
  }
}
