package hannyanggang.catchdoctor.controller.hospitalController;

import hannyanggang.catchdoctor.exception.CustomValidationException;
import hannyanggang.catchdoctor.service.hospitalService.AvailableTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RestController
@RequestMapping("/reservations")
public class AvailableTimeController {

    private final AvailableTimeService availableTimeService;

    @GetMapping("/available-times")
    public Map<String, Object> getAvailableTimes(@RequestParam Long hospitalId, @RequestParam String date) {
      //  validateHospitalId(hospitalId);
        validateDate(date);
        return availableTimeService.getAvailableTimes(hospitalId, date);
    }

//    private void validateHospitalId(String hospitalId) {
//        if (!hospitalId.matches("\\d{3}-\\d{2}-\\d{5}")) {
//            throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 형식(병원아이디 3-2-5)");
//        }
//    }

    private void validateDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(), "잘못된 형식(yyyy-MM-dd)");
        }
    }


}