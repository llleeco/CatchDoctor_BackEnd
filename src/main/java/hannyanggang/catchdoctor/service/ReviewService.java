package hannyanggang.catchdoctor.service;

import hannyanggang.catchdoctor.dto.ReviewDto;
import hannyanggang.catchdoctor.entity.BookMark;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.Review;
import hannyanggang.catchdoctor.entity.User;
import hannyanggang.catchdoctor.repository.ReviewRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final HospitalRepository hospitalRepository;

    public List<ReviewDto> getListOfHospital(Long hospital_id) {
        Optional<Hospital> optionalHospital = hospitalRepository.findById(hospital_id);
        if (optionalHospital.isPresent()) {
            Hospital hospital = optionalHospital.get();
            List<Review> result = reviewRepository.findByHospital(hospital);
            return result.stream().map(this::entityToDto).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public Long register(ReviewDto hospitalReviewDTO, User user, Long hospital_id) {
        Optional<Hospital> optionalHospital = hospitalRepository.findById(hospital_id);
        if (optionalHospital.isPresent()) {
            Hospital hospital = optionalHospital.get();
            Review hospitalReview = dtoToEntity(hospitalReviewDTO, user, hospital);
            reviewRepository.save(hospitalReview);
            return hospitalReview.getReview_id();
        } else {
            // 병원이 존재하지 않는 경우 처리할 방법 추가
            return null;
        }
    }

    public void modify(Long review_id, ReviewDto hospitalReviewDTO) {
        Optional<Review> result = reviewRepository.findById(review_id);
        if(result.isPresent()){
            Review hospitalReview = result.get();
            hospitalReview.changeGrade(hospitalReviewDTO.getGrade());
            hospitalReview.changeText(hospitalReviewDTO.getText());
            reviewRepository.save(hospitalReview);
        }
    }

    public void remove(Long reviewnum) {
        reviewRepository.deleteById(reviewnum);
    }

    private Review dtoToEntity(ReviewDto hospitalReviewDTO, User user, Hospital hospital){
        Review hospitalReview = Review.builder()
                .hospital(hospital)
                .user(user)
                .grade(hospitalReviewDTO.getGrade())
                .text(hospitalReviewDTO.getText())
                .build();
        return hospitalReview;
    }

    private ReviewDto entityToDto(Review hospitalReview){
        ReviewDto hospitalReviewDTO = ReviewDto.builder()
                .review_id(hospitalReview.getReview_id())
                .hospital_id(hospitalReview.getHospital().getHospitalid())
                .user_id(hospitalReview.getUser().getUserid())
                .username(hospitalReview.getUser().getName())
                .grade(hospitalReview.getGrade())
                .text(hospitalReview.getText())
                .regDate(hospitalReview.getRegDate())
                .modDate(hospitalReview.getModDate())
                .build();
        return hospitalReviewDTO;
    }
}
