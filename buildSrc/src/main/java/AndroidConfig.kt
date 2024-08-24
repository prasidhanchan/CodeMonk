import org.gradle.api.JavaVersion

object AndroidConfig {
    const val MIN_SDK = 29
    const val TARGET_SDK = 34
    const val COMPILE_SDK = 34
    val JAVA_VERSION = JavaVersion.VERSION_21
    const val JVM_TARGET = "21"
}