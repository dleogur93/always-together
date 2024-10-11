package projlee.modules.banner;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    Banner findFirstById(Long id);

    Banner findTopByOrderByIdDesc();
}
