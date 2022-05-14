package cos.security1.config.auth;

import cos.security1.domain.Member;
import cos.security1.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * 시큐리티 설정에서 loginProcessingUrl("/login");
 * 요청이 오면 자동으로 loadUserByUsername 메서드가 실행
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {
    //파라미터는 username으로 써야한다.
    //바꾸고 싶으면 configure에
    //usernameParameter("수정 파라미터") 로 수정하면 된다.

    private final MemberRepository memberRepository;

    //시큐리티 session(내부 Authentication(내부 UserDetails))
    //함수 종료시 @AuthenticationPrincipal 애노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        Member member = memberRepository.findByName(username);
        System.out.println("member = " + member);
        if (member != null) {
            return new PrincipalDetails(member);
        }
        return null;
    }
}
