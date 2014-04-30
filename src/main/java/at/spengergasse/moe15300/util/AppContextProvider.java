package at.spengergasse.moe15300.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContextProvider implements ApplicationContextAware {
	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		context = ctx;
	}
}
