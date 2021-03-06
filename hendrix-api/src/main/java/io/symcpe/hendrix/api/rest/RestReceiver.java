/**
 * Copyright 2016 Symantec Corporation.
 * 
 * Licensed under the Apache License, Version 2.0 (the “License”); 
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.symcpe.hendrix.api.rest;

import java.util.Map;
import java.util.Queue;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.symcpe.hendrix.api.ApplicationManager;
import io.symcpe.hendrix.api.dao.AlertReceiver;

/**
 * REST endpoint for receiving the actual alert
 * 
 * @author ambud_sharma
 */
@Path("/receive")
public class RestReceiver {
	
	private AlertReceiver alertReceiver;

	public RestReceiver(ApplicationManager applicationManager) {
		alertReceiver = applicationManager.getAlertReceiver();
	}

	@POST
	@Path("/{rule}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public boolean receiveEvent(@PathParam("rule") Short ruleId, Map<String, Object> event) {
		return alertReceiver.publishEvent(ruleId, event);
	}
	
	@POST
	@Path("/open/{rule}")
	@Produces({ MediaType.APPLICATION_JSON })
	public void openChannel(@PathParam("rule") Short ruleId) {
		alertReceiver.openChannel(ruleId);
	}
	
	@POST
	@Path("/close/{rule}")
	public void closeChannel(@PathParam("rule") Short ruleId) {
		alertReceiver.closeChannel(ruleId);
	}
	
	@GET
	@Path("/events/{rule}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Queue<Map<String, Object>> getEvents(@PathParam("rule") Short ruleId) {
		return alertReceiver.getChannel(ruleId);
	}
	
	@GET
	@Path("/status")
	public boolean status() {
		return true;
	}

}
