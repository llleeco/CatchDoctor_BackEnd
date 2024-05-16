package hannyanggang.catchdoctor.service;

import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalFindAllResponseDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalFindAllWithPagingResponseDto;
import hannyanggang.catchdoctor.dto.hospitalDto.PageInfoDto;
import hannyanggang.catchdoctor.dto.userDto.UserRegisterDto;
import hannyanggang.catchdoctor.entity.BookMark;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.BookMarkRepository;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
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

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;
    private final HospitalRepository hospitalRepository;
   // private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public User register(UserRegisterDto registerDto) {
        User user = User.builder()
                .id(registerDto.getId())
                .password(registerDto.getPassword())
                .name(registerDto.getName())
                .birthday(registerDto.getBirthday())
                .role(UserRole.USER)
                .build();
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUser(Long userid) {
        return userRepository.findById(userid).orElseThrow(()-> {
            return new IllegalArgumentException("User ID를 찾을 수 없습니다.");
        });
    }

    public User login(LoginRequestDto loginDto){
        //getUsername- 로그인 아이디 가져오기
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findById(loginDto.getId()));

        if(optionalUser.isEmpty()){
            return null;
        }

        User user=optionalUser.get();

        if(!user.getPassword().equals(loginDto.getPassword())){
            return null;
        }

        return user;

    }

    public User getLoginUserByLoginId(String id) {
        if(id == null) return null;

        Optional<User> optionalUser = Optional.ofNullable(userRepository.findById(id));
        return optionalUser.orElse(null);

    }

    public boolean checkLoginIdDuplicate(String id) {
        return userRepository.existsById(id);
    }

//    public void join2(UserRegisterDto joinRequest) {
//        userRepository.save(joinRequest.toEntity(encoder.encode(joinRequest.getPassword())));
//    }

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

    @Transactional(readOnly = true)
    public HospitalFindAllWithPagingResponseDto findBookmarkHospitals(Integer page, User user) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<BookMark> bookMarks  = bookMarkRepository.findAllByUser(user,pageRequest);
        List<HospitalFindAllResponseDto> hospitalWithDto = bookMarks.stream().map(BookMark::getHospital).map(HospitalFindAllResponseDto::toDto)
                .collect(toList());

        return HospitalFindAllWithPagingResponseDto.toDto(hospitalWithDto, new PageInfoDto(bookMarks));

    }
}
