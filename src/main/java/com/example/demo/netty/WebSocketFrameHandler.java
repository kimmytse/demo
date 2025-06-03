package com.example.demo.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx.channel().id().asShortText());
        scheduler.scheduleAtFixedRate(() -> {
            ctx.channel().writeAndFlush(new TextWebSocketFrame("Periodic message from server"));
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected: " + ctx.channel().id().asShortText());
        scheduler.shutdown();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            System.out.println("Received: " + request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame("Hello, " + request));
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }
}



