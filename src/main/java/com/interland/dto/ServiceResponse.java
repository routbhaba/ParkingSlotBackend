/****************************************************************************
 *                              COPYRIGHT NOTICE
*
*                      Copyright(@2006) by Interland Technology Services PVT. LTD **
*
*      This program is used to monitor the stream control and Stop/Start
*      the streams. The program and related materials are confidential and
*      proprietary of Interland Technology Services PVT. LTD and no part of these materials
*      should be reproduced, published in any forms without the written
*      approval of INTERLAND
*
** Project Name         : iPSH
** Program description  : ServiceResponse
** Version No           : 1.0.0
** Author               : Yadhu
** Date Created         : 15-April-2020
** Modification Log     :   
CRId/ProjectId	Date Modified      	User		         Description		
Prod_1.0.0	               			 
*****************************************************************************/
package com.interland.dto;

import java.util.List;

import org.json.simple.JSONObject;

public class ServiceResponse {

	public ServiceResponse(String code,String message, List<JSONObject> details) {
		super();
		this.message = message;
		this.details = details;
		this.code = code;
	}

	// General error message about nature of error
	private String message;

	private String code;

	// Specific errors in API request processing
	private List<JSONObject> details;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<JSONObject> getDetails() {
		return details;
	}

	public void setDetails(List<JSONObject> details) {
		this.details = details;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
