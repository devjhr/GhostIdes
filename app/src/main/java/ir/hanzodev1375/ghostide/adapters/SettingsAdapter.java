package ir.hanzodev1375.ghostide.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.color.MaterialColors;
import ir.hanzodev1375.ghostide.models.SettingItem;
import ir.hanzodev1375.ghostide.customui.PreferenceSwitchGroup;
import java.util.List;
import java.util.ArrayList;
import android.widget.Filter;
import android.widget.Filterable;
import ir.hanzodev1375.ghostide.R;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder>
    implements Filterable {

  private List<SettingItem> items;
  private List<SettingItem> itemsFull;
  private OnItemClickListener listener;
  private String currentQuery = "";

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  public SettingsAdapter(List<SettingItem> items) {
    this.items = items;
    this.itemsFull = new ArrayList<>(items);
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

    if (currentQuery != null && !currentQuery.isEmpty()) {
      SpannableString highlightedTitle = highlightText(item.getTitle(), currentQuery, holder);
      holder.switchGroup.setTitle(highlightedTitle);

      if (item.getDescription() != null && !item.getDescription().isEmpty()) {
        SpannableString highlightedDesc =
            highlightText(item.getDescription(), currentQuery, holder);
        holder.switchGroup.setDescription(highlightedDesc);
      } else if (item.getDescription() != null && !item.getDescription().isEmpty()) {
        holder.switchGroup.setDescription(item.getDescription());
      }
    } else {
      holder.switchGroup.setTitle(item.getTitle());
      if (item.getDescription() != null && !item.getDescription().isEmpty()) {
        holder.switchGroup.setDescription(item.getDescription());
      }
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

  private SpannableString highlightText(String text, String query, ViewHolder holder) {
    SpannableString spannableString = new SpannableString(text);
    String lowerText = text.toLowerCase();
    String lowerQuery = query.toLowerCase();
    int startIndex = lowerText.indexOf(lowerQuery);

    while (startIndex != -1) {
      int endIndex = startIndex + query.length();
      spannableString.setSpan(
          new ForegroundColorSpan(
              MaterialColors.getColor(holder.itemView.getContext(), R.attr.colorError, 0)),
          startIndex,
          endIndex,
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      startIndex = lowerText.indexOf(lowerQuery, endIndex);
    }
    return spannableString;
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public SettingItem getItemAtPosition(int position) {
    return items.get(position);
  }

  @Override
  public Filter getFilter() {
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        List<SettingItem> filteredList = new ArrayList<>();
        if (constraint == null || constraint.length() == 0) {
          filteredList.addAll(itemsFull);
          currentQuery = "";
        } else {
          currentQuery = constraint.toString().toLowerCase().trim();
          String filterPattern = currentQuery;
          for (SettingItem item : itemsFull) {
            if (item.getTitle().toLowerCase().contains(filterPattern)) {
              filteredList.add(item);
            } else if (item.getDescription() != null
                && item.getDescription().toLowerCase().contains(filterPattern)) {
              filteredList.add(item);
            }
          }
        }
        FilterResults results = new FilterResults();
        results.values = filteredList;
        return results;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        items.clear();
        items.addAll((List<SettingItem>) results.values);
        notifyDataSetChanged();
      }
    };
  }

  public void filter(String query) {
    getFilter().filter(query);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    PreferenceSwitchGroup switchGroup;

    ViewHolder(PreferenceSwitchGroup itemView) {
      super(itemView);
      switchGroup = itemView;
    }
  }
}
