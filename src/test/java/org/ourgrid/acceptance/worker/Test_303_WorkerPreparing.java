/*
 * Copyright (C) 2011 Universidade Federal de Campina Grande
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
package org.ourgrid.acceptance.worker;

import java.util.concurrent.Future;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.ourgrid.acceptance.util.worker.Req_003_Util;
import org.ourgrid.acceptance.util.worker.Req_004_Util;
import org.ourgrid.acceptance.util.worker.Req_087_Util;
import org.ourgrid.acceptance.util.worker.Req_088_Util;
import org.ourgrid.acceptance.util.worker.Req_092_Util;
import org.ourgrid.acceptance.util.worker.Req_094_Util;
import org.ourgrid.acceptance.util.worker.Req_120_Util;
import org.ourgrid.acceptance.util.worker.Req_125_Util;
import org.ourgrid.acceptance.util.worker.Req_126_Util;
import org.ourgrid.acceptance.util.worker.Req_127_Util;
import org.ourgrid.acceptance.util.worker.Req_128_Util;
import org.ourgrid.common.interfaces.management.WorkerManagementClient;
import org.ourgrid.common.interfaces.to.WorkerStatus;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.worker.WorkerComponent;
import org.ourgrid.worker.business.dao.WorkerDAOFactory;
import org.ourgrid.worker.business.dao.WorkerStatusDAO;

import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;

public class Test_303_WorkerPreparing extends WorkerAcceptanceTestCase {

	private Req_003_Util req_003_Util = new Req_003_Util(getComponentContext());
	private Req_004_Util req_004_Util = new Req_004_Util(getComponentContext());
	private Req_087_Util req_087_Util = new Req_087_Util(getComponentContext());
	private Req_088_Util req_088_Util = new Req_088_Util(getComponentContext());
	private Req_092_Util req_092_Util = new Req_092_Util(getComponentContext());
	private Req_094_Util req_094_Util = new Req_094_Util(getComponentContext());
	private Req_120_Util req_120_Util = new Req_120_Util(getComponentContext());
	private Req_125_Util req_125_Util = new Req_125_Util(getComponentContext());
	private Req_126_Util req_126_Util = new Req_126_Util(getComponentContext());
	private Req_127_Util req_127_Util = new Req_127_Util(getComponentContext());
	private Req_128_Util req_128_Util = new Req_128_Util(getComponentContext());

	private DeploymentID peerID = null;
	private WorkerComponent component = null;
	private Future<?> prepFuture = null;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		String peerPubKey = workerAcceptanceUtil.simulateAuthentication();
		peerID = new DeploymentID(new ContainerID("peerUser", "peerServer",
				PeerConstants.MODULE_NAME, peerPubKey),
				PeerConstants.WORKER_MANAGEMENT_CLIENT_OBJECT_NAME);
		component = req_003_Util.createWorkerComponent(peerID.getServiceID(), false);
		prepFuture = req_004_Util.startWorker(component);
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test 
	public void test_303_1_Start() throws Exception {
		req_004_Util.startWorkerAlreadyStarted(component);
	}
	
	@Test public void test_303_2_Status() throws Exception {
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
	
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
	}
	
	@Test public void test_303_5_Resume() throws Exception {
		req_088_Util.resumePreparingWorker(component);
		
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
	}
	
	@Test public void test_303_6_WorkerManagementClientIsDown() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		
		req_127_Util.notifyPeerFailureAtPreparingWorker(component, peerID);
		
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
	}

	@Test public void test_303_7_WorkerManagementClientIsUp() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());

		req_126_Util.notifyPeerRecoveryAtWorkerWithoutPeer(component, peerID, workerAcceptanceUtil.getPeerMonitorDeployment().getDeploymentID());
		
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}
	
	@Test public void test_303_8_PreparationError() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		
		req_125_Util.allocationError(component, null, prepFuture);
		
		req_094_Util.getWorkerStatus(WorkerStatus.ERROR);
		TestCase.assertFalse(workerStatus.isPreparingAllocationState());
	}
	
	@Test public void test_303_9_Pause() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		
		WorkerManagementClient wmc = workerAcceptanceUtil.createWorkerManagementClient(peerID);
		req_087_Util.pausePreparingWorker(prepFuture, component, wmc);
		
		req_094_Util.getWorkerStatus(WorkerStatus.OWNER);
		TestCase.assertFalse(workerStatus.isPreparingAllocationState());
	}
	
	@Test public void test_303_10_ConcurrentExecutionError() throws Exception {
		//Not necessary
	}
	
	@Test public void test_303_11_ExecutionError() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		
		req_128_Util.executionErrorOnPreparingWorker(component);
		
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
	}
	
	@Test public void test_303_12_ExecutionResult() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		
		req_128_Util.executionResultOnPreparingWorker(component);
		
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
	}
	
	@Test public void test_303_13_ExecutionIsRunning() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		
		req_128_Util.executionIsRunningOnPreparingWorker(component);
		
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
	}
	
	@Test public void test_303_14_ReadyForAllocation() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());

		req_092_Util.prepareAllocationCompleted(component, null,prepFuture);
		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertFalse(workerStatus.isPreparingAllocationState());
	}
	
	@Test public void test_303_15_Stop() throws Exception {
		req_120_Util.stopWorkerWithAllocation(component, prepFuture);
	}

}
