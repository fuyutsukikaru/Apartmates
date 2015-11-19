# Apartmates

Prerequisites:
* Download latest Android Studio and the latest JDK1.7+
* Download the latest version of VirtualBox.
* Download Genymotion and the plugin on Android Studio.
  * Create an account on Genymotion in order to use emulator devices.
  * Go to Preferences on Android Studio -> Plugins -> Browse Repositories, and search and install Genymotion Plugin.

To run the app:
- Checkout project from Version Control System (VCS)
- File -> New -> Import Project, and select the ENTIRE project folder, `Apartmates/`.
- Click the SDK Manager icon on the top bar in Android Studio. Install SDK Platforms versions 5.0, 5.1.0, 6.0 and in SDK Tools install the various SDK tools. View following image for all dependencies.
![SDK Dependencies](http://i.imgur.com/OXqhOz6.png)
- Click Genymotion icon on the top bar in Android Studio.
- A prompt window asking you to input the path where Genymotion is installed will pop up. Find and input the proper location.
- Select New, which opens a Genymotion window. Sign into Genymotion and create a device of your choosing that's at least on Android 5.0.0.
- Launch device through Genymotion or Genymotion Plugin.
- Run app by hitting the green arrow in the toolbar, or Run -> Run 'app'.
- Select the device to run the app on.

To run a test:
- Open the tests folder in app -> java -> com.cs130.apartmates (androidTest), and right click on any of the tests.
- Hit Run on the dropdown menu to run the specific test.

To run the app on a device:
- Download the APK at this [link](https://dl.dropboxusercontent.com/u/12858467/apartmates_snapshot.apk).
- Move over to device's internal storage via your preferred method.
- Install using a file manager.
