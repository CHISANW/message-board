package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.MemberDto;
import messageboard.Exception.BadRequestException;
import messageboard.Exception.Login_RestException;
import messageboard.entity.member.Address;
import messageboard.entity.member.Member;
import messageboard.repository.AddressRepository;
import messageboard.repository.MemberRepository;
import messageboard.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerErrorException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    /**
     * 회원 정보를 저장하고 데이터베이스에 저장된 사용자 정보를 반환합니다.
     * @param memberDto 저장할 회원 정보를 포함하는 Dto
     * @return 데이터베이스에 저장된 회원 정보
     */
    @Override
    public Member saveDto(MemberDto memberDto) {
            // 회원 정보를 생성하여 데이터베이스에 저장합니다.
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
                    .loginType("Normal")
                    .build();
            Address address = Address.builder()
                    .zipcode(memberDto.getAddressDto().getZipcode())
                    .address(memberDto.getAddressDto().getAddress())
                    .detailAddr(memberDto.getAddressDto().getDetailAddr())
                    .subAddr(memberDto.getAddressDto().getSubAddr())
                    .member(member)
                    .build();
            addressRepository.save(address);
            return saveEntity(member);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public Member findByUsernameAndLoginId(String username, String loginId) {
        Member byUsernameAndLoginId = memberRepository.findByUsernameAndLoginId(username, loginId);
        if (byUsernameAndLoginId==null){
            throw new Login_RestException();
        }
        return byUsernameAndLoginId;
    }
//
//    /**
//     * 대소문자를 구분한 이메일 값을 찾는다.
//     * @param email
//     * @return 값이 있다면 true 값이 없다면 false;
//     */
//    @Override
//    public Boolean findByCaseSensitiveEmail(String email) {
//        Member member = memberRepository.findByCaseSensitiveEmail(email);
//        if (member==null){
//            return true;
//        }
//        return true;
//    }

    @Override
    public Member findByLoginId(String loginId) {
        // 로그인 ID에 해당하는 사용자를 조회하고, 사용자가 존재하지 않을 경우 BadRequestException을 발생시킵니다.
        Member byLoginId = memberRepository.findByLoginId(loginId);
        if (byLoginId == null) {
            throw new BadRequestException("Not found User= " + loginId);
        }
        return byLoginId;
    }



    /**
     * 회원가입시 AJAX를 사용한 아이디 중복검사 및 아이디 조건체크(대소문자 구문)
     * @param memberDto 회원가입시 정보
     * @return  아이디가 이미 사용 중이거나 조건이 불충분하면 false를 반환하고, 모두 만족시 true를 반환합니다
     */
    public Map<String, Boolean> LoginIdValid(MemberDto memberDto) {
        Map<String, Boolean> result = new LinkedHashMap<>();
        try {
            boolean isLoginIdValid = isLoginIdValid(memberDto);
            Member member = memberRepository.findByCaseSensitiveLoginId(memberDto.getLoginId());
            result.put("use_loginId", member==null && isLoginIdValid);
            return result;
        } catch (Exception e) {
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
        Member member = memberRepository.findByCaseSensitiveLoginId(loginId);

        boolean  pattern= Pattern.compile("^[a-zA-Z0-9]{8,16}$").matcher(loginId).find();
        if (member!=null||!pattern) {
            return false;
        }

        return true;
    }

    /**
     * 주어진 회원 정보를 통해 비밀번호 중복여부를 체크하고, 결과를 반환합니다.
     * @param memberDto 회원 정보를 담은 Dto
     * @return 비밀번호 일치여부를 Map<String,Boolean>타입으로 반환
     */
    public Map<String, Boolean> duplicatePassword(MemberDto memberDto) {
        Map<String, Boolean> result = new LinkedHashMap<>();
        String password = memberDto.getPassword();          //첫번째 비밀번호
        String passwordRe = memberDto.getPasswordRe();      //두번째 비밀번호

        result.put("duplicate_password", password.equals(passwordRe));
        return result;
    }

    /**
     * 회원가입시 모든 조건검사를 하지 않았을때 회원가입을 하지 못함.
     * @param memberDto 회원가입의 정보
     * @return  모두 사용시 true, 하나라도 검사 안했을시 false
     */
    public boolean ValidationOfSignUp(MemberDto memberDto) {
        Map<String, Boolean> validationResult = validateSignUp(memberDto);

        for (Map.Entry<String, Boolean> entry : validationResult.entrySet()) {
            if (!entry.getValue()) {
                log.error("Failed validation: {}", entry.getKey());
                return false;
            }
        }
        return true;
    }

    /**
     * 강력한 비밀번호인지 확인 "특수문자","0~9","소문자","대문자","8자리이상" 포함 여부 확인
     * @param memberDto 회원가입시 사용자정보
     * @return 강력한 비밀번호시 true, 아닐시 false 반환
     */
    public Map<String, Boolean> checkPasswordStrength(MemberDto memberDto) {
        try {
            String password = memberDto.getPassword();

            Map<String, Boolean> result = new LinkedHashMap<>();
            // 특수 문자 포함 여부, 대문자 알파벳 포함 여부, 비밀번호의 길이가 8자리 이상인지를 한 번에 확인
            boolean checkStrength = Pattern.compile("^(?=.*[`~!@#$%^&*()_+])(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,16}$").matcher(password).find();

            result.put("passwordStrength", checkStrength);
            return result;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 회원가입시 이름이 한국어이며 2글자 이상시에만 회원가입 가능
     * @param memberDto 회원가입 정보
     * @return  한국어로 작성된 이름시 true 그외 false
     */
    public Map<String, Boolean> userNameValid(MemberDto memberDto) {
        try {
            Map<String, Boolean> result = new LinkedHashMap<>();
            String username = memberDto.getUsername();

            boolean isKorean = Pattern.compile("^[가-힣]+.{1,}$").matcher(username).matches();
            // 한국어로 작성된 이름인 경우 true, 그 외에는 false
            result.put("isKorean", isKorean);
            return result;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 회원가입시 010,"-"를 제외한 8자리 번호로 회원가입
     * @param memberDto 회원가입 정보
     * @return 8자리일시 usePhoneNumber:true 아닐시  usePhoneNumber:false
     */
    public Map<String, Boolean> phoneNumberValid(MemberDto memberDto) {
        try {
            Map<String, Boolean> result = new LinkedHashMap<>();
            String phoneNumber = memberDto.getPhoneNumber();

            boolean valid = Pattern.compile("^\\d{8}$").matcher(phoneNumber).matches();
            result.put("usePhoneNumber", valid);
            return result;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 이메일주소가 올바른 주소인지 확인
     * @param memberDto 회원가입시의 이메일정보
     * @return  올바른이메일 주소시 true, 아닐시 false
     */
    public Map<String, Boolean> emailValid(MemberDto memberDto) {
        try {
            Map<String, Boolean> result = new LinkedHashMap<>();
            String email = memberDto.getEmail();

            boolean valid = Pattern.compile("^[a-zA-Z0-9]+@[a-zA-Z]+\\.(com|net)$").matcher(email).matches();

            if (valid) {
                // 이메일이 데이터베이스에 존재하는지 확인
                List<Member> member = memberRepository.findByEmail(email);
                for (Member member1 : member) {
                    if (member1.getLoginType().equals("Normal")) {
                      result.put("duplicateEmail",false);
                    } else {
                        result.put("duplicateEmail",true);
                    }
                }
                if (member.isEmpty()){
                    log.info("사용가능한 이메일");
                    result.put("duplicateEmail",true);
                }
            }else
                result.put("useEmail",valid);

            log.info("reult={}",result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     회원가입 정보를 검증하는 메서드로, 사용자가 제출한 회원가입 폼의 데이터를 기반으로 각각의 조건을 검사하여 결과를 반환.
     반환된 결과는 각 검증 항목의 이름과 검증 결과로 이루어진 맵 형태로 제공한다.
     @param memberDto 검증할 회원 정보가 포함된 MemberDto 객체
     @return 각 검증 항목의 이름과 해당 검증 결과로 이루어진 맵
     */
    private Map<String, Boolean> validateSignUp(MemberDto memberDto) {
        Map<String, Boolean> validationResult = new LinkedHashMap<>();

//        boolean loginIdValid = isLoginIdValid(memberDto);
        validationResult.put("isLoginValid",isLoginIdValid(memberDto));
        validationResult.putAll(duplicatePassword(memberDto));
        validationResult.putAll(checkPasswordStrength(memberDto));
        validationResult.putAll(userNameValid(memberDto));
        validationResult.putAll(phoneNumberValid(memberDto));
        validationResult.putAll(emailValid(memberDto));

        return validationResult;
    }
}
