package hannyanggang.catchdoctor;

import hannyanggang.catchdoctor.exception.CustomValidationException;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeRange { //운영시간과 휴게시간 처리
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeRange(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimeRange fromString(String timeRangeStr) {
        // timeRangeStr 문자열이 null이거나 비어있는 경우 처리
        if (timeRangeStr == null || timeRangeStr.trim().isEmpty()) {
            // 운영 시간이 없는 경우 예약 가능한 시간 없음을 나타냄
            return null;
        }

        try {
            String[] parts = timeRangeStr.split("~");
            if (parts.length != 2) {
                throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(),"Invalid time range format");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime start = LocalTime.parse(parts[0], formatter);
            LocalTime end = LocalTime.parse(parts[1], formatter);

            return new TimeRange(start, end);
        } catch (DateTimeParseException e) {
            throw new CustomValidationException(HttpStatus.BAD_REQUEST.value(),"Invalid time range format");
        } catch (Exception e){
            throw new CustomValidationException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "운영 시간 조회 에러");
        }
    }


    // Getter와 Setter
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<TimeRange> subtract(TimeRange other) {
        // 휴게 시간이 운영 시간에 포함되지 않는 경우
        if (other == null) {
            return Collections.singletonList(this);
        }

        if (other.endTime.isBefore(this.startTime) || other.startTime.isAfter(this.endTime)) {
            return Collections.singletonList(this);
        }

        // 휴게 시간이 운영 시간 내에 완전히 포함되는 경우
        List<TimeRange> result = new ArrayList<>();
        if (other.startTime.isAfter(this.startTime)) {
            result.add(new TimeRange(this.startTime, other.startTime));
        }
        if (other.endTime.isBefore(this.endTime)) {
            result.add(new TimeRange(other.endTime, this.endTime));
        }
        return result;
    }
}
