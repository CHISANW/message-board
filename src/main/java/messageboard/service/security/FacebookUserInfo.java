package messageboard.service.security;

import java.util.Map;

public class FacebookUserInfo implements Oauth2UserInfo{

    private Map<String,Object> attribute;

    public FacebookUserInfo(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getId() {
        return (String) attribute.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attribute.get("email");
    }

    @Override
    public String getName() {
        return (String) attribute.get("name");
    }
}
