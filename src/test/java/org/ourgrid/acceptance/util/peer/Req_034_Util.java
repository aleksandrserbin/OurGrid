/*
 * Copyright (C) 2008 Universidade Federal de Campina Grande
 *  
 * This file is part of OurGrid. 
 *
 * OurGrid is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.ourgrid.acceptance.util.peer;

import java.util.List;

import org.easymock.classextension.EasyMock;
import org.ourgrid.acceptance.util.PeerAcceptanceUtil;
import org.ourgrid.common.interfaces.status.ConsumerInfo;
import org.ourgrid.common.interfaces.status.PeerStatusProvider;
import org.ourgrid.common.interfaces.status.PeerStatusProviderClient;

import br.edu.ufcg.lsd.commune.container.ObjectDeployment;
import br.edu.ufcg.lsd.commune.context.ModuleContext;
import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.testinfra.AcceptanceTestUtil;

public class Req_034_Util extends PeerAcceptanceUtil{
	
	public Req_034_Util(ModuleContext context) {
		super(context);
	}

	/**
	 * Verifies remote consumers' status, comparing it 
	 * to the ConsumerInfo list parameter  
	 * @param expectedResult The expected status of the remote consumers
	 */
	public void getRemoteConsumersStatus(List<ConsumerInfo> expectedResult) {
		// Create client mock
		PeerStatusProviderClient statusProviderClient = EasyMock.createMock(PeerStatusProviderClient.class);
		
		// Get status provider
		PeerStatusProvider statusProvider = getStatusProviderProxy();
		ObjectDeployment stOD = getStatusProviderObjectDeployment();
		
		DeploymentID pspcID = new DeploymentID(new ContainerID("pspc", "pspcServer", "ds", "dsPK"),"ds");
		
		AcceptanceTestUtil.publishTestObject(application, pspcID, statusProviderClient, PeerStatusProviderClient.class);
		
		// Record mock behavior
		statusProviderClient.hereIsRemoteConsumersStatus(stOD.getDeploymentID().getServiceID(), expectedResult);
		EasyMock.replay(statusProviderClient);
		
		// Looking up the local workers of peer
		statusProvider.getRemoteConsumersStatus(statusProviderClient);
		
		EasyMock.verify(statusProviderClient);
	}
	
}