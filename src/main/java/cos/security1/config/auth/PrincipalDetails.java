package cos.security1.config.auth;

import cos.security1.domain.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
 * 로그인 진행이 완료되면 시큐리티 session을 만든다. (Security ContextHolder)
 * 오브젝트 타입은 Authentication 타입의 객체여야한다.
 * Authentication 안에 User정보가 있어야한다.
 * User오브젝트타입은 UserDetails 타입의 객체여야한다.
 *
 * Security Session => Authentication => UserDetails
 */
@Data
public class PrincipalDetails implements UserDetails , OAuth2User {
    private Member member;
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    //oauth 로그인
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //해당 User의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    /**
     * 활용방법
     * 1년동안 회원이 로그인을 안하면 휴먼계정으로 정하기로 한다면
     * 현재시간 - 로그인시간 => 1년을 초과하면 return false;
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
