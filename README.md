# Android_News_Refactoring
First Android Project with Kotlin

## Basic Information
예전에 안드로이드 챌린지로 참여했던 뉴스목록을 보여주는 애플리케이션 **리팩토링** 시도.

기획 및 개발 기간 : 2020.03.20 ~ 2020.03.31

리팩토링 기간 : 2020.08.03 ~ 2020.08.03

기술 스택 : 
* `Android Studio`
* (언어) `Kotlin`
* (비동기) `CoRoutine`
* (네트워크) `Jsoup`
* (이미지처리) `Picasso`

주요 내용 : 
- Kotlin을 이용한 안드로이드 챌린지 참여 작품으로, Kotlin을 사용하여 제작한 첫 애플리케이션. 
- 주어진 조건(RSS읽어들이기, 뉴스 목록과 이미지 보이기, meta-data를 통해 contents를 읽어와 분석하여 키워드 3개 추출 등)을 맞춰 개발된 애플리케이션.

## 리팩토링 주요 기능
1. 기존 비동기 처리 방식 변경 (`AsyncTask` -> `CoRoutine`)
2. 변수명 클린하게 바꾸려고 노력중
3. 객체 지향 언어처럼 써버린 Kotlin코드를 최대한 함수형 언어다운 코드로 바꾸기
4. 중복된 코드 최대한 줄이기

## 현재 screen
<image src="./cur-screen.png" width=800 />

## 리팩토링 하기 전 screen
<image src="./pre-screen.png" width=800 />
