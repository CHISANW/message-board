package messageboard.service.security;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo{

    private Map<String,Object> attribute;
    private Map<String, Object> kakaoProperties;
    private Map<String,String> kakao_account;
    public KakaoUserInfo(Map<String, Object> attribute) {
        this.attribute = attribute;
        this.kakaoProperties = (Map<String, Object>) attribute.get("properties");
        this.kakao_account = (Map<String, String>) attribute.get("kakao_account");
    }

    @Override
    public String getId() {
        Long id = (Long) attribute.get("id");
        return String.valueOf(id);
    }

    @Override
    public String getEmail() {
        return kakao_account.get("email");
    }

    @Override
    public String getName() {
        return (String) kakaoProperties.get("nickname");
    }
}
