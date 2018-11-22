package org.Handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class ChanneInitializerImpl extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel sc) {
//        sc.pipeline().addLast(new HttpServerCodec());
        sc.pipeline().addLast(new HttpRequestDecoder());
        sc.pipeline().addLast(new HttpResponseEncoder());
        sc.pipeline().addLast(new HttpObjectAggregator(65536));
        sc.pipeline().addLast(new RunHandler());
    }
}
