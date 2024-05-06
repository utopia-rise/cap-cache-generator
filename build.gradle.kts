plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.utopia-rise"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

tasks.register<Exec>("generateCapCache") {
    dependsOn(tasks.build)
    group = "graal-ios"

    val graalVmNativeImage = File(System.getenv("GRAALVM_HOME"))
        .resolve("bin")
        .resolve("native-image")
        .absolutePath

    val libsDir = project.layout.buildDirectory.asFile.get().resolve("libs")
    val iosLibDir = libsDir.resolve("ios")
    iosLibDir.mkdirs()
    val dummyJarFile = tasks.jar.get().outputs.files.files.first().absolutePath

    val arguments = arrayOf(
        graalVmNativeImage,
        "-cp",
        dummyJarFile,
        "-H:+SharedLibrary",
        "-H:Name=usercode",
        "-H:PageSize=16384",
        "-Dsvm.targetName=iOS",
        "-Dsvm.targetArch=arm64",
        "-H:+NewCAPCache",
        "-H:+ExitAfterCAPCache",
        "-H:CAPCacheDir=${iosLibDir.absolutePath}",
        "-Dsvm.platform=org.graalvm.nativeimage.Platform\$IOS_AARCH64",
    )

    println(arguments.joinToString(" "))

    commandLine(*arguments)
}