/********************************************************************************
 * Copyright (c) 2017, 2018 Cirrus Link Solutions and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Cirrus Link Solutions - initial implementation
 ********************************************************************************/

package org.eclipse.tahu.util;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.tahu.SparkplugException;
import org.eclipse.tahu.json.DeserializerModifier;
import org.eclipse.tahu.json.DeserializerModule;
import org.eclipse.tahu.message.model.Message;
import org.eclipse.tahu.message.model.Message.MessageBuilder;
import org.eclipse.tahu.message.model.Metric;
import org.eclipse.tahu.message.model.SparkplugBPayload;
import org.eclipse.tahu.message.model.SparkplugBPayload.SparkplugBPayloadBuilder;
import org.eclipse.tahu.message.model.Topic;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utilities for Sparkplug Message handling.
 */
public class MessageUtil {
	private static final String TEN_THOUSAND_BYTE_STRING =
			"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
					+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";

	/**
	 * Serializes a {@link Message} instance in to a JSON string.
	 * 
	 * @param message a {@link Message} instance
	 * @return a JSON string
	 * @throws JsonProcessingException
	 */
	public static String toJsonString(Message message) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(message);
	}

	/**
	 * Serializes a {@link Message} instance in to a JSON string.
	 * 
	 * @param message a {@link Message} instance
	 * @param dateFormat a {@link DateFormat} to use for all {@link Date} Objects
	 * @return a JSON string
	 * @throws JsonProcessingException
	 */
	public static String toJsonString(Message message, DateFormat dateFormat) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(dateFormat);
		return mapper.writeValueAsString(message);
	}

	/**
	 * Deserializes a JSON string into a {@link Message} instance.
	 * 
	 * @param payload a JSON string
	 * @return a {@link Message} instance
	 * @throws JsonProcessingException
	 */
	public static Message fromJsonString(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new DeserializerModule(new DeserializerModifier()));
		return mapper.readValue(jsonString, Message.class);
	}

	/**
	 * Divides a {@link Message} instance into one or more instances based on the maximum JSON encoded size. This method
	 * does not do an initial encoding/check on the size of the passed in message before it divides it in two.
	 * 
	 * @param message the {@link Message} instance to divide
	 * @param maxBytes the maximum bytes per {@link Message} instance
	 * @return a {@link Collection} of {@link Message} instances
	 * @throws JsonProcessingException
	 */
	public static Collection<Message> divideJsonMessageByBytes(Message message, int maxBytes)
			throws SparkplugException, JsonProcessingException {
		Collection<Message> messages = new ArrayList<Message>();
		divideAndAddMessages(messages, message, maxBytes);
		return messages;
	}

	/*
	 * Recursively divides {@link Message} instances and adds them to the {@link Collection} once they are under the
	 * maximum size.
	 */
	private static void divideAndAddMessages(Collection<Message> messages, Message message, int maxBytes)
			throws SparkplugException, JsonProcessingException {
		Topic topic = message.getTopic();
		SparkplugBPayload payload = message.getPayload();
		List<Metric> metrics = payload.getMetrics();
		final int metricCount = message.getPayload().getMetricCount();
		final int size = toJsonString(message).getBytes().length;

		// Check if the message can be divided
		if (metricCount <= 1) {
			String errorMessage = null;
			if (metricCount == 1) {
				errorMessage = "Cannot divide SparkplugBPayload with one metric: "
						+ message.getPayload().getMetrics().get(0).getName();
			} else {
				errorMessage = "Cannot divide SparkplugBPayload with " + metricCount + " metrics";
			}
			throw new SparkplugException(errorMessage);
		}

		int newMessageCount = size / maxBytes + ((size % maxBytes > 0) ? 1 : 0);
		int metricsPerMessageCount = metricCount / newMessageCount + ((metricCount % newMessageCount > 0) ? 1 : 0);
		int index = 0;

		while (index < metricCount) {
			int toIndex = metricCount < (index + metricsPerMessageCount) ? metricCount : index + metricsPerMessageCount;
			// build a new Message with the payload containing the next subset (count) of metrics
			Message newMessage = new MessageBuilder(topic,
					new SparkplugBPayloadBuilder().setTimestamp(payload.getTimestamp()).setUuid(payload.getUuid())
							.setSeq(payload.getSeq()).addMetrics(new ArrayList<Metric>(metrics.subList(index, toIndex)))
							.createPayload()).build();
			String jsonMessage = toJsonString(newMessage);
			if (jsonMessage.getBytes().length < maxBytes) {
				messages.add(newMessage);
			} else {
				divideAndAddMessages(messages, newMessage, maxBytes);
			}
			index += metricsPerMessageCount;
		}
	}

//	public static void main(String[] args) throws Exception {
//		List<Metric> metrics = new ArrayList<Metric>();
//		Random random = new Random();
//		// int numOfMetrics = random.nextInt(10) + 2;
//		int numOfMetrics = 1;
//		for (int i = 0; i < numOfMetrics; i++) {
//			// int r = random.nextInt(10) + 1;
//			int r = 120;
//			String str = "";
//			for (int j = 0; j < r; j++) {
//				str = str + TEN_THOUSAND_BYTE_STRING;
//			}
//			System.out.println("new string: " + str.getBytes().length + ", s" + i);
//			metrics.add(new MetricBuilder("s" + i, MetricDataType.String, "s" + i + "-" + str).createMetric());
//		}
//
//		Message message =
//				new MessageBuilder().topic(TopicUtil.parseTopic("spBv1.0/Group/DBIRTH/My Test Edge/My Device"))
//						.payload(new SparkplugBPayloadBuilder().setTimestamp(new Date()).setUuid(newUUID()).setSeq(1L)
//								.addMetrics(metrics).createPayload())
//						.build();
//
//		System.out.println("message size: " + toJsonString(message).getBytes().length);
//
//		Collection<Message> messages = divideJsonMessageByBytes(message, 11000);
//		System.out.println("new messages: " + messages.size());
//
//		for (Message msg : messages) {
//			System.out.println(" message contains " + msg.getPayload().getMetricCount() + " metrics");
//			for (Metric metric : msg.getPayload().getMetrics()) {
//				System.out.println("  message: " + metric.getValue().toString().substring(0, 10) + "...");
//			}
//		}
//	}
//
//	private static String newUUID() {
//		return java.util.UUID.randomUUID().toString().substring(0, 8);
//	}
}
