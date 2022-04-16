package osp.leobert.android.plugin.pandora.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import org.jetbrains.annotations.NotNull;
import osp.leobert.android.plugin.pandora.CreateKtCodesDialog;
import osp.leobert.android.plugin.pandora.kt.*;
import osp.leobert.android.plugin.pandora.ui.Notify;
import osp.leobert.android.plugin.pandora.util.Properties;
import osp.leobert.android.plugin.pandora.util.Utils;

import java.util.Collections;

import static osp.leobert.android.plugin.pandora.util.Utils.*;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> CreateVOFilesAction </p>
 * Created by leobert on 2018/10/29.
 */
public class CreateKotlinVHVOFilesAction extends AnAction implements DumbAware, CreateKtCodesDialog.OnOKListener {


    private VirtualFile getModuleDir(PsiDirectory directory) {
        return Utils.getModuleDir(directory);
    }


    private Properties parseConfig(VirtualFile moduleDir) {
        return Utils.parseConfig(moduleDir);
    }


    private void initConfig(final PsiDirectory dir) {
        try {
            VirtualFile projectFolder = getModuleDir(dir);
            if (projectFolder != null) {
                Notify.show("加载配置：" + projectFolder.getCanonicalPath());
                Properties configProp = parseConfig(projectFolder);
                if (configProp != null) {
                    rPackage = configProp.getProperty(CONF_R_PACKAGE,
                            "missing R_PACKAGE");

//                    baseVhPackage = configProp.getProperty(CONF_BASE_VH_PACKAGE,
//                            "missing BASE_VH_PACKAGE");
//
//                    baseVhName = configProp.getProperty(CONF_BASE_VH_NAME,
//                            "missing BASE_VH_NAME");

                    baseKtVhPackage = configProp.getProperty(CONF_BASE_KT_VH_PACKAGE,
                            "missing BASE_KT_VH_PACKAGE");

                    baseKtVhName = configProp.getProperty(CONF_BASE_KT_VH_NAME,
                            "missing BASE_KT_VH_NAME");

                    String log = "R_PACKAGE:" + rPackage + "\n" +
//                            "BASE_VH_PACKAGE:" + baseVhPackage + "\n" +
//                            "BASE_VH_NAME:" + baseVhName + "\n" +
                            "BASE_KT_VH_PACKAGE:" + baseKtVhPackage + "\n" +
                            "BASE_KT_VH_NAME:" + baseKtVhName + "\n";
                    Notify.show("加载配置摘要：\n" + log);


                    templateVhImport = configProp.getProperty(TEMPLATE_VH_IMPORT, null);
                    templateVhCreator = configProp.getProperty(TEMPLATE_VH_CREATOR, null);
                    templateReactiveVhCreator = configProp.getProperty(TEMPLATE_REACTIVE_VH_CREATOR, null);

                    templateKt = configProp.getProperty(KT_TEMPLATE, null);
                    templateKtReactive = configProp.getProperty(KT_TEMPLATE_REACTIVE, null);

                }
            } else {
                Notify.show("加载配置失败：无文件");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Notify.show("加载配置失败：" + e.getLocalizedMessage());
        }
    }


    private String rPackage;
//    private String baseVhPackage;
//    private String baseVhName;
    private String baseKtVhPackage;
    private String baseKtVhName;

    private String templateVhImport;
    private String templateVhCreator;
    private String templateReactiveVhCreator;

    private String templateKt;
    private String templateKtReactive;


    public CreateKotlinVHVOFilesAction() {
        super("Pandora-Kotlin");
    }

    private PsiDirectory directory;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Project project = event.getProject();
        if (project == null) return;
        DataContext dataContext = event.getDataContext();
        final Module module = DataKeys.MODULE.getData(dataContext);
        if (module == null) return;

        final Navigatable navigatable = DataKeys.NAVIGATABLE.getData(dataContext);

        if (navigatable != null) {
            if (navigatable instanceof PsiDirectory) {
                directory = (PsiDirectory) navigatable;
            }
        }

        if (directory == null) {
            ModuleRootManager root = ModuleRootManager.getInstance(module);
            for (VirtualFile file : root.getSourceRoots()) {
                directory = PsiManager.getInstance(project).findDirectory(file);
            }
        }

        CreateKtCodesDialog dialog = new CreateKtCodesDialog();
        dialog.setOnOKListener(this);
        dialog.setOnRefreshListener(e -> initConfig(directory));
        dialog.setTitle("Generate Pandora VH and VO Codes");
        dialog.pack();
        dialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(event.getProject()));
        dialog.setVisible(true);
        dialog.requestFocusInWindow();
    }


    @Override
    public boolean isDumbAware() {
        return true;
    }

    @Override
    public void onOK(String text, boolean onlyVh, int type, boolean reactive) {


        Project project = directory.getProject();
        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiDirectoryFactory directoryFactory = PsiDirectoryFactory.getInstance(directory.getProject());
        String packageName = directoryFactory.getQualifiedName(directory, true);

        FileSaver fileSaver = new IDEFileSaver(factory, directory, KotlinFileType.INSTANCE);
        initConfig(directory);

        fileSaver.setListener(fileName -> true);

        SourceFilesGenerator generator;

        //String rPackage, String baseVhPackage, String baseVhName
        Model model = new Model(text, onlyVh, reactive,
                rPackage, ""/*不再使用*/, ""/*不再使用*/, baseKtVhPackage, baseKtVhName);
        Notify.show("准备生成："+ model);

        model.templateVhImport = this.templateVhImport;
        model.templateVhCreator = this.templateVhCreator;
        model.templateReactiveVhCreator = this.templateReactiveVhCreator;

        model.isPandoraKt = type == 2;

        model.templateKt = templateKt;
        model.templateKtReactive = templateKtReactive;


        generator = new PandoraWidgetGenerator(text + "Widget.kt", fileSaver);

        generator.setListener(filesCount -> {
                    Notify.show("generate success");
                }
        );

        generator.generateFiles(packageName, Collections.singletonList(model));

    }

}
