package com.kshrd.kroya_api.exception.constand;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage implements IResponseMessage {
    FETCH_SUCCESS("0000", "데이터 조회에 성공하였습니다."),
    FETCH_FAIL("9999", "데이터 조회에 실패하였습니다."),
    SAVE_SUCCESS("9999", "데이터 저장에 성공하였습니다."),
    SAVE_FAIL("9999", "데이터 저장에 실패하였습니다."),
    DELETE_SUCCESS("9999", "데이터 삭제에 성공하였습니다."),
    DELETE_FAIL("9999", "데이터 삭제에 실패하였습니다."),
    NOT_FOUND("404", "데이터가 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR("9999", "서버에 오류가 발생하였습니다."),
    UPDATE_SUCCESS("9999", "데이터 수정에 성공하였습니다."),
    UPDATE_FAIL("9999", "데이터 수정에 실패하였습니다."),
    DELETE_NOT_FOUND("9999", "삭제할 데이터가 존재하지 않습니다."),
    SAVE_NOT_FOUND("9999", "저장할 데이터가 존재하지 않습니다."),
    UPDATE_NOT_FOUND("9999", "수정할 데이터가 존재하지 않습니다."),
    ID_NOT_FOUND("404", "해당 사용기관이 없습니다. id="),
    MOBILE_PHONE_ALREADY_EXIST("9409", "이미 존재하는 휴대폰번호입니다."),
    NAME_CANNOT_OVER_50("9999", "이름은 최대 50자까지만 입력 가능합니다."),
    CANCEL_CALLED("901", "해지된 발신자입니다."),
    LOGIN_INCORRECT("1999", "로그인 정보가 존재하지 않습니다."),
    API_KEY_NOT_FOUND("9003", "API KEY가 존재하지 않습니다."),
    BAD_REQUEST("400", "잘못된 요청입니다."),

    OK("0000", "정상적으로 처리되었습니다."),
    KEY_NOT_FOUND("0601", "키값이 누락되었습니다."),
    INCORRECT_FORMAT("0603", "수신자료 포맷이 맞지 않습니다."),
    NOT_REGISTER("0910", "발송자 정보가 등록되지 않았습니다."),
    BIZ_BALANCE_INSUFFICIENT("0920", "biz포인트 잔액이 부족합니다."),
    GROUP_NAME_NULL("0930", "그룹 이름은 null일 수 없습니다."),
    TOKEN_EXPIRED("401", "토큰이 만료되었습니다."),
    FORBIDDEN("403", "권한이 없습니다."),
    INVALID_TOKEN("401", "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_SIG("401", "JWT 서명이 잘못되었습니다."),
    UNSUPPORTED_TOKEN("401", "지원되지 않는 JWT 토큰입니다."),
    USER_ID_NOT_FOUND("9101", "사용자 정보가 존재하지 않습니다."),
    APP_TYPE_NOT_FOUND("9102", "앱 타입 정보가 존재하지 않습니다."),
    TR_TIME_NOT_FOUND("9103", "TR_TIME 정보가 존재하지 않습니다."),
    REQ_DATA_NOT_FOUND("9104", "REQ_DATA 정보가 존재하지 않습니다."),
    USER_HAVE_REVOKE("9114", "서비스가 해지된 사용자입니다."),
    NO_SERVICE_CONTACT("9116", "서비스 이용계약이 되어 있지 않습니다."),
    INVALID_PHONE_NUMBER("9108", "입력값이 유효하지 않습니다.(HP_NO)"),
    INVALID_LINK("9110", "링크 메시지의 경우 링크APP정보가 필요합니다."),
    LINK_APP_NOT_FOUND("9111", "링크APP정보가 존재하지 않습니다."),
    RECIPIENT_INFORMATION_REQUIRED("9113", "수신자 정보가 필요합니다."),
    INVALID_PHOTO("9109", "입력값이 유효하지 않습니다.(PHOTO)"),

    //change code
    INVALID_SAUP_NO("9210", "입력값이 유효하지 않습니다.(SAUP_NO)"),
    INVALID_ENCRYPT_GB("9211", "입력값이 유효하지 않습니다.(ENCRYPT_GB)"),
    INVALID_MSG_TYPE("9212", "입력값이 유효하지 않습니다.(MSG_TYPE)"),
    INVALID_MSG_TXT("9213", "입력값이 유효하지 않습니다.(MSG_TXT)"),
    INVALID_LINK_CD("9214", "입력값이 유효하지 않습니다.(LINK_CD)"),
    INVALID_LINK_URL("9215", "입력값이 유효하지 않습니다.(LINK_URL)"),
    INVALID_LINK_NM("9216", "입력값이 유효하지 않습니다.(LINK_NM)"),
    INVALID_HP_NO("9217", "입력값이 유효하지 않습니다.(HP_NO)"),
    INVALID_CHNL_CODE("9218", "입력값이 유효하지 않습니다.(CHNL_CODE)"),
    INVALID_PTL_ID("9219", "입력값이 유효하지 않습니다.(PTL_ID)"),
    INVALID_CHNL_ID("9220", "입력값이 유효하지 않습니다.(CHNL_ID)"),
    INVALID_USE_INTT_ID("9221", "입력값이 유효하지 않습니다.(USE_INTT_ID)"),

    ;

    private final String code;
    private final String message;
}
