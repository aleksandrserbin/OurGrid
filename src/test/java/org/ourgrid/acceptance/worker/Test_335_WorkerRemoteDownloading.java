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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.ourgrid.acceptance.util.WorkerAcceptanceUtil;
import org.ourgrid.acceptance.util.worker.Req_003_Util;
import org.ourgrid.acceptance.util.worker.Req_004_Util;
import org.ourgrid.acceptance.util.worker.Req_006_Util;
import org.ourgrid.acceptance.util.worker.Req_079_Util;
import org.ourgrid.acceptance.util.worker.Req_080_Util;
import org.ourgrid.acceptance.util.worker.Req_081_Util;
import org.ourgrid.acceptance.util.worker.Req_082_Util;
import org.ourgrid.acceptance.util.worker.Req_084_Util;
import org.ourgrid.acceptance.util.worker.Req_087_Util;
import org.ourgrid.acceptance.util.worker.Req_088_Util;
import org.ourgrid.acceptance.util.worker.Req_091_Util;
import org.ourgrid.acceptance.util.worker.Req_092_Util;
import org.ourgrid.acceptance.util.worker.Req_094_Util;
import org.ourgrid.acceptance.util.worker.Req_120_Util;
import org.ourgrid.acceptance.util.worker.Req_121_Util;
import org.ourgrid.acceptance.util.worker.Req_125_Util;
import org.ourgrid.acceptance.util.worker.Req_126_Util;
import org.ourgrid.acceptance.util.worker.Req_127_Util;
import org.ourgrid.acceptance.util.worker.Req_128_Util;
import org.ourgrid.acceptance.util.worker.Req_129_Util;
import org.ourgrid.acceptance.util.worker.Req_130_Util;
import org.ourgrid.broker.BrokerConstants;
import org.ourgrid.common.CommonConstants;
import org.ourgrid.common.interfaces.Worker;
import org.ourgrid.common.interfaces.WorkerClient;
import org.ourgrid.common.interfaces.management.RemoteWorkerManagementClient;
import org.ourgrid.common.interfaces.management.WorkerManagementClient;
import org.ourgrid.common.interfaces.to.WorkerStatus;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.worker.WorkerComponent;
import org.ourgrid.worker.business.dao.EnvironmentDAO;
import org.ourgrid.worker.business.dao.FileTransferDAO;
import org.ourgrid.worker.business.dao.WorkerDAOFactory;
import org.ourgrid.worker.business.dao.WorkerStatusDAO;

import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.identification.ServiceID;
import br.edu.ufcg.lsd.commune.processor.filetransfer.IncomingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.OutgoingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.TransferHandle;
import br.edu.ufcg.lsd.commune.testinfra.util.TestStub;

public class Test_335_WorkerRemoteDownloading extends WorkerAcceptanceTestCase {

	private Req_003_Util req_003_Util = new Req_003_Util(getComponentContext());
	private Req_004_Util req_004_Util = new Req_004_Util(getComponentContext());
	private Req_006_Util req_006_Util = new Req_006_Util(getComponentContext());
	private Req_079_Util req_079_Util = new Req_079_Util(getComponentContext());
	private Req_080_Util req_080_Util = new Req_080_Util(getComponentContext());
	private Req_081_Util req_081_Util = new Req_081_Util(getComponentContext());
	private Req_082_Util req_082_Util = new Req_082_Util(getComponentContext());
	private Req_084_Util req_084_Util = new Req_084_Util(getComponentContext());
	private Req_087_Util req_087_Util = new Req_087_Util(getComponentContext());
	private Req_088_Util req_088_Util = new Req_088_Util(getComponentContext());
	private Req_092_Util req_092_Util = new Req_092_Util(getComponentContext());
	private Req_091_Util req_091_Util = new Req_091_Util(getComponentContext());
	private Req_094_Util req_094_Util = new Req_094_Util(getComponentContext());
	private Req_120_Util req_120_Util = new Req_120_Util(getComponentContext());
	private Req_121_Util req_121_Util = new Req_121_Util(getComponentContext());
	private Req_125_Util req_125_Util = new Req_125_Util(getComponentContext());
	private Req_126_Util req_126_Util = new Req_126_Util(getComponentContext());
	private Req_127_Util req_127_Util = new Req_127_Util(getComponentContext());
	private Req_128_Util req_128_Util = new Req_128_Util(getComponentContext());
	private Req_129_Util req_129_Util = new Req_129_Util(getComponentContext());
	private Req_130_Util req_130_Util = new Req_130_Util(getComponentContext());

	private DeploymentID peerID = null;
	private DeploymentID remotePeerID = null;
	private WorkerComponent component = null;
	private Future<?> prepFuture = null;
	private TestStub testStub = null;
	private String peerPubKey = null;
	private WorkerManagementClient wmc;
	private RemoteWorkerManagementClient rwmc;
	private DeploymentID remoteBrokerID = null;
	private WorkerClient workerClient = null;
	private TransferHandle handle = null;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		String fileName = "file";
		String filePath = "root" + File.separator + fileName;
		String operationType = CommonConstants.PUT_TRANSFER;

		peerPubKey = workerAcceptanceUtil.simulateAuthentication();
		peerID = new DeploymentID(new ContainerID("peerUser", "peerServer",
				PeerConstants.MODULE_NAME, peerPubKey), 
				PeerConstants.WORKER_MANAGEMENT_CLIENT_OBJECT_NAME);

		remotePeerID = new DeploymentID(new ContainerID("remotePeerUser", "peerServer", 
				PeerConstants.MODULE_NAME, "remotePeerPubKey"), 
				PeerConstants.REMOTE_WORKER_MANAGEMENT_CLIENT);

		component = req_003_Util.createWorkerComponent(peerID.getServiceID(), false);
		prepFuture = req_004_Util.startWorker(component);
		req_092_Util.prepareAllocationCompletedOnPreparingWorker(component);
		testStub = req_126_Util.notifyPeerRecoveryAtWorkerWithoutPeer(component, peerID, workerAcceptanceUtil.getPeerMonitorDeployment().getDeploymentID());
		req_129_Util.loginCompleteIdle(component,peerPubKey,peerID, testStub);
		wmc = workerAcceptanceUtil.createWorkerManagementClient(peerID);

		req_006_Util.workForPeerOnIdleWorkerLoggedPeer(component, wmc, 
				peerID.getPublicKey(),remotePeerID.getPublicKey() );

		rwmc = workerAcceptanceUtil.
				createRemoteWorkerManagementClient(remotePeerID.getPublicKey());

		remoteBrokerID = new DeploymentID(new ContainerID("brokerUserName", "brokerServer",
				"brokerModule", "brokerPublicKey"),	"broker");

		req_121_Util.workForBrokerOnAllocatedForPeerWorker(component, rwmc, 
				remotePeerID.getPublicKey(), remoteBrokerID.getServiceID());

		workerClient = req_079_Util.startWorkSuccessfully(component,
				workerAcceptanceUtil.getWorker(), remoteBrokerID);

		handle = req_080_Util.requestToTransferFile(component, 1, remoteBrokerID.
				getContainerID(),filePath, fileName, operationType, 0, 256);

	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test public void test_335_1_Start() throws Exception {
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_004_Util.startWorkerAlreadyStarted(component);

		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_2_WorkerManagementClientIsUp() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isLogged());

		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_088_Util.resumeNotOwnerWorker(component);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_3_LoginSucceeded() throws Exception {
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_4_WorkForPeer() throws Exception {
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		WorkerManagementClient wmc = workerAcceptanceUtil.createWorkerManagementClient(peerID);
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_006_Util.workForPeerOnAllocatedForRemoteBrokerWorkerOnWorkingStateWithIncomingFile(
				component, wmc, remoteBrokerID.getPublicKey(), "RemotePeerPubKey");

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertFalse(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_335_5_PreparationError() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());

		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_125_Util.allocationErrorOnNotPreparingWorker(component, WorkerStatus.
				ALLOCATED_FOR_BROKER);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isWorkingState());

		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_6_StartWork() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		List<TransferHandle> handles = new ArrayList<TransferHandle>();
		handles.add(handle);

		Worker worker = workerAcceptanceUtil.getWorker();

		DeploymentID brokerID = new DeploymentID(new ContainerID("brokerUserName",
				"brokerServer", "brokerModule", "brokerPublicKey"),	"broker");

		req_079_Util.startWorkSuccessfullyWithFileOnTransfer(component,
				worker, brokerID, true, handles);

		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
	}

	@Test
	public void test_335_7_GetFiles() throws Exception {
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_081_Util.requestToRecoverFilesBeforeExecutionFinish(component, 1,
				"brokerPublicKey", "$PLAYPEN/file1");

		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(),	peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_8_RemoteExecute() throws Exception {
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		Worker worker = workerAcceptanceUtil.getWorker();

		EnvironmentDAO environmentDAO = WorkerDAOFactory.getInstance().getEnvironmentDAO();
		String playpenPath = environmentDAO.getPlaypenDir();
		File pDir = new File(playpenPath);

		String storagePath = environmentDAO.getStorageDir();
		File sDir = new File(storagePath);

		Map<String, String> envVars = new HashMap<String, String>();
		String playpenDir = pDir.getAbsolutePath();
		String storageDir = sDir.getAbsolutePath();
		envVars.put("STORAGE", storageDir);
		envVars.put("PLAYPEN", playpenDir);

		long requestID = 1;

		req_084_Util.remoteExecutionWithNoExecutionResult(component, worker, workerClient,
				remoteBrokerID, requestID, envVars, "echo echo", 0);

		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_9_TransferRejected() throws Exception {
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		WorkerClient workerClient = EasyMock.createMock(WorkerClient.class);

		req_081_Util.receiveFileRejectDownloadingStateWorker(component, workerClient,
				remoteBrokerID.getPublicKey(), new OutgoingTransferHandle(2L,"",
						new File(""), "", remoteBrokerID), remoteBrokerID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_10_ReadyForAllocation() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());

		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_092_Util.prepareAllocationCompletedOnAllocatedForBrokerWorker(component, 
				wmc, prepFuture);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_11_OutgoingTransferCancelled() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		WorkerClient workerClient = EasyMock.createMock(WorkerClient.class);

		OutgoingTransferHandle outGoingTransferHandle = new OutgoingTransferHandle(
				2L, "", new File(""),"", remoteBrokerID);

		String filePath = outGoingTransferHandle.getLocalFile().getAbsolutePath();

		req_081_Util.receiveFileTransferCancelledOnDownloadingStateWorker(component, 
				workerClient, remoteBrokerID.getPublicKey(),
				outGoingTransferHandle, filePath, 12, remoteBrokerID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_12_OutgoingTransferFailed() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		WorkerClient workerClient = EasyMock.createMock(WorkerClient.class);

		OutgoingTransferHandle outGoingTransferHandle = new OutgoingTransferHandle(
				2L, "", new File(""),"", remoteBrokerID);

		String filePath = outGoingTransferHandle.getLocalFile().getAbsolutePath();

		req_081_Util.receiveFileTransferFailedOnDownloadingStateWorker(component, 
				workerClient, remoteBrokerID.getPublicKey(),
				outGoingTransferHandle, filePath, 12, new Exception(), remoteBrokerID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_13_OutgoingTransferCompleted() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		WorkerClient workerClient = EasyMock.createMock(WorkerClient.class);

		OutgoingTransferHandle outGoingTransferHandle = new OutgoingTransferHandle(
				2L, "", new File(""),"", remoteBrokerID);

		String filePath = outGoingTransferHandle.getLocalFile().getAbsolutePath();

		req_081_Util.receiveFileTransferCompletedOnDownloadingStateWorker(component, 
				workerClient, remoteBrokerID.getPublicKey(),
				outGoingTransferHandle, filePath, 12);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_14_WorkerClientIsUp() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();

		TestCase.assertNotNull(workerStatus.getConsumerAddress());
		TestCase.assertNotNull(workerStatus.getConsumerDeploymentID());

		req_130_Util.workerClientIsUp(component, remoteBrokerID);

		TestCase.assertNotNull(workerStatus.getConsumerAddress());
		TestCase.assertNotNull(workerStatus.getConsumerDeploymentID());
	}

	@Test public void test_335_15_ConcurrentExecutionError() throws Exception {
		//Not Necessary
	}

	@Test public void test_335_16_ExecutionError() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isLogged());

		req_128_Util.executionErrorOnRemoteDownloadingWorker(component, remoteBrokerID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
	}

	@Test public void test_335_17_ExecutionResult() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_128_Util.executionResultOnRemoteDownloaginWorker(component, remoteBrokerID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_18_ExecutionIsRunning() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_128_Util.executionIsRunningOnIdleWorker(component); 

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_19_Resume() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isLogged());

		req_088_Util.resumeNotOwnerWorker(component);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
	}

	@Test public void test_335_20_Status() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		TestCase.assertTrue(workerStatus.isLogged());

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
	}

	@Test public void test_335_21_GetFileInfo() throws Exception {
		String storageDir = WorkerDAOFactory.getInstance().getEnvironmentDAO().getStorageDir();
		String file1Name = "test_080_14_1.txt";
		String file1Path =  storageDir + File.separator + file1Name;
		WorkerAcceptanceUtil.createFile(file1Path);

		//Client "brokerPublicKey1" sends a getFileInfo "$STORAGE/test_080_14_1.txt" message
		String fileDigest = Req_082_Util.getFileDigest(file1Path);
		req_082_Util.getFileInfoWithIncomingFile(component, workerClient,
				remoteBrokerID.getPublicKey(), "$STORAGE" +File.separator+ file1Name,
				file1Path, fileDigest);

		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_22_UpdateTransferProgress() throws Exception {
		//Not necessary
	}

	@Test public void test_335_23_WorkerManagementClientIsDown() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocated());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertFalse(workerStatus.isPreparingAllocationState());

		req_127_Util.notifyPeerFailureAtWorkerAtWorkingState(component, peerID);

		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertFalse(workerStatus.isLogged());
		TestCase.assertFalse(workerStatus.isAllocated());
		TestCase.assertFalse(workerStatus.isWorkingState());
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_24_WorkerManagementClientIsDown() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocated());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertFalse(workerStatus.isPreparingAllocationState());

		req_127_Util.notifyPeerFailureAtWorkerAtRemoteDownloadingState(component, peerID);

		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertFalse(workerStatus.isLogged());
		TestCase.assertFalse(workerStatus.isAllocated());
		TestCase.assertFalse(workerStatus.isWorkingState());
	}

	@Test public void test_335_25_IncomingTransferCompleted() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		Worker worker = workerAcceptanceUtil.getWorker();

		IncomingTransferHandle incomingHandle = (IncomingTransferHandle) handle;

		req_080_Util.receiveIncomingTransferCompleted(component,
				worker, incomingHandle, 12, remoteBrokerID.getPublicKey());

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
	}

	@Test public void test_335_26_IncomingTransferFailed() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		Worker worker = workerAcceptanceUtil.getWorker();
		IncomingTransferHandle incomingHandle = (IncomingTransferHandle) handle;

		req_080_Util.receiveIncomingTransferFailedDownloadingStateWorker(component,
				worker, workerClient, incomingHandle, new Exception(), 12, remoteBrokerID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());

	}

	@Test public void test_335_27_StopWorking() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		TestCase.assertFalse(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_091_Util.stopWorkingOnWorkingWorker(component, wmc, peerPubKey);

		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertFalse(workerStatus.isWorkingState());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_335_28_WorkerClientIsDown() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();

		TestCase.assertNotNull(workerStatus.getConsumerAddress());
		TestCase.assertNotNull(workerStatus.getConsumerDeploymentID());


		req_130_Util.workerClientIsDown(component, remoteBrokerID);

		TestCase.assertNull(workerStatus.getConsumerAddress());
		TestCase.assertNull(workerStatus.getConsumerDeploymentID());

		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isLogged());
	}

	@Test public void test_335_29_Pause() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		WorkerManagementClient wmc = workerAcceptanceUtil.
				createWorkerManagementClient(peerID);
		
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocated());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_087_Util.pauseWorkerOnWorkingState(component, wmc, prepFuture);

		req_094_Util.getWorkerStatus(WorkerStatus.OWNER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertFalse(workerStatus.isAllocated());
		TestCase.assertFalse(workerStatus.isWorkingState());
	}

	@Test public void test_335_30_WorkForBroker() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertFalse(workerStatus.isPreparingAllocationState());

		req_092_Util.workForBrokerOnWorkingWorker(component, wmc, "brokerPublicKey2");

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertFalse(workerStatus.isWorkingState());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
	}

	@Test public void test_335_31_WorkForRemoteBroker() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isWorkingState());

		ServiceID otherBrokerServiceID = new ServiceID(new ContainerID("otherBroker",
				"otherServer", BrokerConstants.MODULE_NAME), BrokerConstants.WORKER_CLIENT);

		req_121_Util.workForBrokerOnWorkingWorkerAndDiffPubKey(
				component, rwmc, remotePeerID.getPublicKey(), otherBrokerServiceID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertEquals(workerStatus.getRemotePeerPublicKey(), 
				remotePeerID.getPublicKey());
	}

	@Test public void test_335_32_Stop() throws Exception {
		req_120_Util.stopWorkerOnWorkingState(component, prepFuture);
	}

}
