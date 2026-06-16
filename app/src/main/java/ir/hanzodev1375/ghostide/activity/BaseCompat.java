package ir.hanzodev1375.ghostide.activity;

import android.app.ActivityOptions;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import com.google.android.material.transition.platform.MaterialSharedAxis;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ir.hanzodev1375.ghostide.themeengine.Theme;
import ir.hanzodev1375.ghostide.themeengine.ThemeEngineExtensions;
import java.util.ArrayList;
import java.util.List;

import ir.hanzodev1375.ghostide.codeeditors.setting.PreferencesUtils;
import ir.hanzodev1375.ghostide.themeengine.ThemeEngine;
import ir.hanzodev1375.ghostide.utils.LocaleHelper;

public class BaseCompat extends AppCompatActivity
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  private PreferencesUtils prefs;
  private Theme lastTheme;

  private final List<BaseCompat> ACTIVITIES = new ArrayList<>();

  @Override
  protected void attachBaseContext(Context newBase) {
    prefs = new PreferencesUtils(newBase);
    super.attachBaseContext(LocaleHelper.applyLocale(newBase));
  }

  @Override
  protected void onCreate(Bundle arg0) {
    prefs = new PreferencesUtils(this);
    EdgeToEdge.enable(this);
    ThemeEngine.applyToActivity(this);
//    applyTransparentNavigationBar();
    lastTheme = ThemeEngine.getInstance(this).getStaticTheme();
    super.onCreate(arg0);
    ACTIVITIES.add(this);
    getWindow().setNavigationBarColor(Color.TRANSPARENT);
    getWindow().setStatusBarColor(Color.TRANSPARENT);
  }

  @Override
  protected void onDestroy() {
    ACTIVITIES.remove(this);
    super.onDestroy();
  }

  @Override
  protected void onResume() {
    super.onResume();

    Theme currentTheme = ThemeEngine.getInstance(this).getStaticTheme();
 //   applyTransparentNavigationBar();
    if (currentTheme != lastTheme) {
      recreate();
      return;
    }

    prefs.getDefaultPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    prefs.getDefaultPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {}

  public void recreateAllActivities() {
    List<BaseCompat> copy = new ArrayList<>(ACTIVITIES);
    for (BaseCompat activity : copy) {
      if (activity == null) {
        continue;
      }
      if (activity.isFinishing()) {
        continue;
      }
      activity.runOnUiThread(activity::recreate);
    }
  }

  @Override
  public void startActivity(Intent i) {
    ActivityOptions op = ActivityOptions.makeSceneTransitionAnimation(this);
    MaterialSharedAxis enter = new MaterialSharedAxis(MaterialSharedAxis.Z, true);
    enter.setDuration(1000);
    MaterialSharedAxis exit = new MaterialSharedAxis(MaterialSharedAxis.Z, false);
    exit.setDuration(1000);
    MaterialSharedAxis reenter = new MaterialSharedAxis(MaterialSharedAxis.Y, true);
    reenter.setDuration(1000);

    getWindow().setExitTransition(exit);
    getWindow().setEnterTransition(enter);
    getWindow().setReenterTransition(reenter);
    super.startActivity(i, op.toBundle());
  }

  private void applyTransparentNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setNavigationBarColor(Color.TRANSPARENT);
      getWindow().setStatusBarColor(Color.TRANSPARENT);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        getWindow().setNavigationBarDividerColor(Color.TRANSPARENT);
      }

      View decorView = getWindow().getDecorView();
      int flags = decorView.getSystemUiVisibility();
      flags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      boolean isLightMode = !ThemeEngineExtensions.isDarkMode(this);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
          controller.show(WindowInsets.Type.navigationBars());
          if (isLightMode) {
            // در حالت روشن: آیکون‌های نوار تیره (برای پس‌زمینه شفاف و محتوای روشن)
            controller.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
          } else {
            controller.setSystemBarsAppearance(
                0, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
          }
        }
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (isLightMode) {
          flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
          flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
      }
      decorView.setSystemUiVisibility(flags);
    }
  }
}
