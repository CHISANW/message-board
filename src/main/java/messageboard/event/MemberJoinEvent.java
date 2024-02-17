package messageboard.event;

import messageboard.entity.member.Member;
import org.springframework.context.ApplicationEvent;

public class MemberJoinEvent extends ApplicationEvent {

    private Member member;
    public MemberJoinEvent(Member member) {
        super(member);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
