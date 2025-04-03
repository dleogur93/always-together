package projlee.modules.verificationCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projlee.modules.account.CreateAccountForm;
import projlee.modules.verificationCode.domain.VerificationCode;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {


    private final VerificationCodeRepository verificationCodeRepository;


    public String generateCode() {
        try {
            return RandomStringUtils.randomAlphanumeric(6);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 콘솔에 로그 출력
            return ""; // 또는 다른 적절한 기본값으로 설정
        }
    }

    @Transactional
    public void saveCode(@Valid CreateAccountForm createAccountForm, String code) {

        // 만료 시간 설정 (예: 현재 시간으로부터 10분 후)
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        // VerificationCode 객체 생성 및 저장
        VerificationCode verificationCode = VerificationCode.builder()
                .email(createAccountForm.getEmail())
                .code(code)
                .expirationTime(expirationTime)
                .build();
        verificationCodeRepository.save(verificationCode);


    }

    public Optional<VerificationCode> getCodeByEmail(String email) {
        return verificationCodeRepository.findByEmail(email);
    }


    public void deleteCode() {
        verificationCodeRepository.deleteAll();
    }
}
