package cos.security1.config;

import cos.security1.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@RequiredArgsConstructor
@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록
//@EnableGlobalMethodSecurity(securedEnabled = true) //secured 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("name")
                .loginProcessingUrl("/login") //해당 URL이 호출되면 시큐리티가 낚아서 대신 로그인 진행
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/login")
                /*
                //구글 로그인이 완료된 뒤의 후처리가 필요하다.
                1.코드받기(인증)
                2.엑세스토큰(권한)
                3.사용자프로필 정보를 가져옴
                4.그 정보를 활용 (자동으로 회원가입, 자동으로 회원가입이 아니라 추가정보 받기 등)

                Tip. 코드X, (엑세스토큰 + 사용자 프로필정보 O)
                 */
                .userInfoEndpoint()
                .userService(principalOauth2UserService);//후처리 담당 서비스
    }
}
