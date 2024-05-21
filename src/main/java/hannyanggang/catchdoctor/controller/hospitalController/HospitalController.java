package hannyanggang.catchdoctor.controller.hospitalController;

import hannyanggang.catchdoctor.dto.hospitalDto.SearchResponseDto;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalDetailDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalSetDto;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.response.Response2;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.service.hospitalService.HospitalDetailService;
import hannyanggang.catchdoctor.service.hospitalService.HospitalService;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalDTO;
import hannyanggang.catchdoctor.service.hospitalService.OpenApiService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RestController
@RequestMapping("/hospitals")
public class HospitalController {
    private final HospitalService hospitalService;
    private final HospitalDetailService hospitalDetailService;
    private final OpenApiService openApiService;
    private final UserRepository userRepository;

    //진료과목 리스트
    private static final List<String> VALID_DEPARTMENTS = Arrays.asList("소아청소년과", "내과", "이비인후과", "정형외과", "안과", "산부인과", "신경외과", "피부과", "정신건강의학과");

    public HospitalController(HospitalService hospitalService, OpenApiService openApiService, UserRepository userRepository, HospitalRepository hospitalRepository, HospitalDetailService hospitalDetailService, OpenApiService openApiService1, UserRepository userRepository1) {
        this.hospitalService = hospitalService;
        this.hospitalDetailService = hospitalDetailService;
        this.openApiService = openApiService1;
        this.userRepository = userRepository1;
    }

    @Operation(summary = "특정 병원 정보", description = "특정 병원 정보요청하기")
    @GetMapping("/findhospital/{hospitalid}")
    public Response findHospital(@PathVariable Long hospitalid) {
        return new Response("검색성공", "병원 정보 검색성공", hospitalService.findHospital(hospitalid));
    }

    @Operation(summary = "병원 검색", description="병원 검색하기")
    @GetMapping
    public ResponseEntity<?> searchHospitals(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String department,
            @RequestParam(required = true)  Double mapx,
            @RequestParam(required = true) Double mapy,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "200", required = false) int limit) {

        List<HospitalDTO> hospitalDTOs;
        List<SearchResponseDto> responseDTOs = null;

        Map<String, Object> errorBody = new HashMap<>();
        try {
            if ((query == null || query.isEmpty()) && (department == null || department.isEmpty())) { //쿼리 입력값 없을 때 모든 결과 조회
                responseDTOs = openApiService.getAllHospitals(page, limit);
            } /*else if ((query != null && !Pattern.matches("^[가-힣\\s]+$", query)) || (department != null && !Pattern.matches("^[가-힣]+$", department))) {
                throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 입력(한글만 입력)");
            }*/ else {
                if (department != null && !department.isEmpty() && (query == null || query.isEmpty())) { //진료과목으로 검색
                    // department 파라미터에 대한 유효성 검사 (여기에서 유효성 검사 추가)
                    String[] departments = department.split(",");
                    for (String dept : departments) {
                        if (!isValidDepartment(dept.trim())) {
                            throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 입력");
                        }
                    }
                    responseDTOs = openApiService.searchByDepartment(departments, mapx, mapy, page, limit);

                } else if (query != null && !query.isEmpty()) { //쿼리로 검색
                    responseDTOs = openApiService.searchHospitalsWithDetails(query, mapx, mapy, page, limit);
                } else {
                    throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 입력");
                }
            }
            return ResponseEntity.ok(responseDTOs);

        } catch (CustomValidationException e) {
            // CustomValidationException에 대한 특별한 처리
            errorBody.put("status", HttpStatus.BAD_REQUEST.value());
            errorBody.put("message", "입력 형식 오류");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorBody);
        } catch (Exception e) {
            errorBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorBody.put("message", "검색 오류");

            // 예외 발생 시 클라이언트에게 오류 메시지 반환
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorBody);
        }

    }
    private boolean isValidDepartment(String department) {
        return VALID_DEPARTMENTS.contains(department);
        // 진료과목이 유효한 경우 true 반환, 그렇지 않으면 false 반환
    }

    // 병원 detaill 작성
    @Operation(summary = "병원 상세정보", description="병원 상세정보 입력")
    @PostMapping("/hospitaldetail")
    public Response hospitaldetail(Authentication authentication, @RequestBody HospitalDetailDto hospitalDetailDto) {
        String Id = authentication.getName();
        return new Response("입력완료", "병원 정보 입력완료", hospitalDetailService.hospitalMyPage(hospitalDetailDto, Id));
    }

    // 병원 detail 찾기
    @Operation(summary = "병원 상세정보 찾기", description="본인의 병원 상세정보 요청")
    @GetMapping("/findhospitaldetails")
    public Response findHospitalDetail(Authentication authentication) {
        String hospitalId = authentication.getName();
        return new Response("조회", "병원 상세정보 조회", hospitalService.getHospitalDetailByHospitalId(hospitalId));
    }

    // 병원 oepnapi 연결하기
    @Operation(summary = "병원 연결", description="DB에 저장된 병원과 연결하기")
    @PostMapping("/setopenapi/{addnum}")
    public Response setOpenApi(Authentication authentication, @PathVariable String addnum){
        String hospitalId = authentication.getName(); // getName() -> login Id
        return new Response("완료", "병원 등록 완료", hospitalService.connectOpenApi(hospitalId,addnum));
    }

    @GetMapping("/checkhospital/{hospitalname}")
    public Response checkhospital(@PathVariable String hospitalname){
        return new Response("성공", "등록된 병원이름 확인", openApiService.checkhospital(hospitalname));
    }
}
