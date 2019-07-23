package osp.leobert.android.plugin.pandora.kt;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.kt </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> Model </p>
 * <p><b>Description:</b> TODO </p>
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

}
