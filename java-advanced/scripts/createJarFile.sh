pathImplementor ="../java-solutions/info/kgeorgiy/ja/zhimoedov/implementor/Implementor.java"
pathImplementorClass="info/kgeorgiy/ja/zhimoedov/implementor/Implementor.class"
pathToJar="../../java-advanced-2023/artifacts/info.kgeorgiy.java.advanced.implementor.jar"
javac -d . $pathImplementor -cp $pathToJar
jar cfm "Implementor.jar" "MANIFEST.MF" $pathImplementorClass $pathToJar