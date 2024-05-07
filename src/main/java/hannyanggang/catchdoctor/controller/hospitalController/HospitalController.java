package hannyanggang.catchdoctor.controller.hospitalController;

import hannyanggang.catchdoctor.dto.BoardDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalDetailDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalSetDto;
import hannyanggang.catchdoctor.dto.reservationsDTO.ReservationsDTO;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.service.hospitalService.HospitalDetailService;
import hannyanggang.catchdoctor.service.hospitalService.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Controller
@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    private final HospitalRepository hospitalRepository;
    private final HospitalService hospitalService;
    private final HospitalDetailService hospitalDetailService;
    public HospitalController(HospitalService hospitalService, HospitalDetailService hospitalDetailService, HospitalRepository hospitalRepository) {
        this.hospitalService = hospitalService;
        this.hospitalDetailService = hospitalDetailService;
        this.hospitalRepository = hospitalRepository;

    }

//    @Operation(summary = "병원 상세정보", description="병원 상세정보 입력")
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/hospitaldetail")
//    public Response<?> hospitaldetail(@RequestBody HospitalDetailDto joinRequest) {
//        return new Response<>("true", "상세정보 입력완료", hospitalDetailService.hospitalInfo(joinRequest));
//    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hospitaldetail")
    public ResponseEntity<?> hospitaldetail(@RequestBody HospitalDetailDto hospitalDetailDto) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
                // 인증된 사용자의 정보를 활용


                String Id = authentication.getName();
                return hospitalDetailService.hospitalMyPage(hospitalDetailDto, Id);

            } else {
                throw new CustomValidationException(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰");
            }


        } catch (CustomValidationException e) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", e.getStatus());
            errorDetails.put("message", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.valueOf(e.getStatus()))
                    .body(errorDetails);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/findhospitaldetails")
    public Response findHospitalDetail(Authentication authentication) {
        String hospitalId = authentication.getName();
        return new Response("조회", "병원 상세정보 조회", hospitalService.getHospitalDetailByHospitalId(hospitalId));
    }

    @PostMapping("/setopenapi")
    public Response setOpenApi(Authentication authentication, @RequestBody HospitalSetDto hospitalSetDto){
        String hospitalId = authentication.getName();
        return new Response("완료", "병원 등록 완료", hospitalService.connectOpenApi(hospitalSetDto,hospitalId));
    }
}
