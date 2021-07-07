package osp.leobert.android.plugin.pandora.kt;


import osp.leobert.android.plugin.pandora.template.IWidgetTemplate;
import osp.leobert.android.plugin.pandora.template.PandoraKtTemplate;
import osp.leobert.android.plugin.pandora.template.PandoraTemplate;

import java.util.List;

public class PandoraWidgetGenerator extends KotlinFileGenerator {

    private final String fileName;

    public PandoraWidgetGenerator(String fileName, FileSaver fileSaver) {
        super(fileSaver);
        this.fileName = fileName;
    }

    @Override
    public void generateFiles(String packageName, List<Model> models) {

        Model model = models.get(0);

        IWidgetTemplate template;
        if (model.isPandoraKt)
            template = new PandoraKtTemplate();
        else
            template = new PandoraTemplate();


        final String tmp = template.generateFiles(packageName, models);

        fileSaver.saveFile(fileName, tmp);

        if (listener != null) {
            listener.onFilesGenerated(1);
        }
    }
}
