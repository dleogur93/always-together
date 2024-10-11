package projlee.modules.banner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;


    public Banner findOne() {
       return bannerRepository.findTopByOrderByIdDesc();
    }

    public void updateBanner(Banner banner, BannerUpdateForm bannerUpdateForm ) {
        banner.bannerUpdate(bannerUpdateForm);
    }

    public void createBanner(BannerForm bannerForm) {


        Banner banner = Banner.builder()
                .bannerName(bannerForm.getBannerName())
                .bannerImage(bannerForm.getBannerImage())
                .build();

      bannerRepository.save(banner);
    }
}
