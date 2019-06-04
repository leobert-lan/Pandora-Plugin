package osp.leobert.android.plugin.pandora.kt;


import java.util.List;

public abstract class SourceFilesGenerator {

    protected FileSaver fileSaver;
    protected Listener listener;

    public SourceFilesGenerator(FileSaver fileSaver) {
        this.fileSaver = fileSaver;
    }

    public abstract void generateFiles(String packageName, List<Model> models);

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onFilesGenerated(int filesCount);
    }
}