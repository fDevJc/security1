package cos.security1.config.oauth;

import cos.security1.config.auth.PrincipalDetails;
import cos.security1.domain.Member;
import cos.security1.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //순환 참조를 발생
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    /*
            구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    */
    //함수 종료시 @AuthenticationPrincipal 애노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest = " + userRequest);
        System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken());
        System.out.println("userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());
        //registrationId로 어떤 OAuth로 로그인 했는지 확인가능.
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAdditionalParameters() = " + userRequest.getAdditionalParameters());
        /*
        구글로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리)
        -> AccessToken요청
        userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
         */
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("super.loadUser(userRequest) = " + super.loadUser(userRequest));

        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
//        String password = "";
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        Member memberEntity = memberRepository.findByName(username);
        if (memberEntity == null) {
            Member member = new Member(providerId, username, password, email, role, provider, providerId);
            memberRepository.save(member);
        }
        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }
}
