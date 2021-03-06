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
package io.symcpe.wraith.actions.anomalies;

import io.symcpe.wraith.Constants;
import io.symcpe.wraith.Event;
import io.symcpe.wraith.Required;
import io.symcpe.wraith.actions.Action;

/**
 * @author ambud_sharma
 */
public class AnomalyAction implements Action {

	private static final long serialVersionUID = 1L;
	@Required
	private short actionId;
	@Required
	private String seriesName;
	@Required
	private String numericHeaderKey;

	public AnomalyAction(short actionId, String seriesName, String numericHeaderKey) {
		this.actionId = actionId;
		this.seriesName = seriesName;
		this.numericHeaderKey = numericHeaderKey;
	}

	@Override
	public Event actOnEvent(Event inputEvent) {
		Object object = inputEvent.getHeaders().get(numericHeaderKey);
		if (object == null || !(object instanceof Number)) {
			return null;
		} else {
			inputEvent.getHeaders().put(Constants.FIELD_ANOMALY_SERIES, seriesName);
			inputEvent.getHeaders().put(Constants.FIELD_ANOMALY_VALUE, object);
			return inputEvent;
		}
	}

	@Override
	public ACTION_TYPE getActionType() {
		return ACTION_TYPE.ANOMD;
	}

	@Override
	public short getActionId() {
		return actionId;
	}

	@Override
	public void setActionId(short actionId) {
		this.actionId = actionId;
	}

}
