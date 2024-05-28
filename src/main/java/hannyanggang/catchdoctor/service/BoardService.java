package hannyanggang.catchdoctor.service;

import hannyanggang.catchdoctor.dto.BoardDto;
import hannyanggang.catchdoctor.entity.Board;
import hannyanggang.catchdoctor.entity.User;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

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
    public Board write(BoardDto boardDto, User user, MultipartFile[] files) throws IOException {
        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setUser(user);
        if(files[0] != null){
            for (int i = 0; i < files.length && i < 5; i++) {
                byte[] compressedImage = ImageUtils.compressImage(files[i].getBytes());
                switch (i) {
                    case 0:
                        board.setBoardImage1(compressedImage);
                        break;
                    case 1:
                        board.setBoardImage2(compressedImage);
                        break;
                    case 2:
                        board.setBoardImage3(compressedImage);
                        break;
                    case 3:
                        board.setBoardImage4(compressedImage);
                        break;
                    case 4:
                        board.setBoardImage5(compressedImage);
                        break;
                }
            }
        }
        board.setRegDate(LocalDate.now());
        board.setRegTime(LocalTime.now());
        boardRepository.save(board);
        return board;
    }
    // 게시물 작성 이미지 x
    @Transactional
    public Board write2(BoardDto boardDto, User user) {
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
    public Board update(Long id, BoardDto boardDto, MultipartFile[] files) throws IOException {
        Board board = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Board Id를 찾을 수 없습니다!");
        });

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        if (files[0] != null) {
            board.setBoardImage1(null);
            board.setBoardImage2(null);
            board.setBoardImage3(null);
            board.setBoardImage4(null);
            board.setBoardImage5(null);
            for (int i = 0; i < files.length && i < 5; i++) {
                byte[] compressedImage = null;
                if (files[i] != null) {
                    compressedImage = ImageUtils.compressImage(files[i].getBytes());
                }
                switch (i) {
                    case 0:
                        board.setBoardImage1(compressedImage);
                        break;
                    case 1:
                        board.setBoardImage2(compressedImage);
                        break;
                    case 2:
                        board.setBoardImage3(compressedImage);
                        break;
                    case 3:
                        board.setBoardImage4(compressedImage);
                        break;
                    case 4:
                        board.setBoardImage5(compressedImage);
                        break;
                }
            }
        }
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
    // 이미지 파일로 압축하기
    public List<byte[]> downloadImagesBoard(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. 게시글ID : " + boardId));

        List<byte[]> compressedImages = new ArrayList<>();

        if (board.getBoardImage1() != null) {
            byte[] compressedImage1 = ImageUtils.decompressImage(board.getBoardImage1());
            compressedImages.add(compressedImage1);
        }
        if (board.getBoardImage2() != null) {
            byte[] compressedImage2 = ImageUtils.decompressImage(board.getBoardImage2());
            compressedImages.add(compressedImage2);
        }
        if (board.getBoardImage3() != null) {
            byte[] compressedImage3 = ImageUtils.decompressImage(board.getBoardImage3());
            compressedImages.add(compressedImage3);
        }
        if (board.getBoardImage4() != null) {
            byte[] compressedImage4 = ImageUtils.decompressImage(board.getBoardImage4());
            compressedImages.add(compressedImage4);
        }
        if (board.getBoardImage5() != null) {
            byte[] compressedImage5 = ImageUtils.decompressImage(board.getBoardImage5());
            compressedImages.add(compressedImage5);
        }

        return compressedImages;
    }
}

