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
package org.ourgrid.common.spec.main;

import org.ourgrid.common.specification.worker.WorkerSpecification;
import org.ourgrid.common.specification.worker.WorkerSpecificationConstants;

/**
 * Defines the systems info properties names for 
 * the class {@link WorkerSpecification}.
 * 
 */
public interface WorkerSpecGlueConstants extends WorkerSpecificationConstants {

	//JDL Constants defined according to GLUE 1.3 schema
	public String GLUE_SITE_UNIQUE_ID = "GLUESITEUNIQUEID";
	public String GLUE_SITE_NAME = "GLUESITENAME";
	public String GLUE_SITE_DESCRIPTION = "GLUESITEDESCRIPTION";
	public String GLUE_SITE_WEB = "GLUESITEWEB";
	public String GLUE_SITE_OTHER_INFO = "GLUESITEOTHERINFO";
	
	public String GLUE_CE_UNIQUE_ID = "GLUECEUNIQUEID";
	public String GLUE_CE_NAME = "GLUECENAME";
	public String GLUE_CE_POLICY_PREEMPTION = "GLUECEPOLICYPREEMPTION";
	public String GLUE_CE_INFO_TOTAL_CPUS = "GLUECEINFOTOTALCPUS";
	public String GLUE_CE_STATE_FREE_CPUS = "GLUECESTATEFREECPUS";
	
	public String GLUE_HOST_OPERATING_SYSTEM_NAME = "GLUEHOSTOPERATINGSYSTEMNAME"; 
	public String GLUE_HOST_OPERATING_SYSTEM_RELEASE = "GLUEHOSTOPERATINGSYSTEMRELEASE";
	public String GLUE_HOST_OPERATING_SYSTEM_VERSION = "GLUEHOSTOPERATINGSYSTEMVERSION";
	public String GLUE_HOST_APPLICATION_SOFTWARE_RUNTIME_ENVIRONMENT = "GLUEHOSTAPPLICATIONSOFTWARERUNTIMEENVIRONMENT";
	public String GLUE_HOST_PROCESSOR_VENDOR = "GLUEHOSTPROCESSORVENDOR";
	public String GLUE_HOST_PROCESSOR_MODEL = "GLUEHOSTPROCESSORMODEL";
	public String GLUE_HOST_PROCESSOR_VERSION = "GLUEHOSTPROCESSORVERSION";
	public String GLUE_HOST_PROCESSOR_CLOCK_SPEED = "GLUEHOSTPROCESSORCLOCKSPEED";
	public String GLUE_HOST_PROCESSOR_INSTRUCTION_SET = "GLUEHOSTPROCESSORINSTRUCTIONSET";
	public String GLUE_HOST_PROCESSOR_OTHER_DESCRIPTION = "GLUEHOSTPROCESSOROTHERDESCRIPTION";
	public String GLUE_HOST_MAIN_MEMORY_RAM_SIZE = "GLUEHOSTMAINMEMORYRAMSIZE";
	public String GLUE_HOST_MAIN_MEMORY_VIRTUAL_SIZE = "GLUEHOSTMAINMEMORYVIRTUALSIZE";
	public String GLUE_HOST_ARCHITECTURE_PLATFORM_TYPE = "GLUEHOSTARCHITECTUREPLATFORMTYPE";
	public String GLUE_HOST_ARCHITECTURE_SMP_SIZE = "GLUEHOSTARCHITECTURESMPSIZE";
	
	public String GLUE_SUBCLUSTER_UNIQUE_ID = "GLUESUBCLUSTERUNIQUEID";
	public String GLUE_SUBCLUSTER_NAME = "GLUESUBCLUSTERNAME";
	public String GLUE_SUBCLUSTER_PHYSICAL_CPUS = "GLUESUBCLUSTERPHYSICALCPUS";
	public String GLUE_SUBCLUSTER_LOGICAL_CPUS = "GLUESUBCLUSTERLOGICALCPUS";

	public String GLUE_SOFTWARE_NAME = "GLUESOFTWARENAME";
	public String GLUE_SOFTWARE_VERSION = "GLUESOFTWAREVERSION";
	public String GLUE_SOFTWARE_INSTALLED_ROOT = "GLUESOFTWAREINSTALLEDROOT";
	
	public String GLUE_PROCESSOR_LOAD_LAST1MIN = "GLUEPROCESSORLOADLAST1MIN";
	public String GLUE_PROCESSOR_LOAD_LAST5MIN = "GLUEPROCESSORLOADLAST5MIN";
	public String GLUE_PROCESSOR_LOAD_LAST15MIN = "GLUEPROCESSORLOADLAST15MIN";

	public String GLUE_SMPLOADLAST1MIN = "GLUESMPLOADLAST1MIN";
	public String GLUE_SMPLOADLAST5MIN = "GLUESMPLOADLAST5MIN";
	public String GLUE_SMPLOADLAST15MIN = "GLUESMPLOADLAST15MIN";
	
	//JDL Constants defined according to GLUE 2.0 schema
	public String DOMAIN_ID = "DOMAINID";
	public String DOMAIN_NAME = "DOMAINNAME"; 
	public String DOMAIN_DESCRIPTION = "DOMAINDESCRIPTION"; 
	public String DOMAIN_WWW = "DOMAINWWW";
	public String DOMAIN_OTHER_INFO = "DOMAINOTHERINFO";
	
	public String RESOURCE_NAME = "RESOURCENAME";
	public String RESOURCE_ID = "RESOURCEID";

	public String CONTACT_ID = "CONTACTID";

	public String EXECUTION_ENVIRONMENT_PLATFORM = "EXECUTIONENVIRONMENTPLATFORM";
	public String EXECUTION_ENVIRONMENT_VIRTUAL_MACHINE = "EXECUTIONENVIRONMENTVIRTUALMACHINE";
	public String EXECUTION_ENVIRONMENT_PHYSICAL_CPUS = "EXECUTIONENVIRONMENTPHYSICALCPUS";
	public String EXECUTION_ENVIRONMENT_LOGICAL_CPUS = "EXECUTIONENVIRONMENTLOGICALCPUS";
	public String EXECUTION_ENVIRONMENT_CPU_MULTIPLICITY = "EXECUTIONENVIRONMENTCPUMULTIPLICITY";
	public String EXECUTION_ENVIRONMENT_CPU_VENDOR = "EXECUTIONENVIRONMENTCPUVENDOR";
	public String EXECUTION_ENVIRONMENT_CPU_MODEL = "EXECUTIONENVIRONMENTCPUMODEL";
	public String EXECUTION_ENVIRONMENT_CPU_VERSION = "EXECUTIONENVIRONMENTCPUVERSION";
	public String EXECUTION_ENVIRONMENT_CPU_CLOCKS_PEED = "EXECUTIONENVIRONMENTCPUCLOCKSPEED";
	public String EXECUTION_ENVIRONMENT_MAIN_MEMORY_SIZE = "EXECUTIONENVIRONMENTMAINMEMORYSIZE";
	public String EXECUTION_ENVIRONMENT_VIRTUAL_MEMORY_SIZE = "EXECUTIONENVIRONMENTVIRTUALMEMORYSIZE";
	public String EXECUTION_ENVIRONMENT_OS_FAMILY = "EXECUTIONENVIRONMENTOSFAMILY"; 
	public String EXECUTION_ENVIRONMENT_OS_NAME = "EXECUTIONENVIRONMENTOSNAME";
	public String EXECUTION_ENVIRONMENT_OS_VERSION = "EXECUTIONENVIRONMENTOSVERSION";
	
	public String APPLICATION_ENVIRONMENT_OTHER_INFO = "APPLICATIONENVIRONMENTOTHERINFO";
	public String APPLICATION_ENVIRONMENT_NAME = "APPLICATIONENVIRONMENTNAME";
	public String APPLICATION_ENVIRONMENT_VERSION = "APPLICATIONENVIRONMENTVERSION";
	public String APPLICATION_ENVIRONMENT_DESCRIPTION = "APPLICATIONENVIRONMENTDESCRIPTION";
	
	public String COMPUTING_ENDPOINT_RUNNING_JOBS = "COMPUTINGENDPOINTRUNNINGJOBS";
	public String COMPUTING_ENDPOINT_WAITING_JOBS = "COMPUTINGENDPOINTWAITINGJOBS";
	public String COMPUTING_ENDPOINT_TOTAL_JOBS = "COMPUTINGENDPOINTTOTALJOBS";
}
