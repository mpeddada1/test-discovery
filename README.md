# test-discovery

To reproduce:

## With native-maven-plugin

1) Call `mvn clean install`
2) Call `mvn test -Pnative`
3) Notice the following error:

```
 T E S T S
-------------------------------------------------------
Running com.example.MySampleTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.049 sec

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] 
[INFO] --- native-maven-plugin:0.9.18:test (test-native) @ test-discovery ---
[ERROR] Test configuration file wasn't found. Make sure that test execution wasn't skipped.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.420 s
[INFO] Finished at: 2022-12-08T16:44:56-05:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.graalvm.buildtools:native-maven-plugin:0.9.18:test (test-native) on project test-discovery: Execution test-native of goal org.graalvm.buildtools:native-maven-plugin:0.9.18:test failed: Test configuration file wasn't found. -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
```
4) Uncomment the [maven-surefire-plugin setup](https://github.com/mpeddada1/test-discovery/blob/b0f268c62450936904e928595572c8e8206e5da7/pom.xml#L33-L47) and re-run Steps 1-2 to see a successful build.

The steps in this section rely on the native-maven-plugin and run the tests twice (before native-image compilation and after).

## Without native-maven-plugin

Steps 1-2 assume that you already have the `test-ids/` directory populated from the "With native-maven-plugin" section.
Steps 3-5 show how tests can be run with a manually populated `test-ids` directory.

1) Call `mvn clean install -DskipTests` to populate the `target/test-classes` directory with the compiled test class.

2) Run the following command:

```
native-image -cp /${HOME}/IdeaProjects/native-image-experiments/test-discovery/target/test-classes:/${HOME}/.m2/repository/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar:/${HOME}/.m2/repository/org/junit/vintage/junit-vintage-engine/5.8.1/junit-vintage-engine-5.8.1.jar:/${HOME}/.m2/repository/org/junit/platform/junit-platform-engine/1.8.1/junit-platform-engine-1.8.1.jar:/${HOME}/.m2/repository/com/google/guava/failureaccess/1.0.1/failureaccess-1.0.1.jar:/${HOME}/.m2/repository/org/junit/platform/junit-platform-commons/1.8.1/junit-platform-commons-1.8.1.jar:/${HOME}/.m2/repository/junit/junit/4.13.2/junit-4.13.2.jar:/${HOME}/.m2/repository/com/google/errorprone/error_prone_annotations/2.7.1/error_prone_annotations-2.7.1.jar:/${HOME}/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/${HOME}/.m2/repository/com/google/guava/listenablefuture/9999.0-empty-to-avoid-conflict-with-guava/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar:/${HOME}/.m2/repository/com/google/auto/value/auto-value-annotations/1.8.1/auto-value-annotations-1.8.1.jar:/${HOME}/.m2/repository/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar:/${HOME}/.m2/repository/com/google/truth/truth/1.1.3/truth-1.1.3.jar:/${HOME}/.m2/repository/org/checkerframework/checker-compat-qual/2.5.5/checker-compat-qual-2.5.5.jar:/${HOME}/.m2/repository/org/ow2/asm/asm/9.1/asm-9.1.jar:/${HOME}/.m2/repository/com/google/guava/guava/30.1.1-android/guava-30.1.1-android.jar:/${HOME}/.m2/repository/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar:/${HOME}/.m2/repository/org/checkerframework/checker-qual/3.13.0/checker-qual-3.13.0.jar:/${HOME}/.m2/repository/com/google/j2objc/j2objc-annotations/1.3/j2objc-annotations-1.3.jar:/${HOME}/.m2/repository/org/junit/platform/junit-platform-console/1.8.1/junit-platform-console-1.8.1.jar:/${HOME}/.m2/repository/org/junit/platform/junit-platform-reporting/1.8.1/junit-platform-reporting-1.8.1.jar:/${HOME}/.m2/repository/org/junit/platform/junit-platform-launcher/1.8.1/junit-platform-launcher-1.8.1.jar:/${HOME}/.m2/repository/org/junit/platform/junit-platform-engine/1.8.1/junit-platform-engine-1.8.1.jar:/${HOME}/.m2/repository/org/junit/platform/junit-platform-commons/1.8.1/junit-platform-commons-1.8.1.jar:/${HOME}/.m2/repository/org/junit/jupiter/junit-jupiter/5.8.1/junit-jupiter-5.8.1.jar:/${HOME}/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.8.1/junit-jupiter-api-5.8.1.jar:/${HOME}/.m2/repository/org/junit/jupiter/junit-jupiter-params/5.8.1/junit-jupiter-params-5.8.1.jar:/${HOME}/.m2/repository/org/junit/jupiter/junit-jupiter-engine/5.8.1/junit-jupiter-engine-5.8.1.jar:/${HOME}/.m2/repository/org/graalvm/buildtools/graalvm-reachability-metadata/0.9.18/graalvm-reachability-metadata-0.9.18.jar:/${HOME}/.m2/repository/org/graalvm/buildtools/junit-platform-native/0.9.18/junit-platform-native-0.9.18.jar --no-fallback -H:Path=/${HOME}/IdeaProjects/native-image-experiments/test-discovery/target -H:Name=native-tests -Djunit.platform.listeners.uid.tracking.output.dir=/${HOME}/IdeaProjects/native-image-experiments/test-discovery/target/test-ids -H:Class=org.graalvm.junit.platform.NativeImageJUnitLauncher --features=org.graalvm.junit.platform.JUnitPlatformFeature -DtestDiscovery
```

The classpath of the application (including all the test dependencies) needs to be generated accurately in order to 
successfully run this command. 

Results in a successful build:
```
=======================================================================================================================
GraalVM Native Image: Generating 'native-tests' (executable)...
=======================================================================================================================
[1/7] Initializing...                                                                                   (4.2s @ 0.23GB)
 Version info: 'GraalVM 22.3.0 Java 17 CE'
 Java version info: '17.0.5+8-jvmci-22.3-b08'
 C compiler: gcc (linux, x86_64, 12.2.0)
 Garbage collector: Serial GC
 1 user-specific feature(s)
 - org.graalvm.junit.platform.JUnitPlatformFeature
[junit-platform-native] Running in 'test listener' mode using files matching pattern [junit-platform-unique-ids*] found in folder [${HOME}/native-image-experiments/test-discovery/target/test-ids] and its subfolders.
[2/7] Performing analysis...  [******]                                                                 (13.7s @ 1.17GB)
   4,734 (79.50%) of  5,955 classes reachable
   5,737 (54.65%) of 10,498 fields reachable
  20,954 (49.32%) of 42,486 methods reachable
     168 classes,    94 fields, and   784 methods registered for reflection
      58 classes,    58 fields, and    52 methods registered for JNI access
       4 native libraries: dl, pthread, rt, z
[3/7] Building universe...                                                                              (1.6s @ 1.95GB)
[4/7] Parsing methods...      [*]                                                                       (1.6s @ 1.13GB)
[5/7] Inlining methods...     [***]                                                                     (0.9s @ 1.73GB)
[6/7] Compiling methods...    [***]                                                                     (9.1s @ 1.90GB)
[7/7] Creating image...                                                                                 (1.6s @ 2.51GB)
   7.19MB (40.38%) for code area:    12,673 compilation units
   9.71MB (54.53%) for image heap:  139,583 objects and 7 resources
 929.03KB ( 5.09%) for other data
  17.82MB in total
-----------------------------------------------------------------------------------------------------------------------
Top 10 packages in code area:                              Top 10 object types in image heap:
 848.87KB java.util                                           1.52MB byte[] for code metadata
 507.52KB java.lang.invoke                                    1.27MB java.lang.String
 409.78KB com.sun.crypto.provider                             1.10MB java.lang.Class
 360.24KB java.lang                                           1.06MB byte[] for general heap data
 290.69KB java.util.concurrent                              914.13KB byte[] for java.lang.String
 276.09KB java.text                                         473.20KB java.util.HashMap$Node
 216.22KB java.util.regex                                   406.83KB com.oracle.svm.core.hub.DynamicHubCompanion
 209.82KB java.util.stream                                  269.16KB java.util.concurrent.ConcurrentHashMap$Node
 204.43KB java.math                                         248.17KB java.util.HashMap$Node[]
 187.70KB java.io                                           234.17KB java.lang.String[]
   3.67MB for 200 more packages                               2.21MB for 1192 more object types
-----------------------------------------------------------------------------------------------------------------------
                        0.8s (2.2% of total time) in 22 GCs | Peak RSS: 4.22GB | CPU load: 8.09
```

Note that the test-ids file contains the following information: `[engine:junit-vintage]/[runner:com.example.MySampleTest]/[test:testNativeImage(com.example.MySampleTest)]`

3) Now if we try to clear the test-ids directory and rerun the command above:
```
=======================================================================================================================
GraalVM Native Image: Generating 'native-tests' (executable)...
=======================================================================================================================
[1/7] Initializing...                                                                                   (4.2s @ 0.22GB)
 Version info: 'GraalVM 22.3.0 Java 17 CE'
 Java version info: '17.0.5+8-jvmci-22.3-b08'
 C compiler: gcc (linux, x86_64, 12.2.0)
 Garbage collector: Serial GC
 1 user-specific feature(s)
 - org.graalvm.junit.platform.JUnitPlatformFeature
[junit-platform-native] Running in 'test discovery' mode. Note that this is a fallback mode.

Fatal error: org.junit.platform.commons.JUnitException: TestEngine with ID 'junit-vintage' failed to discover tests
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discoverEngineRoot(EngineDiscoveryOrchestrator.java:160)
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discoverSafely(EngineDiscoveryOrchestrator.java:134)
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discover(EngineDiscoveryOrchestrator.java:108)
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discover(EngineDiscoveryOrchestrator.java:80)
	at org.junit.platform.launcher.core.DefaultLauncher.discover(DefaultLauncher.java:110)
	at org.junit.platform.launcher.core.DefaultLauncher.discover(DefaultLauncher.java:78)
	at org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher.discover(DefaultLauncherSession.java:81)
	at org.junit.platform.launcher.core.SessionPerRequestLauncher.discover(SessionPerRequestLauncher.java:46)
	at org.graalvm.junit.platform.JUnitPlatformFeature.discoverTestsAndRegisterTestClassesForReflection(JUnitPlatformFeature.java:135)
	at org.graalvm.junit.platform.JUnitPlatformFeature.beforeAnalysis(JUnitPlatformFeature.java:94)
	at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.lambda$runPointsToAnalysis$9(NativeImageGenerator.java:736)
	at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.FeatureHandler.forEachFeature(FeatureHandler.java:85)
	at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.runPointsToAnalysis(NativeImageGenerator.java:736)
	at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.doRun(NativeImageGenerator.java:578)
	at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.run(NativeImageGenerator.java:535)
	at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.buildImage(NativeImageGeneratorRunner.java:403)
	at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.build(NativeImageGeneratorRunner.java:580)
	at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.main(NativeImageGeneratorRunner.java:128)
Caused by: java.lang.NoClassDefFoundError: [Lkotlin/jvm/functions/Function0;
	at java.base/java.lang.Class.getDeclaredMethods0(Native Method)
	at java.base/java.lang.Class.privateGetDeclaredMethods(Class.java:3402)
	at java.base/java.lang.Class.privateGetPublicMethods(Class.java:3427)
	at java.base/java.lang.Class.getMethods(Class.java:2019)
	at org.junit.platform.commons.util.ReflectionUtils.getDefaultMethods(ReflectionUtils.java:1518)
	at org.junit.platform.commons.util.ReflectionUtils.getDeclaredMethods(ReflectionUtils.java:1491)
	at org.junit.platform.commons.util.ReflectionUtils.findAllMethodsInHierarchy(ReflectionUtils.java:1433)
	at org.junit.platform.commons.util.ReflectionUtils.findMethods(ReflectionUtils.java:1417)
	at org.junit.platform.commons.util.ReflectionUtils.findMethods(ReflectionUtils.java:1403)
	at org.junit.vintage.engine.descriptor.TestSourceProvider.lambda$findMethod$1(TestSourceProvider.java:75)
	at java.base/java.util.HashMap.computeIfAbsent(HashMap.java:1220)
	at java.base/java.util.Collections$SynchronizedMap.computeIfAbsent(Collections.java:2760)
	at org.junit.vintage.engine.descriptor.TestSourceProvider.findMethod(TestSourceProvider.java:75)
	at org.junit.vintage.engine.descriptor.TestSourceProvider.computeTestSource(TestSourceProvider.java:56)
	at java.base/java.util.concurrent.ConcurrentHashMap.computeIfAbsent(ConcurrentHashMap.java:1708)
	at org.junit.vintage.engine.descriptor.TestSourceProvider.findTestSource(TestSourceProvider.java:47)
	at org.junit.vintage.engine.discovery.RunnerTestDescriptorPostProcessor.addChildrenRecursively(RunnerTestDescriptorPostProcessor.java:62)
	at org.junit.vintage.engine.discovery.RunnerTestDescriptorPostProcessor.applyFiltersAndCreateDescendants(RunnerTestDescriptorPostProcessor.java:41)
	at org.junit.vintage.engine.discovery.VintageDiscoverer.discover(VintageDiscoverer.java:46)
	at org.junit.vintage.engine.VintageTestEngine.discover(VintageTestEngine.java:64)
	at org.junit.platform.launcher.core.EngineDiscoveryOrchestrator.discoverEngineRoot(EngineDiscoveryOrchestrator.java:152)
	... 17 more
Caused by: java.lang.ClassNotFoundException: kotlin.jvm.functions.Function0
	at java.base/java.net.URLClassLoader.findClass(URLClassLoader.java:445)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:587)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520)
	... 38 more
-----------------------------------------------------------------------------------------------------------------------
                        0.2s (4.3% of total time) in 8 GCs | Peak RSS: 0.73GB | CPU load: 3.37
=======================================================================================================================
Failed generating 'native-tests' after 4.7s.
```

4) Manually add the test-ids.txt file to the `target/test-ids/` directory:
```
$ vi junit-platform-unique-ids.txt // Copy and paste [engine:junit-vintage]/[runner:com.example.MySampleTest]/[test:testNativeImage(com.example.MySampleTest)]
```
5) Re-run the `native-image` command from step 1. This results in a successful run.

## Additional Notes
If we want the maven-surefire plugin to generate a test-ids file, we will need to add the following configuration: 

```
   <configuration>
       <systemProperties>
           <junit.platform.listeners.uid.tracking.enabled>true</junit.platform.listeners.uid.tracking.enabled>
           <junit.platform.listeners.uid.tracking.output.dir>${project.basedir}/target/test-ids/</junit.platform.listeners.uid.tracking.output.dir>
       </systemProperties>
   </configuration>
```

This configuration setting has not been tested with concurrent testing.