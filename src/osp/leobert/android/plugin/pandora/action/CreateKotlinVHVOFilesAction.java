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
import osp.leobert.android.plugin.pandora.util.Properties;
import osp.leobert.android.plugin.pandora.util.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import static osp.leobert.android.plugin.pandora.util.Utils.*;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> CreateVOFilesAction </p>
 * Created by leobert on 2018/10/29.
 */
public class CreateKotlinVHVOFilesAction extends AnAction implements DumbAware, CreateKtCodesDialog.OnOKListener {

//    public static void refreshConfig(@NotNull AnActionEvent event) {
//        Project project = event.getProject();
//        if (project == null) return;
//
//        DataContext dataContext = event.getDataContext();
//        final Module module = DataKeys.MODULE.getData(dataContext);
//        if (module == null) return;
//
//        ModuleRootManager root = ModuleRootManager.getInstance(module);
//        PsiDirectory directory = null;
//
//        for (VirtualFile file : root.getSourceRoots()) {
//            directory = PsiManager.getInstance(project).findDirectory(file);
//        }
//        if (directory != null)
//            parseConfig(getModuleDir(directory));
//    }

    private /*static*/ VirtualFile getModuleDir(PsiDirectory directory) {
        return Utils.getModuleDir(directory);
    }


    private /*static*/ Properties parseConfig(VirtualFile moduleDir) {
        return Utils.parseConfig(moduleDir);
    }


    private /*static*/ void initConfig(final PsiDirectory dir) {
        try {
            VirtualFile projectFolder = getModuleDir(dir);
            if (projectFolder != null) {
                Properties configProp = parseConfig(projectFolder);
                if (configProp != null) {
                    rPackage = configProp.getProperty(CONF_R_PACKAGE,
                            "com.jdd.motorfans");

                    baseVhPackage = configProp.getProperty(CONF_BASE_VH_PACKAGE,
                            "com.jdd.motorfans.common.base.adapter.vh2");

                    baseVhName = configProp.getProperty(CONF_BASE_VH_NAME,
                            "AbsViewHolder2");

                    baseKtVhPackage = configProp.getProperty(CONF_BASE_KT_VH_PACKAGE,
                            "com.jdd.motorfans.common.base.adapter.vh2");

                    baseKtVhName = configProp.getProperty(CONF_BASE_KT_VH_NAME,
                            "AbsViewHolder2");

                    templateVhImport = configProp.getProperty(TEMPLATE_VH_IMPORT, null);
                    templateVhCreator = configProp.getProperty(TEMPLATE_VH_CREATOR, null);
                    templateReactiveVhCreator = configProp.getProperty(TEMPLATE_REACTIVE_VH_CREATOR, null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private /*static*/ String rPackage;
    private /*static*/ String baseVhPackage;
    private /*static*/ String baseVhName;
    private /*static*/ String baseKtVhPackage;
    private /*static*/ String baseKtVhName;

    private String templateVhImport;
    private String templateVhCreator;
    private String templateReactiveVhCreator;


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
                rPackage, baseVhPackage, baseVhName, baseKtVhPackage, baseKtVhName);
        model.templateVhImport = this.templateVhImport;
        model.templateVhCreator = this.templateVhCreator;
        model.templateReactiveVhCreator = this.templateReactiveVhCreator;


        generator = new SingleFileGenerator(text + "Widget.kt", fileSaver);

        generator.setListener(filesCount -> {
//                NotificationsHelper.showNotification(directory.getProject(),
//                        textResources.getGeneratedFilesMessage(filesCount))
                }
        );

        generator.generateFiles(packageName, Collections.singletonList(model));

    }

}
