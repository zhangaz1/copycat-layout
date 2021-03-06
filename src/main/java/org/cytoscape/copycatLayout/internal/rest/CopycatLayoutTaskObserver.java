package org.cytoscape.copycatLayout.internal.rest;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.cytoscape.ci.model.CIError;
import org.cytoscape.ci.model.CIResponse;
import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskObserver;

public class CopycatLayoutTaskObserver implements TaskObserver {

	/**
	 * 
	 */
	private final CopycatLayoutResource copyLayoutResource;
	CIResponse<?> response;

	public CIResponse<?> getResponse() {
		return response;
	}

	private CopycatLayoutResult result;
	private String resourcePath;
	private String errorCode;

	public CopycatLayoutTaskObserver(CopycatLayoutResource copycatLayoutResource, String resourcePath, String errorCode) {
		this.copyLayoutResource = copycatLayoutResource;
		response = null;
		this.resourcePath = resourcePath;
		this.errorCode = errorCode;
	}

	@SuppressWarnings("unchecked")
	public void allFinished(FinishStatus arg0) {
		if (arg0.getType() == FinishStatus.Type.SUCCEEDED || arg0.getType() == FinishStatus.Type.CANCELLED) {
			response = new CIResponse<CopycatLayoutResult>();
			
			((CIResponse<CopycatLayoutResult>) response).data = result;
			response.errors = new ArrayList<CIError>();
		} else {
			response = this.copyLayoutResource.buildCIErrorResponse(
					Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resourcePath, errorCode,
					arg0.getException().getMessage(), arg0.getException());
		}
		
	}

	
	public void taskFinished(ObservableTask arg0) {
		CopycatLayoutResult res = arg0.getResults(CopycatLayoutResult.class);
		result = res;
	}
}