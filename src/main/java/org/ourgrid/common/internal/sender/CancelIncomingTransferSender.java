package org.ourgrid.common.internal.sender;

import org.ourgrid.common.interfaces.to.IncomingHandle;
import org.ourgrid.common.internal.SenderIF;
import org.ourgrid.common.internal.response.CancelIncomingTransferResponseTO;

import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.identification.ContainerID;
import br.edu.ufcg.lsd.commune.processor.filetransfer.IncomingTransferHandle;

public class CancelIncomingTransferSender implements SenderIF<CancelIncomingTransferResponseTO> {

	public void execute(CancelIncomingTransferResponseTO response, ServiceManager manager) {
		IncomingHandle incomingHandle = response.getIncomingHandle();
		ContainerID senderContainerID = ContainerID.parse(incomingHandle.getSenderContainerID());
		
		IncomingTransferHandle incomingTransferHandle = new IncomingTransferHandle(incomingHandle.getId(), 
				incomingHandle.getLogicalFileName(), incomingHandle.getDescription(), 
				incomingHandle.getFileSize(),senderContainerID);
		
		manager.cancelIncomingTransfer(incomingTransferHandle);
	}
}
