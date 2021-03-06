package com.star.config;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.websocket.GuacamoleWebSocketTunnelEndpoint;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author star
 * @Title: WebSocketTunnel
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @Date 2022/5/12
 **/
@ServerEndpoint(value = "/webSocket", subprotocols = "guacamole")
@Component
public class WebSocketTunnel extends GuacamoleWebSocketTunnelEndpoint {


    private MyConfig myConfig = (MyConfig)SpringBeanFactory.getBean("myConfig");


    /**
     * Returns a new tunnel for the given session. How this tunnel is created
     * or retrieved is implementation-dependent.
     *
     * @param session        The session associated with the active WebSocket
     *                       connection.
     * @param endpointConfig information associated with the instance of
     *                       the endpoint created for handling this single connection.
     * @return A connected tunnel, or null if no such tunnel exists.
     * @throws GuacamoleException If an error occurs while retrieving the
     *                            tunnel, or if access to the tunnel is denied.
     */
    @Override
    protected GuacamoleTunnel createTunnel(Session session, EndpointConfig endpointConfig) throws GuacamoleException {
        System.out.println("sessionMap:" + session.getRequestParameterMap());
        System.out.println("myConfig:" + myConfig.getPort());
        // ??????url??????
        // ?????????????????????????????????
        // ????????????Vue??????????????????????????????????????????????????????????????????????????????windows????????????????????????
        Integer height = Integer.valueOf(session.getRequestParameterMap().get("height").get(0));
        Integer width = Integer.valueOf(session.getRequestParameterMap().get("width").get(0));
        GuacamoleClientInformation information = new GuacamoleClientInformation();
        information.setOptimalScreenHeight(height);
        information.setOptimalScreenWidth(width);
        //guacamole server?????? r??????
        String hostname = "192.168.7.39";
        int port = 4822;
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol("rdp");
        // ??????windows???????????????
        configuration.setParameter("hostname", "192.168.65.93");
        configuration.setParameter("port", "3389");
        configuration.setParameter("username", "administrator");
        configuration.setParameter("password", "123");
        configuration.setParameter("ignore-cert", "true");


        String fileName = getNowTime() + ".guac";//?????????
        String outputFilePath = "/home/guacamole";
        //??????????????????--??????
        configuration.setParameter("recording-path", outputFilePath);
        configuration.setParameter("create-recording-path", "true");
        configuration.setParameter("recording-name", fileName);

        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket(hostname, port),
                configuration,
                information
        );

        GuacamoleTunnel tunnel = new SimpleGuacamoleTunnel(socket);
        return tunnel;
    }

    private void optClose(Session session) {
        // ?????????????????????????????????
        if (session.isOpen()) {
            try {
                // ????????????
                CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "???????????????");
                session.close(closeReason);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String getNowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
