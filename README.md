- ## 사용 기술 

  - Spring boot 2.5.5

  - Postgresql 

  - JPA


  <br>

  ## Class Diagram
  <img src="https://user-images.githubusercontent.com/53510936/139219863-dfe77cc0-6504-48ed-8f45-6f602002c418.png"  width="900" height="600" style="margin-left: auto; margin-right: auto; display: block;"/>

  <br>

  

  

  ## Entity


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
        "payId":"02084585697560479073",
        "cancelPrice": 100,
        "vat": 5
    }
    ~~~

  - Response

    ~~~json
     {
        "responseMessage": "결제 취소가 정상적으로 처리되었습니다",
        "isSuccess": true,
        "data": {
            "payId": "43490876626115834120",
            "oriPayId": "02084585697560479073",        
            "oriPrice": 767,
            "oriVat": 4
        }
    }
    ~~~
    원 결제 관리 번호와 결제 취소 후 금액, 부가세를 응답 데이터로 전송

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



<br>


  - 결제 정보 조회 시 예외 설정
    - 기존 결제정보가 존재 하지 않을 때

  

  <br>

  

  ##  문제 해결

  - 카드정보 암호화를 위해 AES 암호화 방식 사용

  - Jasypt를 이용해 DB정보 및 AES SecretKey 암호화

  - Request data 유효성 검증

    ~~~java
    public class PayRequestDto {
    
        @NotBlank
        @Pattern(regexp = "[0-9]{10,16}" ,message = "10~16자리 숫자를 입력해주세요.")
        private String cardNum;
    
        @NotBlank
        @Pattern(regexp = "[0-9]{4}",message = "4자리 숫자를 MMYY 형식으로 입력해주세요.")
        private String expirationDate;
    
        @NotBlank
        @Pattern(regexp = "[0-9]{3}",message = "3자리 숫자를 입력해주세요.")
        private String cvc;
    
        @Min(0)@Max(12)
        @NotNull
        private Integer installmentMonth;
    
        @Min(100)@Max(1000000000)
        @NotNull
        private Long price;
    
        private Long vat;
    
    }
    ~~~

    

  - ControllerAdvice를 이용한 Exception처리

    ~~~java
    @RestControllerAdvice
    public class ExceptionAdvice {
        
          @ExceptionHandler(PayNotFoundException.class)
          protected ResponseEntity payNotFoundException(HttpServletRequest request, PayNotFoundException e){
              return new ResponseEntity(DefaultResponse.res(ResponseMessage.PAY_NOT_FOUND,false), HttpStatus.NOT_FOUND);
    
     }
    
    ~~~

    

  - JPA PESSIMISTIC Lock을 이용해 원 결제건에서 결제 취소 동시성 방어

    ~~~java
    public interface PayRepository extends JpaRepository<Pay,Long> {
        
          @Lock(LockModeType.PESSIMISTIC_WRITE)
          Optional<Pay> findByPayId(String payId);
        
    }
    
    ~~~


  <br>


  ## Git flow

  <img src="https://user-images.githubusercontent.com/53510936/138382890-25ef69db-dbcc-439e-96a0-689429f16505.png"  width="550" height="350" style="margin-left: auto; margin-right: auto; display: block;"/>

  - master :  기준 브랜치이며, 제품을 배포
  - develop :  개발 브랜치이며 , 이 브랜치를 기준으로 각 기능들을 Merge
  - feature : 처리할 기능 단위로 개발
  - release :  develop에서 작업한 내용을 테스트 
