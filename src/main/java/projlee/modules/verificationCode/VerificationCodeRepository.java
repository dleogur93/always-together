package projlee.modules.verificationCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.verificationCode.domain.VerificationCode;

import java.util.Optional;

@Transactional(readOnly = true)
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {

    Optional<VerificationCode> findByEmail(String email);

    VerificationCode findOneByEmail(String email);
}
