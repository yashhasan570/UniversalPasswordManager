# CredentialManager classes
-keep class androidx.credentials.** { *; }
-keep interface androidx.credentials.** { *; }

# Hilt
-dontwarn com.google.errorprone.annotations.**

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.universalpasswordmanager.testharness.**$$serializer { *; }
-keepclassmembers class com.universalpasswordmanager.testharness.** {
    *** Companion;
}
-keepclasseswithmembers class com.universalpasswordmanager.testharness.** {
    kotlinx.serialization.KSerializer serializer(...);
}
