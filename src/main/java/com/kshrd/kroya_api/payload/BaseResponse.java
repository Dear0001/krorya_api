package com.kshrd.kroya_api.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kshrd.kroya_api.enums.IResponseMessage;
import com.kshrd.kroya_api.enums.ResponseMessage;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class BaseResponse<T> {

    private String message;

    @Builder.Default
    private Object payload = "No data available";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String inqCnt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object inqRec;

    private String statusCode;

    @Builder.Default
    private Date timestamp = new Date();

    @JsonIgnore
    @Builder.Default
    private IResponseMessage responseMessage = ResponseMessage.OK;

    @JsonIgnore
    private org.springframework.data.domain.Page<?> rawData;

    public String getMessage() {
        if (this.message != null)
            return this.message;
        if (this.responseMessage != null)
            return this.responseMessage.getMessage();
        return null;
    }

    public String getStatusCode() {
        if (this.statusCode != null)
            return this.statusCode;
        if (this.responseMessage != null)
            return this.responseMessage.getCode();
        return null;
    }

    public Object getPayload() {
        return (this.payload != null) ? this.payload : "No data available";
    }
}
