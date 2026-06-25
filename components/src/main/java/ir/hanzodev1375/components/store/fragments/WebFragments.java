package ir.hanzodev1375.components.store.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.hanzodev1375.components.R;
import ir.hanzodev1375.components.store.adapter.WebStoreAdapter;
import ir.hanzodev1375.components.store.api.WebStoreApi;
import ir.hanzodev1375.components.store.model.WebStore;

public class WebFragments extends Fragment implements WebStoreAdapter.OnClickItemListener {

  private RecyclerView rv;
  private ProgressBar progressBar;
  private TextView errorText;
  private WebStoreAdapter adapter;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_web, container, false);
    rv = view.findViewById(R.id.recyclerView);
    progressBar = view.findViewById(R.id.progressBar);
    errorText = view.findViewById(R.id.errorText);

    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);
    GridLayoutManager manager = new GridLayoutManager(requireContext(), 1);
    rv.setLayoutManager(manager);
    rv.addItemDecoration(
        new RecyclerView.ItemDecoration() {
          @Override
          public void getItemOffsets(
              Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = 8;
            outRect.bottom = 8;
            int recyclerWidth = parent.getWidth();
            int itemWidth =
                (int) (300 * parent.getContext().getResources().getDisplayMetrics().density);
            int margin = Math.max(0, (recyclerWidth - itemWidth) / 2);
            outRect.left = margin;
            outRect.right = margin;
          }
        });

    adapter = new WebStoreAdapter(new ArrayList<>(), this);
    rv.setAdapter(adapter);
    loadData();
  }

  private void loadData() {
    progressBar.setVisibility(View.VISIBLE);
    errorText.setVisibility(View.GONE);
    WebStoreApi.fetchWebStores(
        new WebStoreApi.Callbacks() {

          @Override
          public void onSuccess(List<WebStore> stores) {
            if (!isAdded()) return;
            progressBar.setVisibility(View.GONE);
            adapter.updateData(stores);
          }

          @Override
          public void onError(String message) {
            if (!isAdded()) return;
            progressBar.setVisibility(View.GONE);
            errorText.setText(message);
            errorText.setVisibility(View.VISIBLE);
          }
        });
  }

  @Override
  public void click(View v, int pos, WebStore model) {}
}
