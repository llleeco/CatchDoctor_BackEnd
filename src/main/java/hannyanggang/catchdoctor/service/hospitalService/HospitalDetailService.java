package hannyanggang.catchdoctor.service.hospitalService;

import hannyanggang.catchdoctor.dto.hospitalDto.HospitalDetailDto;
import hannyanggang.catchdoctor.entity.Hospital;
import hannyanggang.catchdoctor.entity.HospitalDetail;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalDetailRepository;
import hannyanggang.catchdoctor.repository.hospitalRepository.HospitalRepository;
import hannyanggang.catchdoctor.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalDetailService {
    private final HospitalDetailRepository hospitalDetailRepository;
    private final HospitalRepository hospitalRepository;
    public HospitalDetail hospitalMyPage(HospitalDetailDto hospitalDetailsDto, String hospitalId, MultipartFile[] files) throws IOException {
        Hospital hospital = hospitalRepository.findById(hospitalId);

        HospitalDetail hospitalDetail = HospitalDetail.builder()
                .hospital(hospital)
                .hospitalInfo(hospitalDetailsDto.getHospitalInfo())
                .department(hospitalDetailsDto.getDepartment())
                .doctorInfo(hospitalDetailsDto.getDoctorInfo())
                .mon_open(hospitalDetailsDto.getMon_open())
                .mon_close(hospitalDetailsDto.getMon_close())
                .tue_open(hospitalDetailsDto.getTue_open())
                .tue_close(hospitalDetailsDto.getTue_close())
                .wed_open(hospitalDetailsDto.getWed_open())
                .wed_close(hospitalDetailsDto.getWed_close())
                .thu_open(hospitalDetailsDto.getThu_open())
                .thu_close(hospitalDetailsDto.getThu_close())
                .fri_open(hospitalDetailsDto.getFri_open())
                .fri_close(hospitalDetailsDto.getFri_close())
                .sat_open(hospitalDetailsDto.getSat_open())
                .sat_close(hospitalDetailsDto.getSat_close())
                .sun_open(hospitalDetailsDto.getSun_open())
                .sun_close(hospitalDetailsDto.getSun_close())
                .hol_open(hospitalDetailsDto.getHol_open())
                .hol_close(hospitalDetailsDto.getHol_close())
                .lunch_start(hospitalDetailsDto.getLunch_start())
                .lunch_end(hospitalDetailsDto.getLunch_end())
                .build();

        // 파일이 존재하는 경우에만 이미지를 처리
        if (files[0] != null) {
            for (int i = 0; i < files.length && i < 5; i++) {
                byte[] compressedImage = ImageUtils.compressImage(files[i].getBytes());
                switch (i) {
                    case 0:
                        hospitalDetail.setBoardImage1(compressedImage);
                        break;
                    case 1:
                        hospitalDetail.setBoardImage2(compressedImage);
                        break;
                    case 2:
                        hospitalDetail.setBoardImage3(compressedImage);
                        break;
                    case 3:
                        hospitalDetail.setBoardImage4(compressedImage);
                        break;
                    case 4:
                        hospitalDetail.setBoardImage5(compressedImage);
                        break;
                }
            }
        }

        hospital.setHospitalDetail(hospitalDetail);
        hospitalDetailRepository.save(hospitalDetail);
        hospitalRepository.save(hospital);

        return hospitalDetail;
    }
    public HospitalDetail modifyHospitalMyPage (HospitalDetailDto hospitalDetailsDto, Long detail_id, MultipartFile[] files) throws IOException {
        Optional<HospitalDetail> optionalHospitalDetail = hospitalDetailRepository.findById(detail_id);
        HospitalDetail hospitalDetail = optionalHospitalDetail.get();
        hospitalDetail.setHospitalInfo(hospitalDetailsDto.getHospitalInfo());
        hospitalDetail.setDepartment(hospitalDetailsDto.getDepartment());
        hospitalDetail.setDoctorInfo(hospitalDetailsDto.getDoctorInfo());
        hospitalDetail.setMon_open(hospitalDetailsDto.getMon_open());
        hospitalDetail.setMon_close(hospitalDetailsDto.getMon_close());
        hospitalDetail.setTue_open(hospitalDetailsDto.getTue_open());
        hospitalDetail.setTue_close(hospitalDetailsDto.getTue_close());
        hospitalDetail.setWed_open(hospitalDetailsDto.getWed_open());
        hospitalDetail.setWed_close(hospitalDetailsDto.getWed_close());
        hospitalDetail.setThu_open(hospitalDetailsDto.getThu_open());
        hospitalDetail.setThu_close(hospitalDetailsDto.getThu_close());
        hospitalDetail.setFri_open(hospitalDetailsDto.getFri_open());
        hospitalDetail.setFri_close(hospitalDetailsDto.getFri_close());
        hospitalDetail.setSat_open(hospitalDetailsDto.getSat_open());
        hospitalDetail.setSat_close(hospitalDetailsDto.getSat_close());
        hospitalDetail.setSun_open(hospitalDetailsDto.getSun_open());
        hospitalDetail.setSun_close(hospitalDetailsDto.getSun_close());
        hospitalDetail.setHol_open(hospitalDetailsDto.getHol_open());
        hospitalDetail.setHol_close(hospitalDetailsDto.getHol_close());
        hospitalDetail.setLunch_start(hospitalDetailsDto.getLunch_start());
        hospitalDetail.setLunch_end(hospitalDetailsDto.getLunch_end());
        if (files[0] != null) {
            hospitalDetail.setBoardImage1(null);
            hospitalDetail.setBoardImage2(null);
            hospitalDetail.setBoardImage3(null);
            hospitalDetail.setBoardImage4(null);
            hospitalDetail.setBoardImage5(null);
            for (int i = 0; i < files.length && i < 5; i++) {
                byte[] compressedImage = null;
                if (files[i] != null) {
                    compressedImage = ImageUtils.compressImage(files[i].getBytes());
                }
                switch (i) {
                    case 0:
                        hospitalDetail.setBoardImage1(compressedImage);
                        break;
                    case 1:
                        hospitalDetail.setBoardImage2(compressedImage);
                        break;
                    case 2:
                        hospitalDetail.setBoardImage3(compressedImage);
                        break;
                    case 3:
                        hospitalDetail.setBoardImage4(compressedImage);
                        break;
                    case 4:
                        hospitalDetail.setBoardImage5(compressedImage);
                        break;
                }
            }
        }

        return hospitalDetailRepository.save(hospitalDetail);
        }
    // 이미지 파일로 압축하기
    public List<byte[]> downloadImagesBoard(Long detailId) {
        Optional<HospitalDetail> optionalHospitalDetail = hospitalDetailRepository.findById(detailId);
        HospitalDetail hospitalDetail = optionalHospitalDetail.orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다. 병원ID : " + detailId));

        List<byte[]> compressedImages = new ArrayList<>();

        if (hospitalDetail.getBoardImage1() != null) {
            byte[] compressedImage1 = ImageUtils.decompressImage(hospitalDetail.getBoardImage1());
            compressedImages.add(compressedImage1);
        }
        if (hospitalDetail.getBoardImage2() != null) {
            byte[] compressedImage2 = ImageUtils.decompressImage(hospitalDetail.getBoardImage2());
            compressedImages.add(compressedImage2);
        }
        if (hospitalDetail.getBoardImage3() != null) {
            byte[] compressedImage3 = ImageUtils.decompressImage(hospitalDetail.getBoardImage3());
            compressedImages.add(compressedImage3);
        }
        if (hospitalDetail.getBoardImage4() != null) {
            byte[] compressedImage4 = ImageUtils.decompressImage(hospitalDetail.getBoardImage4());
            compressedImages.add(compressedImage4);
        }
        if (hospitalDetail.getBoardImage5() != null) {
            byte[] compressedImage5 = ImageUtils.decompressImage(hospitalDetail.getBoardImage5());
            compressedImages.add(compressedImage5);
        }

        return compressedImages;
    }
}
