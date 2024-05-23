package hannyanggang.catchdoctor.service;

import hannyanggang.catchdoctor.dto.BoardLikeListDto;
import hannyanggang.catchdoctor.dto.BookmarkListDto;
import hannyanggang.catchdoctor.dto.LoginRequestDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalFindAllResponseDto;
import hannyanggang.catchdoctor.dto.hospitalDto.HospitalFindAllWithPagingResponseDto;
import hannyanggang.catchdoctor.dto.hospitalDto.PageInfoDto;
import hannyanggang.catchdoctor.dto.userDto.UserRegisterDto;
import hannyanggang.catchdoctor.entity.Board;
import hannyanggang.catchdoctor.entity.BoardLike;
import hannyanggang.catchdoctor.entity.BookMark;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.BoardLikeRepository;
import hannyanggang.catchdoctor.repository.BoardRepository;
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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;
    private final HospitalRepository hospitalRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
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
    public List<BookmarkListDto> findBookmarkAll(){
        return bookMarkRepository.findAll().stream()
                .map(BookmarkListDto::new)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public HospitalFindAllWithPagingResponseDto findBookmarkHospitals(Integer page, User user) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<BookMark> bookMarks  = bookMarkRepository.findAllByUser(user,pageRequest);
        List<HospitalFindAllResponseDto> hospitalWithDto = bookMarks.stream().map(BookMark::getHospital).map(HospitalFindAllResponseDto::toDto)
                .collect(toList());

        return HospitalFindAllWithPagingResponseDto.toDto(hospitalWithDto, new PageInfoDto(bookMarks));

    }

    public String updateBoardLike(Long id, User user){
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if(optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            if(!hasBoardLike(board, user)){
                return createBoardLike(board, user);
            }
            return removeBoardLike(board, user);
        }
        // 처리할 Board이 존재하지 않을 경우에 대한 로직
        return "게시글을 찾을 수 없습니다.";
    }
    private boolean hasBoardLike(Board board, User user) {
        return boardLikeRepository.findByBoardAndUser(board, user).isPresent();
    }

    private String createBoardLike(Board board, User user){
        BoardLike boardLike = new BoardLike(board, user);
        boardLikeRepository.save(boardLike);
        return "이 게시글을 좋아합니다.";
    }
    private String removeBoardLike(Board board, User user){
        Optional<BoardLike> boardLike = boardLikeRepository.findByBoardAndUser(board, user);
        if (boardLike.isPresent()) {
            boardLikeRepository.delete(boardLike.get());
            return "이 게시글 좋아요를 취소합니다.";
        } else {
            // 즐겨찾기가 이미 존재하지 않는 경우에 대한 처리
            return "게시글 좋아요가 되어있지 않습니다.";
        }
    }
    public List<BoardLikeListDto> findBoardLikeAll(){
        return boardLikeRepository.findAll().stream()
                .map(BoardLikeListDto::new)
                .collect(toList());
    }
    public List<BoardLikeListDto> findBoardLike(Long boardid) {
        return boardLikeRepository.findByBoardId(boardid).stream()
                .map(BoardLikeListDto::new)
                .collect(Collectors.toList());
    }
    public boolean findBoardLikeUser(Long boardId, String Id){
        User user = userRepository.findById(Id);
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (!optionalBoard.isPresent()) {
            throw new ResourceNotFoundException("게시글을 찾을 수 없습니다. 게시글Id : " + boardId);
        }
        Board board = optionalBoard.get();
        Optional<BoardLike> optionalBoardLike = boardLikeRepository.findByBoardAndUser(board,user);
        return optionalBoardLike.isPresent();
    }
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

}
