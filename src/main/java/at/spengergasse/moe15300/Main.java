package at.spengergasse.moe15300;

import at.spengergasse.moe15300.util.Loggable;
import at.spengergasse.moe15300.view.AdjacencyFrame;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;

public class Main {
    @Loggable
    private static Logger log;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            log.warn("Couldn't set look and feel to system default.", e);
        }

		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:appContext.xml");
		ctx.getBean("adjacencyFrame", AdjacencyFrame.class);
        //AppContextProvider.getContext().getBean("adjacencyFrame", AdjacencyFrame.class);
    }
}
