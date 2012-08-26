package com.leandog.gametel.driver;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.leandog.gametel.driver.commands.Command;
import com.leandog.gametel.driver.commands.CommandRunner;
import com.leandog.gametel.json.*;

public class GametelRequestHandler extends AbstractHandler {

    private final GametelServer gametelServer;
    private final CommandRunner commandRunner;

    public GametelRequestHandler(final GametelServer gametelServer, final CommandRunner commandRunner) {
        this.gametelServer = gametelServer;
        this.commandRunner = commandRunner;
    }

    @Override
    public void handle(String target, HttpServletRequest theRequest, HttpServletResponse theResponse, int dispatch) throws IOException, ServletException {
        setHandled(theRequest);

        if (isKillCommand(theRequest)) {
            stopServer(theResponse);
            return;
        }

        try {
            for (final Command command : getCommands(theRequest)) {
                commandRunner.execute(command);
            }

            writeResultTo(theResponse);
            theResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Throwable e) {
            writeTo(theResponse, e);
            theResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    private boolean isKillCommand(HttpServletRequest request) {
        return request.getPathInfo().equals("/kill");
    }

    private void stopServer(HttpServletResponse response) {
        try {
            response.flushBuffer();
            this.gametelServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHandled(HttpServletRequest request) {
        ((Request) request).setHandled(true);
    }

    private void writeResultTo(HttpServletResponse response) throws IOException {
        writeTo(response, commandRunner.theLastResult());
    }

    private void writeTo(HttpServletResponse response, Object theValue) throws IOException {
        response.getWriter().print(gson().toJson(theValue));
    }

    private Gson gson() {
        return new GsonBuilder()
            .registerTypeAdapter(Command.class, new CommandDeserializer())
            .registerTypeHierarchyAdapter(Exception.class, new ExceptionTypeHierarchyAdapter())
            .registerTypeHierarchyAdapter(View.class, new ViewTypeHeirarchyAdapter())
            .create();
    }

    private List<Command> getCommands(HttpServletRequest request) {
        Type collectionType = new TypeToken<Collection<Command>>(){}.getType();
        return gson().fromJson(commandsParameter(request), collectionType);
    }

    private String commandsParameter(HttpServletRequest request) {
        final String commands = request.getParameter("commands");
        if (null == commands) {
            throw new IllegalArgumentException("gametel-driver requires a \"commands\" parameter");
        }
        return commands;
    }

}