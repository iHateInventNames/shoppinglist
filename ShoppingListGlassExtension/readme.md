This extension is based on https://github.com/twaddington/mirror-quickstart-android

The extension must be registered with the Google API's in order to 
allow it to communicate with Google.

## Step 1: Generate the signing certificate fingerprint (SHA1)

Google verifies API requests sent from Android devices by matching the package
name and SHA1 signing certificate fingerprint with those defined by the application.

You need to generate the signing certificate's SHA-1 fingerprint by running the
following keytool command in a terminal:

    $ keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

> Note: The above command expects you to be signing your app with the Android
> debug certificate.

## Step 2: Enable the Mirror API

Before the sample code will work you'll need to enable the Mirror API for your
app. You can do this in your app's API project in the [Google APIs Console][3].

1. Create a new project in the [Google Cloud Console][3].
2. Select the **APIs & auth** tab in your project, and enable the Mirror API.
3. Select the **Registered apps** tab in your project, and click
   **Register App**
4. In the **Name** section, provide a name for your application
   (e.g. "OI Shopping List Glass Extension").
5. In the **Platform** section, select **Android** and **Accessing APIs directly from Android**.
6. In the **Android Identification** section,
  1. Type "org.openintents.shopping.glass" in **Package name.**
  2. Paste the SHA-1 fingerprint generated in Step 1 into the
     **Signing certificate fingerprint.**
  3. Click **Register**

[1]: http://android-developers.blogspot.com/2012/09/google-play-services-and-oauth-identity.html
[2]: https://developers.google.com/drive/quickstart-android
[3]: https://cloud.google.com/console
