package org.ourgrid.discoveryservice.response;


public class DSHereIsRemoteWorkerProvidersListResponseTO extends AbstractHereIsRemoteWorkerProvidersListResponseTO {
	
	private static final String REQUEST_TYPE = DiscoveryServiceResponseConstants.DS_HERE_IS_REMOTE_WORKER_PROVIDERS_LIST;

	
	public String getResponseType() {
		return REQUEST_TYPE;
	}

}
