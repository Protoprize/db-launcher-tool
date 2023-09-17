package org.p.launcher;

import java.util.List;

public class Configuration {
    private List<String> classPath;
    private String mainClass;
    private List<String> vmArgs;

    public Configuration(List<String> classPath, String mainClass, List<String> vmArgs) {
        this.classPath = classPath;
        this.mainClass = mainClass;
        this.vmArgs = vmArgs;
    }
        public String getJarName() {
            return classPath.get(0);
        }
        public List<String> getVmArgs() {
            return vmArgs;
        }
}
