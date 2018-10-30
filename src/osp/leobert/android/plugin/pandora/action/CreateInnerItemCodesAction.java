package osp.leobert.android.plugin.pandora.action;

import com.google.common.base.CaseFormat;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import osp.leobert.android.plugin.pandora.CreateInnerCodesDialog;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> CreateInnerItemCodesAction </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/10/29.
 */
public class CreateInnerItemCodesAction extends BaseGenerateAction implements CreateInnerCodesDialog.OnOKListener {

//    private boolean and;
    private CreateInnerCodesDialog dialog;
    private PsiFile psiFile;
    private Editor editor;


    public CreateInnerItemCodesAction() {
        super(null);
    }


    public CreateInnerItemCodesAction(CodeInsightActionHandler handler) {
        super(handler);
    }


    @Override
    public void actionPerformed(AnActionEvent event) {
        psiFile = event.getData(LangDataKeys.PSI_FILE);
        editor = event.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return;
        }
        dialog = new CreateInnerCodesDialog();
        dialog.setOnOKListener(this);
        dialog.setTitle("Generate Pandora VH and VO Codes");
        dialog.pack();
        dialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(event.getProject()));
        dialog.setVisible(true);
    }


    @Override
    public void onOK(String typeName, boolean onlyBinder) {
        new CodeWriter(typeName, onlyBinder, psiFile, getTargetClass(editor, psiFile)).execute();
    }


    private class CodeWriter extends WriteCommandAction.Simple {

        private final Project project;
        private final PsiClass clazz;
        private final PsiElementFactory factory;
        private final String typeName;
        private final boolean onlyBinder;


        CodeWriter(String typeName, boolean onlyBinder, PsiFile psiFile, PsiClass clazz) {
            super(clazz.getProject(), "");
            this.typeName = typeName;
            this.onlyBinder = onlyBinder;
            this.clazz = clazz;
            this.project = clazz.getProject();
            this.factory = JavaPsiFacade.getElementFactory(project);
        }


        @Override
        protected void run() throws Throwable {
            if (!onlyBinder) {
                createViewObjectInterface();
            }
            createViewHolderClass();
            // reformat class
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
            styleManager.optimizeImports(psiFile);
            styleManager.shortenClassReferences(clazz);
            new ReformatCodeProcessor(project, clazz.getContainingFile(), null, false).runWithoutProgress();
        }


        private void createViewObjectInterface() {
            PsiClass innerVoInterface = createInnerClassFromText(INNER_VO_TEMPLATE
                    .replace("${NAME}VO", typeName + "VO2"), clazz);
            makePublicStatic(innerVoInterface);

            clazz.add(innerVoInterface);
        }


        private void createViewHolderClass() {
            PsiClass innerVhClass = createInnerClassFromText(INNER_VH_TEMPLATE
                    .replace("${NAME}VH", typeName + "VH2")
                    .replace("${NAME}VO", typeName + "VO2")
                    .replace("VHVO_PREFIXCLASS", typeName)
                    .replace("VHVO_PREFIXLOWER_NAME", CaseFormat.UPPER_CAMEL.to(LOWER_UNDERSCORE, typeName))
                    .replace("VHVO_PREFIXNAME", CaseFormat.UPPER_CAMEL.to(LOWER_CAMEL, typeName)), clazz);


            makePublicStatic(innerVhClass);

            clazz.add(innerVhClass);
        }


        private PsiClass createInnerClassFromText(String text, PsiElement element) {
            return factory.createClassFromText(text, element).getInnerClasses()[0];
        }


        private void makePublicStatic(PsiClass innerTypeClass) {
            PsiModifierList psiModifierList = innerTypeClass.getModifierList();
            assert psiModifierList != null;
            psiModifierList.setModifierProperty("static", true);
            psiModifierList.setModifierProperty("public", true);
        }

        private final String INNER_VO_TEMPLATE =
                "public interface ${NAME}VO extends DataSet.Data<DataSet.Data, AbsViewHolder2<DataSet.Data>> {\n" +
                        "\n" +
                        "}";

        private final String INNER_VH_TEMPLATE =
                "public static class ${NAME}VH extends AbsViewHolder2<${NAME}VO> {\n" +
                        "    private final ${NAME}VH.ItemInteract mItemInteract;\n" +
                        "\n" +
                        "    private ${NAME}VO mData;\n" +
                        "\n" +
                        "\n" +
                        "    public ${NAME}VH(View itemView, ${NAME}VH.ItemInteract itemInteract) {\n" +
                        "        super(itemView);\n" +
                        "        this.mItemInteract = itemInteract;\n" +
                        "        //TODO: find views and bind actions here\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void setData(${NAME}VO data) {\n" +
                        "        mData = data;\n" +
                        "        //TODO: bind data to views\n" +
                        "\n" +
                        "    }\n" +
                        "\n" +
                        "    public static final class Creator extends ViewHolderCreator {\n" +
                        "        private final ${NAME}VH.\n" +
                        "                ItemInteract itemInteract;\n" +
                        "\n" +
                        "        public Creator(${NAME}VH.ItemInteract itemInteract) {\n" +
                        "            this.itemInteract = itemInteract;\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public AbsViewHolder2<${NAME}VO> createViewHolder(ViewGroup parent) {\n" +
                        "            View view = LayoutInflater.from(parent.getContext())\n" +
                        "                    .inflate(R.layout.app_vh_VHVO_PREFIXLOWER_NAME, parent, false);\n" +
                        "            return new ${NAME}VH(view, itemInteract);\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    public interface ItemInteract {\n" +
                        "        //TODO: define your actions here\n" +
                        "    }\n" +
                        "\n" +
                        "\n" +
                        "}";
    }
}
