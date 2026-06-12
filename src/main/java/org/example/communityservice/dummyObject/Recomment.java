package org.example.communityservice.dummyObject;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public class Recomment {
    private UUID recommentUuid;
    private UUID commentUuid;
    private UUID recommentWriterUuid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recommentDate;
    private String recommentContent;
}
