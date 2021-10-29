package com.sparrow.pay.config.Response;



public class ResponseMessage {

    public static final String PAY_SUCCESS="결제가 성공적으로 처리되었습니다.";
    public static final String PAY_FAIL="결제 오류가 발생했습니다.";
    public static final String PAY_CANCEL_SUCCESS="결제 취소가 정상적으로 처리되었습니다";
    public static final String PAY_NOT_FOUND="기존 결제 정보를 찾을 수 없습니다.";



    public static final String PAY_CANCEL_EXCEED="취소금액이 결제금액을 초과했습니다.";
    public static final String PAY_CANCEL_VAT_EXCEED="취소부가세가 결제부가세를 초과했습니다.";
    public static final String VAT_EXCEED_PAY ="부가세가 결제금액을 초과했습니다.";
    public static final String PAY_CANCEL_FAIL="결제 취소 오류가 발생했습니다.";

    public static final String PAY_FIND_SUCCESS="결제 정보 조회에 성공했습니다.";
    public static final String PAY_FIND_FAIL="결제 정보 조회 오류가 발생했습니다.";




}
