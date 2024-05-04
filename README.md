# GraalVM iOS CAP cache generation

This project generates CAP (C Annotation Processor) cache needed to compile iOS native image using GraalVM.  
CAP cache is used to bind GraalVM's java code to C. It contains structs layout that are used in GraalVM's java code
using annotations, see [here](https://github.com/oracle/graal/blob/ac0113b1c6e16e55a1033dd6603486710a57dec5/substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/jni/headers/JNIInvokeInterface.java#L38)
for an example.  

In order to generate those CAP files, this project creates a dummy jar and runs GraalVM `native-image` tool which
generates the cache.  
Internally this will look into OS and Java C code to get struct layouts, and then output them in `.cap` files.  

In order to generate CAP files, run `generateCapCache` gradle task.