package ir.hanzodev1375.ghostide.themeengine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import ir.hanzodev1375.ghostide.R;

public class ThemeEngine {

  private static final String PREFS_NAME = "theme_engine_prefs";
  private static final String THEME_MODE = "theme_mode";
  private static final String APP_THEME = "app_theme";
  private static final String TRUE_BLACK = "true_black";
  private static final String FIRST_START = "first_start";

  private final SharedPreferences prefs;
  private final Context context;

  private static ThemeEngine instance;

  private ThemeEngine(Context context) {
    this.context = context.getApplicationContext();
    prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    if (isFirstStart()) {
      setDefaultValues();
      setFirstStart(false);
    }
  }

  public static synchronized ThemeEngine getInstance(Context context) {
    if (instance == null) {
      instance = new ThemeEngine(context);
    }
    return instance;
  }

  private boolean isFirstStart() {
    return prefs.getBoolean(FIRST_START, true);
  }

  private void setFirstStart(boolean value) {
    prefs.edit().putBoolean(FIRST_START, value).apply();
  }

  public int getThemeMode() {
    return prefs.getInt(THEME_MODE, ThemeMode.AUTO);
  }

  public void setThemeMode(int themeMode) {

    if (themeMode < 0 || themeMode > 2) {
      themeMode = ThemeMode.AUTO;
    }

    prefs.edit().putInt(THEME_MODE, themeMode).apply();

    switch (themeMode) {
      case ThemeMode.LIGHT:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        break;

      case ThemeMode.DARK:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        break;

      default:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        break;
    }
  }

  private int getNightMode() {
    switch (getThemeMode()) {
      case ThemeMode.LIGHT:
        return AppCompatDelegate.MODE_NIGHT_NO;

      case ThemeMode.DARK:
        return AppCompatDelegate.MODE_NIGHT_YES;

      default:
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    }
  }

  /** مهم: همیشه تم انتخابی کاربر را برگردان. */
  public int getTheme() {
    return getStaticTheme().getThemeId();
  }

  public Theme getStaticTheme() {

    int ordinal = prefs.getInt(APP_THEME, 0);

    if (ordinal < 0 || ordinal >= Theme.values().length) {
      ordinal = 0;
    }

    return Theme.values()[ordinal];
  }

  public void setStaticTheme(Theme theme) {

    if (theme == null) {
      return;
    }

    prefs.edit().putInt(APP_THEME, theme.ordinal()).commit();
  }

  public void resetTheme() {

    prefs.edit().putInt(APP_THEME, 0).commit();
  }

  public boolean isTrueBlack() {
    return prefs.getBoolean(TRUE_BLACK, false);
  }

  public void setTrueBlack(boolean trueBlack) {
    prefs.edit().putBoolean(TRUE_BLACK, trueBlack).apply();
  }

  private void setDefaultValues() {

    setTrueBlack(ThemeEngineExtensions.getBooleanSafe(context, R.bool.true_black, false));

    setThemeMode(ThemeEngineExtensions.getIntSafe(context, R.integer.theme_mode, ThemeMode.AUTO));

    prefs.edit().putInt(APP_THEME, 0).apply();
  }

  public static void applyToActivities(Application application) {
    application.registerActivityLifecycleCallbacks(new ThemeEngineActivityCallback());
  }

  public static void applyToActivity(Activity activity) {
    ThemeEngine engine = getInstance(activity);
    activity.setTheme(engine.getTheme());
    AppCompatDelegate.setDefaultNightMode(engine.getNightMode());
    // خط زیر را کاملاً حذف کن
    // if (engine.isTrueBlack()) { ... }
  }

  private static class ThemeEngineActivityCallback
      implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityPreCreated(@NonNull Activity activity, Bundle savedInstanceState) {

      applyToActivity(activity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(@NonNull Activity activity) {}

    @Override
    public void onActivityResumed(@NonNull Activity activity) {}

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {}
  }
}
