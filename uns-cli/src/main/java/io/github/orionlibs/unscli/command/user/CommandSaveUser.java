package io.github.orionlibs.unscli.command.user;

import io.github.orionlibs.core.json.JSONService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandScan;
import org.springframework.shell.command.annotation.Option;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Command
@Component
@CommandScan
public class CommandSaveUser
{
    @Autowired
    private WebClient unsWebClient;
    @Autowired
    private JSONService jsonService;


    @Command(command = "save-user",
                    description = "Registers a new user. " +
                                    "Usage: uns-cli save-user username=me@example.com " +
                                    "password=secret authority=USER,ADMIN first_name=Jimmy last_name=Smith")
    public String commandSaveUser(
                    @Option(longNames = "username", required = true, description = "User's email address")
                    String username,
                    @Option(longNames = "password", required = true, description = "User's password")
                    String password,
                    @Option(longNames = "authority", required = true, description = "Comma-separated roles, e.g. USER,ADMIN")
                    String authority,
                    @Option(longNames = "first_name", required = true, description = "User's first name")
                    String firstName,
                    @Option(longNames = "last_name", required = true, description = "User's last name")
                    String lastName,
                    @Option(longNames = "phone_number", required = false, description = "User's phone number")
                    String phoneNumber)
    {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                        .username(username)
                        .password(password)
                        .authority(authority)
                        .firstName(firstName)
                        .lastName(lastName)
                        .phoneNumber(phoneNumber)
                        .build();
        String response = unsWebClient
                        .post()
                        .uri("/v1/users")
                        .bodyValue(jsonService.toJson(request))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
        return response;
    }
}
