package Pinecone.Framework.System.util;

import Pinecone.Framework.Unit.UnitUtils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class PackageUtils {
    public static List<String> fetchClassName( String szPackageName ) {
        List<String > list = new ArrayList<>();
        PackageUtils.fetchClassName( szPackageName, list );
        return list;
    }

    public static void fetchClassName( String szPackageName, List<String> classNames ) {
        PackageUtils.fetchClassName( szPackageName, classNames, true );
    }

    public static void fetchClassName( String szPackageName, List<String> classNames, boolean bChildPackage ) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = szPackageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if ( url != null ) {
            String type = url.getProtocol();
            if ( type.equals("file") ) {
                PackageUtils.fetchClassNamesByFile(url.getPath(), classNames, bChildPackage);
            }
            else if (type.equals("jar")) {
                PackageUtils.fetchClassNamesByJar(url.getPath(), classNames, bChildPackage);
            }
        }
        else {
            PackageUtils.fetchClassNamesByJars( ((URLClassLoader) loader).getURLs(), packagePath, classNames, bChildPackage );
        }
    }

    public static void fetchClassNamesByFile( String szFilePath, List<String> classNames, boolean bChildPackage ) {
        File file = new File(szFilePath);
        File[] childFiles = file.listFiles();
        for ( File childFile : childFiles ) {
            if ( childFile.isDirectory() ) {
                if ( bChildPackage ) {
                    List<String > subList = UnitUtils.spawnExtendParent( classNames );
                    PackageUtils.fetchClassNamesByFile( childFile.getPath(), subList, bChildPackage );
                    classNames.addAll( subList );
                }
            }
            else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    classNames.add(childFilePath);
                }
            }
        }
    }

    public static void fetchClassNamesByJar( String szJarPath, List<String> classNames, boolean bChildPackage ) {
        String[] jarInfo = szJarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while ( entrys.hasMoreElements() ) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if ( entryName.endsWith(".class") ) {
                    if ( bChildPackage ) {
                        if ( entryName.startsWith(packagePath) ) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            classNames.add(entryName);
                        }
                    }
                    else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        }
                        else {
                            myPackagePath = entryName;
                        }
                        if ( myPackagePath.equals(packagePath )) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            classNames.add(entryName);
                        }
                    }
                }
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void fetchClassNamesByJars( URL[] urls, String szPackagePath, List<String> classNames, boolean bChildPackage ) {
        if ( urls != null ) {
            for ( int i = 0; i < urls.length; i++ ) {
                URL url = urls[i];
                String urlPath = url.getPath();
                if ( urlPath.endsWith("classes/") ) {
                    continue;
                }
                String jarPath = urlPath + "!/" + szPackagePath;
                List<String > subList = UnitUtils.spawnExtendParent( classNames );
                PackageUtils.fetchClassNamesByJar(jarPath, subList, bChildPackage);
                classNames.addAll( subList );
            }
        }
    }
}
