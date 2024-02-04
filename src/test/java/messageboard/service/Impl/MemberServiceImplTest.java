package messageboard.service.Impl;

import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.LoginDto;
import messageboard.Dto.MemberDto;
import messageboard.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
@Transactional
@Slf4j
class MemberServiceImplTest {

    @Autowired MemberServiceImpl memberService;
    @Test
    @Rollback(value = false)
    void saveTest(){
        MemberDto name = MemberDto.builder()
                .username("name")
                .password("1234").build();

        Member member = memberService.saveDto(name);

        assertThat(member.getUsername()).isEqualTo("name");

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("name1");
        loginDto.setPassword("1234");

        LoginDto loginDto1 = new LoginDto();
        loginDto1.setUsername("name");
        loginDto1.setPassword("1234");


        boolean login = memberService.login(loginDto);
        assertThat(login).isFalse();

        boolean login1 = memberService.login(loginDto1);
        assertThat(login1).isTrue();

    }

}