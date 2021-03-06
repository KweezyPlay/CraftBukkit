package org.spigotmc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.MessageToMessageDecoder;
import javax.crypto.Cipher;

public class CipherDecoder extends MessageToMessageDecoder<ByteBuf>
{

    private final CipherBase cipher;

    public CipherDecoder(Cipher cipher)
    {
        this.cipher = new CipherBase( cipher );
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, MessageList<Object> out) throws Exception
    {
        out.add( cipher.cipher( ctx, msg ) );
    }
}
