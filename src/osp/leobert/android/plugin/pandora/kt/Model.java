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

    public Model(String prefix, boolean onlyVh, String rPackage, String baseVhPackage, String baseVhName) {
        this.prefix = prefix;
        this.onlyVh = onlyVh;
        this.rPackage = rPackage;
        this.baseVhPackage = baseVhPackage;
        this.baseVhName = baseVhName;
    }

}
