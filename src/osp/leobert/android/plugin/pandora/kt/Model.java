package osp.leobert.android.plugin.pandora.kt;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.kt </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> Model </p>
 * Created by leobert on 2019-06-03.
 */
public class Model {
    public final String prefix;
    public final boolean onlyVh;

    public final String rPackage;
    public final String baseVhPackage;
    public final String baseVhName;
    public final String baseKtVhPackage;
    public final String baseKtVhName;

    public final boolean reactive;

    public String templateVhImport;
    public String templateVhCreator;
    public String templateReactiveVhCreator;


    public String templateKt;
    public String templateKtReactive;

    public boolean isPandoraKt = false;

    public Model(String prefix, boolean onlyVh, boolean reactive,
                 String rPackage, String baseVhPackage, String baseVhName,
                 String baseKtVhPackage, String baseKtVhName) {
        this.prefix = prefix;
        this.onlyVh = onlyVh;
        this.rPackage = rPackage;
        this.reactive = reactive;
        this.baseVhPackage = baseVhPackage;
        this.baseVhName = baseVhName;
        this.baseKtVhPackage = baseKtVhPackage;
        this.baseKtVhName = baseKtVhName;
    }

    @Override
    public String toString() {
        return "Model{" +
                "prefix='" + prefix + '\'' +
                ", onlyVh=" + onlyVh +
                ", rPackage='" + rPackage + '\'' +
                ", baseVhPackage='" + baseVhPackage + '\'' +
                ", baseVhName='" + baseVhName + '\'' +
                ", baseKtVhPackage='" + baseKtVhPackage + '\'' +
                ", baseKtVhName='" + baseKtVhName + '\'' +
                ", reactive=" + reactive +
                ", templateVhImport='" + templateVhImport + '\'' +
                ", templateVhCreator='" + templateVhCreator + '\'' +
                ", templateReactiveVhCreator='" + templateReactiveVhCreator + '\'' +
                ", templateKt='" + templateKt + '\'' +
                ", templateKtReactive='" + templateKtReactive + '\'' +
                ", isPandoraKt=" + isPandoraKt +
                '}';
    }
}
