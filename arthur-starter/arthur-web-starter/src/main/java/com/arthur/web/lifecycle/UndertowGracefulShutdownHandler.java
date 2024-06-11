package com.arthur.web.lifecycle;

import com.arthur.boot.lifecycle.AbstractShutdownHandler;
import com.arthur.boot.lifecycle.ShutdownHandler;
import com.arthur.boot.lifecycle.SmartGracefulShutdownHandler;
import com.arthur.common.lifecycle.ShutdownHook;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.embedded.undertow.UndertowWebServer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.List;

/**
 * Undertow实现的优雅关闭
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-24
 * @see SmartGracefulShutdownHandler
 * @see ShutdownHandler
 * @see ShutdownHook
 * @since 1.0
 */
public class UndertowGracefulShutdownHandler extends AbstractShutdownHandler implements ApplicationListener<ApplicationEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(UndertowGracefulShutdownHandler.class);

	private static VarHandle gracefulShutdownVarHandle;
	private static VarHandle shutdownListenersVarHandle;

	static {
		try {
			gracefulShutdownVarHandle = MethodHandles.privateLookupIn(UndertowWebServer.class, MethodHandles.lookup())
				.findVarHandle(UndertowWebServer.class, "gracefulShutdown", GracefulShutdownHandler.class);

			shutdownListenersVarHandle = MethodHandles.privateLookupIn(GracefulShutdownHandler.class, MethodHandles.lookup())
				.findVarHandle(GracefulShutdownHandler.class, "shutdownListeners", List.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOG.warn("undertow not found, ignore fetch var handles");
		}
	}

	public UndertowGracefulShutdownHandler(ObjectProvider<List<ShutdownHook>> shutdownListenerProvider) {
		super(shutdownListenerProvider);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onApplicationEvent(@NonNull ApplicationEvent event) {
		if (!(event instanceof WebServerInitializedEvent initializedEvent)) {
			return;
		}
		if (!(initializedEvent.getWebServer() instanceof UndertowWebServer webServer)) {
			return;
		}

		GracefulShutdownHandler shutdownHandler = (GracefulShutdownHandler) gracefulShutdownVarHandle.getVolatile(webServer);
		if (shutdownHandler == null) {
			return;
		}

		List<GracefulShutdownHandler.ShutdownListener> shutdownListeners = (List<GracefulShutdownHandler.ShutdownListener>)
			shutdownListenersVarHandle.get(shutdownHandler);
		shutdownListeners.add(shutdownSuccessful -> {
			if (!shutdownSuccessful) {
				LOG.warn("UndertowGracefulShutdownHandler onApplicationEvent shutdown failed");
				return;
			}

			// 优雅关闭
			this.invokeShutdown();
		});
	}

}
