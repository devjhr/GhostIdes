package ir.hanzodev1375.ghostide.customui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.hanzodev1375.ghostide.adapters.NavAdapter;
import ir.hanzodev1375.ghostide.models.NavModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NavModelPanel extends RecyclerView {

  public static final String STORAGE_EMULATED =
      File.separator + "storage" + File.separator + "emulated";
  public static final String STORAGE_EMULATED_0 = STORAGE_EMULATED + File.separator + "0";
  private final List<NavModel> breadCrumbs = new ArrayList<>();
  private NavAdapter adapter;
  private boolean visible;

  public NavModelPanel(Context context) {
    this(context, null);
  }

  public NavModelPanel(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public NavModelPanel(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    adapter = new NavAdapter();
    setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    setAdapter(adapter);
    visible = true;
  }

  public NavAdapter getAdapter() {
    return this.adapter;
  }

  public void setFile(File file) {
    if (visible && file != null) {
      breadCrumbs.clear();

      while (file != null) {
        if (file.getPath().equals(STORAGE_EMULATED)) {
          break;
        }

        var breadCrumb = NavModel.fileTonav(file);

        if (breadCrumb != null) {
          if (breadCrumb.getFilePath().equals(STORAGE_EMULATED_0)) {
            breadCrumb.setName(getDeviceStorageName());
          }
          breadCrumbs.add(breadCrumb);
          file = file.getParentFile();
        }
      }

      Collections.reverse(breadCrumbs);
      adapter.notifyDataSetChanged();
      adapter.submitList(breadCrumbs);
      scrollToPosition(adapter.getItemCount() - 1);
    }
  }

  public void setVisible(boolean enabled) {
    setVisibility(enabled ? View.VISIBLE : View.GONE);
    this.visible = enabled;
  }

  String getDeviceStorageName() {
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;

    if (model.startsWith(manufacturer)) {
      return capitalize(model);
    } else {
      return capitalize(manufacturer) + " " + model;
    }
  }

  private String capitalize(String s) {
    if (s == null || s.isEmpty()) {
      return "";
    }
    char first = s.charAt(0);
    if (Character.isUpperCase(first)) {
      return s;
    } else {
      return Character.toUpperCase(first) + s.substring(1);
    }
  }
}
