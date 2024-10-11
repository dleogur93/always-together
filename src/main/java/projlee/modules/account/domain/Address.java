package projlee.modules.account.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class Address {

    private String postcode ;

    private String address  ;

    private String detailAddress;

    private String extraAddress;


}

