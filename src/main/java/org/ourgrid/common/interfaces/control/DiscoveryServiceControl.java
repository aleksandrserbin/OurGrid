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
package org.ourgrid.common.interfaces.control;

import br.edu.ufcg.lsd.commune.api.Remote;
import br.edu.ufcg.lsd.commune.container.control.ModuleControl;

/**
 * <p>
 * Discovery Service will receive some operations through this interface:
 * <ul>
 * <li> Execute a SQL SELECT in the database
 * </ul>
 * <p>
 * The correspondent callback calls or error notification should be made on the
 * <code>DiscoveryServiceControlClient</code> interface.
 */
@Remote
public interface DiscoveryServiceControl extends ModuleControl {
	
	/**
	 * Executes a SQL SELECT in the database of the DiscoveryService
	 * @param dsControlClient The client of DiscoveryServiceControl.
	 * @param query SELECT query to be executed in DiscoveryService's database.
	 */
	void query ( DiscoveryServiceControlClient dsControlClient, String query);
}

