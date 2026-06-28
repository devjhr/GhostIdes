package ir.hanzodev1375.ghostide.translator.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import ir.hanzodev1375.ghostide.R;
import ir.hanzodev1375.ghostide.databinding.BottomSheetTranslatorBinding;
import ir.hanzodev1375.ghostide.translator.model.AndroidLanguage;
import ir.hanzodev1375.ghostide.translator.viewmodel.TranslatorViewModel;

public class StringsTranslatorSheet extends BottomSheetDialogFragment {
    public static final String TAG = "StringsTranslatorSheet";
    private static final String ARG_ROOT = "root_path";
    private BottomSheetTranslatorBinding binding;
    private TranslatorViewModel viewModel;
    private final List<AndroidLanguage> selectedLanguages = new ArrayList<>();
    private File foundStringsXml = null;

    public static StringsTranslatorSheet newInstance(String rootPath) {
        StringsTranslatorSheet sheet = new StringsTranslatorSheet();
        Bundle args = new Bundle();
        args.putString(ARG_ROOT, rootPath);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
                    ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetTranslatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandSheet();
        viewModel = new ViewModelProvider(this).get(TranslatorViewModel.class);
        buildLanguageChips();
        setupButtons();
        observeViewModel();
        checkAndShowPath();
    }

    private void checkAndShowPath() {
        String root = getArguments() != null ? getArguments().getString(ARG_ROOT) : null;
        if (root == null) {
            binding.tvStringsPath.setText("Project dir not found");
            binding.tvStringsPath.setTextColor(Color.parseColor("#F44336"));
            return;
        }
        foundStringsXml = findStringsXml(new File(root));
        if (foundStringsXml != null) {
            binding.tvStringsPath.setText("✓ " + foundStringsXml.getAbsolutePath());
            binding.tvStringsPath.setTextColor(Color.parseColor("#4CAF50"));
            binding.btnTranslate.setEnabled(!selectedLanguages.isEmpty());
        } else {
            binding.tvStringsPath.setText("✗ strings.xml not found: " + root);
            binding.tvStringsPath.setTextColor(Color.parseColor("#F44336"));
            binding.btnTranslate.setEnabled(false);
        }
    }

    private void expandSheet() {
        View bs = requireDialog().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bs != null) {
            BottomSheetBehavior<View> b = BottomSheetBehavior.from(bs);
            b.setState(BottomSheetBehavior.STATE_EXPANDED);
            b.setSkipCollapsed(true);
        }
    }

    private void buildLanguageChips() {
        binding.chipGroupLanguages.removeAllViews();
        for (AndroidLanguage lang : AndroidLanguage.ALL) {
            Chip chip = new Chip(requireContext());
            chip.setText(lang.displayName);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            chip.setTag(lang);
            chip.setOnCheckedChangeListener((btn, checked) -> {
                AndroidLanguage l = (AndroidLanguage) btn.getTag();
                if (checked) {
                    if (!selectedLanguages.contains(l)) selectedLanguages.add(l);
                } else selectedLanguages.remove(l);
                binding.btnTranslate.setEnabled(!selectedLanguages.isEmpty() && foundStringsXml != null);
            });
            binding.chipGroupLanguages.addView(chip);
        }
        binding.btnTranslate.setEnabled(false);
    }

    private void setupButtons() {
        binding.btnSelectAll.setOnClickListener(v -> {
            selectedLanguages.clear();
            for (int i = 0; i < binding.chipGroupLanguages.getChildCount(); i++) {
                ((Chip) binding.chipGroupLanguages.getChildAt(i)).setChecked(true);
            }
        });
        binding.btnDeselectAll.setOnClickListener(v -> {
            selectedLanguages.clear();
            for (int i = 0; i < binding.chipGroupLanguages.getChildCount(); i++) {
                ((Chip) binding.chipGroupLanguages.getChildAt(i)).setChecked(false);
            }
            binding.btnTranslate.setEnabled(false);
        });
        binding.btnTranslate.setOnClickListener(v -> startTranslation());
        binding.btnCancel.setOnClickListener(v -> {
            viewModel.cancel();
            setUiRunning(false);
        });
    }

    private void startTranslation() {
        if (selectedLanguages.isEmpty() || foundStringsXml == null) return;
        String resDir = foundStringsXml.getParentFile().getParentFile().getAbsolutePath();
        setUiRunning(true);
        binding.tvLog.setText("");
        binding.progressBar.setProgress(0);
        viewModel.startTranslation(foundStringsXml, resDir, new ArrayList<>(selectedLanguages));
    }

    private File findStringsXml(File dir) {
        if (dir == null || !dir.exists()) return null;
        if (dir.isFile()) {
            if (dir.getName().equals("strings.xml") && dir.getParentFile() != null
                    && dir.getParentFile().getName().equals("values")) {
                return dir;
            }
            return null;
        }
        String name = dir.getName();
        if (name.equals("build") || name.equals(".git") || name.startsWith(".")) return null;
        File[] children = dir.listFiles();
        if (children == null) return null;
        for (File child : children) {
            File found = findStringsXml(child);
            if (found != null) return found;
        }
        return null;
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), s -> {
            switch (s) {
                case RUNNING:
                    setUiRunning(true);
                    break;
                case DONE:
                    setUiRunning(false);
                    binding.progressBar.setProgress(100);
                    showSnackbar(getString(R.string.translator_done));
                    break;
                case CANCELLED:
                    setUiRunning(false);
                    showSnackbar(getString(R.string.translator_cancelled));
                    break;
                case ERROR:
                    setUiRunning(false);
                    break;
                default:
                    break;
            }
        });
        viewModel.getProgress().observe(getViewLifecycleOwner(), p -> {
            if (p == null) return;
            binding.progressBar.setProgress(p.getPercent());
            binding.tvProgressLabel.setText(p.getPercent() + "% — " + p.languageFolder + " / " + p.currentKey);
        });
        viewModel.getLastCompletedFolder().observe(getViewLifecycleOwner(), folder -> {
            if (folder == null) return;
            binding.tvLog.setText(binding.tvLog.getText() + "✓ " + folder + "\n");
            binding.scrollLog.post(() -> binding.scrollLog.fullScroll(View.FOCUS_DOWN));
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) showSnackbar("Error: " + msg);
        });
    }

    private void setUiRunning(boolean running) {
        binding.btnTranslate.setVisibility(running ? View.GONE : View.VISIBLE);
        binding.btnCancel.setVisibility(running ? View.VISIBLE : View.GONE);
        binding.chipGroupLanguages.setEnabled(!running);
        binding.btnSelectAll.setEnabled(!running);
        binding.btnDeselectAll.setEnabled(!running);
        binding.layoutProgress.setVisibility(running ? View.VISIBLE : View.GONE);
    }

    private void showSnackbar(String msg) {
        if (getView() != null) Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
