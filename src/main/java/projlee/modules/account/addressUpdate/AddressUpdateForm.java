package projlee.modules.account.addressUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;
import projlee.modules.account.domain.Account;

@Data
@NoArgsConstructor
public class AddressUpdateForm {


    private String postcode;

    private String address;

    private String detailAddress;

    private String extraAddress;

    public AddressUpdateForm(Account account) {
        this.postcode = account.getAddress().getPostcode();
        this.address = account.getAddress().getAddress();
        this.detailAddress = account.getAddress().getDetailAddress();
        this.extraAddress = account.getAddress().getExtraAddress();
    }
}
