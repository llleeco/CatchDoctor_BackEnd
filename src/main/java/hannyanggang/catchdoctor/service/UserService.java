package hannyanggang.catchdoctor.service;

import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.userDto.UserRegisterDto;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
   // private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public User register(UserRegisterDto registerDto) {
        User user = User.builder()
                .id(registerDto.getId())
                .password(registerDto.getPassword())
                .name(registerDto.getName())
//                .birthday(registerDto.getBirthday())
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
}
