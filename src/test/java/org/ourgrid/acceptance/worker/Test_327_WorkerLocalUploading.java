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
import org.ourgrid.acceptance.util.worker.Req_125_Util;
import org.ourgrid.acceptance.util.worker.Req_126_Util;
import org.ourgrid.acceptance.util.worker.Req_127_Util;
import org.ourgrid.acceptance.util.worker.Req_128_Util;
import org.ourgrid.acceptance.util.worker.Req_129_Util;
import org.ourgrid.acceptance.util.worker.Req_130_Util;
import org.ourgrid.common.CommonConstants;
import org.ourgrid.common.executor.ExecutorHandle;
import org.ourgrid.common.executor.ExecutorResult;
import org.ourgrid.common.interfaces.Worker;
import org.ourgrid.common.interfaces.WorkerClient;
import org.ourgrid.common.interfaces.management.WorkerManagementClient;
import org.ourgrid.common.interfaces.to.WorkerStatus;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.worker.WorkerComponent;
import org.ourgrid.worker.business.dao.ExecutionDAO;
import org.ourgrid.worker.business.dao.FileTransferDAO;
import org.ourgrid.worker.business.dao.WorkerDAOFactory;
import org.ourgrid.worker.business.dao.WorkerStatusDAO;

import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.processor.filetransfer.IncomingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.OutgoingTransferHandle;
import br.edu.ufcg.lsd.commune.processor.filetransfer.TransferHandle;
import br.edu.ufcg.lsd.commune.testinfra.AcceptanceTestUtil;
import br.edu.ufcg.lsd.commune.testinfra.util.TestStub;

public class Test_327_WorkerLocalUploading extends WorkerAcceptanceTestCase {

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
	private Req_091_Util req_091_Util = new Req_091_Util(getComponentContext());
	private Req_092_Util req_092_Util = new Req_092_Util(getComponentContext());
	private Req_094_Util req_094_Util = new Req_094_Util(getComponentContext());
	private Req_120_Util req_120_Util = new Req_120_Util(getComponentContext());
	private Req_125_Util req_125_Util = new Req_125_Util(getComponentContext());
	private Req_126_Util req_126_Util = new Req_126_Util(getComponentContext());
	private Req_127_Util req_127_Util = new Req_127_Util(getComponentContext());
	private Req_128_Util req_128_Util = new Req_128_Util(getComponentContext());
	private Req_129_Util req_129_Util = new Req_129_Util(getComponentContext());
	private Req_130_Util req_130_Util = new Req_130_Util(getComponentContext());

	private DeploymentID peerID = null;
	private WorkerComponent component = null;
	private Future<?> prepFuture = null;
	private TestStub testStub = null;
	private String peerPubKey = null;
	private WorkerManagementClient wmc;
	private DeploymentID brokerID = null;
	private WorkerClient workerClient = null;
	private String playpenDir = null;
	private String storageDir = null;
	private List<TransferHandle> handles;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		peerPubKey = workerAcceptanceUtil.simulateAuthentication();
		peerID = new DeploymentID(new ContainerID("peerUser", "peerServer",
				PeerConstants.MODULE_NAME, peerPubKey),
				PeerConstants.WORKER_MANAGEMENT_CLIENT_OBJECT_NAME);
		component = req_003_Util.createWorkerComponent(peerID.getServiceID(), false);
		prepFuture = req_004_Util.startWorker(component);
		req_092_Util.prepareAllocationCompletedOnPreparingWorker(component);
		testStub = req_126_Util.notifyPeerRecoveryAtWorkerWithoutPeer(component, peerID, workerAcceptanceUtil.getPeerMonitorDeployment().getDeploymentID());
		req_129_Util.loginCompleteIdle(component,peerPubKey,peerID, testStub);
		wmc = workerAcceptanceUtil.createWorkerManagementClient(peerID);
		req_092_Util.workForBrokerOnIdleWorkerLoggedOnPeer(component, wmc, "brokerPublicKey" );
		brokerID = new DeploymentID(new ContainerID(
				"brokerUserName", "brokerServer", "brokerModule", "brokerPublicKey"), "broker");
		workerClient = req_079_Util.startWorkSuccessfully(
				component, workerAcceptanceUtil.getWorker(), brokerID);

		Worker worker = workerAcceptanceUtil.getWorker();
		String playpenPath = WorkerDAOFactory.getInstance().getEnvironmentDAO().getPlaypenDir();
		File pDir = new File(playpenPath);

		String storagePath = WorkerDAOFactory.getInstance().getEnvironmentDAO().getStorageDir();
		File sDir = new File(storagePath);

		Map<String, String> envVars = new HashMap<String, String>();
		playpenDir = pDir.getAbsolutePath();
		storageDir = sDir.getAbsolutePath();
		envVars.put("STORAGE", storageDir);
		envVars.put("PLAYPEN", playpenDir);
		long requestID = 1;
		req_084_Util.remoteExecutionWithNoExecutionResult(component,
				worker, workerClient, brokerID, requestID, envVars, "echo echo", 0);

		ExecutorHandle handle = workerAcceptanceUtil.createIntegerExecutorHandle(0);		
		req_128_Util.executionIsRunningOnIdleLocalExecuteWorker(component, handle);

		//Received the result of the information.
		ExecutorResult executor = workerAcceptanceUtil.createExecutorResult(0, "", "");
		req_128_Util.executionResultOnLocalExecutingWorker(component, executor, brokerID);

		File file = new File(playpenDir + File.separator + "file1");
		file.createNewFile();

		handles = req_081_Util.requestToRecoverFilesWithSuccess(component, 1L, brokerID.getPublicKey(), 
				workerClient, playpenDir, storageDir, "file1");
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test public void test_327_1_Start() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertTrue(workerStatus.isLogged());

		req_004_Util.startWorkerAlreadyStarted(component);

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_2_Resume() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_088_Util.resumeNotOwnerWorker(component);
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_3_Status() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_4_WorkerManagementClientIsUp() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_126_Util.notifyPeerRecoveryAtWorkerWithPeer(component, peerID,workerAcceptanceUtil.getPeerMonitorDeployment().getDeploymentID());

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_5_LoginSucceeded() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());

		req_129_Util.loginAlreadyLoggedInIdle(component, peerPubKey, peerID, testStub);

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_6_WorkForPeer() throws Exception {
		WorkerManagementClient wmc = workerAcceptanceUtil.createWorkerManagementClient(peerID);

		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_006_Util.workForPeerOnAllocatedForBrokerWorkerOnWorkingState(component,
				wmc, peerID.getPublicKey(), "RemotePeerPubKey");

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);

		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertFalse(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertFalse(executionDAO.isExecutionFinished());
		TestCase.assertTrue(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertTrue(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_7_ReadyForAllocation() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_092_Util.prepareAllocationCompletedOnAllocatedForBrokerWorker(component, wmc, prepFuture);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_8_PreparationError() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_125_Util.allocationErrorOnNotPreparingWorker(component,
				WorkerStatus.ALLOCATED_FOR_BROKER);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_9_StartWork() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		// Creating read-write playpen root directory
		WorkerAcceptanceUtil.createDirectory(WorkerAcceptanceUtil.DEF_PLAYPEN_ROOT_PATH, false);

		// Client [mgPubKey1] sends a startWork message - expect an info to be logged
		DeploymentID brokerID = new DeploymentID(new ContainerID("brokerUserName",
				"brokerServer", "brokerModule", "brokerPublicKey"),	"broker");
		
		Worker worker = workerAcceptanceUtil.getWorker();

		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		req_079_Util.startWorkSuccessfullyWithFileOnTransfer(
				component, worker, brokerID, false, handles);
		
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertFalse(executionDAO.isExecutionFinished());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertTrue(fileTransferDAO.getUploadingFileHandles().isEmpty());
	}

	@Test public void test_327_10_GetFileInfo() throws Exception {
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		String storageDir = WorkerDAOFactory.getInstance().getEnvironmentDAO().getStorageDir();
		String file1Name = "test_080_14_1.txt";
		String file1Path =  storageDir + File.separator + file1Name;
		WorkerAcceptanceUtil.createFile(file1Path);

		//Client "brokerPublicKey1" sends a getFileInfo "$STORAGE/test_080_14_1.txt" message
		String fileDigest = Req_082_Util.getFileDigest(file1Path);
		
		WorkerClient workerClient = (WorkerClient) AcceptanceTestUtil.getBoundObject(brokerID);
		
		req_082_Util.getFileInfoWithIncomingFile(component, workerClient, brokerID.getPublicKey(),
				"$STORAGE" +File.separator+ file1Name, file1Path, fileDigest);
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());

	}

	@Test public void test_327_11_RemoteExecute() throws Exception {
		Worker worker = workerAcceptanceUtil.getWorker();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();

		String playpenPath = WorkerDAOFactory.getInstance().getEnvironmentDAO().getPlaypenDir();
		File pDir = new File(playpenPath);

		String storagePath = WorkerDAOFactory.getInstance().getEnvironmentDAO().getStorageDir();
		File sDir = new File(storagePath);

		Map<String, String> envVars = new HashMap<String, String>();
		String playpenDir = pDir.getPath();
		String storageDir = sDir.getPath();
		envVars.put("STORAGE", storageDir);
		envVars.put("PLAYPEN", playpenDir);

		long requestID = 1;

		req_084_Util.remoteExecutionWithWokerInExecutionFinishedState(component, worker,
				workerClient, brokerID, requestID, envVars, "echo echo", 0);

		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
	}

	@Test public void test_327_12_TransferRequestReceived() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();

		String fileName = "file";
		String filePath = "root" + File.separator + fileName;
		String operationType = CommonConstants.PUT_TRANSFER;

		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());

		req_080_Util.requestToTransferFileOnLocalExecutionFinished(
				component, 1, brokerID.getContainerID(),
				filePath, fileName, operationType, 0, 256);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_13_IncomingTransferFailed() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		ContainerID senderID = brokerID.getContainerID();
		Worker worker = workerAcceptanceUtil.getWorker();
		IncomingTransferHandle handle = new IncomingTransferHandle("file.a",
				CommonConstants.PUT_TRANSFER + ":file.a", 1204, senderID);
		
		//These booleans are only set when the transfer is accepted
		handle.setReadable(true);
		handle.setWritable(true);
		handle.setExecutable(true);

		req_080_Util.receiveIncomingTransferFailedWorkingWorker(component,
				worker, workerClient, handle, new Exception(), 12, brokerID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_14_IncomingTransferCompleted() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		Worker worker = workerAcceptanceUtil.getWorker();

		IncomingTransferHandle handle = new IncomingTransferHandle("file.a",
				CommonConstants.PUT_TRANSFER + ":file.a", 1204, brokerID.getContainerID());

		//These booleans are only set when the transfer is accepted
		handle.setReadable(true);
		handle.setWritable(true);
		handle.setExecutable(true);
		
		req_080_Util.receiveIncomingTransferCompletedWorkingWorker(component,
				worker, handle, 12, brokerID.getPublicKey());
		
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_15_UpdateTransferProgress() throws Exception {
		//Not necessary
	}

	@Test public void test_327_16_GetFiles() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		File file = new File(playpenDir + File.separator + "file1");
		file.createNewFile();
		
		req_081_Util.requestToRecoverFilesThatAlreadyBeingUploaded(component, 1, 
				brokerID.getPublicKey(), workerClient, "file1",
				playpenDir, storageDir, "file1");
		
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
	}

	@Test public void test_327_17_ConcurrentExecutionError() throws Exception {
		//Not necessary
	}

	@Test public void test_327_18_ExecutionError() throws Exception {
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		
		req_128_Util.executionErrorExecutionFinishedWorker(component, brokerID);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());

	}

	@Test public void test_327_19_WorkerClientIsUp() throws Exception {
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		TestCase.assertNotNull(workerStatus.getConsumerAddress());
		TestCase.assertNotNull(workerStatus.getConsumerDeploymentID());

		req_130_Util.workerClientIsUp(component, brokerID);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertFalse(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());

	}

	@Test public void test_327_20_OutgoingTransferCompleted() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		WorkerClient workerClient = EasyMock.createMock(WorkerClient.class);

		TransferHandle handle = handles.get(0);
		OutgoingTransferHandle outGoingTransferHandle = new OutgoingTransferHandle(
				handle.getId(), handle.getLogicalFileName(), handle.getLocalFile(),
				handle.getDescription(), brokerID);

		String filePath = outGoingTransferHandle.getLocalFile().getAbsolutePath();

		req_081_Util.receiveFileTransferCompletedForLastUploadingFile(component, 
				workerClient, brokerID.getPublicKey(), outGoingTransferHandle, filePath, 
				outGoingTransferHandle.getFileSize());

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_21_WorkForBroker() throws Exception {
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_092_Util.workForBrokerOnLocalExecuting(component, wmc, "brokerPublicKey10");

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertNull(workerStatus.getRemotePeerPublicKey());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertTrue(fileTransferDAO.getUploadingFileHandles().isEmpty());
		
	}

	@Test public void test_327_22_TransferRejected() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		WorkerClient workerClient = EasyMock.createMock(WorkerClient.class);

		TransferHandle handle = handles.get(0);
		
		OutgoingTransferHandle outgoingHandle = new OutgoingTransferHandle(handle.getId(),
				handle.getLogicalFileName(), handle.getLocalFile(),
				handle.getDescription(), brokerID);
		
		String filePath = outgoingHandle.getLocalFile().getAbsolutePath();

		req_081_Util.receiveFileReject(component, workerClient, 
				brokerID.getPublicKey(), outgoingHandle, filePath, brokerID);
		
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertTrue(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_23_OutgoingTransferCancelled() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		TransferHandle handle = handles.get(0);
		
		OutgoingTransferHandle outgoingHandle = new OutgoingTransferHandle(handle.getId(),
				handle.getLogicalFileName(), handle.getLocalFile(),
				handle.getDescription(), brokerID);
		
		String filePath = outgoingHandle.getLocalFile().getAbsolutePath();

		req_081_Util.receiveFileTransferCancelledWithSuccess(component, 
				workerClient, brokerID.getPublicKey(), outgoingHandle, filePath, 
				handle.getFileSize(), brokerID);


		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertTrue(fileTransferDAO.getUploadingFileHandles().isEmpty());
		TestCase.assertTrue(workerStatus.isFileTransferErrorState());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_24_OutgoingTransferFailed() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		ExecutionDAO executionDAO = WorkerDAOFactory.getInstance().getExecutionDAO();
		FileTransferDAO fileTransferDAO = WorkerDAOFactory.getInstance().getFileTransferDAO();
		
		WorkerClient workerClient = EasyMock.createMock(WorkerClient.class);

		TransferHandle handle = handles.get(0);
		
		OutgoingTransferHandle outgoingHandle = new OutgoingTransferHandle(handle.getId(),
				handle.getLogicalFileName(), handle.getLocalFile(),
				handle.getDescription(), brokerID);
		
		String filePath = outgoingHandle.getLocalFile().getAbsolutePath();

		req_081_Util.receiveFileTransferFailed(component, 
				workerClient, brokerID.getPublicKey(),
				outgoingHandle, filePath, 12, new Exception(), brokerID);

		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertNull(executionDAO.getCurrentHandle());
		TestCase.assertTrue(executionDAO.isExecutionFinished());
		TestCase.assertFalse(workerStatus.isAllocatedForRemotePeer());
		TestCase.assertTrue(fileTransferDAO.getIncomingFileHandles().isEmpty());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_25_StopWorking() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_091_Util.stopWorkingOnWorkingWorker(component, wmc, peerPubKey);

		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertFalse(workerStatus.isWorkingState());
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
	}

	@Test public void test_327_26_WorkerClientIsDown() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();

		TestCase.assertNotNull(workerStatus.getConsumerAddress());
		TestCase.assertNotNull(workerStatus.getConsumerDeploymentID());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_130_Util.workerClientIsDown(component, brokerID);

		TestCase.assertNull(workerStatus.getConsumerAddress());
		TestCase.assertNull(workerStatus.getConsumerDeploymentID());

		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
	}

	@Test public void test_327_27_WorkerManagementClientIsDown() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocatedForBroker());
		TestCase.assertTrue(workerStatus.isWorkingState());
		TestCase.assertFalse(workerStatus.isPreparingAllocationState());

		req_127_Util.notifyPeerFailureAtWorkerAtWorkingState(component, peerID);

		req_094_Util.getWorkerStatus(WorkerStatus.IDLE);
		
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertTrue(workerStatus.isPreparingAllocationState());
		TestCase.assertFalse(workerStatus.isLogged());
		TestCase.assertFalse(workerStatus.isAllocated());
		TestCase.assertFalse(workerStatus.isWorkingState());
	}

	@Test public void test_327_28_Pause() throws Exception {
		WorkerStatusDAO workerStatus = WorkerDAOFactory.getInstance().getWorkerStatusDAO();
		WorkerManagementClient wmc = workerAcceptanceUtil.createWorkerManagementClient(peerID);
		
		req_094_Util.getWorkerStatus(WorkerStatus.ALLOCATED_FOR_BROKER);
		
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertTrue(workerStatus.isAllocated());
		TestCase.assertTrue(workerStatus.isWorkingState());

		req_087_Util.pauseWorkerOnLocalExecutionFinishedState(component, wmc, prepFuture);

		req_094_Util.getWorkerStatus(WorkerStatus.OWNER);
		
		TestCase.assertNotNull(workerStatus.getMasterPeerAddress());
		TestCase.assertEquals(workerStatus.getMasterPeerAddress(), peerID.getServiceID().toString());
		TestCase.assertTrue(workerStatus.isLogged());
		TestCase.assertFalse(workerStatus.isAllocated());
		TestCase.assertFalse(workerStatus.isWorkingState());
		
	}

	@Test public void test_327_29_Stop() throws Exception {
		req_120_Util.stopWorkerOnWorkingState(component, prepFuture);
	}

}
