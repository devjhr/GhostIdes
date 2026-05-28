package ir.hanzodev1375.ghostide.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;
import ir.hanzodev1375.ghostide.fragments.EditorFragment;
import ir.hanzodev1375.ghostide.models.TabModel;

public class EditorPagerAdapter extends FragmentStateAdapter {
  private List<TabModel> tabs;

  public EditorPagerAdapter(@NonNull FragmentActivity fa, List<TabModel> tabs) {
    super(fa);
    this.tabs = tabs;
  }

  public void setTabs(List<TabModel> tabs) {
    this.tabs = tabs;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    return EditorFragment.newInstance(tabs.get(position).getFilePath());
  }

  @Override
  public int getItemCount() {
    return tabs == null ? 0 : tabs.size();
  }

  @Override
  public long getItemId(int position) {
    return tabs.get(position).getFilePath().hashCode();
  }

  @Override
  public boolean containsItem(long itemId) {
    for (TabModel t : tabs) if (t.getFilePath().hashCode() == itemId) return true;
    return false;
  }
}
