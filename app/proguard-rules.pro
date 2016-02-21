# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\ASDK\Android\sdk/tools/proguard/proguard-android.txt
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

-ignorewarnings
-dontnote 'org.apache.http.conn.*'
-dontnote 'org.apache.http.conn.scheme.*'
-dontnote 'org.apache.http.params.*'
-dontnote 'android.net.http.*'
-dontnote 'com.google.android.gms.common.*'
-dontnote 'com.google.vending.licensing.*'
-dontnote 'com.android.vending.licensing.*'
-dontnote 'android.support.v4.text.*'
-dontnote 'android.support.v7.widget.*'
-dontnote 'android.support.v4.app.*'
-dontnote 'com.google.gson.internal.*'
-dontskipnonpubliclibraryclassmembers


-keep class *
-keepattributes Signature
-keepattributes InnerClasses