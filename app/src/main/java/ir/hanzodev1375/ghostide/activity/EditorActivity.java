package ir.hanzodev1375.ghostide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import ir.hanzodev1375.ghostide.R;
import ir.hanzodev1375.ghostide.adapters.EditorPagerAdapter;
import ir.hanzodev1375.ghostide.databinding.ActivityEditorBinding;
import ir.hanzodev1375.ghostide.models.TabModel;
import ir.hanzodev1375.ghostide.mvvm.viewmodel.EditorActivityViewModel;
import ir.hanzodev1375.ghostide.mvvm.viewmodel.EditorViewModel;

public class EditorActivity extends AppCompatActivity {

  private ActivityEditorBinding binding;
  private EditorActivityViewModel viewModel;
  private EditorPagerAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityEditorBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    viewModel = new ViewModelProvider(this).get(EditorActivityViewModel.class);
    setupViewPager();
    setupTabLayout();
    setupFAB();

    // باز کردن فایل از Intent
    String path = getIntent().getStringExtra("file_path");
    String name = getIntent().getStringExtra("file_name");
    if (path != null && name != null) {
      viewModel.openFile(path, name);
    }
    binding.viewPager.setUserInputEnabled(false);
    observeData();
  }

  private void setupViewPager() {
    adapter = new EditorPagerAdapter(this, viewModel.getTabs().getValue());
    binding.viewPager.setAdapter(adapter);
    binding.viewPager.registerOnPageChangeCallback(
        new ViewPager2.OnPageChangeCallback() {
          @Override
          public void onPageSelected(int position) {
            viewModel.setCurrentTabPosition(position);
          }
        });
  }

  private void setupTabLayout() {
    new TabLayoutMediator(
            binding.tab,
            binding.viewPager,
            (tab, position) -> {
              TabModel model = viewModel.getTabs().getValue().get(position);
              tab.setText(model.getFileName());
              //  if (model.isPinned()) tab.setIcon(R.drawable.ic_pin);
              // else tab.setIcon(null);
            })
        .attach();

    binding.tab.addOnTabSelectedListener(
        new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
            binding.viewPager.setCurrentItem(tab.getPosition());
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {}

          @Override
          public void onTabReselected(TabLayout.Tab tab) {
            showPopupMenu(tab.view, tab.getPosition());
          }
        });
  }

  private void showPopupMenu(View anchor, int position) {
    PopupMenu popup = new PopupMenu(this, anchor);
    popup.inflate(R.menu.tab_menu);
    popup.setOnMenuItemClickListener(
        item -> {
          int id = item.getItemId();
          if (id == R.id.close) viewModel.closeTab(position);
          else if (id == R.id.close_others) viewModel.closeOtherTabs(position);
          else if (id == R.id.close_all) viewModel.closeAllTabs();
          else if (id == R.id.pin) viewModel.togglePin(position);
          return true;
        });
    popup.show();
  }

  private void setupFAB() {
    binding.fabineditor.setOnClickListener(
        v -> {
          Intent intent = new Intent(this, FileManagerActivity.class);
          startActivityForResult(intent, 100);
        });
  }

  private void observeData() {
    viewModel
        .getTabs()
        .observe(
            this,
            tabs -> {
              adapter.setTabs(tabs);
              if (tabs.isEmpty()) finish();
              else {
                for (int i = 0; i < binding.tab.getTabCount(); i++) {
                  TabLayout.Tab tab = binding.tab.getTabAt(i);
                  if (tab != null && i < tabs.size()) {
                    //  if (tabs.get(i).isPinned()) tab.setIcon(R.drawable.ic_pin);
                    // else tab.setIcon(null);
                  }
                }
              }
            });
    viewModel
        .getCurrentPos()
        .observe(
            this,
            pos -> {
              if (pos != null && pos >= 0) binding.viewPager.setCurrentItem(pos, false);
            });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
      String path = data.getStringExtra("selected_file_path");
      String name = data.getStringExtra("selected_file_name");
      if (path != null) viewModel.openFile(path, name);
    }
  }
}