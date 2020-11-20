![version](https://img.shields.io/badge/ver-2.1-e4ff4d)
![language](https://img.shields.io/badge/language-Kotlin-9cf)
![비동기처리](https://img.shields.io/badge/%EB%B9%84%EB%8F%99%EA%B8%B0%EC%B2%98%EB%A6%AC-CoRoutine-6054d1)
![network](https://img.shields.io/badge/network-Jsoup-yellow)
![image](https://img.shields.io/badge/image-Picasso-edfcd2)

# Android_News_Refactoring
First Android Project with Kotlin.

News List Application with RSS parsing.

## History
* ver 1.0 - 2020.03.20 ~ 2020.03.31
* ver 2.0 - 2020.08
* ver 2.1 - 2020.11 (UI 수정 및 SharedElementsTransition 적용)

## Basic Information
2020년 3월, 안드로이드 챌린지로 참여했던 뉴스 목록 애플리케이션 + **리팩토링**

**기술 스택** : 
* `Android Studio`
* (언어) `Kotlin`
* (비동기) `CoRoutine`
* (네트워크) `Jsoup`
* (이미지처리) `Picasso`

**주요 내용** : 
- Kotlin을 이용한 안드로이드 챌린지 참여 작품으로, Kotlin을 사용하여 제작한 첫 애플리케이션. 
- 주어진 조건(RSS읽어들이기, 뉴스 목록과 이미지 보이기, meta-data를 통해 contents를 읽어와 분석하여 키워드 3개 추출 등)을 맞춰 개발된 애플리케이션.


## ver 2.0 리팩토링 주요 내용
1. 기존 비동기 처리 방식 변경 (`AsyncTask` -> `CoRoutine`)
2. 변수명 클린하게 바꾸려고 노력중
3. 객체 지향 언어처럼 써버린 Kotlin코드를 최대한 함수형 언어다운 코드로 바꾸기
4. 중복된 코드 최대한 줄이기

## ver 2.1 screen
<image src="./images/ver2_gif_1.gif" width=260>

## ver 2.0 screen
<image src="./images/ver2_screen_0.png" width=700 />

## ver 1.0 screen
<image src="./images/ver1_screen_0.png" width=700 />
