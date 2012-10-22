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
package org.ourgrid.broker.controlws;

import java.io.Serializable;
import java.util.List;

import org.ourgrid.common.specification.CompilerMessages;
import org.ourgrid.common.specification.exception.TaskSpecificationException;

/**
 * Entity that encapsulates all the infomations given by the user about each
 * task. To inform, the user uses the Description Files that can be compiled by
 * CommonCompiler.
 * 
 * @version 1.0
 * @see org.ourgrid.common.specification.main.CommonCompiler
 */
public class WSTaskSpec implements Serializable {

	/**
	 * Serial identification of the class. It need to be changed only if the
	 * class interface is changed.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The init part of a task specification.
	 */
	private List<WSIOEntry> initBlock;

	/**
	 * The remote part of a task specification.
	 */
	private String remoteExec;

	/**
	 * The final part of a task specification.
	 */
	private List<WSIOEntry> finalBlock;

	/**
	 * The sabotage check command.
	 */
	private String sabotageCheck;

	/** Job source directory where results files are stored */
	private String sourceParentDir;
	
	private int taskSequenceNumber;
	
	public WSTaskSpec() {}

	/**
	 * The constructor.
	 * 
	 * @param initBlock The information about the files that will be transfered
	 *        to the remote machine that will be used at the execution of the
	 *        task.
	 * @param remoteExec The command that will be run at the remote machine to
	 *        execute the task.
	 * @param finalBlock The information about the files that will be transfered
	 *        from the remote machine as results of the execution.
	 * @param sabotageCheckCommand The command that will be run at local machine to check 
	 * 		  the anti-sabotage water proof.
	 * 
	 * @throws TaskSpecificationException If the informations about the task are
	 *         not valid.
	 */
	public WSTaskSpec( List<WSIOEntry> initBlock, String remoteExec, List<WSIOEntry> finalBlock, 
			String sabotageCheckCommand ) throws TaskSpecificationException {

		this.initBlock = initBlock;
		this.remoteExec = remoteExec;
		this.finalBlock = finalBlock;
		this.sabotageCheck = sabotageCheckCommand;
		validate();
	}

	/**
	 * Validates the informations passed as arguments to this task specification
	 * 
	 * @throws TaskSpecificationException If the task specs are not valid
	 */
	private void validate() throws TaskSpecificationException {

		if ( remoteExec == null || remoteExec == "" )
			throw new TaskSpecificationException( CompilerMessages.BAD_TASK_SPEC_REMEXEC_MISSING );
	}

	/**
	 * Gets the init part of a task.
	 * 
	 * @return Returns the initBlock.
	 */
	public List<WSIOEntry> getInitBlock() {
		return initBlock;
	}

	/**
	 * Gets the final part of a task.
	 * 
	 * @return Returns the finalBlock.
	 */
	public List<WSIOEntry> getFinalBlock() {
		return finalBlock;
	}

	/**
	 * Gets the remote part of a task.
	 * 
	 * @return Returns the remoteExec.
	 */
	public String getRemoteExec() {
		return remoteExec;
	}
	
	/**
	 * Gets the sabotage check command.
	 * 
	 * @return
	 */
	public String getSabotageCheckCommand(){
		return this.sabotageCheck;
	}
	
	
	public void setSourceDirPath( String sourceParentDir ) {

		this.sourceParentDir = sourceParentDir;
	}

	public String getSourceParentDir() {

		return this.sourceParentDir;
	}

	public void setTaskSequenceNumber(int taskSequenceNumber) {
		this.taskSequenceNumber = taskSequenceNumber;
	}
	
	public int getTaskSequenceNumber() {
		return taskSequenceNumber;
	}

	public String getSabotageCheck() {
		return sabotageCheck;
	}

	public void setSabotageCheck(String sabotageCheck) {
		this.sabotageCheck = sabotageCheck;
	}

	public void setInitBlock(List<WSIOEntry> initBlock) {
		this.initBlock = initBlock;
	}

	public void setRemoteExec(String remoteExec) {
		this.remoteExec = remoteExec;
	}

	public void setFinalBlock(List<WSIOEntry> finalBlock) {
		this.finalBlock = finalBlock;
	}

	public void setSourceParentDir(String sourceParentDir) {
		this.sourceParentDir = sourceParentDir;
	}

	@Override
	public String toString() {

		StringBuffer message = new StringBuffer();

		message.append( "   INIT PHASE:\n" );
		if ( initBlock != null ) {
			message.append( initBlock.toString() );
		} else {
			message.append( "      empty!" );
		}

		message.append( "   REMOTE PHASE:\n" );
		message.append( "      " + remoteExec + "\n" );

		message.append( "   FINAL PHASE:\n" );
		if ( finalBlock != null ) {
			message.append( finalBlock.toString() );
		} else {
			message.append( "      empty!" );
		}
		
		message.append( "   SABOTAGE CHECK COMMAND:\n" );
		if ( sabotageCheck != null ) {
			message.append( sabotageCheck );
		} else {
			message.append( "      empty!" );
		}

		return message.toString();

	}


	@Override
	public int hashCode() {

		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.finalBlock == null) ? 0 : this.finalBlock.hashCode());
		result = PRIME * result + ((this.initBlock == null) ? 0 : this.initBlock.hashCode());
		result = PRIME * result + ((this.remoteExec == null) ? 0 : this.remoteExec.hashCode());
		result = PRIME * result + ((this.sabotageCheck == null) ? 0 : this.sabotageCheck.hashCode());
		return result;
	}


	@Override
	public boolean equals( Object obj ) {

		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		final WSTaskSpec other = (WSTaskSpec) obj;
		if ( !(this.finalBlock == null ? other.finalBlock == null : this.finalBlock.equals( other.finalBlock )) )
			return false;
		if ( !(this.initBlock == null ? other.initBlock == null : this.initBlock.equals( other.initBlock )) )
			return false;
		if ( !(this.remoteExec == null ? other.remoteExec == null : this.remoteExec.equals( other.remoteExec )) )
			return false;
		if ( !(this.sabotageCheck == null ? other.sabotageCheck == null : this.sabotageCheck.equals( other.sabotageCheck )) )
			return false;
		return true;
	}
}
