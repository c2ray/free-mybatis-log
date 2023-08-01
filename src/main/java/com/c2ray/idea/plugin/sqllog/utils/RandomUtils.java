package com.c2ray.idea.plugin.sqllog.utils;

import java.util.Random;

/**
 * @author c2ray
 * @since 2023/5/23
 */
public class RandomUtils {

    public static int randomPort() {
        // https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers#Dynamic,_private_or_ephemeral_ports
        // Random((2 << 15) - (2 << 14) - (2 << 14)) + (2<<14) + (2<<13)
        return new Random().nextInt(2 << 13) + (2 << 14) + (2 << 13);
    }

}
