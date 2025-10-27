package com.hunch.realestate.domain.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoDTO {
    private String businessName;      // 상호명
    private String address1;          // 주소
    private String address2;          // 상세주소
    private String directions;        // 찾아오는 길
    private String phoneNumber;       // 전화번호
    private String faxNumber;         // 팩스
    private String managerPosition;   // 담당자 직급
    private String managerName;       // 담당자 이름
    private String managerPhone;      // 담당자 연락처
    private String managerEmail;      // 담당자 이메일
    private String managerPhoto;      // 담당자 사진 URL

    /**
     * 유효성 검사
     */
    public Map<String, String> validate() {
        Map<String, String> errors = new HashMap<>();

        if (businessName == null || businessName.trim().isEmpty()) {
            errors.put("businessName", "상호명을 입력해주세요");
        }

        if (address1 == null || address1.trim().isEmpty()) {
            errors.put("address1", "주소를 입력해주세요");
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            errors.put("phoneNumber", "전화번호를 입력해주세요");
        }

        if (managerName == null || managerName.trim().isEmpty()) {
            errors.put("managerName", "담당자 이름을 입력해주세요");
        }

        if (managerPhone == null || managerPhone.trim().isEmpty()) {
            errors.put("managerPhone", "담당자 연락처를 입력해주세요");
        }

        return errors;
    }

    /**
     * 전체 주소 반환
     */
    public String getFullAddress() {
        if (address2 != null && !address2.trim().isEmpty()) {
            return address1 + " " + address2;
        }
        return address1;
    }
}
