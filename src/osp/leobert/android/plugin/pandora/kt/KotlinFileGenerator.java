package osp.leobert.android.plugin.pandora.kt;


public abstract class KotlinFileGenerator extends SourceFilesGenerator {

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

    public static final String vh_import = "import osp.leobert.android.tracker.LayoutInflaterWrapper\n" +
            "import android.view.ViewGroup\n" +
            "import androidx.databinding.DataBindingUtil\n" +
            "import R_PACKAGE.databinding.AppVh${NAME}Binding\n" +
            "import osp.leobert.android.pandora.rv.ViewHolderCreator\n" +
            "import R_PACKAGE.R\n" +
            "import BASE_KT_VH_PACKAGE.BASE_KT_VH_NAME\n" +
            "import osp.leobert.android.tracker.BuryPoint\n" +
            "import osp.leobert.android.tracker.BuryPointContextWrapper\n" +
            "import java.util.*\n" +
            "import android.util.Pair\n";


    public static final String vh_item_interact =
            "interface ${NAME}ItemInteract {\n" +
                    "   //TODO: define interact functions here\n" +
                    "}\n";

    public static final String vh_creator_template =
            "class ${NAME}VHCreator(private val itemInteract: ${NAME}ItemInteract?,private val parentContext: BuryPointContextWrapper) : ViewHolderCreator() {\n" +
                    "\n" +
                    "    override fun createViewHolder(parent: ViewGroup): DataBindingViewHolder<${NAME}VO2, AppVh${NAME}Binding> {\n" +
                    "        val binding = DataBindingUtil.inflate<AppVh${NAME}Binding>(\n" +
                    "            LayoutInflaterWrapper.from(parent.context),\n" +
                    "            R.layout.app_vh_WIDGET_PREFIXLOWER_NAME, parent, false\n" +
                    "        )\n" +
                    "\n" +
                    "        val vh = object : DataBindingViewHolder<${NAME}VO2, AppVh${NAME}Binding>(binding) {\n" +
                    "            val buryPointContext = object : BuryPointContextWrapper() {\n" +
                    "                override fun createContextDataInternal(pointKey: String?): MutableList<Pair<String, String>>? {\n" +
                    "                    return Collections.emptyList()\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            init {\n" +
                    "                buryPointContext.wrapBy(parentContext)\n" +
                    "                binding.bp = buryPointContext\n" +
                    "            }\n" +
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
            "class ${NAME}VHCreator(private val itemInteract: ${NAME}ItemInteract?,private val parentContext: BuryPointContextWrapper) : ViewHolderCreator() {\n" +
                    "\n" +
                    "    override fun createViewHolder(parent: ViewGroup): DataBindingViewHolder<${NAME}VO2, AppVh${NAME}Binding> {\n" +
                    "        val binding = DataBindingUtil.inflate<AppVh${NAME}Binding>(\n" +
                    "            LayoutInflaterWrapper.from(parent.context),\n" +
                    "            R.layout.app_vh_WIDGET_PREFIXLOWER_NAME, parent, false\n" +
                    "        )\n" +
                    "\n" +
                    "        val vh = object : DataBindingViewHolder<${NAME}VO2, AppVh${NAME}Binding>(binding),IReactiveViewHolder<${NAME}VO2> {\n" +
                    "            val buryPointContext = object : BuryPointContextWrapper() {\n" +
                    "                override fun createContextDataInternal(pointKey: String?): MutableList<Pair<String, String>>? {\n" +
                    "                    return Collections.emptyList()\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            init {\n" +
                    "                buryPointContext.wrapBy(parentContext)\n" +
                    "                binding.bp = buryPointContext\n" +
                    "            }\n" +
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

    //,IReactiveViewHolder<OldVO2>

    public KotlinFileGenerator(FileSaver fileSaver) {
        super(fileSaver);
    }
}
