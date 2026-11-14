#!/usr/bin/env python3
"""Re-apply the Universal Password Manager rebrand after each upstream sync (idempotent)."""
from pathlib import Path
import os

ROOT = Path(__file__).resolve().parent.parent

# Functional anchors that must never change:
#  - bitwarden.com/.eu/.pw/.net/.gov = the hosted service the app talks to
#  - bitwarden:// = SSO/duo/webauthn deep-link scheme the server registers
#  - com.bitwarden.* = external SDK maven coords, official Authenticator bridge interop,
#    official Authenticator app package, privileged-app allowlist entries
ANCHORS = [
    'bitwarden.com', 'bitwarden.eu', 'bitwarden.pw', 'bitwarden.net',
    'bitwarden-gov.com', 'bitwarden://', 'com.bitwarden',
    'github.com/bitwarden/sdk', 'bitwarden.community',
]
REPL = [
    ('BitwardenFirebaseMessagingService', 'UniversalFirebaseMessagingService'),
    ('bitwarden_', 'universalpasswordmanager_'),
    ('bitwardenSdk', 'universalpasswordmanagerSdk'),
    ('bitwarden-sdk', 'universalpasswordmanager-sdk'),
    ('BW_', 'UNIVERSALPM_'),
    ('Bitwarden', 'Universal Password Manager'),
    ('BitWarden', 'Universal Password Manager'),
    ('BITWARDEN', 'UNIVERSAL PASSWORD MANAGER'),
    ('bitwarden', 'universalpasswordmanager'),
]

def main():
    TEXT_EXTS = {'.kt', '.kts', '.xml', '.md', '.txt', '.toml', '.json', '.gradle', '.pro', '.properties'}
    SKIP = {'.git', 'scripts', 'node_modules'}
    changed = 0
    for dirpath, dirnames, filenames in os.walk(ROOT):
        rel = os.path.relpath(dirpath, ROOT).replace(os.sep, '/')
        dirnames[:] = [d for d in dirnames if rel.split('/')[0] not in SKIP]
        for fn in filenames:
            p = Path(dirpath) / fn
            if p.suffix.lower() not in TEXT_EXTS: continue
            try: s = p.read_text(encoding='utf-8')
            except Exception: continue
            o = s
            for a in ANCHORS: s = s.replace(a, chr(2) + chr(1))
            for a, b in REPL: s = s.replace(a, b)
            s = s.replace(chr(2) + chr(1), '')
            if s != o:
                p.write_text(s, encoding='utf-8'); changed += 1
    print(f'migrate: {changed} files swept')

if __name__ == '__main__':
    main()
