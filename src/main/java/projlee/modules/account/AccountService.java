package projlee.modules.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import projlee.email.EmailMessage;
import projlee.email.EmailService;
import projlee.modules.account.addressUpdate.AddressUpdateForm;
import projlee.modules.account.domain.Account;
import projlee.modules.account.domain.Address;
import projlee.modules.account.domain.Role;
import projlee.modules.account.forget.AccountIdForgetForm;
import projlee.modules.account.forget.PasswordForgetForm;
import projlee.modules.verificationCode.VerificationCodeRepository;
import projlee.modules.verificationCode.VerificationCodeService;
import projlee.modules.verificationCode.domain.VerificationCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static projlee.modules.account.domain.Role.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final VerificationCodeService verificationCodeService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private final TemplateEngine templateEngine;



    public Account accountSignup(CreateAccountForm createAccountForm) {

        Account newAccount = getAccount(createAccountForm);

        return newAccount;

    }

    private Account getAccount(CreateAccountForm createAccountForm) {

        Address address = Address.builder()
                .postcode(createAccountForm.getPostcode())
                .address(createAccountForm.getAddress())
                .detailAddress(createAccountForm.getDetailAddress())
                .extraAddress(createAccountForm.getExtraAddress())
                .build();


        Account newAccount= Account.builder()
                .accountId(createAccountForm.getAccountId())
                .name(createAccountForm.getName())
                .password(passwordEncoder.encode(createAccountForm.getPassword()))
                .phoneNumber(createAccountForm.getPhoneNumber())
                .email(createAccountForm.getEmail())
                .address(address)
                .code(createAccountForm.getCode())
                .createdAt(LocalDateTime.now())
                .role(ROLE_USER)
                .accountDogReservation(false)
                .build();


        return accountRepository.save(newAccount);

    }

    public boolean isEmailUnique(String email) {
        return !accountRepository.findByEmail(email).isPresent();
    }

    //중복 이메일 체크
    public String sendVerificationCode(@Valid CreateAccountForm createAccountForm ) {

        VerificationCode existingCode = verificationCodeRepository.findOneByEmail(createAccountForm.getEmail());

        if (existingCode != null) {
            // 기존 정보 삭제
            verificationCodeRepository.delete(existingCode);
        }
        if (isEmailUnique(createAccountForm.getEmail())) {
            String code = verificationCodeService.generateCode();
            verificationCodeService.saveCode(createAccountForm, code);

            Context context = new Context();
            context.setVariable("email",createAccountForm.getEmail());
            context.setVariable("message", "인증번호 : " + "Your verification code is: " + code);

            String message = templateEngine.process("mail/verifyCodeMail",context);

            EmailMessage emailMessage = EmailMessage.builder()
                    .to(createAccountForm.getEmail())
                    .subject("회원 가입 인증번호")
                    .message(message)
                    .build();

            emailService.sendEmail(emailMessage);
            return "이메일을 보냈습니다.";
        } else {
            return "이미 존재하는 이메일 입니다.";
        }
    }



    public boolean verifyCode(String email, String code) {

        Optional<VerificationCode> optionalCode = verificationCodeService.getCodeByEmail(email);
        if (optionalCode.isPresent()) {
            VerificationCode saveCode = optionalCode.get();
            return saveCode.getCode().equals(code);
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountId(accountId);

        if (account == null) {
            throw new UsernameNotFoundException(accountId);
        }


        Set<GrantedAuthority> authorities = new HashSet<>();

        if (account.getRole().equals(Role.ROLE_ADMIN)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (account.getRole().equals(Role.ROLE_USER)){
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if(account.getRole().equals(Role.ROLE_MANAGER)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        }


        return new UserAccount(account,authorities);

    }

    public Account getAccount(String accountId) {

        Account account = accountRepository.findByAccountId(accountId);
        if (account == null ) {
            throw new IllegalArgumentException( accountId + "존재하지 않습니다.");
        }
        return account;
    }

    public void updateAddress(Account account, AddressUpdateForm addressUpdateForm) {


        Address updateAddress = Address.builder()
                .postcode(addressUpdateForm.getPostcode())
                .address(addressUpdateForm.getAddress())
                .detailAddress(addressUpdateForm.getDetailAddress())
                .extraAddress(addressUpdateForm.getExtraAddress())
                .build();


        Account updatedAccount = Account.builder()
                .id(account.getId())
                .address(updateAddress)
                .phoneNumber(account.getPhoneNumber())
                .accountId(account.getAccountId())
                .name(account.getName())
                .password(account.getPassword())
                .email(account.getEmail())
                .createdAt(account.getCreatedAt())
                .code(account.getCode())
                .build();

      accountRepository.save(updatedAccount);

    }

    public Account checkAccount(String accountId) {
       return accountRepository.findByAccountId(accountId);
    }

    public void updatePassword(Account account, String newPassword) {
        account.passwordUpdate(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void updatePhoneNumber(Account account, String newPhoneNumber) {
        account.phoneNumberUpdate(newPhoneNumber);
        accountRepository.save(account);
    }

    public void accountDelete(Account account) {
            accountRepository.delete(account);
    }

    /**
     *
     * 아이디 잊어버렸을때
     */
    public String sendAccountId(@Valid AccountIdForgetForm accountIdForgetForm) {

        if (accountRepository.existsByEmail(accountIdForgetForm.getEmail())) {

            Optional<Account> accountOptional = accountRepository.findByEmail(accountIdForgetForm.getEmail());

            accountOptional.ifPresent(account -> {
                String accountId = account.getAccountId();


                Context context = new Context();
                context.setVariable("email",accountIdForgetForm.getEmail());
                context.setVariable("message", "회원 아이디" + "Your verification accountId: " + accountId);

                String message = templateEngine.process("mail/accountIdFind",context);


                EmailMessage emailMessage = EmailMessage.builder()
                        .to(accountIdForgetForm.getEmail())
                        .subject("회원 아이디")
                        .message(message)
                        .build();

                emailService.sendEmail(emailMessage);

            });

            return "메일을 보냈습니다.";
        } else {

            return "존재하는 이메일이 없습니다.";
        }
    }

    public String sendPasswordId(@Valid PasswordForgetForm passwordForgetForm) {


        if (accountRepository.existsByEmailAndAccountId(passwordForgetForm.getEmail(),passwordForgetForm.getAccountId())) {

            Account accountId = accountRepository.findByAccountId(passwordForgetForm.getAccountId());

            String uuid = UUID.randomUUID().toString().substring(0, 8);
            accountId.passwordUpdate(passwordEncoder.encode(uuid));

            accountRepository.save(accountId);

            Context context = new Context();
            context.setVariable("email",passwordForgetForm.getEmail());
            context.setVariable("message", "회원 비밀번호" + "Your verification password: " + uuid);

            String message = templateEngine.process("mail/passwordFind",context);


            EmailMessage emailMessage = EmailMessage.builder()
                    .to(passwordForgetForm.getEmail())
                    .subject("회원 비밀번호")
                    .message(message)
                    .build();

            emailService.sendEmail(emailMessage);


                 return "메일을 보냈습니다.";

        } else {

            return "아이디와 이메일을 다시한번 확인해주세요";
        }
    }


    public Account getAccountById(Long accountId) {
       return accountRepository.findById(accountId).orElseThrow();
    }

    public boolean reservationCheck(Long id) {
        return accountRepository.existsById(id);
    }
}

