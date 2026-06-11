package ir.hanzodev1375.ghostide.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.switchmaterial.SwitchMaterial;
import ir.hanzodev1375.ghostide.R;
import ir.hanzodev1375.ghostide.activity.FileManagerActivity;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZipUtil {

  private static final ExecutorService executor = Executors.newFixedThreadPool(2);
  private static final Handler mainHandler = new Handler(Looper.getMainLooper());

  public interface ZipCallback {
    @MainThread
    void onStart(int totalbyte);

    @MainThread
    void onSuccess(@NonNull File zipFile);

    @MainThread
    void onError(Exception e);
  }

  public static void zipFiles(
      List<File> sources, File destination, String password, ZipCallback callback) {
    executor.execute(
        () -> {
          try {
        //    if (callback != null) mainHandler.post(callback::onStart);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            parameters.setCompressionLevel(CompressionLevel.NORMAL);
            if (password != null && !password.isEmpty()) {
              parameters.setEncryptFiles(true);
              parameters.setEncryptionMethod(EncryptionMethod.AES);
              parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            }
            try (ZipFile zipFile =
                new ZipFile(destination, password != null ? password.toCharArray() : null)) {
              for (File src : sources) {
                if (src.isDirectory()) {
                  zipFile.addFolder(src, parameters);
                } else {
                  zipFile.addFile(src, parameters);
                }
              }
            }
            if (callback != null) mainHandler.post(() -> callback.onSuccess(destination));
          } catch (Exception e) {
            if (callback != null) mainHandler.post(() -> callback.onError(e));
          }
        });
  }

  static String defaultName = "archive.zip";

  public static void showZipDialog(Context context, List<File> selectedFiles) {
    if (selectedFiles == null || selectedFiles.isEmpty()) {
      Toast.makeText(context, R.string.no_items_selected, Toast.LENGTH_SHORT).show();
      return;
    }
    View dialogView = View.inflate(context, R.layout.dialog_create_zip, null);
    TextInputLayout tilName = dialogView.findViewById(R.id.til_zip_name);
    TextInputEditText etName = dialogView.findViewById(R.id.et_zip_name);
    TextInputLayout tilPassword = dialogView.findViewById(R.id.til_zip_password);
    TextInputEditText etPassword = dialogView.findViewById(R.id.et_zip_password);
    SwitchMaterial swPassword = dialogView.findViewById(R.id.sw_zip_password);
    tilPassword.setVisibility(View.GONE);
    swPassword.setOnCheckedChangeListener(
        (button, isChecked) -> {
          tilPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
          if (!isChecked) etPassword.setText("");
        });

    if (selectedFiles.size() == 1) {
      String name = selectedFiles.get(0).getName();
      int dot = name.lastIndexOf('.');
      if (dot > 0) name = name.substring(0, dot);
      defaultName = name + ".zip";
    }
    etName.setText(defaultName);
    new MaterialAlertDialogBuilder(context)
        .setTitle(R.string.create_zip_title)
        .setView(dialogView)
        .setPositiveButton(
            R.string.create,
            (d, which) -> {
              String zipName = etName.getText().toString().trim();
              if (zipName.isEmpty()) zipName = defaultName;
              if (!zipName.endsWith(".zip")) zipName += ".zip";
              File parentDir = selectedFiles.get(0).getParentFile();
              File zipFile = new File(parentDir, zipName);
              String password = swPassword.isChecked() ? etPassword.getText().toString() : null;
              if (password != null && password.isEmpty()) password = null;
              zipFiles(
                  selectedFiles,
                  zipFile,
                  password,
                  new ZipCallback() {
                    @MainThread
                    @Override
                    public void onStart(int totalbyte) {
                      Toast.makeText(context, R.string.creating_zip, Toast.LENGTH_SHORT).show();
                    }

                    @MainThread
                    @Override
                    public void onSuccess(@NonNull File zip) {
                      Toast.makeText(context, R.string.zip_created_success, Toast.LENGTH_LONG)
                          .show();
                       if(context instanceof FileManagerActivity) {
                       //	((FileManagerActivity)context).re
                       }   
                    }

                    @MainThread
                    @Override
                    public void onError(Exception e) {
                      Toast.makeText(
                              context, R.string.zip_error + e.getMessage(), Toast.LENGTH_LONG)
                          .show();
                    }
                  });
            })
        .setNegativeButton(R.string.cancel, null)
        .show();
  }
}
