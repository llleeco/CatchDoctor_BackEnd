package hannyanggang.catchdoctor.dto.hospitalDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HospitalFindAllWithPagingResponseDto {
    private List<HospitalFindAllResponseDto> boards;
    private PageInfoDto pageInfoDto;

    public static HospitalFindAllWithPagingResponseDto toDto(List<HospitalFindAllResponseDto> hospitals, PageInfoDto pageInfoDto) {
        return new HospitalFindAllWithPagingResponseDto(hospitals, pageInfoDto);
    }
}
