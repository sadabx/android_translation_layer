# Using Android Translation Layer (ATL)

This guide covers running, configuring, and installing Android applications using ATL.

---

## Running Applications

### 1. Running from `builddir` (Development)

During development or testing, run ATL directly from your build directory:

```sh
cd builddir
```

#### Standard Single APK
```sh
RUN_FROM_BUILDDIR=1 ./android-translation-layer /path/to/app.apk
```

#### Split APKs (Base + Config Splits)
Modern Android apps often ship as multiple APKs (e.g., base APK + screen density / architecture configs). Pass all APK files as arguments:
```sh
RUN_FROM_BUILDDIR=1 ./android-translation-layer /path/to/base.apk /path/to/config.mdpi.apk
```

#### Launching a Specific Activity
By default, ATL launches the main launcher Activity defined in `AndroidManifest.xml`. To launch a specific Activity:
```sh
RUN_FROM_BUILDDIR=1 ./android-translation-layer /path/to/app.apk -l com/example/app/TargetActivity
```

---

### 2. Running after System Installation

If you installed ATL via `meson install`:

```sh
# Using default data directory (~/.local/share/android_translation_layer/)
android-translation-layer /path/to/app.apk

# Specifying a custom data directory
ANDROID_APP_DATA_DIR=/path/to/data android-translation-layer /path/to/app.apk
```

---

### 3. Installing an APK to the Desktop

To register an Android application as a native desktop application with an application icon and launcher entry:

```sh
android-translation-layer --install /path/to/app.apk
```

This will:
1. Copy the APK into `_installed_apks_` inside your data directory (`~/.local/share/android_translation_layer/`).
2. Use the XDG Desktop Portal to install a native desktop entry (`.desktop` file) into your application menu.

---

## Window Sizing & Display Tweaks

Some applications expect a mobile viewport and handle arbitrary window resizing poorly. You can set initial window constraints:

| Flag | Description | Example |
| :--- | :--- | :--- |
| `-w <width>` | Set initial window width in pixels | `-w 400` |
| `-h <height>` | Set initial window height in pixels | `-h 800` |
| `--sdk-int=<version>` | Target Android API level | `--sdk-int=28` |

Example launching a phone-sized portrait window:
```sh
RUN_FROM_BUILDDIR=1 ./android-translation-layer -w 400 -h 800 /path/to/app.apk
```

---

## App Data and Storage

By default, application data is isolated under:
```
~/.local/share/android_translation_layer/[apk-name]_/
```

### Storage Structure:
* **`/data/data/[app-id]` (Internal Storage):** App databases, shared preferences, and cache are stored here. Native shared libraries extracted from the APK reside in `lib/`.
* **`/storage/emulated/0` (External Storage):** Mapped to this folder. Application downloads and expansion files (OBBs) reside under `Android/obb/[app-id]/`.
* **Resource Overrides:** You can override assets packed inside the APK by placing files in `assets/` within this folder. If an app queries `assets/config.json`, ATL will prefer the file in this directory over the one packed inside the APK.

---

## Test Applications

A repository of test APKs (including full games and OpenGL native rendering samples) is available at:
[gitlab.com/android_translation_layer/atl_test_apks](https://gitlab.com/android_translation_layer/atl_test_apks)

Examples:
* **Full game (Gravity Defied):**
  ```sh
  RUN_FROM_BUILDDIR=1 ./android-translation-layer /path/to/org.happysanta.gd_29.apk -l org/happysanta/gd/GDActivity
  ```
* **Native OpenGL rendering (GLES3 JNI):**
  ```sh
  RUN_FROM_BUILDDIR=1 ./android-translation-layer /path/to/gles3jni.apk -l com/android/gles3jni/GLES3JNIActivity
  ```

