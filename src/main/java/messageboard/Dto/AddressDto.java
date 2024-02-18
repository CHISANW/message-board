package messageboard.Dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddressDto {

    @NotEmpty(message = "우편번호를 입력하세요")
    private String zipcode;

    @NotEmpty(message = "주소를 입력하세요")
    private String address;

    private String detailAddr;
    private String subAddr;
}
