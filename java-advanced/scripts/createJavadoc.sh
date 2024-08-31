implementorMain="../java-solutions/info/kgeorgiy/ja/zhimoedov/implementor/Implementor.java"
ImplerInterface="../../java-advanced-2023/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/Impler.java"
JarImplerInterface="../../java-advanced-2023/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/JarImpler.java"
ImplerException="../../java-advanced-2023/modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor/ImplerException.java"
pathToDoc="../javadoc"
implementorJar="../../java-advanced-2023/artifacts/info.kgeorgiy.java.advanced.implementor.jar"
javadoc $implementorMain $ImplerInterface $JarImplerInterface $ImplerException -d $pathToDoc -cp $implementorJar -private
