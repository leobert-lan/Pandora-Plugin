package osp.leobert.android.plugin.pandora.kt;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.kt </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> IDEFileSaver </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2019-06-03.
 */

public class IDEFileSaver extends FileSaver {

    private PsiDirectory directory;
    private PsiFileFactory factory;
    private FileType fileType;

    public IDEFileSaver(PsiFileFactory factory, PsiDirectory directory, FileType fileType) {
        this.directory = directory;
        this.factory = factory;
        this.fileType = fileType;
    }

    @Override
    public void saveFile(String fileName, String fileContent) {
        PsiFile classFile = factory.createFileFromText(fileName, fileType, fileContent);
        PsiFile oldFile = directory.findFile(fileName);

        if (oldFile != null) {

            if (listener != null && listener.shouldOverwriteFile(fileName)) {
                WriteCommandAction.runWriteCommandAction(directory.getProject(), () -> {
                    oldFile.delete();
                    directory.add(classFile);
                });
            }
        } else {
            WriteCommandAction.runWriteCommandAction(directory.getProject(), () -> {
                directory.add(classFile);
            });
        }

    }
}
