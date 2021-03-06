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
package org.ourgrid.broker.communication.actions;

import java.io.Serializable;

import org.ourgrid.broker.request.SchedulerActionRequestTO;
import org.ourgrid.common.internal.OurGridRequestControl;

import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.actions.RepeatedAction;

/**
 *
 */
public class SchedulerAction implements RepeatedAction {

	public SchedulerAction() {
	}
	
	public void run(Serializable handler, ServiceManager serviceManager) {
		
		SchedulerActionRequestTO to = new SchedulerActionRequestTO();
		
		OurGridRequestControl.getInstance().execute(to, serviceManager);
	}

}
