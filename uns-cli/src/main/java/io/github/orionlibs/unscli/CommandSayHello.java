package io.github.orionlibs.unscli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandScan;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Command
@Component
@CommandScan
public class CommandSayHello
{
    @Autowired
    private WebClient unsWebClient;


    @Command(command = "say-hello")
    public String commandSayHello(String arg)
    {
        String response = unsWebClient
                        .get()
                        .uri("/v1/documents?q={documentID}", "hello")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
        return arg + "----" + response;
    }
}
