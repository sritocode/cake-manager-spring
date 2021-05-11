package com.waracle.cakemgr.exceptions;
import lombok.Data;

@Data
public class CakeMgrErrorResponse {
private int statusCode;
private String message;
private long timeStamp;

}
