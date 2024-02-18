package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.MemberDto;
import messageboard.Exception.BadRequestException;
import messageboard.entity.member.Address;
import messageboard.entity.member.Member;
import messageboard.repository.AddressRepository;
import messageboard.repository.MemberRepository;
import messageboard.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerErrorException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

    /**
     * 회원가입시 입력값을 저장하며, 이메일인증을통해 회원가입을 실시하게 되므로 verified의 기존값은 false로 설정하여 가입한다.
     * @param memberDto 회원가입시 필요한 정보를 담은 Dto
     * @return 사용자 정보를 db에 저장
     */
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

    /**
     * 회원가입시 AJAX를 사용한 아이디 중복검사 및 아이디 조건체크
     * @param memberDto 회원가입시 정보
     * @return  아이디가 이미 사용 중이거나 조건이 불충분하면 false를 반환하고, 모두 만족시 true를 반환합니다
     */
    public ResponseEntity<Map<String, Boolean>> LoginIdVaild(MemberDto memberDto){
        Map<String,Boolean> result = new LinkedHashMap<>();
        boolean isLoginIdValid  = isLoginIdValid(memberDto);
        try {
            Member byLoginId = memberRepository.findByLoginId(memberDto.getLoginId());

            if (byLoginId!=null||!isLoginIdValid) {
                result.put("use_loginId", false);
            } else if (byLoginId == null && isLoginIdValid) {
                result.put("use_loginId", true);
            }

            return ResponseEntity.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("오류");
        }
    }

    /**
     * 아이디 조건검사 대문자나 소문자(8~16)자리 입력시 사용할수있다.
     * @param memberDto
     * @return 대문자나 소문자일시 true, 아닐시 false
     */
    private boolean isLoginIdValid(MemberDto memberDto) {
        String loginId = memberDto.getLoginId();
        return Pattern.compile("^[a-zA-Z0-9]{8,16}$").matcher(loginId).find();
    }


    /**
     *해당 메소드는 주어진 넘어온 Dto를 통해서 비밀번호 중복여부를 체크하고, 비밀번호가 일치하는지 확인 및 반환
     * @param memberDto 회원 정보를 담은 Dto
     * @return 비밀번호 일치여부를 Map<String,Boolean>타입으로 반환
     */
    public ResponseEntity<Map<String,Boolean>> duplicatePassword(MemberDto memberDto){
        Map<String,Boolean> result = new LinkedHashMap<>();
        String password = memberDto.getPassword();          //첫번쩨 비밀번호
        String passwordRe = memberDto.getPasswordRe();      //두번째 비밀번호 비밀번호

        if (!password.equals(passwordRe)){
            result.put("duplicate_password",false);
        }else {
            result.put("duplicate_password", true);
        }

        return ResponseEntity.ok(result);
    }


    /**
     * 회원가입시 아이디중복검사, 비밀번호 중복검사를 하지않았을때 회원가입을 못함.
     * @param memberDto 회원가입의 정보
     * @return  모두 사용시 true, 하나라도 검사 안했을시 false
     */
    public boolean VerificationOfSingUp(MemberDto memberDto){
        String loginId = memberDto.getLoginId();
        Member byLoginId = memberRepository.findByLoginId(loginId);

        String password = memberDto.getPassword();
        String passwordRe = memberDto.getPasswordRe();

        if (byLoginId!=null||!password.equals(passwordRe)){
            return false;
        }
        return true;
    }


    /**
     * 강력한 비밀번호인지 확인 "특수문자","0~9","소문자","대문자","8자리이상" 포함 여부 확인
     * @param memberDto 회원가입시 사용자정보
     * @return 강력한 비밀번호시 true, 아닐시 false 반환
     */
    public ResponseEntity<Map<String,Boolean>> checkPasswordStrength(MemberDto memberDto){
        try {
            String password = memberDto.getPassword();

            Map<String,Boolean> result = new LinkedHashMap<>();
            // 특수 문자 포함 여부, 대문자 알파벳 포함 여부, 비밀번호의 길이가 8자리 이상인지를 한 번에 확인
            boolean checkStrength = Pattern.compile("^(?=.*[`~!@#$%^&*()_+])(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,16}$").matcher(password).find();

            if (!checkStrength){
                result.put("passwordStrength",false);
            }else
                result.put("passwordStrength",true);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
