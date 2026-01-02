-dontwarn android.media.LoudnessCodecController$OnLoudnessCodecUpdateListener
-dontwarn android.media.LoudnessCodecController
-dontwarn java.lang.reflect.AnnotatedType

# Keep all the members of public constructor
-keepclassmembers class com.mca.repository.impl.* {
    public <init>();
}

# Keep all the network models
-keepattributes Signature
-keep class com.mca.util.model.* { *; }
-keep class * extends com.google.firebase.database.GenericTypeIndicator { *; }

# Suppress SLF4J missing class warnings used by Google Auth/gRPC
-dontwarn org.slf4j.**
-dontwarn com.google.auth.oauth2.Slf4jUtils
-dontwarn com.google.auth.oauth2.Slf4jLoggingHelpers

# Keep Google API classes
-keep class com.google.** { *; }

# Keep all the Routes
-keep class com.mca.util.navigation.Route { *; }
-keep class com.mca.util.navigation.Route$* { *; }