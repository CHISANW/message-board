package messageboard.service.security;

import java.util.Map;

public class NaverUserInfo implements Oauth2UserInfo{

    private Map<String,Object> attribute;
    private Map<String, String> kakaoProperties;
    public NaverUserInfo(Map<String, Object> attribute) {
        this.attribute = attribute;
        this.kakaoProperties = (Map<String, String>) attribute.get("response");
    }

    @Override
    public String getId() {
        return kakaoProperties.get("id");
    }

    @Override
    public String getEmail() {
        return kakaoProperties.get("email");
    }

    @Override
    public String getName() {
        return kakaoProperties.get("name");
    }
}
