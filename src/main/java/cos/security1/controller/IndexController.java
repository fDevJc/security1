package cos.security1.controller;

import cos.security1.config.auth.PrincipalDetails;
import cos.security1.domain.Member;
import cos.security1.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.Arrays;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @ResponseBody
    @GetMapping("/test/login")
    public String loginTest(Authentication authentication,
                            @AuthenticationPrincipal PrincipalDetails userDetails) {

        System.out.println("================IndexController.loginTest================");
        /*
        authentication = PrincipalDetails(member=Member(id=1, name=1, password=$2a$10$jlFUsx/AwvMlJq2Gm5o50OVOUxIqEtdk7BXrWP9jCO77geiMGrNXC, email=123@asd.asd, role=ROLE_USER, createDate=2022-05-13T17:29:40.030817))
        principalDetails.getMember() = Member(id=1, name=1, password=$2a$10$jlFUsx/AwvMlJq2Gm5o50OVOUxIqEtdk7BXrWP9jCO77geiMGrNXC, email=123@asd.asd, role=ROLE_USER, createDate=2022-05-13T17:29:40.030817)
        userDetails = 1
         */

        System.out.println("authentication = " + authentication.getPrincipal());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails.getMember() = " + principalDetails.getMember());

        System.out.println("userDetails = " + userDetails.getUsername());

        return "ok";

    }


    @ResponseBody
    @GetMapping("/test/oauth/login")
        public String loginOauthTest(Authentication authentication,
                                     @AuthenticationPrincipal OAuth2User oAuth2) {

        System.out.println("IndexController.loginOauthTest");

        System.out.println("authentication = " + authentication.getPrincipal());
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        oAuth2User.getAttributes().keySet()
                .forEach(k-> System.out.println("oAuth2User.get = " + oAuth2User.getAttributes().get(k)));
        System.out.println("============================================================");
        oAuth2.getAttributes().keySet()
                .forEach(k-> System.out.println("oAuth2User.get = " + oAuth2.getAttributes().get(k)));

        return "ok";

    }

    @GetMapping("/")
    public String index() {
        log.info("indexController");
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails);
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute Member member) {
        member.setRole("ROLE_USER");
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        return "redirect:/login";
    }

    @ResponseBody
    @GetMapping("/joinComp")
    public String joinComp() {
        return "회원가입 완료";
    }


    //@EnableGlobalMethodSecurity(securedEnabled = true) //secured 어노테이션 활성화
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "개인정보";
    }
}
