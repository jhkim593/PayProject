## 개발 환경 

- InteliJ 2021.2.2

- Spring boot 2.5.5

- Postgresql 

<br>



## table




<img src="https://user-images.githubusercontent.com/53510936/138397771-2779c640-c528-492c-b89e-fc537c7ae5cf.png"  width="250" height="350" style="margin-left: auto; margin-right: auto; display: block;"/>



- payId : 관리번호

- data :  카드사로 전송되는 String 데이터
- parentPay : 취소의 경우 원 거래 테이블을 부모로 가짐



<br>



## 결제 

- `Post` /pay

- Request
  ~~~json
  {
      "cardNum":"12222222222",
      "expirationDate": "1111",
      "cvc": "222",
      "installmentMonth" :9,
      "price" : 10000,
      "vat": 1000
  }
  ~~~

- Response

  ~~~json
  {
      "responseMessage": "결제가 성공적으로 처리되었습니다.",
      "isSuccess": true,
      "data": "53823392594814455731"
  }  
  ~~~

<br>



## 결제 취소

- `post` /pay/cancel

- Request

  ~~~json
  {
      "payId":"18821010485878521872",
      "cancelPrice": 1088,
      "vat": 500
  }
  ~~~

- Response

  ~~~json
  {
      "responseMessage": "결제 취소가 정상적으로 처리되었습니다",
      "isSuccess": true,
      "data": "62607357309746674335"
  }
  ~~~

<br>



- 결제 취소시 예외 설정

  - 기존 결제 정보 존재 하지 않을 때
  - 취소 금액  > 원 결제 금액
  - 취소 부가세> 원 결제 부가세
  - 부가세 > 결제금액

  

<br>



## 결제 정보 조회

- `Get` / post /{ id }   ( id : 관리번호)   

- Response

  ~~~json
  {
      "responseMessage": "결제 정보 조회에 성공했습니다.",
      "isSuccess": true,
      "data": {
          "payId": "98093362544390115682",
          "cardInfo": {
              "cardNum": "122222**222",
              "expirationDate": "1111",
              "cvc": "222"
          },
          "type": "PAYMENT",
          "priceInfo": {
              "price": 10000,
              "vat": 1000
          }
      }
  }
  ~~~



- 결제 정보 조회 시 예외 설정
  - 기존 결제정보가 존재 하지 않을 때



<br>



##  문제 해결

- 카드정보 암호화를 위해 AES 암호화 방식 사용
- Request data 유효성 검증
- String 데이터 명세와 일치를 위해 String.format을 사용
- JPA PESSIMISTIC Lock을 이용해 원 결제건에서 결제 취소 동시성 방어


---


## Git flow

<img src="https://user-images.githubusercontent.com/53510936/138382890-25ef69db-dbcc-439e-96a0-689429f16505.png"  width="550" height="350" style="margin-left: auto; margin-right: auto; display: block;"/>

- master :  기준 브랜치 제품을 배포
- develop :  개발 브랜치이며 , 이 브랜치를 기준으로 각 기능들을 Merge
- feature : 처리할 기능 단위로 개발
- release :  develop에서 작업한 내용을 테스트 
