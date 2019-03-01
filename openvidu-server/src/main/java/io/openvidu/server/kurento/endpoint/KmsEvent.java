/*
 * (C) Copyright 2017-2019 OpenVidu (https://openvidu.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.openvidu.server.kurento.endpoint;

import org.kurento.client.RaiseBaseEvent;
import org.kurento.jsonrpc.JsonUtils;

import com.google.gson.JsonObject;

public class KmsEvent {

	long timestamp;
	long msSinceCreation;
	String sessionId;
	String connectionId;
	String endpoint;
	RaiseBaseEvent event;

	public KmsEvent(RaiseBaseEvent event, String sessionId, String connectionId, String endpointName, long createdAt) {
		this.event = event;
		this.sessionId = sessionId;
		this.connectionId = connectionId;
		this.endpoint = endpointName;
		this.timestamp = System.currentTimeMillis(); // TODO: Change to event.getTimestampMillis()
		this.msSinceCreation = this.timestamp - createdAt;

		this.removeSourceForJsonCompatibility();
	}

	public JsonObject toJson() {
		JsonObject json = JsonUtils.toJsonObject(event);
		json.remove("tags");
		json.addProperty("timestamp", timestamp);
		json.addProperty("session", sessionId);
		json.addProperty("connection", connectionId);
		json.addProperty("endpoint", this.endpoint);
		json.addProperty("msSinceEndpointCreation", msSinceCreation);
		return json;
	}

	private void removeSourceForJsonCompatibility() {
		// This avoids stack overflow error when transforming RaiseBaseEvent into
		// JsonObject
		this.event.setSource(null);
	}

}