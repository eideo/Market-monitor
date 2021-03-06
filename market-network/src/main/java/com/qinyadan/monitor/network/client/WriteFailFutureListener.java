package com.qinyadan.monitor.network.client;

import org.slf4j.Logger;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class WriteFailFutureListener implements ChannelFutureListener {

	private final Logger logger;
	private final String failMessage;
	private final String successMessage;

	public WriteFailFutureListener(Logger logger, String failMessage) {
		this(logger, failMessage, null);
	}

	public WriteFailFutureListener(Logger logger, String failMessage, String successMessage) {
		if (logger == null) {
			throw new NullPointerException("logger must not be null");
		}
		this.logger = logger;
		this.failMessage = failMessage;
		this.successMessage = successMessage;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (!future.isSuccess()) {
			if (logger.isWarnEnabled()) {
				final Throwable cause = future.cause();
				logger.warn("{} channel:{} Caused:{}", failMessage, future.channel(), cause.getMessage(), cause);
			}
		} else {
			if (successMessage != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("{} channel:{}", successMessage, future.channel());
				}
			}
		}
	}
}
