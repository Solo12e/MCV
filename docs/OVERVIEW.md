## MCV Architecture Overview

Modules:
- app: UI + Service + Managers
- core: VPN service skeleton and process managers
- security: Keystore-backed crypto helpers
- inspection: Host/IP tools
- api: Local HTTP API server

Data:
- Room DB for logs/metadata
- Encrypted file storage for configs

Native:
- v2ray-core expected via prebuilt binaries in `app/src/main/jniLibs/<ABI>/v2ray`

CI:
- GitHub Actions builds APK, runs tests, uploads artifacts, optional signing and release