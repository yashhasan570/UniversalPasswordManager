#!/usr/bin/env python3
"""Reapply the Universal Password Manager rebrand after an upstream sync."""
from pathlib import Path
import re, shutil, os, glob

SKIP_FILES = {'LICENSE.txt', 'migrate.py'}
SKIP_DIRS = {'.git'}

def sub_file(p: Path, text: str, pairs):
    for old, new in pairs:
        text = text.replace(old, new)
    return text

def phase_a_surgery():
    shutil.rmtree('authenticator', ignore_errors=True)
    sg = Path('settings.gradle.kts')
    if sg.exists():
        s = sg.read_text()
        s = s.replace('    ":authenticator",\n', '')
        s = s.replace('rootProject.name = "Bitwarden"', 'rootProject.name = "UniversalPasswordManager"')
        sg.write_text(s)
    src = Path('app/src/fdroid/kotlin/com/x8bit/bitwarden/data/platform/manager/LogsManagerImpl.kt')
    dst = Path('app/src/standard/kotlin/com/x8bit/bitwarden/data/platform/manager/LogsManagerImpl.kt')
    if src.exists():
        t = src.read_text().replace('F-droid flavor builds', 'standard flavor builds (no crash logging)')
        dst.parent.mkdir(parents=True, exist_ok=True)
        dst.write_text(t)
    for mf in ['app/src/standard/AndroidManifest.xml', 'app/src/standardDebug/AndroidManifest.xml']:
        p = Path(mf)
        if p.exists():
            s = p.read_text()
            s = re.sub(r'\s*<service[^>]*FirebaseMessagingService[\s\S]*?</service>', '', s)
            s = re.sub(r'\s*<!-- Disable Crashlytics for debug builds -->\s*<meta-data[^>]*firebase_crashlytics_collection_enabled[\s\S]*?/>', '', s)
            p.write_text(s)
    svc = Path('app/src/standard/kotlin/com/x8bit/bitwarden/data/push/BitwardenFirebaseMessagingService.kt')
    if svc.exists():
        svc.unlink()
    bg = Path('app/build.gradle.kts')
    if bg.exists():
        s = bg.read_text()
        s = re.sub(r'^import com\.google\.firebase[^\n]*\n', '', s, flags=re.M)
        s = re.sub(r'^import com\.google\.gms[^\n]*\n', '', s, flags=re.M)
        s = re.sub(r'    // Crashlytics is enabled for all builds initially but removed for FDroid builds in gradle and\n    // standardDebug builds in the merged manifest\.\n    alias\(libs\.plugins\.crashlytics\)\n', '', s)
        s = s.replace('    standardImplementation(platform(libs.google.firebase.bom))\n', '')
        s = s.replace('    standardImplementation(libs.google.firebase.cloud.messaging)\n', '')
        s = s.replace('    standardImplementation(libs.google.firebase.crashlytics)\n', '')
        s = re.sub(r'afterEvaluate \{\n    // Disable Fdroid-specific tasks that we want to exclude\n    val fdroidTasksToDisable = tasks\.withType<GoogleServicesTask>\(\) \+\n        tasks\.withType<InjectMappingFileIdTask>\(\) \+\n        tasks\.withType<UploadMappingFileTask>\(\)\n    fdroidTasksToDisable\n        \.filter \{ it\.name\.contains\("Fdroid"\) \}\n        \.forEach \{ it\.enabled = false \}\n\}\n\n', '', s, flags=re.S)
        s = s.replace('    // APK and AAB artifact names when uploading to Firebase and Play Store.\n', '    // APK and AAB artifact names when uploading artifacts.\n')
        bg.write_text(s)
    gs = Path('app/src/standardDebug/google-services.json')
    if gs.exists(): gs.unlink()

def phase_b_moves():
    for d in list(glob.glob('**/com/x8bit/bitwarden', recursive=True)) + list(glob.glob('**/com/bitwarden', recursive=True)):
        if '.git' in d.split('/'): continue
        if d.startswith('authenticator/'): continue
        if 'authenticatorbridge/' in d + '/': continue
        target = d.replace('/com/x8bit/bitwarden', '/com/universalpasswordmanager').replace('/com/bitwarden', '/com/universalpasswordmanager')
        if d == target: continue
        if Path(target).exists():
            for child in Path(d).iterdir():
                shutil.move(str(child), str(Path(target) / child.name))
            shutil.rmtree(d)
        else:
            shutil.move(d, target)
    for d in glob.glob('**/com/x8bit', recursive=True):
        try: Path(d).rmdir()
        except OSError: pass

def phase_c_sweep():
    binary_ext = {'.png','.webp','.jpg','.jpeg','.gif','.ico','.keystore','.jar','.zip','.so','.bin','.ttf','.otf','.woff','.woff2','.mp3','.aab','.apk','.pdf','.svgz'}
    base = [
        ('com.x8bit.bitwarden', 'com.universalpasswordmanager'),
        ('com.bitwarden.', 'com.universalpasswordmanager.'),
        ('com.universalpasswordmanager.authenticatorbridge', 'com.bitwarden.authenticatorbridge'),
        ('com.universalpasswordmanager.authenticator', 'com.bitwarden.authenticator'),
        ('Bitwarden Authenticator', '@@BWA@@'),
        ('bitwarden/android', 'yashhasan570/UniversalPasswordManager'),
    ]
    for root, dirs, files in os.walk('.'):
        dirs[:] = [d for d in dirs if d not in SKIP_DIRS and d != '.github' and d != 'docs']
        for f in files:
            p = Path(root) / f
            if p.name in SKIP_FILES or p.suffix.lower() in binary_ext: continue
            try: text = p.read_text(encoding='utf-8')
            except (UnicodeDecodeError, PermissionError): continue
            original = text
            text = sub_file(p, text, base)
            if '/res/values' in p.as_posix():
                text = text.replace('Bitwarden', 'Universal Password Manager')
            elif p.suffix in ('.md', '.txt'):
                text = text.replace('Bitwarden', 'Universal Password Manager')
            elif p.suffix in ('.kt', '.java', '.xml', '.kts', '.gradle', '.pro', '.properties', '.json', '.yaml', '.yml', '.aidl', '.toml'):
                text = text.replace('Bitwarden', 'Universal')
            text = text.replace('@@BWA@@', 'Bitwarden Authenticator')
            if text != original: p.write_text(text, encoding='utf-8')

def phase_d_renames():
    for root, dirs, files in os.walk('.'):
        dirs[:] = [d for d in dirs if d not in SKIP_DIRS]
        for f in files:
            if 'Bitwarden' in f:
                p = Path(root) / f
                p.rename(p.parent / f.replace('Bitwarden', 'Universal'))

def phase_e_docs():
    for item in ['CONTRIBUTING.md','SECURITY.md','crowdin.yml','Gemfile','Gemfile.lock','docs','fastlane','.claude','.mcp.json','.ruby-version',
                 'scripts/download-artifacts.sh','scripts/get-repo-changelog.sh','scripts/public_suffix_list_generator.sh',
                 'scripts/release-notes.sh','scripts/update-sdk-version.sh','scripts/update_app_ci_build_info.sh']:
        p = Path(item)
        if p.is_dir(): shutil.rmtree(p, ignore_errors=True)
        elif p.exists(): p.unlink()

def write_docs():
    Path('README.md').write_text("""# Universal Password Manager

Password manager app for Android. Store and manage your passwords and passkeys locally, sync with the cloud server you choose, and use autofill across apps and browsers.

Built on open-source code under GPL-3.0.

## Build

```
./gradlew :app:assembleStandardRelease
```

For an F-Droid style build without Google services:

```
./gradlew :app:assembleFdroidRelease
```

## Contact

Telegram: [@johnconstantine570](https://t.me/johnconstantine570)
""")
    Path('PRIVACY.md').write_text("""# Privacy Policy

Universal Password Manager does not collect, transmit, or share any personal data, crash logs, or usage analytics.

All password data stays on your device unless you configure your own server for synchronization.

## Contact

Telegram: [@johnconstantine570](https://t.me/johnconstantine570)
""")

if __name__ == '__main__':
    phase_a_surgery(); phase_b_moves(); phase_c_sweep(); phase_d_renames(); phase_e_docs(); write_docs()
    print('rebrand reapplied')
