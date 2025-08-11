# Keep classes used by reflection (Room, JSch, NanoHTTPD)
-keep class org.nanohttpd.** { *; }
-keep class com.jcraft.jsch.** { *; }
-keep class com.journeyapps.** { *; }
-keep class com.google.zxing.** { *; }

# Room
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-dontwarn javax.annotation.**