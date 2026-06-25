package ir.hanzodev1375.components.store.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import ir.hanzodev1375.components.store.fragments.WebFragments;

public class ViewPagerAdapter extends FragmentStateAdapter {

  public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    return new WebFragments();
  }

  @Override
  public int getItemCount() {
    return 1;
  }
}
