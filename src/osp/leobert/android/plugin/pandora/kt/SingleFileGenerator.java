package osp.leobert.android.plugin.pandora.kt;


import com.google.common.base.CaseFormat;

import java.util.List;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_BASE_VH_NAME;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_BASE_VH_PACKAGE;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_R_PACKAGE;

public class SingleFileGenerator extends KotlinFileGenerator {

    private String fileName;

    public SingleFileGenerator(String fileName, FileSaver fileSaver) {
        super(fileSaver);
        this.fileName = fileName;
    }

    @Override
    public void generateFiles(String packageName, List<Model> models) {

        final StringBuilder resultFileContent = new StringBuilder();

        resultFileContent.append(String.format(PACKAGE_BLOCK, packageName));

        Model model = models.get(0);

        resultFileContent.append(vh_import);
        if (!model.onlyVh) {
            resultFileContent.append(vo_import);
            resultFileContent.append(vo_interface);
        }

        resultFileContent.append("\n\n\n");
        resultFileContent.append(vh_creator_template).append("\n\n\n");

        resultFileContent.append(vh_item_interact).append("\n\n\n");


        String tmp = resultFileContent.toString();
        if (model.prefix != null) {
            tmp = tmp.replace("${NAME}", model.prefix);

            tmp = tmp.replace("WIDGET_PREFIXLOWER_NAME",
                    CaseFormat.UPPER_CAMEL.to(LOWER_UNDERSCORE, model.prefix));
        }

        if (model.rPackage != null)
            tmp = tmp.replace(CONF_R_PACKAGE, model.rPackage);
        else
            tmp = tmp.concat("\n\nr package is null");

        //依旧使用最基础VO，使得旧代码可复用 {
        if (model.baseVhPackage != null)
            tmp = tmp.replace(CONF_BASE_VH_PACKAGE, model.baseVhPackage);
        else
            tmp = tmp.concat("\n\n baseVhPackage is null");

        if (model.baseVhName != null)
            tmp = tmp.replace(CONF_BASE_VH_NAME, model.baseVhName);
        else
            tmp = tmp.concat("\n\n baseVhName is null");
        //}


        fileSaver.saveFile(fileName, tmp);

        if (listener != null) {
            listener.onFilesGenerated(1);
        }
    }
}
