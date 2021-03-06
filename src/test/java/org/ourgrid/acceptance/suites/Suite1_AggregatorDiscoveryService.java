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
package org.ourgrid.acceptance.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.ourgrid.acceptance.broker.AllBrokerAcceptanceTests;
import org.ourgrid.acceptance.discoveryservice.AllDSAcceptanceTests;
import org.ourgrid.acceptance.worker.AllWorkerAcceptanceTests;

@RunWith(Suite.class)  
@SuiteClasses({  
			  //AllAggregatorAcceptanceTests.class,
			AllBrokerAcceptanceTests.class,
			AllDSAcceptanceTests.class,
			AllWorkerAcceptanceTests.class
              })  
public class Suite1_AggregatorDiscoveryService {
}
