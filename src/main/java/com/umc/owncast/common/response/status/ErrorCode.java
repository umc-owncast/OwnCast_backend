package com.umc.owncast.common.response.status;

import com.umc.owncast.common.response.BaseErrorCode;
import com.umc.owncast.common.response.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {
    /* API 실행 실패 시 응답에 담을 상태 코드 */

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    MEMBER_LOGIN_FAILURE(HttpStatus.BAD_REQUEST, "MEMBER4003", "아이디 혹은 비밀번호를 잘못 입력하였습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "SIGNUP4004", "이미 존재하는 닉네임입니다."),
    ID_ALREADY_EXIST(HttpStatus.CONFLICT, "SIGNUP4003", "이미 존재하는 아이디입니다."),
    MEMBER_SIGNUP_ERROR(HttpStatus.BAD_REQUEST, "SIGNUP4001", "회원가입 유효성 검사 실패"),
    EXIST_NICKNAME(HttpStatus.CONFLICT, "MEMBER_4091", "이미 존재하는 닉네임입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMBER_4010", "비밀번호가 일치하지 않습니다"),
    INACTIVATE_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER_4030", "비활성된 회원입니다"),
    LANGUAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4011", "언어가 없습니다."),
    CATEGORY_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4012", "카테고리가 없습니다."),

    // 로그인 관련 에러
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "AUTH_4000", "잘못된 파라미터 형식입니다"),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_4010", "로그인 정보가 잘못되었습니다"),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_4011", "토큰이 존재하지 않습니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_4012", "토큰이 만료되었습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_4013", "토큰이 올바르지 않습니다"),
    OUTPUT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_5000", "서버 출력에 오류가 있습니다. 관리자에게 문의하세요"),
    // 기타 에러는 아래에 추가
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
