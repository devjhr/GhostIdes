package ir.hanzodev1375.ghostide.customui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import ir.hanzodev1375.ghostide.R;
import ir.hanzodev1375.ghostide.codeeditors.setting.PreferencesUtils;
import ir.hanzodev1375.ghostide.utils.ShapeUtil;

public class FloatingToolbarView extends FrameLayout {

  private FloatingActionButton fab;
  private RecyclerView recyclerView;
  private FrameLayout cardView;
  private LinearLayout root;
  private Orientation orientation = Orientation.Left;
  private boolean expanded = false;
  private PreferencesUtils setting;

  public enum Orientation {
    Left,
    Right
  }

  public FloatingToolbarView(Context context) {
    super(context);
    init(context);
  }

  public FloatingToolbarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public FloatingToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    setting = new PreferencesUtils(getContext());
    root = new LinearLayout(context);
    root.setOrientation(LinearLayout.HORIZONTAL);
    root.setGravity(Gravity.CENTER_VERTICAL);

    LayoutParams rootParams =
        new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    addView(root, rootParams);

    fab = new FloatingActionButton(context);
    fab.setImageResource(R.drawable.add);
    fab.setCompatElevation(dp(8));
    fab.setOutlineProvider(null);
    fab.setUseCompatPadding(true);

    cardView = new FrameLayout(context);
    updateShapeForOrientation();
    cardView.setScaleX(0f);
    cardView.setVisibility(GONE);

    recyclerView = new RecyclerView(context);
    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
    cardView.addView(
        recyclerView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

    fab.setOnClickListener(v -> toggleToolbar());
    fab.setCustomSize(dp(70));
    int defcolor = MaterialColors.getColor(fab,R.attr.colorPrimaryContainer);
    int defIcon = MaterialColors.getColor(fab,R.attr.colorOnPrimaryContainer);
    fab.setBackgroundTintList(
        ColorStateList.valueOf(
            setting.isShowBackground() ? ColorUtils.setAlphaComponent(defcolor, 128) : defcolor));
    fab.setImageTintList(
        ColorStateList.valueOf(
            setting.isShowBackground() ? ColorUtils.setAlphaComponent(defIcon, 128) : defIcon));
    applyOrientation();
    
  }

  private void updateShapeForOrientation() {
    float roundedCorner = dp(20);
    float flatCorner = dp(20);

    ShapeAppearanceModel model;
    if (orientation == Orientation.Right) {
      model =
          ShapeAppearanceModel.builder()
              .setTopLeftCornerSize(roundedCorner)
              .setTopRightCornerSize(flatCorner)
              .setBottomLeftCornerSize(roundedCorner)
              .setBottomRightCornerSize(flatCorner)
              .build();
    } else {
      model =
          ShapeAppearanceModel.builder()
              .setTopLeftCornerSize(flatCorner)
              .setTopRightCornerSize(roundedCorner)
              .setBottomLeftCornerSize(flatCorner)
              .setBottomRightCornerSize(roundedCorner)
              .build();
    }

    MaterialShapeDrawable drawable = new MaterialShapeDrawable(model);

    drawable.setFillColor(
        ColorStateList.valueOf(
            setting.isShowBackground()
                ? ColorUtils.setAlphaComponent(ShapeUtil.getcolorSurfaceContainer(cardView), 128)
                : ShapeUtil.getcolorSurfaceContainer(cardView)));
    drawable.setStroke(
        3.3f,
        ColorStateList.valueOf(
            setting.isShowBackground()
                ? ColorUtils.setAlphaComponent(ShapeUtil.getcolorPrimaryContainer(cardView), 128)
                : ShapeUtil.getcolorPrimaryContainer(cardView)));
    drawable.setElevation(dp(8));
    cardView.setBackground(drawable);
  }

  private void applyOrientation() {
    root.removeAllViews();
    LinearLayout.LayoutParams fabParams =
        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams cardParams =
        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, dp(56));

    if (orientation == Orientation.Right) {

      root.addView(fab, fabParams);
      root.addView(cardView, cardParams);
      cardParams.leftMargin = dp(2);
      cardParams.rightMargin = 0;
      cardView.setPivotX(0f);
    } else {

      root.addView(cardView, cardParams);
      root.addView(fab, fabParams);
      cardParams.rightMargin = dp(2);
      cardParams.leftMargin = 0;
      cardView.setPivotX(1f);
    }
    cardView.setLayoutParams(cardParams);
  }

  public void setOrientation(Orientation orientation) {
    this.orientation = orientation;
    applyOrientation();

    if (expanded) {
      cardView.setVisibility(VISIBLE);
      cardView.setScaleX(1f);
      cardView.setAlpha(1f);
    } else {
      cardView.setVisibility(GONE);
      cardView.setScaleX(0f);
    }
  }

  private void toggleToolbar() {
    if (expanded) {

      cardView
          .animate()
          .scaleX(0f)
          .alpha(0f)
          .setDuration(220)
          .setInterpolator(new AccelerateInterpolator())
          .withEndAction(() -> cardView.setVisibility(GONE))
          .start();
      fab.setCustomSize(dp(70));
    } else {
      fab.setCustomSize(dp(58));
      cardView.setVisibility(VISIBLE);
      cardView.setScaleX(0f);
      cardView.setAlpha(0f);
      cardView
          .animate()
          .scaleX(1f)
          .alpha(1f)
          .setDuration(280)
          .setInterpolator(new DecelerateInterpolator(1.5f))
          .withEndAction(this::playWiggleAnimation)
          .start();
    }
    expanded = !expanded;
  }

  private void playWiggleAnimation() {
    int delta = dp(4);
    if (orientation == Orientation.Left) {

      cardView
          .animate()
          .translationX(-delta)
          .setDuration(40)
          .withEndAction(
              () ->
                  cardView
                      .animate()
                      .translationX(delta)
                      .setDuration(60)
                      .withEndAction(
                          () ->
                              cardView
                                  .animate()
                                  .translationX(-delta / 2)
                                  .setDuration(40)
                                  .withEndAction(
                                      () ->
                                          cardView
                                              .animate()
                                              .translationX(0)
                                              .setDuration(30)
                                              .start())
                                  .start())
                      .start())
          .start();
    } else {

      cardView
          .animate()
          .translationX(delta)
          .setDuration(40)
          .withEndAction(
              () ->
                  cardView
                      .animate()
                      .translationX(-delta)
                      .setDuration(60)
                      .withEndAction(
                          () ->
                              cardView
                                  .animate()
                                  .translationX(delta / 2)
                                  .setDuration(40)
                                  .withEndAction(
                                      () ->
                                          cardView
                                              .animate()
                                              .translationX(0)
                                              .setDuration(30)
                                              .start())
                                  .start())
                      .start())
          .start();
    }
  }

  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  public FloatingActionButton getFab() {
    return fab;
  }

  public void expand() {
    if (!expanded) {
      toggleToolbar();
    }
  }

  public void collapse() {
    if (expanded) {
      toggleToolbar();
    }
  }

  public boolean isExpanded() {
    return expanded;
  }

  public void dismiss() {
    if (isExpanded()) {
      collapse();
    }
  }

  private int dp(float value) {
    return (int)
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
  }
}
