package projlee.security.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import projlee.modules.account.AccountService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AccountService accountService;

    private final DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/","/home","/account/sign-up","/checkEmail","/login",
                                        "/sendVerificationCode","/account/idForget","/account/passwordForget"
                        ).permitAll()
                        .requestMatchers("/account/**").hasAnyRole("USER","MANAGER","ADMIN")
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/profile/*").permitAll()
                        .anyRequest().authenticated()
                ).formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .usernameParameter("accountId")

                ).logout(logout ->
                            logout.logoutSuccessUrl("/login")

                ).rememberMe(rememberMe ->
                        rememberMe
                            .userDetailsService(accountService)
                            .tokenRepository(tokenRepository()));

        return http.build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/node_modules/**","/favicon.ico","/error")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }


}
