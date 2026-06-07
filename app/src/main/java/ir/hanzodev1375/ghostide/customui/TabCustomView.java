package ir.hanzodev1375.ghostide.customui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.bluewhaleyt.materialfileicon.core.FileIconHelper;
import ir.hanzodev1375.ghostide.R;
import ir.hanzodev1375.ghostide.codeeditors.setting.Constants;
import ir.hanzodev1375.ghostide.codeeditors.setting.PreferencesUtils;
import ir.hanzodev1375.ghostide.databinding.CustomTabBinding;
import ir.hanzodev1375.ghostide.models.TabModel;
import ir.theme.ThemeManager;
import ir.theme.ThemeUtils;

public class TabCustomView extends LinearLayout
    implements SharedPreferences.OnSharedPreferenceChangeListener {

  private CustomTabBinding binding;
  private PreferencesUtils setting;
  private TabModel currentModel;
  private boolean showTabIcon = false;

  public TabCustomView(Context context) {
    super(context);
    binding = CustomTabBinding.inflate(LayoutInflater.from(context));
    removeAllViews();
    if (binding != null) {
      addView(binding.getRoot());
    }
    setting = new PreferencesUtils(getContext());
    showTabIcon = setting.getShowIconTab();
    setting.getDefaultPreferences().registerOnSharedPreferenceChangeListener(this);
    var theme = new ThemeManager(context);
    var themeutil = new ThemeUtils(theme);
    themeutil.applyImageView(binding.tabPinIcon);
    binding.tabIcon.clearColorFilter();
    themeutil.applyImageView(binding.tabIcon);
    
  }

  public void bind(TabModel tabModel) {
    this.currentModel = tabModel;
    binding.tabTitle.setText(tabModel.getFileName());
    binding.tabPinIcon.setVisibility(tabModel.isPinned() ? View.VISIBLE : View.GONE);

    FileIconHelper icon = new FileIconHelper(tabModel.getFilePath());
    icon.setDynamicFolderEnabled(false);
    icon.setEnvironmentEnabled(false);

    if (showTabIcon) {
      binding.tabIcon.setVisibility(View.VISIBLE);
      icon.bindIcon(binding.tabIcon);
    } else {
      binding.tabIcon.setVisibility(View.GONE);
    }
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(Constants.SharedPreferenceKeys.KEY_SHOWTAB_ICON)) {
      showTabIcon = setting.getShowIconTab();
      if (currentModel != null) {
        bind(currentModel);
      }
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (setting != null) {
      setting.getDefaultPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
  }
}
