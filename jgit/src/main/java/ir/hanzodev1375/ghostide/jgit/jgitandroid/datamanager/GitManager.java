package ir.hanzodev1375.ghostide.jgit.jgitandroid.datamanager;

import ir.hanzodev1375.ghostide.jgit.jgitandroid.model.FileChange;
import ir.hanzodev1375.ghostide.jgit.jgitandroid.model.CommitInfo;
import ir.hanzodev1375.ghostide.jgit.jgitandroid.model.RemoteInfo;
import ir.hanzodev1375.ghostide.jgit.jgitandroid.model.PushResult;
import ir.hanzodev1375.ghostide.jgit.jgitandroid.model.PullResult;
import ir.hanzodev1375.ghostide.jgit.jgitandroid.model.FetchResult;
import ir.hanzodev1375.ghostide.jgit.jgitandroid.ChangeType;
import java.io.ByteArrayOutputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.ignore.IgnoreNode;
import org.eclipse.jgit.attributes.AttributesNode;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class GitManager {

  private final String projectPath;
  private Git git = null;
  private Repository repository = null;
  private IgnoreNode ignoreNode = null;
  private AttributesNode attributesNode = null;

  public GitManager(String projectPath) {
    this.projectPath = projectPath;
  }

  public boolean initRepository(String initialBranchName) {
    try {
      File projectDir = new File(projectPath);
      if (!projectDir.exists()) {
        projectDir.mkdirs();
      }

      git = Git.init().setDirectory(projectDir).call();
      repository = git.getRepository();

      if (!initialBranchName.equals("master")) {
        try {
          git.branchRename().setOldName("master").setNewName(initialBranchName).call();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      loadGitIgnore();
      loadGitAttributes();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean initRepository() {
    return initRepository("main");
  }

  public boolean openRepository() {
    try {
      File gitDir = new File(projectPath, ".git");
      if (!gitDir.exists()) return false;

      repository =
          new FileRepositoryBuilder().setGitDir(gitDir).readEnvironment().findGitDir().build();

      git = new Git(repository);
      loadGitIgnore();
      loadGitAttributes();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private void loadGitIgnore() {
    try {
      File gitignoreFile = new File(projectPath, ".gitignore");
      if (gitignoreFile.exists()) {
        ignoreNode = new IgnoreNode();
        try (FileInputStream input = new FileInputStream(gitignoreFile)) {
          ignoreNode.parse(input);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadGitAttributes() {
    try {
      File gitattributesFile = new File(projectPath, ".gitattributes");
      if (gitattributesFile.exists()) {
        attributesNode = new AttributesNode();
        try (FileInputStream input = new FileInputStream(gitattributesFile)) {
          attributesNode.parse(input);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean isIgnored(String path) {
    try {
      if (repository == null) return false;
      File workTree = repository.getWorkTree();
      File file = new File(workTree, path);
      String relativePath =
          workTree.toPath().relativize(file.toPath()).toString().replace(File.separatorChar, '/');

      return ignoreNode != null
          && ignoreNode.isIgnored(relativePath, file.isDirectory())
              == IgnoreNode.MatchResult.IGNORED;
    } catch (Exception e) {
      return false;
    }
  }

  public Status getStatus() {
    try {
      return git != null ? git.status().call() : null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean setUserConfig(String name, String email) {
    try {
      if (repository == null) return false;
      org.eclipse.jgit.lib.StoredConfig config = repository.getConfig();
      config.setString("user", null, "name", name);
      config.setString("user", null, "email", email);
      config.save();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public String[] getUserConfig() {
    try {
      if (repository == null) return null;
      org.eclipse.jgit.lib.StoredConfig config = repository.getConfig();
      String name = config.getString("user", null, "name");
      String email = config.getString("user", null, "email");
      if (name == null) name = "User";
      if (email == null) email = "user@example.com";
      return new String[] {name, email};
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<FileChange> getChangedFiles() {
    List<FileChange> changes = new ArrayList<>();
    Status status = getStatus();
    if (status == null) return changes;

    for (String path : status.getAdded()) {
      if (!isIgnored(path)) {
        changes.add(new FileChange(path, ChangeType.ADDED, true));
      }
    }

    for (String path : status.getChanged()) {
      if (!isIgnored(path)) {
        changes.add(new FileChange(path, ChangeType.MODIFIED, true));
      }
    }

    for (String path : status.getRemoved()) {
      if (!isIgnored(path)) {
        changes.add(new FileChange(path, ChangeType.DELETED, true));
      }
    }

    for (String path : status.getModified()) {
      if (!status.getChanged().contains(path) && !isIgnored(path)) {
        changes.add(new FileChange(path, ChangeType.MODIFIED, false));
      }
    }

    for (String path : status.getMissing()) {
      if (!status.getRemoved().contains(path) && !isIgnored(path)) {
        changes.add(new FileChange(path, ChangeType.DELETED, false));
      }
    }

    for (String path : status.getUntracked()) {
      if (!isIgnored(path)) {
        changes.add(new FileChange(path, ChangeType.UNTRACKED, false));
      }
    }

    for (String path : status.getConflicting()) {
      if (!isIgnored(path)) {
        changes.add(new FileChange(path, ChangeType.CONFLICTING, false));
      }
    }

    return changes;
  }

  public boolean stageFile(String filePath) {
    try {
      if (isIgnored(filePath)) {
        // Force add ignored files if explicitly staged
        git.add().addFilepattern(filePath).setUpdate(false).call();
      } else {
        git.add().addFilepattern(filePath).call();
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean stageAllFiles() {
    try {
      git.add().addFilepattern(".").call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean unstageFile(String filePath) {
    try {
      git.reset().addPath(filePath).call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean commit(String message, String author, String email) {
    try {
      git.commit().setMessage(message).setAuthor(new PersonIdent(author, email)).call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean commit(String message) {
    return commit(message, "User", "user@example.com");
  }

  public List<CommitInfo> getCommitHistory(int maxCount) {
    List<CommitInfo> commits = new ArrayList<>();
    try {
      Iterable<org.eclipse.jgit.revwalk.RevCommit> logs = git.log().setMaxCount(maxCount).call();
      for (org.eclipse.jgit.revwalk.RevCommit commit : logs) {
        commits.add(
            new CommitInfo(
                commit.getName(),
                commit.getName().substring(0, 7),
                commit.getFullMessage(),
                commit.getAuthorIdent().getName(),
                commit.getAuthorIdent().getEmailAddress(),
                (long) commit.getCommitTime() * 1000));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return commits;
  }

  public List<CommitInfo> getCommitHistory() {
    return getCommitHistory(100);
  }

  public String getCurrentBranch() {
    try {
      return repository != null ? repository.getBranch() : null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<String> getAllBranches() {
    List<String> branches = new ArrayList<>();
    try {
      var refs = git.branchList().call();
      for (var ref : refs) {
        branches.add(ref.getName().replaceFirst("^refs/heads/", ""));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return branches;
  }

  public boolean createBranch(String branchName) {
    try {
      git.branchCreate().setName(branchName).call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public String getFullDiff() {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      git.diff().setOutputStream(out).call();
      String result = out.toString();
      if (result.isEmpty()) {
        return "No changes detected.";
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return "Error getting diff: " + e.getMessage();
    }
  }

  public boolean checkoutBranch(String branchName) {
    try {
      git.checkout().setName(branchName).call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteBranch(String branchName) {
    try {
      git.branchDelete().setBranchNames(branchName).call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public String getFileDiff(String filePath) {
    try {
      if (repository == null) return "";
      org.eclipse.jgit.lib.ObjectId head = repository.resolve(Constants.HEAD);

      try (RevWalk revWalk = new RevWalk(repository)) {
        org.eclipse.jgit.revwalk.RevCommit commit = revWalk.parseCommit(head);
        org.eclipse.jgit.revwalk.RevTree tree = commit.getTree();

        CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
        try (org.eclipse.jgit.lib.ObjectReader reader = repository.newObjectReader()) {
          oldTreeParser.reset(reader, tree);
        }

        List<org.eclipse.jgit.diff.DiffEntry> diffs =
            git.diff().setOldTree(oldTreeParser).setPathFilter(PathFilter.create(filePath)).call();

        StringBuilder diffBuilder = new StringBuilder();
        if (diffs != null) {
          for (org.eclipse.jgit.diff.DiffEntry diff : diffs) {
            diffBuilder
                .append(diff.getChangeType())
                .append(": ")
                .append(diff.getNewPath())
                .append("\n");
          }
        }
        return diffBuilder.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public boolean discardChanges(String filePath) {
    try {
      git.checkout().addPath(filePath).call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void reloadGitIgnoreAndAttributes() {
    loadGitIgnore();
    loadGitAttributes();
  }

  public void close() {
    if (git != null) git.close();
    if (repository != null) repository.close();
  }

  public boolean addRemote(String name, String url) {
    try {
      git.remoteAdd().setName(name).setUri(new URIish(url)).call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean removeRemote(String name) {
    try {
      git.remoteRemove().setRemoteName(name).call();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<RemoteInfo> getRemotes() {
    List<RemoteInfo> remotes = new ArrayList<>();
    try {
      List<org.eclipse.jgit.transport.RemoteConfig> remoteConfigs = git.remoteList().call();
      for (org.eclipse.jgit.transport.RemoteConfig remote : remoteConfigs) {
        String url = remote.getURIs().isEmpty() ? "" : remote.getURIs().get(0).toString();
        remotes.add(new RemoteInfo(remote.getName(), url, url));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return remotes;
  }

  public PushResult push(String remoteName, String branchName, String username, String password) {
    try {
      org.eclipse.jgit.api.PushCommand pushCommand = git.push();
      pushCommand.setRemote(remoteName);

      if (branchName != null) {
        pushCommand.setRefSpecs(
            new RefSpec("refs/heads/" + branchName + ":refs/heads/" + branchName));
      }

      if (username != null && password != null) {
        pushCommand.setCredentialsProvider(
            new UsernamePasswordCredentialsProvider(username, password));
      }

      Iterable<org.eclipse.jgit.transport.PushResult> results = pushCommand.call();
      boolean success = true;
      for (org.eclipse.jgit.transport.PushResult result : results) {
        for (RemoteRefUpdate update : result.getRemoteUpdates()) {
          if (update.getStatus() != RemoteRefUpdate.Status.OK
              && update.getStatus() != RemoteRefUpdate.Status.UP_TO_DATE) {
            success = false;
            break;
          }
        }
      }

      return new PushResult(success, success ? "Push successful" : "Push failed");
    } catch (Exception e) {
      e.printStackTrace();
      return new PushResult(false, e.getMessage() != null ? e.getMessage() : "Push failed");
    }
  }

  public PushResult push() {
    return push("origin", null, null, null);
  }

  public PullResult pull(String remoteName, String branchName, String username, String password) {
    try {
      org.eclipse.jgit.api.PullCommand pullCommand = git.pull();
      pullCommand.setRemote(remoteName);

      if (branchName != null) {
        pullCommand.setRemoteBranchName(branchName);
      }

      if (username != null && password != null) {
        pullCommand.setCredentialsProvider(
            new UsernamePasswordCredentialsProvider(username, password));
      }

      org.eclipse.jgit.api.PullResult result = pullCommand.call();
      boolean success = result.isSuccessful();

      return new PullResult(success, success ? "Pull successful" : "Pull failed");
    } catch (Exception e) {
      e.printStackTrace();
      return new PullResult(false, e.getMessage() != null ? e.getMessage() : "Pull failed");
    }
  }

  public PullResult pull() {
    return pull("origin", null, null, null);
  }

  public FetchResult fetch(String remoteName, String username, String password) {
    try {
      org.eclipse.jgit.api.FetchCommand fetchCommand = git.fetch();
      fetchCommand.setRemote(remoteName);

      if (username != null && password != null) {
        fetchCommand.setCredentialsProvider(
            new UsernamePasswordCredentialsProvider(username, password));
      }

      fetchCommand.call();
      return new FetchResult(true, "Fetch successful");
    } catch (Exception e) {
      e.printStackTrace();
      return new FetchResult(false, e.getMessage() != null ? e.getMessage() : "Fetch failed");
    }
  }

  public FetchResult fetch() {
    return fetch("origin", null, null);
  }

  public boolean clone(String remoteUrl, String localPath, String username, String password) {
    try {
      var cloneCommand = Git.cloneRepository();
      cloneCommand.setURI(remoteUrl);
      cloneCommand.setDirectory(new File(localPath));

      if (username != null && password != null) {
        cloneCommand.setCredentialsProvider(
            new UsernamePasswordCredentialsProvider(username, password));
      }

      git = cloneCommand.call();
      repository = git.getRepository();
      loadGitIgnore();
      loadGitAttributes();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean hasRemote() {
    try {
      return git != null && !git.remoteList().call().isEmpty();
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isRepositoryInitialized() {
    try {
      File gitDir = new File(projectPath, ".git");
      return gitDir.exists() && gitDir.isDirectory();
    } catch (Exception e) {
      return false;
    }
  }
}
