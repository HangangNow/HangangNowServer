## 한강나우 백엔드 서버
<img width="800" alt="스크린샷 2022-10-10 오후 4 45 23" src="https://user-images.githubusercontent.com/68818952/196185488-1dce56f6-7617-40fa-a478-199aacb065d9.png">

한강 나들이에 필요한 모든 지식을 담은 **한강나우**
  
> 🏞 한강의 현재 상황을 한 눈에 볼 수 있어요. 날씨와 비교하여 옷차림을 추천 받고, 주차장 혼잡도 등을 통해 한강 공원의 실시간 상황을 만나보세요.
  
> 🤷 어떤 한강공원으로 놀러갈지 모르는 사람들을 위해, 사용자 기반 한강공원 맞춤형 코스를 추천해드립니다.

> ⭐️ 한강 전단지와 축제, 공연, 이벤트 정보를 한눈에 모아볼 수 있도록 준비했습니다.
  
> 🌇 한강 공원에 존재하는 화장실, 편의점, 자전거 대여소 등 당신이 필요로 하는 모든 주변시설의 위치를 보여드립니다.

> 💁🏻‍♂️ 한강유형검사를 통해 나의 한강유형을 파악해보고, 한강에서의 하루를 기록해보아요.

## 개발 인원 및 기간
* 개발기간: 2022/06/15 ~ 2022/09/02
* 개발 인원: 기획 3명, 디자이너 1명, 프론트엔드 1명, 백엔드 2명, 크롤링 1명


## 서버 구조도
<img width="800" alt="스크린샷 2022-10-17 오후 10 06 15" src="https://user-images.githubusercontent.com/68818952/196185022-15451621-96ce-4a34-a45b-40aacd699f5c.png">



## 시연 영상

[![한강나우 탭](http://img.youtube.com/vi/EoTKRspuEEo/0.jpg)](https://youtu.be/EoTKRspuEEo)


[![나들이 탭](http://img.youtube.com/vi/dEr99z0eS8Y/0.jpg)](https://youtu.be/dEr99z0eS8Y)


[![홈 탭](http://img.youtube.com/vi/0oXQWhXSpQE/0.jpg)](https://youtu.be/0oXQWhXSpQE)


[![주변시설 탭](http://img.youtube.com/vi/AV9CYgyf2ME/0.jpg)](https://youtu.be/AV9CYgyf2ME)


[![마이페이지 탭](http://img.youtube.com/vi/IcvUSecQ45M/0.jpg)](https://youtu.be/IcvUSecQ45M)

## 기술 스택
* Java: 11
* Spring Boot: 2.5.8
* MariaDB: 10.6.10
* Redis: 7.0.4
* JPA: 2.5.8
* AWS EC2, S3


## 기능 소개
```
- 로그인
AuthenticationProvider를 이용해 일반 로그인, 카카오 로그인 2가지 각각 구현해 적용
사용자 ID/PW를 전달받아 멤버 인증에 성공한 후, JWT accessToken을 클라이언트에게 전달
```

```
- 사용자 인증
클라이언트는 전달받은 토큰 값을 모든 request header에 담아 요청
클라이언트에게 받은 accessToken이 만료된 경우, refreshToken의 기간을 확인해 토큰 재발급
Refresh Token과 자동로그인 여부를 확인해 TTL을 redis에 key, value로 저장
```

```
- 이메일 인증
회원가입, 아이디 찾기, 비밀번호 찾기 과정에서 사용자 인증을 위해 사용
스프링에서 제공하는 JavaMailSender를 이용해 구현
MailSender를 이용해도 되지만, html template를 활용할 수 있는 장점 때문에 JavaMailSender 선택
사용자 이메일과, 이메일 TTL을 redis에 key, value로 저장해, 클라이언트에서 받은 코드로 인증 여부 결정
```

```
- 회원 PK 전략
기존에는 Long 타입의 autoIncrement 방식으로 사용하였으나, URL에서 회원의 PK를 쉽게 유추할 수 있을 수 있다고 판단하여 UUID 타입으로 변경
그러나 UUID 타입의 PK를 데이터베이스에서 탐색하게 되면 정렬이 되어 있지 않기 때문에 탐색 오버헤드가 커짐
Long 타입에 비해 저장공간이 더 필요하고 그로인한 인덱스 메모리 증가 이슈가 있지만, 사용자의 PK인 만큼 보안성이 중요하다고 판단
따라서 보안과 데이터베이스 성능을 고려해 unixtime을 기반으로 한 sequential UUID 사용
```

```
- 한강공원 데이터 동기화
Open API 동기화 및 크롤링 자동화를 위한 별도의 EC2 서버 구현
스프링의 @EnableScheduling, @Scheduled를 이용해 자동화
```


## Rest API Docs
* [HangangNOW REST API Docs](http://ec2-13-124-170-69.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/)

