package ir.hanzodev1375.ghostide.customui;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import ir.hanzodev1375.ghostide.R;

public class ExpandableLayout extends LinearLayout {
  private TextView titleView;
  private ImageView arrowIcon;
  private RecyclerView recyclerView;
  private boolean isExpanded = false;

  public ExpandableLayout(@NonNull Context context) {
    super(context);
    init(context);
  }

  public ExpandableLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public ExpandableLayout(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    setOrientation(VERTICAL);
    LayoutInflater.from(context).inflate(R.layout.layout_expandable, this, true);
    titleView = findViewById(R.id.expandable_title);
    arrowIcon = findViewById(R.id.expandable_arrow);
    recyclerView = findViewById(R.id.expandable_recycler);
    findViewById(R.id.expandable_header).setOnClickListener(v -> toggle());
    recyclerView.setVisibility(GONE);
  }

  public void setTitle(String title) {
    titleView.setText(title);
  }

  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  public void toggle() {
    if (isExpanded) collapse();
    else expand();
  }

  public void expand() {
    if (isExpanded) return;
    isExpanded = true;

    AutoTransition transition = new AutoTransition();
    transition.setDuration(250);
    TransitionManager.beginDelayedTransition(this, transition);

    recyclerView.setVisibility(VISIBLE);
    arrowIcon.animate().rotation(90).setDuration(200).start();
  }

  public void collapse() {
    if (!isExpanded) return;
    isExpanded = false;

    AutoTransition transition = new AutoTransition();
    transition.setDuration(250);
    TransitionManager.beginDelayedTransition(this, transition);

    recyclerView.setVisibility(GONE);
    arrowIcon.animate().rotation(0).setDuration(200).start();
  }

  public boolean isExpanded() {
    return isExpanded;
  }
}
