package ir.hanzodev1375.ghostide.mvvm.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import ir.hanzodev1375.ghostide.models.TabModel;

public class EditorActivityViewModel extends AndroidViewModel {
  private MutableLiveData<List<TabModel>> tabs = new MutableLiveData<>(new ArrayList<>());
  private MutableLiveData<Integer> currentPos = new MutableLiveData<>(0);
  private SharedPreferences prefs;
  private Gson gson = new Gson();
  private static final String KEY_TABS = "editor_tabs";

  public EditorActivityViewModel(Application app) {
    super(app);
    prefs = app.getSharedPreferences("editor", Context.MODE_PRIVATE);
    loadTabs();
  }

  public LiveData<List<TabModel>> getTabs() {
    return tabs;
  }

  public LiveData<Integer> getCurrentPos() {
    return currentPos;
  }

  public void openFile(String path, String name) {
    List<TabModel> list = tabs.getValue();
    if (list == null) list = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).getFilePath().equals(path)) {
        currentPos.setValue(i);
        return;
      }
    }
    list.add(new TabModel(path, name));
    tabs.setValue(list);
    currentPos.setValue(list.size() - 1);
    saveTabs(list);
  }

  public void closeTab(int position) {
    List<TabModel> list = tabs.getValue();
    if (list == null || position < 0 || position >= list.size()) return;
    // اگر پین شده، نبند
    if (list.get(position).isPinned()) return;

    list.remove(position);
    tabs.setValue(list);
    if (list.isEmpty()) currentPos.setValue(-1);
    else if (position >= list.size()) currentPos.setValue(list.size() - 1);
    else currentPos.setValue(position);
    saveTabs(list);
  }

  public void closeAllTabs() {
    List<TabModel> list = tabs.getValue();
    if (list == null) return;
    // فقط تب‌هایی که پین نشده‌اند حذف کن
    List<TabModel> newList = new ArrayList<>();
    for (TabModel tab : list) {
      if (tab.isPinned()) newList.add(tab);
    }
    tabs.setValue(newList);
    if (newList.isEmpty()) currentPos.setValue(-1);
    else currentPos.setValue(0);
    saveTabs(newList);
  }

  public void closeOtherTabs(int position) {
    List<TabModel> list = tabs.getValue();
    if (list == null || position < 0 || position >= list.size()) return;

    TabModel current = list.get(position);
    List<TabModel> newList = new ArrayList<>();
    newList.add(current);
    // اضافه کردن سایر تب‌های پین شده به جز خود current
    for (int i = 0; i < list.size(); i++) {
      if (i != position && list.get(i).isPinned()) {
        newList.add(list.get(i));
      }
    }
    tabs.setValue(newList);
    // موقعیت تب فعلی
    int newPos = newList.indexOf(current);
    currentPos.setValue(newPos);
    saveTabs(newList);
  }

  public void togglePin(int pos) {
    List<TabModel> list = tabs.getValue();
    if (list != null && pos >= 0 && pos < list.size()) {
      list.get(pos).setPinned(!list.get(pos).isPinned());
      tabs.setValue(list);
      saveTabs(list);
    }
  }

  private void saveTabs(List<TabModel> list) {
    String json = gson.toJson(list);
    prefs.edit().putString(KEY_TABS, json).apply();
  }

  private void loadTabs() {
    String json = prefs.getString(KEY_TABS, "");
    if (json.isEmpty()) return;
    try {
      Type type = new TypeToken<List<TabModel>>() {}.getType();
      List<TabModel> saved = gson.fromJson(json, type);
      if (saved == null) return;
      List<TabModel> valid = new ArrayList<>();
      for (TabModel t : saved) {
        if (new File(t.getFilePath()).exists()) valid.add(t);
      }
      tabs.setValue(valid);
      if (!valid.isEmpty()) currentPos.setValue(0);
    } catch (Exception ignored) {
    }
  }

  // در EditorViewModel اضافه کن:
  public void setCurrentTabPosition(int position) {
    currentPos.setValue(position);
  }
}
