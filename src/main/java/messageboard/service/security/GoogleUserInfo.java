package messageboard.service.security;

import java.util.Map;

public class GoogleUserInfo implements Oauth2UserInfo{

    private Map<String,Object> attribute;

    public GoogleUserInfo(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getId() {
        return (String) attribute.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attribute.get("email");
    }

    @Override
    public String getName() {
        return (String) attribute.get("given_name");
    }
}
