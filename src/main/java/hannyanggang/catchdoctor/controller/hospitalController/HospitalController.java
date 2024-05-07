package hannyanggang.catchdoctor.controller.hospitalController;

import hannyanggang.catchdoctor.dto.hospitalDto.SearchResponseDto;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.response.Response;
import hannyanggang.catchdoctor.response.Response2;
import hannyanggang.catchdoctor.service.hospitalService.HospitalService;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalDTO;
import hannyanggang.catchdoctor.service.hospitalService.OpenApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Controller
@RestController
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    private final OpenApiService openApiService;
    private final UserRepository userRepository;

    //진료과목 리스트
    private static final List<String> VALID_DEPARTMENTS = Arrays.asList("소아청소년과", "내과", "이비인후과", "정형외과", "안과", "산부인과", "신경외과", "피부과", "정신건강의학과");

    public HospitalController(HospitalService hospitalService, OpenApiService openApiService, UserRepository userRepository) {
        this.hospitalService = hospitalService;
        this.openApiService = openApiService;
        this.userRepository = userRepository;
    }

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
              //  hospitalDTOs = hospitalService.getAllHospitals(page, limit);
                responseDTOs = openApiService.getAllHospitals(page, limit);
            } /*else if ((query != null && !Pattern.matches("^[가-힣\\s]+$", query)) || (department != null && !Pattern.matches("^[가-힣]+$", department))) {
                throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 입력(한글만 입력)");
            }*/ else {
                if (department != null && !department.isEmpty() && (query == null || query.isEmpty())) { //진료과목으로 검색
                    // department 파라미터에 대한 유효성 검사 (여기에서 유효성 검사 추가)
                    if (!isValidDepartment(department)) {
                        throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 입력");
                    }
               //     responseDTOs = hospitalService.searchByDepartment(department, page, limit);

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

    //병원 즐겨찾기
    @PostMapping("/{id}/bookmarks")
    @ResponseStatus(HttpStatus.OK)
    public Response2 bookmarkHospital(@PathVariable Long id){
        return Response2.success(hospitalService.updateBookmarkHospital(id, getPrincipal()));
    }

    @GetMapping("/bookmarks")
    @ResponseStatus(HttpStatus.OK)
    public Response2 findFavoriteBoards(@RequestParam(defaultValue = "0") Integer page){
        return Response2.success(hospitalService.findBookmarkHospitals(page, getPrincipal()));
    }
    public User getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserid(Long.parseLong(authentication.getName()));
        String loginid = user.getId();
        return userRepository.findByUserid(Long.parseLong(authentication.getName()));

    }
}
