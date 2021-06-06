package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class CommandRelease extends Command{
    private final AccountManager accountManager;

    public CommandRelease(AccountManager accountManager) {
        super(new String[] {"release"}, null, "Releases a ticket that you claimed");
        this.accountManager = accountManager;
    }

    @Override
    public void onUse(Message query, List<String> arguments, CommandManager commandManager) throws Exception {
        try {
            query.getGuild().getMemberById(this.accountManager.getFreelancer(query.getTextChannel().getId()));
            if(this.accountManager.getFreelancer(query.getTextChannel().getId()).contains(query.getAuthor().getId()) || this.accountManager.getTicketOwner(query.getTextChannel().getId()).contains(query.getAuthor().getId()) || query.getMember().getPermissions().contains("MANAGE_CHANNEL")) {
                query.getTextChannel().sendMessage("The claim on this ticket has been released.").queue();
            } else {
                query.getTextChannel().sendMessage("You do not have permission to release this ticket!");
            }
        } catch (Exception e) {
            query.getTextChannel().sendMessage("You can't release a ticket that hasn't been claimed!");
        }

    }
}
