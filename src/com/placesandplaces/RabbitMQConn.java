package com.placesandplaces;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

//code inspired and built upon: http://simonwdixon.wordpress.com/2011/06/03/getting-started-with-rabbitmq-on-android-part-1/
public abstract class RabbitMQConn {
	// declare rabbitmq variables
	public String mServer;
	public String mExchange;

	protected Channel mModel = null;
	protected Connection mConnection;

	protected boolean Running;

	protected String ExchangeType;

	public RabbitMQConn(String server, String exchange,
			String exchangeType) {
		mServer = server;
		mExchange = exchange;
		ExchangeType = exchangeType;
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
			mModel.exchangeDeclare(mExchange, ExchangeType, true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}