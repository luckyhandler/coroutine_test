# Testing Coroutines

This is an isolated example to reproduce a bug I experienced in another project.
Though following working examples in the web like https://t.co/TYnoykB5EH?amp=1 it's not working in this project.

Things considered here are:

* Created own TestCoroutineDispatcher to run coroutine tests blocking
* Created own TestCoroutineRule to override Main dispatcher

Still the test fails with the exception:
```
java.lang.IllegalStateException: This job has not completed yet

	at kotlinx.coroutines.JobSupport.getCompletionExceptionOrNull(JobSupport.kt:1189)
	at kotlinx.coroutines.test.TestBuildersKt.runBlockingTest(TestBuilders.kt:53)
	at kotlinx.coroutines.test.TestBuildersKt.runBlockingTest(TestBuilders.kt:78)
	at com.example.testingcoroutines.TestCoroutineRuleKt.runBlocking(TestCoroutineRule.kt:61)
	at com.example.testingcoroutines.UiStateTest.ui states are emitted in the correct order(UiStateTest.kt:38)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
	at org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
	at org.junit.rules.RunRules.evaluate(RunRules.java:20)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:230)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:58)
```

**UPDATE**
The issue is Flow, when using Mockito the code is working as expected --> to see the working solution check out the mockito-Branch