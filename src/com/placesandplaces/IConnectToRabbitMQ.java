package com.placesandplaces;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *  * Base class for objects that connect to a RabbitMQ Broker  
 */
public abstract class IConnectToRabbitMQ {
	// declare rabbitmq variables
	public String mServer;
	public String mExchange;

	protected Channel mModel = null;
	protected Connection mConnection;

	protected boolean Running;

	protected String MyExchangeType;

	public IConnectToRabbitMQ(String server, String exchange,
			String exchangeType) {
		mServer = server;
		mExchange = exchange;
		MyExchangeType = exchangeType;
	}

	public void Dispose() {
		Running = false;
		try {

			if (mConnection != null) {
				mConnection.close();
			}// end if

			if (mModel != null) {
				mModel.abort();
			}// end if
		}// end try
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean connectToRabbitMQ() {
		if (mModel != null && mModel.isOpen())// already declared
			return true;
		try {
			ConnectionFactory connectionFactory = new ConnectionFactory();
			connectionFactory.setHost(mServer);
			mConnection = connectionFactory.newConnection();
			mModel = mConnection.createChannel();
			mModel.exchangeDeclare(mExchange, MyExchangeType, true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}