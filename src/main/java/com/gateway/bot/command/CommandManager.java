package com.gateway.bot.command;

import com.gateway.bot.database.AccountManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private Message query;
    private List<String> fullQuery;
    public String prefix = "/";
    private final List<Command> commands;
    private final JDA api;
    private final AccountManager accountManager;

    public CommandManager(JDA api, AccountManager accountManager) throws SQLException, IOException, ClassNotFoundException {
        this.commands = new ArrayList<>();
        this.api = api;
        this.accountManager = accountManager;
    }

    //Looks for a command to be executed and ensures it's not a bot user or a null/bad message.
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Thread thread = new Thread(() -> {
            try {
                query = event.getMessage();
                String content = (query.getContentRaw().toLowerCase());
                content = content.replaceFirst(prefix, "");
                List<String> fullQuery = (Arrays.asList(content.split(" ")));
                if(event.getMessage().getContentStripped().length() > 0) {
                    if(!event.getMessage().getAuthor().isBot()) {
                        if(event.getMessage().getContentStripped().toLowerCase().startsWith("/")){
                            for(Command command : getCommands()){
                                String queryIdentifier = fullQuery.get(0);
                                if(command.identifierMatches(queryIdentifier)){
                                    if(!command.modCommand()){
                                        command.onUse(query, fullQuery, CommandManager.this);
                                    }
                                }
                            }
                            //Prevents people from sending messages in ticket channels that aren't involved with them
                        } else if(query.getCategory().getId().contains("849658284490752041")) {
                                try{
                                    if(query.getAuthor().getId().contains(this.accountManager.getFreelancer(query.getTextChannel().getId())) || query.getAuthor().getId().contains(this.accountManager.getTicketOwner(query.getTextChannel().getId())) || query.getMember().getPermissions().contains("MANAGE_CHANNELS")) {

                                    } else {
                                        query.delete().queue();
                                    }
                                } catch (Exception e) {
                                    query.delete().queue();
                                }
                        }
                    }
                }
            } catch(Exception uncaughtMessageException) {
                System.err.println("Exception from message event");
                uncaughtMessageException.printStackTrace();
            }
        });
        thread.run();
    }

    public void registerCommand(Command command){
        this.getCommands().add(command);
    }

    public List<Command> getCommands(){
        return this.commands;
    }
}
