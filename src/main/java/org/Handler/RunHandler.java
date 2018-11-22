package org.Handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.Runtest.Run;

import java.util.logging.Logger;

public class RunHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());

    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("==channelActive==");
        Global.group.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println("==channelInactive==");
        Global.group.remove(ctx.channel());
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("==messageReceived==");
        if (msg instanceof FullHttpRequest) {
            System.out.println("====1====");
            handleHttpRequest(ctx, ((FullHttpRequest) msg));

        } else if (msg instanceof WebSocketFrame) {
            System.out.println("====2====");
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        System.out.println("==channelReadComplete==");
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx,
                                       WebSocketFrame frame){

        if (frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),((CloseWebSocketFrame) frame).retain());
        } else

        if (frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        } else
        if (!(frame instanceof TextWebSocketFrame)) {


            System.out.println("本例程仅支持文本消息，不支持二进制消息");

            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        } else
        if (frame instanceof TextWebSocketFrame) {
            String s = ((TextWebSocketFrame) frame).text();
            System.out.println(s);
            String complete = "Program is running,please wait....";
            TextWebSocketFrame tsf = new TextWebSocketFrame(complete);
            ctx.writeAndFlush(tsf);

            String out = null;
            try {
                out = new Run().test(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TextWebSocketFrame textOut = new TextWebSocketFrame(out);
            ctx.writeAndFlush(textOut);
            System.out.println("finsh");
        }

    }


    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {


        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {


            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));

            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://0.0.0.0:8080", null, false);

        handshaker = wsFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }

    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {

        System.out.println("==sendHttpResponse==");
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
    private static boolean isKeepAlive(FullHttpRequest req) {

        return false;
    }
}
