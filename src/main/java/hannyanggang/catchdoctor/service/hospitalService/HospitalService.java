package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.hospitalDto.*;
import hannyanggang.catchdoctor.entity.*;
import hannyanggang.catchdoctor.repository.BookMarkRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalDetailRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.OpenApiRepository;
import hannyanggang.catchdoctor.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final HospitalDetailRepository hospitalDetailRepository;
    private final OpenApiRepository openApiRepository;
    private final BookMarkRepository bookMarkRepository;

    @Transactional
    public String updateBookmarkHospital(Long id, User user) {
        Optional<Hospital> optionalHospital = hospitalRepository.findByHospitalid(id);
        if(optionalHospital.isPresent()) {
            Hospital hospital = optionalHospital.get();
            if(!hasBookmarkHospital(hospital, user)){
                return createBookmarkHospital(hospital, user);
            }
            return removeBookmarkHospital(hospital, user);
        }
        // 처리할 Hospital이 존재하지 않을 경우에 대한 로직
        return "Hospital not found";
    }


    private boolean hasBookmarkHospital(Hospital hospital, User user) {
        return bookMarkRepository.findByHospitalAndUser(hospital, user).isPresent();
    }

    private String createBookmarkHospital(Hospital hospital, User user){
        BookMark bookmark = new BookMark(hospital, user);
        bookMarkRepository.save(bookmark);
        return "이 병원을 즐겨찾기에 추가합니다.";
    }
    private String removeBookmarkHospital(Hospital hospital, User user){
        Optional<BookMark> bookmark = bookMarkRepository.findByHospitalAndUser(hospital, user);
        if (bookmark.isPresent()) {
            bookMarkRepository.delete(bookmark.get());
            return "이 병원을 즐겨찾기에서 삭제합니다.";
        } else {
            // 즐겨찾기가 이미 존재하지 않는 경우에 대한 처리
            return "이 병원이 즐겨찾기에 존재하지 않습니다.";
        }
    }


    public Hospital register(HospitalRegisterDto registerDto) {
        Hospital hospital = Hospital.builder()
                .id(registerDto.getId())
                .password(registerDto.getPassword())
                .name(registerDto.getName())
                .addnum(registerDto.getAddnum())
                .role(UserRole.USER)
                .build();
        return hospitalRepository.save(hospital);
    }

    public List<Hospital> findAll() {
        return hospitalRepository.findAll();
    }

    public Hospital findHospital(Long hospitalid) {
        return hospitalRepository.findById(hospitalid).orElseThrow(()-> {
            return new IllegalArgumentException("User ID를 찾을 수 없습니다.");
        });
    }

    public Hospital login(LoginRequestDto loginDto){
        //getUsername- 로그인 아이디 가져오기
        Optional<Hospital> optionalHospital = Optional.ofNullable(hospitalRepository.findById(loginDto.getId()));

        if(optionalHospital.isEmpty()){
            return null;
        }

        Hospital hospital=optionalHospital.get();

        if(!hospital.getPassword().equals(loginDto.getPassword())){
            return null;
        }

        return hospital;

    }

    public Hospital getLoginUserByLoginId(String id) {
        if(id == null) return null;

        Optional<Hospital> optionalUser = Optional.ofNullable(hospitalRepository.findById(id));
        return optionalUser.orElse(null);

    }

//    public List<HospitalDTO> searchByDepartment(String department, int page, int size) {
//        // 오늘 날짜에 해당하는 요일을 한글로 구합니다.
//        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
//        PageRequest pageRequest = PageRequest.of(page,size);
//        Page<Hospital> hospitals = hospitalRepository.findByDepartment(department, pageRequest);
//
//        // 병원 목록을 HospitalDTO 목록으로 변환
//        return hospitals.stream().map(hospital -> {
//            // HospitalDTO 객체 생성
//            return new HospitalDTO(
//                    hospital.getHospitalid(),
//                    hospital.getName(),
//                    hospit
//            );
//        }).collect(toList());
//    }
//
//    public List<HospitalDTO> getAllHospitals(int page, int size) {
//
//        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date());
//        PageRequest pageRequest = PageRequest.of(page,size);
//        Page<Hospital> hospitals = hospitalRepository.findAll(pageRequest);
//
//        // 병원 목록을 HospitalDTO 목록으로 변환
//        return hospitals.stream().map(hospital -> {
//            // 운영시간 및 좋아요 수 조회
//            String operatingHours = operationTimeRepository.findOperatingHoursByHospitalIdAndDay(hospital.getHospitalid(), today);
//
//            // HospitalDTO 객체 생성
//            return new HospitalDTO(
//                    hospital.getHospitalid(),
//                    hospital.getName(),
//                    hospital.getDepartment(),
//                    operatingHours
//            );
//        }).collect(toList());
//
//    }

    @Transactional(readOnly = true)
    public HospitalFindAllWithPagingResponseDto findBookmarkHospitals(Integer page,  User user) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<BookMark> bookMarks  = bookMarkRepository.findAllByUser(user,pageRequest);
        List<HospitalFindAllResponseDto> hospitalWithDto = bookMarks.stream().map(BookMark::getHospital).map(HospitalFindAllResponseDto::toDto)
                .collect(toList());

        return HospitalFindAllWithPagingResponseDto.toDto(hospitalWithDto, new PageInfoDto(bookMarks));

    }

    public HospitalDetail getHospitalDetailByHospitalId(String hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId);
        HospitalDetail hospitalDetail = hospitalDetailRepository.findByHospital(hospital);
        return hospitalDetail;
    }

    public Hospital connectOpenApi(String hospitalId) {
       Hospital hospital = hospitalRepository.findById(hospitalId);
        String addnum = hospital.getAddnum();
        String hospitalname = hospital.getName();
        OpenApiHospital openapi = openApiRepository.findByAddressAndHospitalName(addnum,hospitalname);

        // 이미 연결된 openapi가 존재하는지 확인
        OpenApiHospital existingOpenApi = openApiRepository.findByHospital(hospital);
        if (existingOpenApi != null) {
            throw new RuntimeException("이미 연결된 openapi가 존재합니다.");
        }

        openapi.setHospital(hospital);
        return hospitalRepository.save(hospital);
    }


}
