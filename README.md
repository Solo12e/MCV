# MCV (Mr.Classic VPN)

A professional, multi-protocol Android VPN/proxy app skeleton supporting V2Ray (vmess/vless/trojan/shadowsocks), SSH tunneling, DNS tunneling, HTTP payloads, hybrid/multipath chains, advanced config management, host/IP tests, AI intrusion skeleton, and extensive UI/UX features.

- Package: `com.mrclassic.mcv`
- Min SDK: 26
- Target SDK: 34
- Languages: Kotlin (primary)
- Theme: Dark by default (English + Arabic)

## Status
This repository contains a production-ready Android project skeleton with the following implemented:
- App structure, dark theme, first-run Telegram prompt with "Don't show again"
- Config manager + Keystore-backed AES-256-GCM encryption helper
- VpnService skeleton with foreground notification
- SSH tunnel manager (JSch) skeleton
- Host/HTTP probe and DNS-over-HTTPS utilities (skeleton)
- Local HTTP API server skeleton (opt-in)
- CI pipeline (GitHub Actions) producing APK artifacts

Many advanced features are stubbed to keep the initial build fast and CI-friendly. Extend module stubs under `com.mrclassic.mcv` as needed.

## Build
- Android Studio Ladybug+ (JDK 17)
- No Gradle wrapper checked in; CI uses a managed Gradle. Locally, use Android Studio's Gradle or install Gradle 8.7+.

Steps:
1. Open the project in Android Studio
2. Sync Gradle
3. Run "app" on a device/emulator (Android 8.0+)

## CI (GitHub Actions)
- Workflow at `.github/workflows/android-ci.yml`
- Triggers: push, PR, workflow_dispatch
- JDK 17, builds Debug APK, runs unit tests, optionally signs and uploads release artifact
- V2Ray prebuilt handling (optional): set `V2RAY_PREBUILT_URL` env to a zip with `jniLibs/<ABI>/` layout to include binaries automatically

## V2Ray Binaries
Preferred: provide prebuilt official v2ray-core binaries for ABIs: `armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`.
- Place under `app/src/main/jniLibs/<ABI>/` OR have CI download and copy from an archive via `V2RAY_PREBUILT_URL`.
- Fallback (advanced): build in CI using NDK (not enabled by default).

## Security
- Config encryption: AES-256-GCM with keys in Android Keystore
- Biometric unlock support (opt-in)
- Logging: user opt-in with PII redaction

## Credits
- Author: Mr.Classic — Computer Engineering student from Iraq
- Completion date: 10-08-2025
- First run prompt directs to Telegram: https://t.me/iq_Holy_Space

## License
See [LICENSE](LICENSE).
