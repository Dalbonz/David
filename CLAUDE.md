# DAVID 프로젝트 — Claude Code 안내

이 프로젝트는 아버지(Codespaces/Claude Code)와 아들(Claude 채팅)이 번갈아 작업하며
GitHub(`github.com/Dalbonz/David`, main 브랜치)을 정본으로 동기화한다.

## 시동어 한 쌍: "다비드" / "종료"

`docs/PROCESS.md`에 정의된 절차를 그대로 따른다.

- 사용자가 **"다비드"**라고 말하면 (세션 시작):
  1. `git pull`로 최신화
  2. `docs/HANDOFF.md`, `docs/PROGRESS.md`, `docs/ROADMAP.md`를 순서대로 읽고 현재 상태 파악
  3. 파악한 상태를 짧게 요약해 사용자에게 확인받은 뒤 작업 시작
- 사용자가 **"종료"**라고 말하면 (세션 종료):
  1. `docs/PROGRESS.md`에 새 항목 기록(HANDOFF.md 14번 형식) → 필요하면
     `docs/HANDOFF.md`/`docs/ROADMAP.md`도 갱신
  2. commit → push
  3. 저장·push가 실제로 끝났는지 사용자에게 확인시켜준다

## 항상 지킬 것 (AI 작업 규칙)
`docs/HANDOFF.md` 상단 "AI 작업 규칙" 참고 — 요약하면:
- 보고하기 전에 실제로 코드를 읽거나 명령을 실행해서 검증한다. 추측으로 말하지 않는다.
- 확인하지 못한 내용은 "확인 필요"라고 명시한다.

## 더 읽을 것
- 협업 구조와 각 지점의 역할: `docs/HANDOFF.md` 12번
- 장기 방향(모듈형 아키텍처, 음성 우선, 자비스 지향)과 아직 정해지지 않은 것: `docs/ROADMAP.md`
