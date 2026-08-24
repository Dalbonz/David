#!/usr/bin/env bash
# Installs the Android SDK components this project needs and points the
# Gradle project at them (local.properties). Runs as the devcontainer
# postCreateCommand — see .devcontainer/devcontainer.json.
#
# Re-derived from the manual steps that fixed the 2026-08-24 build failure
# (see docs/PROGRESS.md, "2026-08-24 — Gradle assembleDebug 빌드 실패 수정").
set -euo pipefail

ANDROID_HOME="${ANDROID_HOME:-$HOME/android-sdk}"
CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-13114758_latest.zip"

if [ ! -x "$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" ]; then
  echo "[setup-android-sdk] Installing Android cmdline-tools into $ANDROID_HOME"
  mkdir -p "$ANDROID_HOME/cmdline-tools"
  tmp_zip="$(mktemp)"
  curl -sL -o "$tmp_zip" "$CMDLINE_TOOLS_URL"
  unzip -q "$tmp_zip" -d "$ANDROID_HOME/cmdline-tools"
  # The zip contains a top-level "cmdline-tools" dir; the SDK expects it at .../cmdline-tools/latest
  mv "$ANDROID_HOME/cmdline-tools/cmdline-tools" "$ANDROID_HOME/cmdline-tools/latest"
  rm -f "$tmp_zip"
fi

SDKMANAGER="$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager"

echo "[setup-android-sdk] Accepting SDK licenses"
# `yes` gets SIGPIPE (exit 141) once sdkmanager stops reading input; with
# `set -o pipefail` that alone would fail the pipeline even though license
# acceptance succeeded, so it's explicitly not treated as a failure here.
yes | "$SDKMANAGER" --sdk_root="$ANDROID_HOME" --licenses > /dev/null || true

echo "[setup-android-sdk] Installing platform-tools, platform 36, build-tools 36.0.0/35.0.0"
"$SDKMANAGER" --sdk_root="$ANDROID_HOME" \
  "platform-tools" \
  "platforms;android-36" \
  "build-tools;36.0.0" \
  "build-tools;35.0.0"

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
echo "sdk.dir=$ANDROID_HOME" > "$REPO_ROOT/local.properties"

echo "[setup-android-sdk] Done. ANDROID_HOME=$ANDROID_HOME, local.properties written."
