package ir.hanzodev1375.ghostide.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import io.github.rosemoe.sora.event.ContentChangeEvent;
import ir.hanzodev1375.ghostide.codeeditors.IdeEditor;
import ir.hanzodev1375.ghostide.codeeditors.langs.antlr.AntlrLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.c.CLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.cpp.CppLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.csharp.CSharpLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.css.CssLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.dart.DartLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.go.GoLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.gradle.GradleLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.html.HtmlLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.ini.IniLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.java.JavaLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.js.JsLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.json.JsonLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.kotlin.KotlinLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.lua.LuaLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.markdown.MarkdownLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.php.PhpLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.python3.Python3Language;
import ir.hanzodev1375.ghostide.codeeditors.langs.ruby.RubyLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.rust.RustLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.sass.SassLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.shell.ShellLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.sql.SqlLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.toml.TomlLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.tsx.TsxLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.typescript.TypeScriptLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.xml.XmlLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.yaml.YamlLanguage;
import ir.hanzodev1375.ghostide.codeeditors.langs.zig.ZigLanguage;
import ir.hanzodev1375.ghostide.codeeditors.setting.PreferencesUtils;
import ir.hanzodev1375.ghostide.databinding.EditorFragmentBinding;
import ir.hanzodev1375.ghostide.mvvm.viewmodel.EditorViewModel;
import ir.theme.ThemeManager;
import ir.theme.ThemeUtils;

public class EditorFragment extends Fragment {
  private EditorFragmentBinding binding;
  private EditorViewModel viewModel;
  private IdeEditor editor;
  private String filePath;
  private ThemeUtils theme;
  private PreferencesUtils setting;

  public static EditorFragment newInstance(String path) {
    EditorFragment f = new EditorFragment();
    Bundle args = new Bundle();
    args.putString("file_path", path);
    f.setArguments(args);
    return f;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = EditorFragmentBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    filePath = getArguments().getString("file_path");
    viewModel = new ViewModelProvider(this).get(EditorViewModel.class);
    editor = binding.editor;
    var manager = new ThemeManager(requireActivity());
    theme = new ThemeUtils(manager);
    theme.applyEditor(editor);
    applyImeInsets(binding.getRoot());
    setting = new PreferencesUtils(getContext());
    editor.subscribeEvent(
          ContentChangeEvent.class,
          (event, unevent) -> {
             if (setting.autoSaveFiles()) saveCurrentFile();            
          });   
    viewModel
        .getLoading()
        .observe(
            getViewLifecycleOwner(),
            loading -> {
              binding.prograssLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
            });

    viewModel
        .getText()
        .observe(
            getViewLifecycleOwner(),
            content -> {
              var b = new Bundle();
              b.putString("path", filePath);
              if (content != null) editor.setText(content, b);
            });

    if (filePath != null) viewModel.loadFile(filePath);
    if (filePath.endsWith(".java")) {
      editor.setEditorLanguage(new JavaLanguage());
    } else if (filePath.endsWith(".c")) {
      editor.setEditorLanguage(new CLanguage());
    } else if (filePath.endsWith(".cs")) {
      editor.setEditorLanguage(new CSharpLanguage());
    } else if (filePath.endsWith(".cpp")
        || filePath.endsWith(".cxx")
        || filePath.endsWith(".hpp")
        || filePath.endsWith(".hxx")
        || filePath.endsWith(".cc")
        || filePath.endsWith(".h")) {
      editor.setEditorLanguage(new CppLanguage());
    } else if (filePath.endsWith(".html")) {
      editor.setEditorLanguage(new HtmlLanguage(getContext(), filePath));
    } else if (filePath.endsWith(".css")) {
      editor.setEditorLanguage(new CssLanguage(getContext(), filePath));
    } else if (filePath.endsWith(".js")) {
      editor.setEditorLanguage(new JsLanguage(getContext(), filePath));
    } else if (filePath.endsWith(".py")) {
      editor.setEditorLanguage(new Python3Language());
    } else if (filePath.endsWith(".json")) {
      editor.setEditorLanguage(new JsonLanguage(getContext(), filePath));
    } else if (filePath.endsWith(".xml")) {
      editor.setEditorLanguage(new XmlLanguage());
    } else if (filePath.endsWith(".kt") || filePath.endsWith(".kts")) {
      editor.setEditorLanguage(new KotlinLanguage());
    } else if (filePath.endsWith(".ts")) {
      editor.setEditorLanguage(new TypeScriptLanguage());
    } else if (filePath.endsWith(".toml")) {
      editor.setEditorLanguage(new TomlLanguage());
    } else if (filePath.endsWith(".gradle") || filePath.endsWith(".groovy")) {
      editor.setEditorLanguage(new GradleLanguage());
    } else if (filePath.endsWith(".sass") || filePath.endsWith(".scss")) {
      editor.setEditorLanguage(new SassLanguage());
    } else if (filePath.endsWith(".md") || filePath.endsWith(".markdown")) {
      editor.setEditorLanguage(new MarkdownLanguage());
    } else if (filePath.endsWith(".yml") || filePath.endsWith(".yaml")) {
      editor.setEditorLanguage(new YamlLanguage());
    } else if (filePath.endsWith(".lua")) {
      editor.setEditorLanguage(new LuaLanguage());
    } else if (filePath.endsWith(".go")) {
      editor.setEditorLanguage(new GoLanguage());
    } else if (filePath.endsWith(".php")) {
      editor.setEditorLanguage(new PhpLanguage());
    } else if (filePath.endsWith(".dart")) {
      editor.setEditorLanguage(new DartLanguage());
    } else if (filePath.endsWith(".tsx") || filePath.endsWith(".jsx")) {
      editor.setEditorLanguage(new TsxLanguage());
    } else if (filePath.endsWith(".sql")) {
      editor.setEditorLanguage(new SqlLanguage());
    } else if (filePath.endsWith(".sh")
        || filePath.endsWith(".rc")
        || filePath.endsWith(".bash")
        || filePath.endsWith(".bashrc")
        || filePath.endsWith(".ash")
        || filePath.endsWith(".zsh")
        || filePath.endsWith(".zshrc")) {
      editor.setEditorLanguage(new ShellLanguage());
    } else if (filePath.endsWith(".rs")) {
      editor.setEditorLanguage(new RustLanguage());
    } else if (filePath.endsWith(".rb")) {
      editor.setEditorLanguage(new RubyLanguage());
    } else if (filePath.endsWith(".g4")) {
      editor.setEditorLanguage(new AntlrLanguage());
    } else if (filePath.endsWith(".ini")) {
      editor.setEditorLanguage(new IniLanguage());
    } else if (filePath.endsWith(".zig")) {
      editor.setEditorLanguage(new ZigLanguage());
    }
  }

  public void saveCurrentFile() {
    if (filePath != null && viewModel != null && editor != null) {
      String content = editor.getText().toString();
      if (content != null) {
        viewModel.saveFile(content);
      } else {
        Log.e("EditorFragment", "محتوای ادیتور نال است");
      }
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public IdeEditor getEditor() {
    return editor;
  }

  /**
   * Applies dynamic bottom padding or margin adjustment so that the given view stays above the soft
   * keyboard when it appears.
   *
   * @param target The view that should remain visible (e.g., bottom sheet, header, etc.)
   */
  void applyImeInsets(@NonNull final View target) {
    ViewCompat.setOnApplyWindowInsetsListener(
        target,
        (v, insets) -> {
          Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
          Insets navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
          int bottomInset = Math.max(imeInsets.bottom, navInsets.bottom);

          v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottomInset);
          return insets;
        });
  }
}
