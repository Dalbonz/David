# DAVID 프로젝트 인수인계서
마지막 갱신: 2026-08-22 (v3 — 개발 환경 분리 방식 확정)
목적: 다른 AI 도구 또는 개발자가 이 문서만 읽고 현재 프로젝트를 안전하게 이어서 작업할 수 있게 한다.

> **v2 변경 요약**: 실기기 테스트 성공 기준 명시, 협업 역할(아버지/아들) 섹션 추가, 배포 관련 항목(Google Play 타겟 API 정책)을 별도 섹션으로 분리해 **차후 처리**로 명확히 표시, 빌드 실패 시 흔한 원인 추가, 버전 관리 상태 TBD 표시.
>
> **v3 변경 요약**: 아들(채팅 기반) / 아버지(GitHub Codespaces) 작업 방식 분리를 확정하고, 파일 정본 위치를 `D:\01-개인 파일\엄빠\아빠\AX\DAVID`로 지정. 두 작업 방식 간 동기화 방법(Git) 명시. Gradle 프로젝트 골격(app/wear 모듈) 신규 작성.

> **AI 작업 규칙 (2026-08-22 지정, 모든 세션 공통 적용)**
> 이 프로젝트에서 작업하는 모든 AI 도구(채팅 세션, Codespaces의 Claude Code 등)는 아래 원칙을 따른다.
> 1. **결과를 보고하기 전에 반드시 자체 검증을 거친다** — 코드는 실제로 읽고 확인한 뒤 설명하고, 버전/정책 등 사실관계는 근거(공식 문서, 검색 출처)를 확인한 뒤 말한다.
> 2. **할루시네이션(추측을 사실처럼 말하는 것) 금지** — 확인 못 한 내용은 "확인 필요"라고 명시하고 추측으로 채우지 않는다.
> 3. 위 두 원칙은 이 프로젝트의 표준 운영 규칙이며, 다음 세션에서도 계속 적용한다.

## 1. 프로젝트 목표
**다비드(DAVID)** 는 Galaxy 휴대폰을 본체로 삼고, Galaxy Watch와 Galaxy Buds에서 대화를 시작할 수 있도록 확장하는 개인 AI 비서다. 아버지와 아들이 함께 만드는 학습 겸 개발 프로젝트다.

- 실제 사용 대상 (2026-08-31 확인, 가족 4인 — 만드는 사람은 아버지·아들이지만 쓰는 사람은 넷 다):
  - 휴대폰: 나(아버지) Galaxy Fold 8 / 와이프 Galaxy S24+ / 아들 Galaxy S24 FE / 딸 Galaxy S24 Ultra
  - 워치: 각자 Galaxy Watch4~Watch8 사이로 세대가 다양함
  - 버즈: 각자 Galaxy Buds Pro 2~Pro 4 사이로 세대가 다양함
  - (참고: 이전에 "Galaxy S24 FE 이후"로 뭉뚱그려 적었던 것을 실제 기기 목록으로 정정)
- 비용 원칙 (2026-08-31 갱신): 원래는 "개발·개인 사용 단계는 무료/로컬 우선, AI는 나중에 집 PC의
  Ollama로 연결"이었으나, Gemini(Bixby 내장) 응답 품질이 부족하다는 판단으로 **두뇌는 우선
  Claude API를 쓰기로 변경**함(`ClaudeBrain`, 구현 완료 — ROADMAP.md "확정된 방향" 참고). 종량제라
  무료는 아니지만, 초기엔 사용량이 많다가 안정화될 것으로 예상 중. Ollama는 나중에 사용량이 커지면
  비용 절감용 대안으로 다시 검토할 수 있음.
- 디자인: 특정 상표/로고를 복제하지 않는다. 검정 배경, 오렌지 포인트, 독자적인 원형 로고를 쓴다.
- 개인정보: 기본적으로 로컬 저장을 목표로 한다. 외부 전송이나 영향 있는 행동은 사용자에게 알리고 확인받는다.
- **배포 원칙**: Google Play 등록 등 외부 배포는 **차후 별도 결정 시점**에 다룬다 (10번 섹션 참고). 지금은 사이드로딩(APK 직접 설치)과 개인 기기 테스트만 대상으로 한다.

## 2. 현재 파일 구조
```text
.
├─ CLAUDE.md                  # Claude Code가 세션마다 자동으로 읽는 안내 — "다비드"/"종료" 시동어 정의 (2026-08-24 신규)
├─ .devcontainer/             # Codespaces 컨테이너 생성 시 JDK 17·Android SDK 자동 설치 (2026-08-24 신규)
│  ├─ devcontainer.json
│  └─ setup-android-sdk.sh
├─ app/                       # Android 휴대폰 앱
│  └─ src/main/java/com/david/assistant/
│     ├─ MainActivity.kt      # 권한, Android 음성 인식, TTS 초기화
│     ├─ DavidApp.kt          # Compose 대화 UI와 현재 데모 응답
│     ├─ SimpleRecognitionListener.kt
│     └─ WearRequestListenerService.kt # 워치 요청 수신
├─ wear/                      # Galaxy Watch용 Wear OS 앱
│  └─ src/main/java/com/david/assistant/wear/WearMainActivity.kt
├─ docs/
│  ├─ ROADMAP.md              # 장기 설계와 단계 (2026-08-24 신규 작성)
│  ├─ PROCESS.md              # 세션 시작/종료 절차 — "다비드"/"종료" 시동어 (2026-08-24 신규)
│  ├─ GALAXY_SUPPORT.md       # 기기별 지원 범위와 제한 (아직 미작성 — 확인 필요)
│  ├─ PROGRESS.md             # 날짜별 진행 기록
│  └─ HANDOFF.md              # 이 문서
├─ README.md                  # 처음 실행하는 사람용 안내
├─ CONTRIBUTING.md            # 함께 개발할 때의 규칙
├─ settings.gradle.kts
├─ build.gradle.kts
└─ .gitignore
```

## 3. 구현된 기능 — 코드상 완료
| 영역 | 구현 내용 | 핵심 파일 | 실기기 확인 |
|---|---|---|---|
| 휴대폰 UI | 시작 인사, 대화 목록, 텍스트 입력·전송 | `DavidApp.kt` | 미확인 |
| 음성 입력 | 마이크 권한 요청 후 Android `SpeechRecognizer` 시작 | `MainActivity.kt` | 미확인 |
| 음성 출력 | 한국어 `TextToSpeech`로 답변 읽기 | `MainActivity.kt` | 미확인 |
| 데모 AI | 입력에 따라 간단한 로컬 문자열 답변 | `localReply()` in `DavidApp.kt` | 미확인 |
| 워치 호출 | 워치 버튼 → Wear OS Data Layer 메시지 → 휴대폰 `MainActivity` 열기 | `WearMainActivity.kt`, `WearRequestListenerService.kt` | 미확인 |
| 버즈 | Android 표준 Bluetooth 헤드셋 오디오 경로에 의존 | 별도 전용 코드 없음 | 미확인 |

**중요:** '구현됨'은 소스 파일이 작성된 상태를 뜻한다. **(2026-08-24 갱신)** Codespaces 환경에서 `./gradlew :app:assembleDebug`가 실제로 `BUILD SUCCESSFUL`로 성공하는 것을 확인했다 (JDK 버전 문제와 Android SDK 미설치 문제를 해결— 자세한 내용은 PROGRESS.md의 "2026-08-24 — Gradle assembleDebug 빌드 실패 수정" 참고). 다만 이건 **컴파일 성공만 확인된 것**이며, 위 표의 "실기기 확인" 칸은 여전히 실제 기기/에뮬레이터에서 앱을 설치·실행해본 적이 없어 모두 "미확인" 그대로다.

## 4. 의도적으로 구현하지 않은 기능
- 실제 LLM API 또는 Ollama 통신
- 대화/개인 기억의 영구 저장(Room)
- 워치에서 직접 음성 녹음·답변 표시
- 버즈 터치 동작 재지정
- 백그라운드 상시 웨이크 워드("다비드" 감지)
- 로그인, 클라우드 동기화, 웹 검색, 캘린더/알림 실행

버즈의 제조사 고유 터치 동작이나 시스템 음성 비서 교체는 일반 Android 앱의 공개 API 범위 밖이다. 우회 구현을 시도하지 않는다.

## 5. 기술 설정
- 언어: Kotlin
- UI: Jetpack Compose Material 3 (휴대폰)
- Android 최소 버전: API 26 (Android 8)
- Wear 최소 버전: API 30 (Wear OS 3)
- 컴파일/대상 SDK: 36 (`app/build.gradle.kts`를 직접 읽어 2026-08-24에 확인·정정. 2026-08-22 세션에서 targetSdk를 36으로 올렸지만 이 문서에는 반영되지 않았던 상태였음)
- Java/Kotlin 도구 체인: 17 (2026-08-24, Codespaces에서 JDK 17로 실제 빌드 성공까지 확인함. 기본 JDK가 다른 버전이면 빌드가 즉시 실패하니 주의 — PROGRESS.md 참고)
- Android SDK: Codespaces 컨테이너에 `platform-tools` / `platforms;android-36` / `build-tools;36.0.0`·`35.0.0` 설치 확인 (2026-08-24). `.devcontainer/devcontainer.json` + `.devcontainer/setup-android-sdk.sh`로 새 Codespace 생성 시 자동 설치되도록 구성함. `setup-android-sdk.sh`는 빈 `ANDROID_HOME`에 대해 처음부터 끝까지 실제 실행해 검증 완료(그 과정에서 `yes | sdkmanager --licenses`가 `pipefail`과 충돌해 죽는 버그를 발견해 수정함). 다만 `devcontainer.json`의 Java feature 자체는 실제 새 Codespace 생성으로는 아직 확인 못함 — **확인 필요**
- 워치 통신: Google Play services Wearable Data Layer
- 패키지 ID: 휴대폰과 워치 모두 `com.david.assistant` (Data Layer 페어링 목적)
- 버전 관리: **TBD** — Git 저장소/원격 백업 여부를 다음 작업자가 확인 후 이 줄을 갱신할 것

## 6. 다음 개발자가 가장 먼저 할 일 (성공 기준 포함)
각 단계는 완료 시 PROGRESS.md에 성공/실패를 기록한다 (11번 형식 참고).

1. Android Studio에서 이 폴더를 연다. **성공 기준**: Gradle sync가 에러 없이 끝난다.
2. SDK 35와 JDK 17을 설치/선택한다. **성공 기준**: `File > Project Structure`에서 두 값이 모두 인식됨.
3. 휴대폰 `app` 구성을 먼저 빌드한다. **성공 기준**: APK가 생성되고 설치까지 성공. 오류가 있으면 오류 내용과 수정 사항을 `PROGRESS.md`에 기록한다.
4. Galaxy S24 FE 이상에서 텍스트 입력 → 음성 버튼 → 음성 응답을 확인한다. **성공 기준**: (a) 텍스트 입력 시 데모 응답이 대화 목록에 표시됨, (b) 마이크 버튼 클릭 시 권한 팝업 또는 인식 시작, (c) TTS 응답이 스피커로 재생됨. 이 중 하나라도 실패하면 어느 항목인지 구체적으로 기록.
5. 같은 Google 계정과 Bluetooth/Wi-Fi로 페어링된 Watch6 이상에 `wear` 구성을 설치해 워치 호출을 확인한다. **성공 기준**: 워치 버튼 클릭 → 휴대폰 화면이 열림 (또는 최신 Android 백그라운드 제한으로 실패 시 7번 참고).
6. 위 확인이 끝난 후에만 Ollama 연결 또는 Room 기억 기능을 추가한다.

## 7. 협업 역할 (아버지 · 아들) — 제안, 협의 후 확정
> 이 섹션은 실제 역할 분담이 정해지지 않아 초안으로 제안한다. 두 분이 논의해 확정한 뒤 이 표를 수정해서 쓰는 것을 권장.

| 역할 후보 | 담당(제안) | 내용 |
|---|---|---|
| 빌드/환경 설정 | 아버지 | Android Studio, SDK, Gradle 등 초기 설정 |
| 실기기 테스트 & 피드백 | 아들 | 3~6번 단계의 버튼 클릭, 음성 응답 확인, "이상해요/좋아요" 같은 사용성 피드백 |
| UI/디자인 아이디어 | 아들 | 색상, 로고, 대화 화면 레이아웃 아이디어 제시 (오렌지 포인트 원칙 안에서) |
| 코드 작성 | 아버지 | Kotlin 로직, LLM/Ollama 연동, 데이터 저장 |
| 진행 기록(PROGRESS.md) | 공동 | 매 세션 종료 시 함께 정리 |

## 8. 주의할 기술 사항
- 현재 워치 수신 서비스는 휴대폰 화면을 여는 가장 단순한 초기 구현이다. 최신 Android의 백그라운드 활동 시작 제한 때문에 실기기에서 실패하면 알림의 탭 동작으로 바꾸는 것이 안전하다.
- Android `SpeechRecognizer`는 기기와 설치된 언어팩에 따라 네트워크를 사용할 수 있다. 완전 오프라인 음성은 별도 엔진(예: Vosk/Whisper.cpp)을 검토해야 한다.
- `wear` 앱의 배포와 설치는 Play Console/Android Studio 설정에 따라 별도 구성이 필요할 수 있다. Data Layer는 같은 애플리케이션 ID와 서명된 페어링 앱을 전제로 한다. (필수는 아니지만 표준 관행)
- API 키, 개인 대화, 녹음 파일, 서명 키는 Git에 올리지 않는다.
- **빌드 첫 시도 시 흔한 실패 지점** (신규): Gradle/AGP(Android Gradle Plugin) 버전 불일치, 로컬 `local.properties`의 SDK 경로 누락, JDK 17이 아닌 다른 버전이 기본으로 잡혀있는 경우. 오류 로그의 첫 줄을 그대로 PROGRESS.md에 남기면 다음 작업자가 빠르게 원인을 좁힐 수 있다.

## 9. 미검증 사항 (문서 vs 실제 코드)
이 문서는 소스 코드 내용을 요약한 것이며, 이번 갱신에서도 실제 `.kt`/`build.gradle.kts` 파일을 재열람하지 않았다. 아래는 다음 작업자가 실제 코드로 재확인해야 할 항목이다.
- `build.gradle.kts`의 실제 의존성 버전 (Compose, Wearable, SpeechRecognizer 관련 라이브러리 등)
- Git 저장소 초기화 여부 및 원격 저장소 주소

## 10. 배포 준비 (차후 처리 — 지금은 진행하지 않음)
Google Play 등록은 현재 로드맵에 없다. 다만 **나중에 등록을 검토할 때** 아래 사항이 걸림돌이 될 수 있어 미리 기록해둔다.

- Google Play는 2026년 8월 31일부터 신규 앱·업데이트 앱에 **Android 16(API 36) 이상 타겟**을 요구한다 (연장 신청 시 11월 1일까지 유예 가능). 휴대폰 `app` 모듈(현재 targetSdk 35)은 Play 스토어 배포를 결정하는 시점에 36으로 올려야 한다.
- `wear` 모듈은 Wear OS 앱에 대한 완화된 기준(API 34~35대)이 적용되므로 지금 수준으로도 당분간 문제 없음.
- **지금 해야 할 일은 없음.** 사이드로딩·개인 테스트 단계에는 영향이 없다. 이 섹션은 "배포하기로 결정한 날" 다시 펼쳐볼 체크리스트로만 남겨둔다.
- 출처: Google Play Console Help, "Target API level requirements for Google Play apps" (2026-08-22 확인).

## 12. 개발 환경 분리 방식 (2026-08-22 결정)
아버지와 아들이 서로 다른 도구로 같은 프로젝트를 다룬다. 아래 원칙으로 운영한다.

| 사람 | 도구 | 역할 |
|---|---|---|
| 아들 | Claude 채팅(Cowork) | 개념 학습, 디자인/UI 아이디어, 코드 리뷰를 대화로 진행. 이 폴더의 문서를 직접 읽고 쓸 수 있음 |
| 아버지 | GitHub Codespaces | 실제 Kotlin 코드 작성, Gradle 빌드, 디버깅 등 무거운 개발 작업 |
| 파일 정본(source of truth) 위치 | 로컬: `D:\01-개인 파일\엄빠\아빠\AX\DAVID` | 두 작업 방식이 최종적으로 합쳐지는 곳 |

**중요 — 동기화 방법**: GitHub Codespaces는 클라우드 VM으로, 로컬 `D:\...\DAVID` 폴더를 자동으로 보지 못한다. Codespaces는 GitHub 저장소에 붙어서 동작하므로, 아래 흐름으로 동기화해야 파일이 어긋나지 않는다.

1. `D:\...\DAVID`를 Git 저장소로 초기화하고 GitHub에 (비공개) 원격 저장소로 push 해둔다.
2. 아버지가 Codespaces에서 작업 → 커밋 → 원격 저장소에 push.
3. 채팅(아들과의 세션)에서 작업하기 전, 로컬 폴더를 `git pull`로 최신화한 뒤 시작한다. (현재는 Claude가 로컬 폴더 파일을 직접 읽고 쓸 수 있지만 Git 명령 자체는 아직 이 세션에서 실행 검증 전 — 13번 '다음 확인 사항' 참고)
4. 채팅에서 파일을 바꾸면 → 로컬에 반영 → 아버지가 다음 Codespaces 세션 전에 `git pull`로 받아간다.

이 순서를 지키지 않으면 "아들과 대화하며 바꾼 내용"과 "아버지가 Codespaces에서 바꾼 내용"이 서로 덮어써질 수 있다.

**세 작업 지점 (2026-08-24 확정, 2026-08-31 아버지 쪽 기기 3갈래로 구체화)** — 아들은 이 채팅(Cowork)에서 대화 기반으로 참여하고, 아버지는 기기에 따라 아래 세 가지 방식 중 하나로 같은 GitHub Codespaces 환경에 접속한다. **GitHub 저장소(`github.com/Dalbonz/David`)가 유일한 정본(source of truth)** 이며, 모든 지점은 아래 원칙으로 연결된다.

| 지점 | 접속 방식 | 무엇을 하든 시작할 때 | 무엇을 하든 끝낼 때 |
|---|---|---|---|
| 아버지 — PC | Claude Code (이 세션이 실행 중인 방식) | `git pull`로 최신화 | `docs/PROGRESS.md` 기록 → commit → push |
| 아버지 — 태블릿 | 브라우저 기반 웹 Codespaces | 〃 | 〃 |
| 아버지 — 폰 | 브라우저 기반 웹 Codespaces (태블릿과 동일 조건) | 〃 | 〃 |
| 아들 — 이 채팅 | Claude 채팅(Cowork) | GitHub의 `docs/HANDOFF.md`, `docs/PROGRESS.md`를 먼저 읽고 현재 상태 파악 (저장소가 Public이라 인증 없이 읽기 가능) | 변경 파일을 사용자에게 전달 → PC 연결 시 로컬 폴더에 직접 반영, 연결 안 될 시 파일로 전달 후 사용자가 Codespaces에 업로드·push |
| 로컬 PC 폴더 (`D:\...\DAVID`) | — | — | 위의 변경을 받는 미러. PC가 연결 안 될 때는 이 폴더를 거치지 않고 GitHub로 직접 주고받는다 |

아버지 쪽 세 기기(PC/태블릿/폰)는 접속 방식만 다를 뿐 모두 같은 Codespaces 환경·같은 GitHub 저장소를 보므로, 절차상 구분 없이 동일하게 "시작 시 `git pull`, 종료 시 커밋·push"만 지키면 된다. 즉 "누가 어느 기기에서 작업하든 먼저 GitHub 최신 상태를 확인하고, 끝나면 GitHub에 반영한다"가 원칙이다.

**시동어 (2026-08-24 결정)**: 이 원칙을 실제 대화에서 쓰기 쉽게 "다비드"(시작)/"종료"(종료)
한 쌍의 말로 정했다. 절차의 자세한 내용은 `docs/PROCESS.md` 참고. Codespaces/Claude Code
세션에서는 `CLAUDE.md`가 이 시동어를 자동으로 인식하도록 연결돼 있다. 채팅(Cowork) 세션은
자동 인식이 안 돼서, 아들이 대화 시작·종료 시 직접 "다비드"/"종료"를 말하며 `docs/PROCESS.md`를
따르도록 안내해야 한다 — Cowork에 이 절차를 자동으로 걸어둘 방법이 있는지는 확인 필요.

## 13. 다음 확인 사항 (신규)
- `D:\...\DAVID` 폴더가 현재 비어 있음 — 기존에 작성됐다고 알려진 `.kt` 소스 파일들(`MainActivity.kt`, `DavidApp.kt` 등)의 실제 위치를 확인해서 이 폴더로 옮기거나, 여기서 새로 시작할지 결정 필요.
- GitHub 저장소 생성 여부 및 이름 확정 (Codespaces를 쓰려면 필수).
- 로컬 폴더에서 Git 명령이 정상 동작하는지 확인 (이번 세션에서는 문서 파일만 생성했고 git init/push는 아직 실행하지 않음).

## 14. 기록 규칙
작업을 마칠 때마다 [PROGRESS.md](PROGRESS.md)에 아래 형식으로 추가한다.
```md
## YYYY-MM-DD — 작업 제목
- 한 일:
- 변경한 파일:
- 확인: (성공/실패, 기기/환경)
- 결정 또는 위험:
- 다음:
```
이 문서의 '구현된 기능', '의도적으로 구현하지 않은 기능', '다음 할 일'도 실제 상태가 달라지면 함께 갱신한다.
