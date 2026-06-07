package ir.hanzodev1375.ghostide.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ir.hanzodev1375.ghostide.models.SettingItem;
import ir.hanzodev1375.ghostide.customui.PreferenceSwitchGroup;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

  private List<SettingItem> items;
  private OnItemClickListener listener;

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public SettingsAdapter(List<SettingItem> items) {
    this.items = items;
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    PreferenceSwitchGroup view = new PreferenceSwitchGroup(parent.getContext());
    view.setLayoutParams(
        new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    return new ViewHolder(view);
  }

  public void updateItem(int position, SettingItem newItem) {
    items.set(position, newItem);
    notifyItemChanged(position);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    SettingItem item = items.get(position);
    holder.switchGroup.setListPosition(position, getItemCount());
    holder.switchGroup.setTitle(item.getTitle());
    if (item.getDescription() != null && !item.getDescription().isEmpty()) {
      holder.switchGroup.setDescription(item.getDescription());
    }
    if (item.getIconRes() != 0) {
      holder.switchGroup.setIcon(item.getIconRes());
    }
    holder.switchGroup.setValue(item.isChecked());
    holder.switchGroup.getSwitch().setOnCheckedChangeListener(null);
    if (item.getListener() != null) {
      holder.switchGroup.setSwitchChangedListener(
          (button, isChecked) -> {
            item.setChecked(isChecked);
            item.getListener().onCheckedChanged(isChecked);
          });
    } else {
      holder.switchGroup.getSwitch().setVisibility(android.view.View.GONE);
      holder.switchGroup.setOnClickListener(
          v -> {
            if (listener != null) listener.onItemClick(position);
          });
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public SettingItem getItemAtPosition(int position) {
    return items.get(position);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    PreferenceSwitchGroup switchGroup;

    ViewHolder(PreferenceSwitchGroup itemView) {
      super(itemView);
      switchGroup = itemView;
    }
  }
}
