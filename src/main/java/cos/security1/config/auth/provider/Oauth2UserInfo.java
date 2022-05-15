package cos.security1.config.auth.provider;

public interface Oauth2UserInfo {
    String getProvider();
    String getProviderId();
    String getName();
    String getEmail();
}
