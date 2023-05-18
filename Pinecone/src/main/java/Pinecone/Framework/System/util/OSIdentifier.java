package Pinecone.Framework.System.util;

public class OSIdentifier {
    private static String OS = System.getProperty("os.name").toLowerCase();

    private static OSIdentifier OSIInstance = new OSIdentifier();

    private Platform platform;

    private OSIdentifier(){}


    public static boolean isLinux(){
        return OS.contains("linux");
    }

    public static boolean isMacOS(){
        return OS.contains("mac") &&OS.indexOf("os")>0&& !OS.contains("x");
    }

    public static boolean isMacOSX(){
        return OS.contains("mac") &&OS.indexOf("os")>0&&OS.indexOf("x")>0;
    }

    public static boolean isWindows(){
        return OS.contains("windows");
    }

    public static boolean isOS2(){
        return OS.contains("os/2");
    }

    public static boolean isSolaris(){
        return OS.contains("solaris");
    }

    public static boolean isSunOS(){
        return OS.contains("sunos");
    }

    public static boolean isMPEiX(){
        return OS.contains("mpe/ix");
    }

    public static boolean isHPUX(){
        return OS.contains("hp-ux");
    }

    public static boolean isAix(){
        return OS.contains("aix");
    }

    public static boolean isOS390(){
        return OS.contains("os/390");
    }

    public static boolean isFreeBSD(){
        return OS.contains("freebsd");
    }

    public static boolean isIrix(){
        return OS.contains("irix");
    }

    public static boolean isDigitalUnix(){
        return OS.contains("digital") &&OS.indexOf("unix")>0;
    }

    public static boolean isNetWare(){
        return OS.contains("netware");
    }

    public static boolean isOSF1(){
        return OS.contains("osf1");
    }

    public static boolean isOpenVMS(){
        return OS.contains("openvms");
    }


    public static Platform getOSname(){
        if(isAix()){
            OSIdentifier.OSIInstance.platform = Platform.AIX;
        }else if (isDigitalUnix()) {
            OSIdentifier.OSIInstance.platform = Platform.Digital_Unix;
        }else if (isFreeBSD()) {
            OSIdentifier.OSIInstance.platform = Platform.FreeBSD;
        }else if (isHPUX()) {
            OSIdentifier.OSIInstance.platform = Platform.HP_UX;
        }else if (isIrix()) {
            OSIdentifier.OSIInstance.platform = Platform.Irix;
        }else if (isLinux()) {
            OSIdentifier.OSIInstance.platform = Platform.Linux;
        }else if (isMacOS()) {
            OSIdentifier.OSIInstance.platform = Platform.Mac_OS;
        }else if (isMacOSX()) {
            OSIdentifier.OSIInstance.platform = Platform.Mac_OS_X;
        }else if (isMPEiX()) {
            OSIdentifier.OSIInstance.platform = Platform.MPEiX;
        }else if (isNetWare()) {
            OSIdentifier.OSIInstance.platform = Platform.NetWare_411;
        }else if (isOpenVMS()) {
            OSIdentifier.OSIInstance.platform = Platform.OpenVMS;
        }else if (isOS2()) {
            OSIdentifier.OSIInstance.platform = Platform.OS2;
        }else if (isOS390()) {
            OSIdentifier.OSIInstance.platform = Platform.OS390;
        }else if (isOSF1()) {
            OSIdentifier.OSIInstance.platform = Platform.OSF1;
        }else if (isSolaris()) {
            OSIdentifier.OSIInstance.platform = Platform.Solaris;
        }else if (isSunOS()) {
            OSIdentifier.OSIInstance.platform = Platform.SunOS;
        }else if (isWindows()) {
            OSIdentifier.OSIInstance.platform = Platform.Windows;
        }else{
            OSIdentifier.OSIInstance.platform = Platform.Others;
        }
        return OSIdentifier.OSIInstance.platform;
    }

    public enum Platform {
        Any("any"),
        Linux("Linux"),
        Mac_OS("Mac OS"),
        Mac_OS_X("Mac OS X"),
        Windows("Windows"),
        OS2("OS/2"),
        Solaris("Solaris"),
        SunOS("SunOS"),
        MPEiX("MPE/iX"),
        HP_UX("HP-UX"),
        AIX("AIX"),
        OS390("OS/390"),
        FreeBSD("FreeBSD"),
        Irix("Irix"),
        Digital_Unix("Digital Unix"),
        NetWare_411("NetWare"),
        OSF1("OSF1"),
        OpenVMS("OpenVMS"),
        Others("Others");

        private String description;

        private Platform(String desc) {
            this.description = desc;
        }

        public String toString() {
            return this.description;
        }
    }

}
