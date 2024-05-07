package hannyanggang.catchdoctor.controller;

import hannyanggang.catchdoctor.dto.ReviewDto;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.UserRepository;
import hannyanggang.catchdoctor.response.Response2;
import hannyanggang.catchdoctor.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    @GetMapping("/{hospital_id}/all")
    public ResponseEntity<List<ReviewDto>> getList(@PathVariable("hospital_id") Long hospital_id){
        List<ReviewDto> reviewDTOList = reviewService.getListOfHospital(hospital_id);
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    @PostMapping("/{hospital_id}")
    public ResponseEntity<Long> addReview(@PathVariable Long hospital_id, @RequestBody ReviewDto hospitalReviewDTO){
        User user = getPrincipal(); // 현재 로그인한 사용자 정보를 가져옴
        Long review_id = reviewService.register(hospitalReviewDTO, user, hospital_id);
        return new ResponseEntity<>(review_id, HttpStatus.OK);
    }

    @PutMapping("/{hospital_id}/{review_id}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long review_id, @RequestBody ReviewDto hospitalReviewDTO){
        // 권한 확인 및 리뷰 수정
        reviewService.modify(review_id, hospitalReviewDTO);
        return new ResponseEntity<>(review_id, HttpStatus.OK);
    }

    @DeleteMapping("/{hospital_id}/{review_id}")
    public ResponseEntity<Long> removeReview(@PathVariable Long review_id){
        // 권한 확인 및 리뷰 삭제
        reviewService.remove(review_id);
        return new ResponseEntity<>(review_id, HttpStatus.OK);
    }

    private User getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserid(Long.parseLong(authentication.getName()));
    }
}

