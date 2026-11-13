# Troubleshooting & Caveats

This document lists common issues, platform-specific workarounds, and debugging techniques when running Android applications on ATL.

---

## 1. Graphics & Display

### X11 OpenGL / EGL Issues
On X11, GTK might attempt to use GLX instead of EGL, which breaks Android's EGL-dependent rendering pipeline.
* **Fix:** Force GTK to use EGL:
  ```sh
  GDK_DEBUG=gl-egl ./android-translation-layer /path/to/app.apk
  ```

### Cairo Fallback Renderer
If your GPU driver has issues with Vulkan or OpenGL in GTK 4, switch to the Cairo software renderer:
```sh
GSK_RENDERER=cairo ./android-translation-layer /path/to/app.apk
```

---

## 2. Platform & Hardware Specifics

### Apple Silicon (AArch64) 16KB Page Size
Upstream Android Runtime (ART) typically assumes 4KB page sizes. On Apple Silicon systems running 16KB kernels (such as Asahi Linux), Ahead-Of-Time (AOT) compiled `.oat` files might fail.
* **Fix:** Force the use of the interpreter by disabling image dex2oat and JIT:
  ```sh
  ./android-translation-layer -X "-Xnoimage-dex2oat" -X "-Xusejit:false" /path/to/app.apk
  ```
* **Note:** Clear the oat cache at `~/.cache/art/` after changing flags to prevent loading previously compiled oat files.

---

## 3. Native Libraries & NativeBridge

When running on an x86_64 desktop, apps containing only ARM/ARM64 native shared libraries (`.so`) require binary translation:
* ATL can load an ARM translation bridge (e.g. `libndk_translation.so`) via ART's native bridge parameter:
  ```sh
  -X "-XX:NativeBridge=/path/to/libnativebridge.so"
  ```
* The Bionic translation layer in `bionic_translation/` supports multi-architecture relocations (including AArch64 relocations such as `R_AARCH64_JUMP_SLOT`, `R_AARCH64_GLOB_DAT`, and `R_AARCH64_RELATIVE`).

---

## 4. Debugging & Logs

### Enabling Verbose Android Logcat
To inspect Android framework logs (`Log.d`, `Log.v`, `Log.e`):
```sh
ANDROID_LOG_TAGS=*:v ./android-translation-layer /path/to/app.apk
```
*(Values: `v` = verbose, `d` = debug, `i` = info, `w` = warning, `e` = error)*

### Missing Methods or Classes (`ClassNotFoundException`, `NoSuchMethodError`)
If an app crashes on startup, check the output for missing Android APIs. Most crashes are due to unimplemented stubs in `src/api-impl/`. Refer to [doc/QuickHelp.md](QuickHelp.md) for how to add missing stubs or implementations.

