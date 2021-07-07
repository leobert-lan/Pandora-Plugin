package osp.leobert.android.plugin.pandora.template;

import com.google.common.base.CaseFormat;
import osp.leobert.android.plugin.pandora.kt.Model;

import java.util.List;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static osp.leobert.android.plugin.pandora.util.Utils.*;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.template </p>
 * <p><b>Project:</b> Pandora-Plugin2 </p>
 * <p><b>Classname:</b> PandoraTemplate </p>
 * Created by leobert on 2021/7/7.
 */
public class PandoraTemplate implements IWidgetTemplate {
    public static final String PACKAGE_BLOCK = "package %s\n\n";

    public static final String vo_import = "import BASE_VH_PACKAGE.BASE_VH_NAME\n" +
            "import osp.leobert.android.pandora.rv.DataSet\n";

    public static final String reactive_import = "import osp.leobert.android.pandora.rv.ReactiveData\n" +
            "import osp.leobert.android.pandora.rv.IReactiveViewHolder\n" +
            "import androidx.databinding.Observable\n" +
            "import androidx.databinding.BaseObservable\n" +
            "import osp.leobert.android.pandora.rv.IViewHolder\n";

    public static final String vo_interface =
            "interface ${NAME}VO2 : DataSet.Data<${NAME}VO2, BASE_VH_NAME<${NAME}VO2>> {\n" +
                    "    override fun setToViewHolder(viewHolder: BASE_VH_NAME<${NAME}VO2>?) {\n" +
                    "        viewHolder?.setData(this)\n" +
                    "    }\n" +
                    "}";

    public static final String reactive_vo_interface =
            "interface ${NAME}VO2 : DataSet.Data<${NAME}VO2, BASE_VH_NAME<${NAME}VO2>>," +
                    " ReactiveData<${NAME}VO2,BASE_VH_NAME<${NAME}VO2>>  {\n" +
                    "    override fun setToViewHolder(viewHolder: BASE_VH_NAME<${NAME}VO2>?) {\n" +
                    "        viewHolder?.setData(this)\n" +
                    "    }\n" +
                    "\n" +
                    "    class Impl : ${NAME}VO2 {\n" +
                    "        private var viewHolder: IReactiveViewHolder<${NAME}VO2>? = null\n" +
                    "\n" +
                    "        private val observable = BaseObservable().apply {\n" +
                    "            addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {\n" +
                    "                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {\n" +
                    "                    viewHolder?.onPropertyChanged(sender ?: this@apply, this@Impl, propertyId)\n" +
                    "                }\n" +
                    "\n" +
                    "            })\n" +
                    "        }\n" +
                    "\n" +
                    "        override fun bindReactiveVh(viewHolder: IReactiveViewHolder<${NAME}VO2>?) {\n" +
                    "            this.viewHolder = viewHolder\n" +
                    "        }\n" +
                    "\n" +
                    "        override fun unbindReactiveVh() {\n" +
                    "            viewHolder = null\n" +
                    "        }\n" +
                    "    }\n" +
                    "}\n";

    public static final String vh_import = "import android.view.LayoutInflater\n" +
            "import android.view.ViewGroup\n" +
            "import androidx.databinding.DataBindingUtil\n" +
            "import R_PACKAGE.databinding.AppVh${NAME}Binding\n" +
            "import osp.leobert.android.pandora.rv.ViewHolderCreator\n" +
            "import R_PACKAGE.R\n" +
            "import BASE_KT_VH_PACKAGE.BASE_KT_VH_NAME\n" +
            "import java.util.*\n" +
            "import android.util.Pair\n";


    public static final String vh_item_interact =
            "interface ${NAME}ItemInteract {\n" +
                    "   //TODO: define interact functions here\n" +
                    "}\n";

    public static final String vh_creator_template =
            "class ${NAME}VHCreator(private val itemInteract: ${NAME}ItemInteract?) : ViewHolderCreator() {\n" +
                    "\n" +
                    "    override fun createViewHolder(parent: ViewGroup): DataBindingViewHolder<${NAME}VO2, AppVh${NAME}Binding> {\n" +
                    "        val binding = DataBindingUtil.inflate<AppVh${NAME}Binding>(\n" +
                    "            LayoutInflater.from(parent.context),\n" +
                    "            R.layout.app_vh_WIDGET_PREFIXLOWER_NAME, parent, false\n" +
                    "        )\n" +
                    "\n" +
                    "        val vh = object : DataBindingViewHolder<${NAME}VO2, AppVh${NAME}Binding>(binding) {\n" +
                    "\n" +
                    "            var mData: ${NAME}VO2? = null\n" +
                    "\n" +
                    "            override fun setData(data: ${NAME}VO2?) {\n" +
                    "                super.setData(data)\n" +
                    "                mData = data\n" +
                    "                binding.vo = data\n" +
                    "                binding.executePendingBindings()\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        binding.vh = vh\n" +
                    "        binding.itemInteract = itemInteract\n" +
                    "\n" +
                    "        return vh\n" +
                    "    }\n" +
                    "}\n";

    public static final String reactive_vh_creator_template =
            "class ${NAME}VHCreator(private val itemInteract: ${NAME}ItemInteract?) : ViewHolderCreator() {\n" +
                    "\n" +
                    "    override fun createViewHolder(parent: ViewGroup): DataBindingViewHolder<${NAME}VO2, AppVh${NAME}Binding> {\n" +
                    "        val binding = DataBindingUtil.inflate<AppVh${NAME}Binding>(\n" +
                    "            LayoutInflater.from(parent.context),\n" +
                    "            R.layout.app_vh_WIDGET_PREFIXLOWER_NAME, parent, false\n" +
                    "        )\n" +
                    "\n" +
                    "        val vh = object : DataBindingViewHolder<${NAME}VO2, AppVh${NAME}Binding>(binding),IReactiveViewHolder<${NAME}VO2> {\n" +
                    "            var mData: ${NAME}VO2? = null\n" +
                    "\n" +
                    "            override fun setData(data: ${NAME}VO2?) {\n" +
                    "                super.setData(data)\n" +
                    "                mData = data\n" +
                    "                binding.vo = data\n" +
                    "                binding.executePendingBindings()\n" +
                    "            }\n" +
                    "\n" +
                    "            override fun getReactiveDataIfExist(): ReactiveData<out ${NAME}VO2, out IViewHolder<${NAME}VO2>>? = mData\n" +
                    "\n" +
                    "            override fun accept(visitor: IViewHolder.Visitor) { visitor.visit(this)}\n" +
                    "\n" +
                    "            override fun onPropertyChanged(sender: Observable?, data: ${NAME}VO2, propertyId: Int) {\n" +
                    "               TODO(\"not implemented\") //To change body of created functions use File | Settings | File Templates.\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        binding.vh = vh\n" +
                    "        binding.itemInteract = itemInteract\n" +
                    "\n" +
                    "        return vh\n" +
                    "    }\n" +
                    "}\n";


    private String check(String a, String b) {
        if (a == null || a.isEmpty()) {
            return b;
        }
        return a;
    }

    public String generateFiles(String packageName, List<Model> models) {

        final StringBuilder resultFileContent = new StringBuilder();

        resultFileContent.append(String.format(PACKAGE_BLOCK, packageName));

        Model model = models.get(0);

        resultFileContent.append(check(model.templateVhImport, vh_import)).append("\n");

        if (model.reactive) {
            resultFileContent.append(reactive_import).append("\n");

            if (!model.onlyVh) {
                resultFileContent.append(vo_import).append("\n");
                resultFileContent.append(reactive_vo_interface).append("\n");
            }

            resultFileContent.append(check(model.templateReactiveVhCreator, reactive_vh_creator_template)).append("\n");

        } else {

            if (!model.onlyVh) {
                resultFileContent.append(vo_import).append("\n");
                resultFileContent.append(vo_interface).append("\n\n");
            }
            resultFileContent.append(check(model.templateVhCreator, vh_creator_template)).append("\n\n");
        }


        resultFileContent.append(vh_item_interact).append("\n\n");


        String tmp = resultFileContent.toString();

        if (packageName != null)
            tmp = tmp.replace("WIDGET_PACKAGE", packageName);

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

        if (model.baseKtVhPackage != null)
            tmp = tmp.replace(CONF_BASE_KT_VH_PACKAGE, model.baseKtVhPackage);
        else
            tmp = tmp.concat("\n\n baseKtVhPackage is null");

        if (model.baseKtVhName != null)
            tmp = tmp.replace(CONF_BASE_KT_VH_NAME, model.baseKtVhName);
        else
            tmp = tmp.concat("\n\n baseKtVhName is null");

        while (tmp.contains("~~"))
            tmp = tmp.replace("~~", "  ");

        return tmp;
    }
}