# PocketManager


화면
1. 일정 (월)
  -한 달 동안의 일정을 간략하게 보여줌
  
2. 일정 (주)
  -한 주 동안의 일정을 간략하게 보여줌

3. 홈
  -하루 일정, 날씨, 교통

3. 날씨
  -온도 체감온도 습도 날씨 풍속 강수량 강설량 미세먼지
  -오늘, 내일, 7일

5. 지도
  -하루 이동경로를 지도상으로 보여줌
  
6. ----------------------------------------------------


기능
1. 일정을 보고 길찾기를 실행해 출발시간을 알려준다
2. 늦지 않게하기 위해 타야하는 지하철 혹은 버스를 알려준다
3. 일정을 추가, 수정할수 있게 하고 설명도 추가할 수 있게 한다
4. 필요한 경우, 외출  30분 전 긴급 날씨(비, 심한 일교차, 눈, 강풍 등)를 알람으로 알려준다
5. 에타 정보를 일정으로 자동으로 변환해준다
6. 구글 캘린더 연동?


요구사항 1
UI - 창 만들고, 인터페이스랑 연결
1. 월 일정탭에 이벤트 출력
2. 주 일정탭에 이벤트 출력
3. 홈 화면 교통, 날씨, 하루 일정 출력
4. 이벤트 추가 구현 (에타)
6. 온도 반올림

요구사항 2
1. 이벤트에 따른 날씨 API로 가지고 오기
2. GPS 유저가 PERMISSION을 안 주었을때 날씨탭 오류 해결
3. 알람 기능 추가 (미세먼지, 날씨, 일교차)

요구사항 3
1. 지도화면 구현
2. 지도화면에 이동경로 출력
3. 에타 시간표 자동 삽입
4. 에타 로그인 정보 다루기
5. 교통 구하고 이벤트에 추가


