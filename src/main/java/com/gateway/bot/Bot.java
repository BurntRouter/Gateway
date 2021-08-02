package com.gateway.bot;

import com.gateway.bot.command.*;
import com.gateway.bot.database.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public class Bot extends Thread {
    private JDA api;
    private final String token;
    private CommandManager commandManager;
    private AccountManager accountManager;

    public Bot(String token) throws LoginException, IllegalAccessException, InterruptedException, RateLimitedException {
        this.token = token;

        this.setup();
        this.start();
    }

    public void setup() throws LoginException, InterruptedException {
        System.out.println("Gateway is Warming Up");
        this.api = JDABuilder.createDefault(token).setChunkingFilter(ChunkingFilter.ALL).disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE).setActivity(Activity.watching("/help")).setEnabledIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES).build().awaitReady();
    }

    @Override
    public void run(){
        try{
            System.out.println("Attempting to Connect to the Database...");
            this.openDatabase();
            System.out.println("Database connected");

            System.out.println("Attempting to Register Command Manager...");
            this.commandManager = new CommandManager(this.api, this.getAccountManager());
            System.out.println("Registered Command Manager");

            System.out.println("Registering Commands...");
            this.registerCommands();
            System.out.println("Registered Commands");

            System.out.println("Connecting to Discord...");
            this.connect();
            System.out.println("CONNECTED");

            System.out.println("Gateway Ready | Ping: " + this.api.getGatewayPing());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void registerCommands(){
        this.api.getGuildById("849262611417923584").upsertCommand("ticket", "Used to create a new project. `/ticket my-project`").addOption(OptionType.STRING, "project-name", "What you would like your project channel to be called in discord.", true).queue();
        this.api.updateCommands().complete();
        this.commandManager.registerCommand(new CommandClose(this.accountManager));
        this.commandManager.registerCommand(new CommandClaim(this.accountManager));
        this.commandManager.registerCommand(new CommandRelease(this.accountManager));
    }

    public void openDatabase() throws IOException, SQLException, ClassNotFoundException {
        Authenticator authenticator = new Authenticator();
        MySQL database = new MySQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://" + authenticator.getDatabaseHost() + "/" + authenticator.getDatabaseName() + "?autoReconnect=true&user=" + authenticator.getDatabaseUser() + "&password=" + authenticator.getDatabasePassword());
        this.setAccountManager(new AccountManager(database));
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    private void connect(){
        this.api.addEventListener(this.commandManager);
        this.api.addEventListener(new CommandTicket(this.accountManager));
    }
}
