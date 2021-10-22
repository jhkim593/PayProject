

## 개발 환경 

- Spring boot 2.5.5

- Postgresql 
- InteliJ 



## table



<img src="https://user-images.githubusercontent.com/53510936/138392552-89f3b6e6-0371-4441-9f62-9727919656a9.png"  width="250" height="350" style="margin-left: auto; margin-right: auto; display: block;"/>



- payId : 관리번호

- data :  카드사로 전송되는 String 데이터
- parentPay : 취소의 경우 원 거래 테이블을 부모로 가짐



## Git flow

<img src="https://user-images.githubusercontent.com/53510936/138382890-25ef69db-dbcc-439e-96a0-689429f16505.png"  width="550" height="350" style="margin-left: auto; margin-right: auto; display: block;"/>

- master : 
- develop
- feature : 기능 개발
- release :  develop에서 작업한 내용을 테스트 



##  문제 해결

- 카드정보 암호화를 위해 AES 암호화 방식 사용
- Request data 유효성 검증
- String.format을 사용해 
-  JPA PESSIMISTIC Lock을 이용해 원 결제건에서 결제 취소 동시성 방어

