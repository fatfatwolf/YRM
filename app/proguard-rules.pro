# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
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
-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn  com.newland.**
-dontwarn  org.bouncycastle.**
-dontwarn android.support.v4.**
-dontwarn android.**
#-dontwarn com.payment.SimpleBundle
#-dontwarn com.payment.hrtpayment.activity.SettingActivity
-dontwarn com.baidu.**
-dontwarn com.bbpos.**
-dontwarn com.example.**
-dontwarn com.hx.**
-dontwarn com.landicorp.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep class com.landicorp.** {*;}

-keep class com.newland.** {*;}
-keep class com.itron.** {*;}
-keep class com.bbpos.** {*;}
-keep class com.baidu.location.** {*;}
-keep class com.google.gson.** {*;}
-keep class com.hybunion.member.model.** {*;}
-keep class com.example.** {*;}
-keep class com.hx.** {*;}
-keep class org.xmlpull.v1.** { *; }



-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}