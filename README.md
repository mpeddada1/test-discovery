# test-discovery

To reproduce:

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
4) Uncomment the maven-surefire-plugin and re-running `mvn test -Pnative` results in a successful build.
5) Running the following command:

```
native-image -cp <classpath> --no-fallback -H:Path=${HOME}/native-image-experiments/test-discovery/target -H:Name=native-tests -Djunit.platform.listeners.uid.tracking.output.dir=${HOME}/native-image-experiments/test-discovery/target/test-ids -H:Class=org.graalvm.junit.platform.NativeImageJUnitLauncher --features=org.graalvm.junit.platform.JUnitPlatformFeature -DtestDiscovery
```

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

6) Now if we try to clear the test-ids directory and rerun the command above:
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