package com.gateway.bot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;

public class UserLookup {
    private static Guild guild;
    private static String arg;
    private static Member member;

    //Attempts to lookup a member in the guild where the query came from using either a user ID or a full Discord tag with a discriminator.
    //If it can not find the target member with the provided methods or the methods provided don't match a user ID or tag it will return null.
    public static Member getMember(Message query) {
        try {
            guild = query.getGuild();
            arg = Arrays.asList(query.getContentStripped().split(" ")).get(2);
            System.out.println(arg);
            if(arg.contains("#")) {
                member = guild.getMemberByTag(arg);
            } else if(arg.matches("[0-9]+")) {
                member = guild.getMemberById(arg);
            } else {
                member = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            member = null;
        }
        return member;
    }
}
