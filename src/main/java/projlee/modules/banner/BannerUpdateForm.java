package projlee.modules.banner;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BannerUpdateForm {

    private String bannerName;

    private String bannerImage;


    public BannerUpdateForm(Banner banner) {
        this.bannerName = banner.getBannerName();
        this.bannerImage = banner.getBannerImage();
    }
}
