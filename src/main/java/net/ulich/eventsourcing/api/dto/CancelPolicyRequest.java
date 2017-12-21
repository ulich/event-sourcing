package net.ulich.eventsourcing.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.time.LocalDate;

@Value
public class CancelPolicyRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate coverEndDate;
}
