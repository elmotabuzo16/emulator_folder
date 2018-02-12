# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/henri/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# square/okio

# Proguard with okio #60 (https://github.com/square/okio/issues/60)
-dontwarn okio.**

# square/retrofit

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

##---------------Begin: proguard configuration for Gson  ----------

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.vitalityactive.va.networking.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder

-keep class com.vitalityactive.va.membershippass.MembershipPassFragment {
 public *;
}
-keep class com.vitalityactive.va.profile.ProfileFragment {
 public *;
}
-keep public class com.vitalityactive.va.help.HelpSearchView {
 public *;
}
-keep class com.vitalityactive.va.pointsmonitor.PointsMonitorFragment {
 public *;
}
-dontwarn com.vitalityactive.va.membershippass.MembershipPassFragment
-dontwarn com.vitalityactive.va.pointsmonitor.PointsMonitorFragment
-dontwarn com.vitalityactive.va.profile.ProfileFragment


# Google Tag Manager
-dontwarn com.google.android.gms.**
-dontwarn com.google.common.annotations.**
-keep class **.proto.** { *; }

# Pushwoosh
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*
-keep class com.pushwoosh.** { *; }
-keep public class * extends com.pushwoosh.notification.NotificationServiceExtension
-keep public class * extends com.pushwoosh.notification.PushwooshNotificationFactory
-dontwarn com.pushwoosh.**
## Excempting everything on the push notifiation pacakge in the meantime
#-keep class com.vitalityactive.va.pushnotification.** { *; }