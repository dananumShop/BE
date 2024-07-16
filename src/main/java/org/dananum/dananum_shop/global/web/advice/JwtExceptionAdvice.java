package org.dananum.dananum_shop.global.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.global.web.advice.exception.jwt.CustomExpiredJwtException;
import org.dananum.dananum_shop.global.web.advice.exception.jwt.CustomJwtBadReqException;
import org.dananum.dananum_shop.global.web.advice.exception.jwt.CustomJwtException;
import org.dananum.dananum_shop.global.web.dto.CommonResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class JwtExceptionAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CustomExpiredJwtException.class)
    public ResponseEntity<CommonResponseDto> handleExpiredTokenException(CustomExpiredJwtException e){
        log.error("[FORBIDDEN] " + e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                CommonResponseDto.builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .success(false)
                        .build()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<CommonResponseDto> handleUnAuthorizedException(CustomJwtException e){
        log.error("[UNAUTHORIZED] " + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                CommonResponseDto.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message(e.getMessage())
                        .success(false)
                        .build()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomJwtBadReqException.class)
    public ResponseEntity<CommonResponseDto> handleJwtBadReqException(CustomJwtBadReqException e){
        log.error("[BAD_REQUEST] " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                CommonResponseDto.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .success(false)
                        .build()
        );
    }
}
