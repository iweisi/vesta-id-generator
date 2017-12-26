package com.robert.vesta.rest.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class VestaRestNettyServerInitializer extends
        ChannelInitializer<SocketChannel> {
    private VestaRestNettyServerHandler handler = new VestaRestNettyServerHandler();

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast("codec", new HttpServerCodec());
        p.addLast("handler", handler);
    }
}
