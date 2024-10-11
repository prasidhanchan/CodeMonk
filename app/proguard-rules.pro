-dontobfuscate

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

# Keep Google API classes
-keep class com.google.** { *; }

# Keep all the Routes
-keep class com.mca.util.navigation.Route { *; }
-keep class com.mca.util.navigation.Route$* { *; }