package projlee.modules.banner;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Banner {

    @Id @GeneratedValue
    private Long id;

    private String bannerName;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "LONGTEXT")
    private String bannerImage;


    public void bannerUpdate(BannerUpdateForm bannerUpdateForm) {
        this.bannerName = bannerUpdateForm.getBannerName();
        this.bannerImage = bannerUpdateForm.getBannerImage();
    }
}
