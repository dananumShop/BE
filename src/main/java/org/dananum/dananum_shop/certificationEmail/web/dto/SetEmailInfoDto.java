package org.dananum.dananum_shop.certificationEmail.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SetEmailInfoDto {

    private String userCid;

    private String verificationCode;

    private String userEmail;

    public static SetEmailInfoDto from(final Long userCid, final int verificationCode, final String userEmail) {
        return SetEmailInfoDto.builder()
                .userCid(userCid.toString())
                .verificationCode(Integer.toString(verificationCode))
                .userEmail(userEmail)
                .build();
    }
}
