package io.github.orionlibs.core.observability;

import java.util.Map;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

//GET /actuator/info
public class BuildInfo implements InfoContributor
{
    private String pieceOfInformationName;
    private String version;
    private String environment;


    public BuildInfo(String pieceOfInformationName, String version, String environment)
    {
        this.pieceOfInformationName = pieceOfInformationName;
        this.version = version;
        this.environment = environment;
        contribute(new Info.Builder());
    }


    @Override
    public void contribute(Info.Builder builder)
    {
        builder.withDetail(pieceOfInformationName, Map.of(
                        "version", version,
                        "environment", environment));
    }
}
