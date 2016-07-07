package com.thinknear.test.exception;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
public class ExceptionMessage {

    private int status;
    private String error;
    private String message;
    private String reason;

    @ApiModelProperty(value = "Http Status Code", position = 1)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @ApiModelProperty(value = "Http Status Reason Phrase", position = 2)
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @ApiModelProperty(value = "Service Error Code", position = 3)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ApiModelProperty(value = "Service Error Detailed Message", position = 4)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
