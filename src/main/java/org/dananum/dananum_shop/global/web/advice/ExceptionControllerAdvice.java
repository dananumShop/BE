package org.dananum.dananum_shop.global.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.advice.exception.CustomAccessDeniedException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomDataIntegerityCiolationException;
import org.dananum.dananum_shop.global.web.advice.exception.CustomNotFoundException;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomDataIntegerityCiolationException.class)
    public ResponseEntity<CommonResponseDto> handleDataIntegrityViolationException(CustomDataIntegerityCiolationException e) {
        log.error("[CONFLICT] 데이터 무결성 에러로 다음의 에러메시지를 출력합니다." + e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                CommonResponseDto.builder()
                        .code(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .success(false)
                        .build()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<CommonResponseDto> handleNotFoundException(CustomNotFoundException e){
        log.error("[NOTFOUND] DB에 일치하는 결과가 없어 다음의 에러메시지를 출력합니다." + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                CommonResponseDto.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .success(false)
                        .build()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<CommonResponseDto> handleAccessDeniedException(CustomAccessDeniedException e) {
        log.error("[CONFLICT] 인증 에러로 다음의 에러메시지를 출력합니다." + e.getMessage());
        CommonResponseDto responseDto = CommonResponseDto.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .success(false)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDto);
    }
}
