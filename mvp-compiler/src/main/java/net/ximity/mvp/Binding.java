package net.ximity.mvp;

/**
 * Binding model container
 *
 * @author by Emarc Magtanong on 2017/08/17.
 */
public final class Binding {

    private final String packageName;
    private final String componentName;
    private final String moduleName;

    public Binding(String packageName, String componentName, String moduleName) {
        this.packageName = packageName;
        this.componentName = componentName;
        this.moduleName = moduleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getModuleName() {
        return moduleName;
    }
}
