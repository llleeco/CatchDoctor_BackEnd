package hannyanggang.catchdoctor.service;

import hannyanggang.catchdoctor.dto.BoardDto;
import hannyanggang.catchdoctor.entity.Board;
import hannyanggang.catchdoctor.entity.BoardImage;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.BoardImageRepository;
import hannyanggang.catchdoctor.repository.BoardRepository;
import hannyanggang.catchdoctor.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    // 전체 게시물 조회
    @Transactional(readOnly = true)
    public List<BoardDto> getBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardDto> boardDtos = new ArrayList<>();
        boards.forEach(s -> boardDtos.add(BoardDto.toDto(s)));
        return boardDtos;
    }

    // 개별 게시물 조회
    @Transactional(readOnly = true)
    public BoardDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Board Id를 찾을 수 없습니다.");
        });
        BoardDto boardDto = BoardDto.toDto(board);
        return boardDto;
    }

    // 게시물 작성
    @Transactional
    public Board write(BoardDto boardDto, User user) {
        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setUser(user);
        board.setRegDate(LocalDate.now());
        board.setRegTime(LocalTime.now());
        boardRepository.save(board);
        return board;
    }

    // 게시물 수정
    @Transactional
    public Board update(Long id, BoardDto boardDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Board Id를 찾을 수 없습니다!");
        });

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setRegDate(LocalDate.now());
        board.setRegTime(LocalTime.now());

        return board;
    }


    // 게시글 삭제
    @Transactional
    public void delete(Long id) {
        // 매개변수 id를 기반으로, 게시글이 존재하는지 먼저 찾음
        // 게시글이 없으면 오류 처리
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Board Id를 찾을 수 없습니다!");
        });

        // 게시글이 있는 경우 삭제처리
        boardRepository.deleteById(id);

    }
    // 이미지 업로드
    public String uploadImage(MultipartFile file) throws IOException {
        log.info("upload file: {}", file);
        BoardImage boardImage = boardImageRepository.save(
                BoardImage.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .boardImage(ImageUtils.compressImage(file.getBytes()))
                        .build());
        if (boardImage != null) {
            log.info("imageData: {}", boardImage);
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    // 이미지 파일로 압축하기
    public byte[] downloadImage(String fileName) {
        BoardImage boardImage = boardImageRepository.findByName(fileName)
                .orElseThrow(RuntimeException::new);

        log.info("download imageData: {}", boardImage);

        return ImageUtils.decompressImage(boardImage.getBoardImage());
    }
}


