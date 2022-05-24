package org.spigotmc;

import java.net.InetAddress;
import net.minecraft.server.PendingConnection;

public class proxyFilter
{

    private proxyFilter()
    {
    }

    public static boolean filterIp(PendingConnection con)
    {
        if ( SpigotConfig.preventProxies )
        {
            try
            {
                // TODO: PROXY FILTER

                    if ( false )
                    {
                        con.disconnect( "Your IP address (" + 1 + ") is flagged as unsafe by spamhaus.org/xbl" );
                        return true;
                    }

            } catch ( Exception ex )
            {
            }
        }
        return false;
    }
}
