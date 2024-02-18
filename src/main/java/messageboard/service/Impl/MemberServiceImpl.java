package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.MemberDto;
import messageboard.Exception.BadRequestException;
import messageboard.entity.member.Address;
import messageboard.entity.member.Member;
import messageboard.handler.SuccessResult;
import messageboard.repository.AddressRepository;
import messageboard.repository.MemberRepository;
import messageboard.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member saveDto(MemberDto memberDto) {
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .loginId(memberDto.getLoginId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .year(memberDto.getYear())
                .month(memberDto.getMonth())
                .day(memberDto.getDay())
                .email(memberDto.getEmail())
                .phoneNumber(memberDto.getPhoneNumber())
                .verified(false)
                .build();
        Address address = Address.builder()
                .zipcode(memberDto.getAddressDto().getZipcode())
                .address(memberDto.getAddressDto().getAddress())
                .detailAddr(memberDto.getAddressDto().getDetailAddr())
                .subAddr(memberDto.getAddressDto().getSubAddr())
                .member(member).build();

        addressRepository.save(address);
        return saveEntity(member);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public Member findByLoginId(String loginId) {
        Member byLoginId = memberRepository.findByLoginId(loginId);
        if (byLoginId==null){
            throw new BadRequestException("Not found User= "+loginId);
        }

        return byLoginId;
    }

    //아이디 중복검사
    public ResponseEntity<?> duplicateLoginId(MemberDto memberDto){
        Map<String,Boolean> result = new LinkedHashMap<>();
        try {
            Member byLoginId = findByLoginId(memberDto.getLoginId());

            if (byLoginId!=null) {
                result.put("use_loginId", false);
                log.info("아이디 중복");
            }


            return ResponseEntity.ok(result);
        }catch (BadRequestException e){
            result.put("use_loginId", true);
            log.info("아이디 중복안됨");
            return  ResponseEntity.ok(result);
        }
    }

}
